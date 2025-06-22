package com.github.phanthaiduong22.trailingspaces.actions

import com.github.phanthaiduong22.trailingspaces.settings.TrailingSpacesSettings
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor

class ToggleTrailingSpacesAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val settings = TrailingSpacesSettings.getInstance()

        settings.highlightingEnabled = !settings.highlightingEnabled

        thisLogger().info("Trailing spaces highlighting ${if (settings.highlightingEnabled) "enabled" else "disabled"}")

        val editors = FileEditorManager.getInstance(project)
            .allEditors
            .filterIsInstance<TextEditor>()
            .map { it.editor }

        editors.forEach { editor ->
            if (settings.highlightingEnabled) {
                editor.project?.let { project ->
                    val activity = project.getService(com.github.phanthaiduong22.trailingspaces.startup.TrailingSpacesActivity::class.java)
                    activity?.refreshHighlighting(editor)
                }
            } else {
                clearHighlighters(editor)
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = editor != null

        val settings = TrailingSpacesSettings.getInstance()
        e.presentation.text = if (settings.highlightingEnabled) {
            "Trailing Spaces: Disable Highlighting"
        } else {
            "Trailing Spaces: Enable Highlighting"
        }
    }

    private fun clearHighlighters(editor: com.intellij.openapi.editor.Editor) {
        val markupModel = editor.markupModel
        markupModel.allHighlighters.forEach { highlighter ->
            if (highlighter.layer == com.github.phanthaiduong22.trailingspaces.config.PluginConfig.HIGHLIGHTER_LAYER) {
                markupModel.removeHighlighter(highlighter)
            }
        }
    }
}