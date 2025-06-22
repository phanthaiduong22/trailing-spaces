package com.github.phanthaiduong22.trailingspaces.startup

import com.github.phanthaiduong22.trailingspaces.config.PluginConfig
import com.github.phanthaiduong22.trailingspaces.settings.TrailingSpacesSettings
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.ui.JBColor
import kotlinx.coroutines.*
import java.awt.Color
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

class TrailingSpacesActivity : ProjectActivity {
    private val trailingSpacePattern = Pattern.compile(PluginConfig.TRAILING_SPACE_PATTERN, PluginConfig.PATTERN_FLAGS)
    private val highlighters = mutableMapOf<Editor, MutableList<RangeHighlighter>>()
    private val debounceJobs = ConcurrentHashMap<Editor, Job>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override suspend fun execute(project: Project) {
        thisLogger().info("Starting trailing spaces plugin for project: ${project.name}")

        val documentListener =
            object : DocumentListener {
                override fun documentChanged(event: DocumentEvent) {
                    val document = event.document
                    val file = FileDocumentManager.getInstance().getFile(document)

                    if (file != null) {
                        // Find all editors for this document
                        val editors =
                            FileEditorManager
                                .getInstance(project)
                                .allEditors
                                .filterIsInstance<TextEditor>()
                                .map { it.editor }
                                .filter { it.document == document }

                        // Debounce highlighting for each editor
                        editors.forEach { editor ->
                            debounceHighlighting(editor)
                        }

                        thisLogger().debug("Scheduled debounced trailing spaces check for file: ${file.name}")
                    }
                }
            }

        // Register the document listener
        EditorFactory.getInstance().eventMulticaster.addDocumentListener(documentListener, project)

        // Also add editor factory listener to handle new editors
        val editorFactoryListener =
            object : com.intellij.openapi.editor.event.EditorFactoryListener {
                override fun editorCreated(event: com.intellij.openapi.editor.event.EditorFactoryEvent) {
                    val editor = event.editor
                    if (editor.project == project) {
                        highlightTrailingSpaces(editor)
                    }
                }

                override fun editorReleased(event: com.intellij.openapi.editor.event.EditorFactoryEvent) {
                    val editor = event.editor
                    // Cancel any pending debounce job
                    debounceJobs[editor]?.cancel()
                    debounceJobs.remove(editor)
                    clearHighlighters(editor)
                }
            }

        EditorFactory.getInstance().addEditorFactoryListener(editorFactoryListener, project)

        thisLogger().info("Trailing spaces plugin initialized successfully")
    }

    private fun debounceHighlighting(editor: Editor) {
        // Cancel existing job for this editor
        debounceJobs[editor]?.cancel()
        
        // Start new debounced job
        val job = coroutineScope.launch {
            delay(PluginConfig.HIGHLIGHTING_DEBOUNCE_DELAY_MS)
            highlightTrailingSpaces(editor)
        }
        
        debounceJobs[editor] = job
    }

    private fun highlightTrailingSpaces(editor: Editor) {
        clearHighlighters(editor)

        val document = editor.document
        val text = document.text
        val markupModel = editor.markupModel
        val settings = TrailingSpacesSettings.getInstance()

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

        thisLogger().debug("Added ${editorHighlighters.size} trailing space highlights (highlightCurrentLine: ${settings.highlightCurrentLine})")
    }

    private fun clearHighlighters(editor: Editor) {
        highlighters[editor]?.forEach { highlighter ->
            editor.markupModel.removeHighlighter(highlighter)
        }
        highlighters.remove(editor)
    }
}
