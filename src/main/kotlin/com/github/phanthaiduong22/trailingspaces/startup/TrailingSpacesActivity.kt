package com.github.phanthaiduong22.trailingspaces.startup

import com.github.phanthaiduong22.trailingspaces.config.PluginConfig
import com.github.phanthaiduong22.trailingspaces.listeners.TrailingSpacesDocumentListener
import com.github.phanthaiduong22.trailingspaces.listeners.TrailingSpacesEditorFactoryListener
import com.github.phanthaiduong22.trailingspaces.listeners.TrailingSpacesSaveListener
import com.github.phanthaiduong22.trailingspaces.settings.TrailingSpacesSettings
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.editor.Document
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.CommandProcessor
import com.intellij.ui.JBColor
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

@Service(Service.Level.PROJECT)
class TrailingSpacesActivity : ProjectActivity {
    private val trailingSpacePattern = Pattern.compile(PluginConfig.TRAILING_SPACE_PATTERN, PluginConfig.PATTERN_FLAGS)
    private val highlighters = mutableMapOf<Editor, MutableList<RangeHighlighter>>()
    private val debounceJobs = ConcurrentHashMap<Editor, Job>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override suspend fun execute(project: Project) {
        thisLogger().info("Starting trailing spaces plugin for project: ${project.name}")

        val documentListener = TrailingSpacesDocumentListener(project, this)
        val editorFactoryListener = TrailingSpacesEditorFactoryListener(project, this)
        val saveListener = TrailingSpacesSaveListener(project, this)

        EditorFactory.getInstance().eventMulticaster.addDocumentListener(documentListener, project)
        EditorFactory.getInstance().addEditorFactoryListener(editorFactoryListener, project)
        project.messageBus.connect().subscribe(FileDocumentManagerListener.TOPIC, saveListener)

        thisLogger().info("Trailing spaces plugin initialized successfully")
    }

    fun debounceHighlighting(editor: Editor) {
        // Cancel existing job for this editor
        debounceJobs[editor]?.cancel()

        // Start new debounced job
        val job =
            coroutineScope.launch {
                delay(PluginConfig.HIGHLIGHTING_DEBOUNCE_DELAY_MS)
                highlightTrailingSpaces(editor)
            }

        debounceJobs[editor] = job
    }

    fun highlightTrailingSpaces(editor: Editor) {
        clearHighlighters(editor)

        val document = editor.document
        val text = document.text
        val markupModel = editor.markupModel
        val settings = TrailingSpacesSettings.getInstance()

        if (!settings.highlightingEnabled) {
            return
        }

        val caretModel = editor.caretModel
        val caretOffset = caretModel.offset
        val caretLine = document.getLineNumber(caretOffset)

        val textAttributes =
            TextAttributes().apply {
                backgroundColor = JBColor.RED
                foregroundColor = JBColor.WHITE
            }

        val matcher = trailingSpacePattern.matcher(text)
        val editorHighlighters = mutableListOf<RangeHighlighter>()

        while (matcher.find()) {
            val startOffset = matcher.start()
            val endOffset = matcher.end()
            val matchLine = document.getLineNumber(startOffset)

            // Skip highlighting based on settings
            if (!settings.highlightCurrentLine && matchLine == caretLine) {
                // When highlightCurrentLine is false, skip the entire current line
                continue
            }

            val highlighter =
                markupModel.addRangeHighlighter(
                    startOffset,
                    endOffset,
                    PluginConfig.HIGHLIGHTER_LAYER,
                    textAttributes,
                    HighlighterTargetArea.EXACT_RANGE,
                )

            editorHighlighters.add(highlighter)
        }

        highlighters[editor] = editorHighlighters

        thisLogger().debug(
            "Added ${editorHighlighters.size} trailing space highlights (highlightCurrentLine: ${settings.highlightCurrentLine})",
        )
    }

    fun clearHighlighters(editor: Editor) {
        highlighters[editor]?.forEach { highlighter ->
            editor.markupModel.removeHighlighter(highlighter)
        }
        highlighters.remove(editor)
    }

    fun cancelDebounceJob(editor: Editor) {
        debounceJobs[editor]?.cancel()
        debounceJobs.remove(editor)
    }

    fun refreshHighlighting(editor: Editor) {
        highlightTrailingSpaces(editor)
    }

    fun deleteTrailingSpaces(document: Document, projectName: String) {
        val text = document.text
        val matcher = trailingSpacePattern.matcher(text)
        
        if (!matcher.find()) {
            thisLogger().debug("No trailing spaces found in document")
            return
        }
        
        // Reset matcher to find all matches
        matcher.reset()
        val replacements = mutableListOf<Pair<Int, Int>>()
        
        while (matcher.find()) {
            replacements.add(Pair(matcher.start(), matcher.end()))
        }
        
        if (replacements.isEmpty()) return
        
        // Perform the deletion in a write action with command grouping
        WriteAction.run<RuntimeException> {
            CommandProcessor.getInstance().runUndoTransparentAction {
                // Process replacements in reverse order to maintain correct offsets
                replacements.reversed().forEach { (start, end) ->
                    document.deleteString(start, end)
                }
            }
        }
        
        val deletedCount = replacements.size
        thisLogger().info("Deleted $deletedCount trailing space occurrences on save in project: $projectName")
    }
}
