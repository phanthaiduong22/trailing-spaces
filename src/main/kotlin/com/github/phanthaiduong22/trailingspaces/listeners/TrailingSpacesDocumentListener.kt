package com.github.phanthaiduong22.trailingspaces.listeners

import com.github.phanthaiduong22.trailingspaces.startup.TrailingSpacesActivity
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project

class TrailingSpacesDocumentListener(
    private val project: Project,
    private val activity: TrailingSpacesActivity
) : DocumentListener {
    
    override fun documentChanged(event: DocumentEvent) {
        val document = event.document
        val file = FileDocumentManager.getInstance().getFile(document)

        if (file != null) {
            val editors = FileEditorManager
                .getInstance(project)
                .allEditors
                .filterIsInstance<TextEditor>()
                .map { it.editor }
                .filter { it.document == document }

            editors.forEach { editor ->
                activity.debounceHighlighting(editor)
            }

            thisLogger().debug("Scheduled debounced trailing spaces check for file: ${file.name}")
        }
    }
} 