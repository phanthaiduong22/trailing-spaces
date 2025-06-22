package com.github.phanthaiduong22.trailingspaces.actions

import com.github.phanthaiduong22.trailingspaces.config.PluginConfig
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Document
import java.util.regex.Pattern

/**
 * Action to delete all trailing spaces in the current document
 */
class DeleteTrailingSpacesAction : AnAction() {

    private val trailingSpacePattern = Pattern.compile(PluginConfig.TRAILING_SPACE_PATTERN, PluginConfig.PATTERN_FLAGS)

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project ?: return
        val document = editor.document

        deleteTrailingSpaces(document, project.name)
    }

    override fun update(e: AnActionEvent) {
        // Enable action only when an editor is available
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = editor != null
    }

    private fun deleteTrailingSpaces(document: Document, projectName: String) {
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
        thisLogger().info("Deleted $deletedCount trailing space occurrences in project: $projectName")
    }
}