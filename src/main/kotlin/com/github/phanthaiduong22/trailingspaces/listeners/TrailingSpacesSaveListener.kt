package com.github.phanthaiduong22.trailingspaces.listeners

import com.github.phanthaiduong22.trailingspaces.startup.TrailingSpacesActivity
import com.github.phanthaiduong22.trailingspaces.settings.TrailingSpacesSettings
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project

class TrailingSpacesSaveListener(
    private val project: Project,
    private val activity: TrailingSpacesActivity
) : FileDocumentManagerListener {

    override fun beforeDocumentSaving(document: Document) {
        val settings = TrailingSpacesSettings.getInstance()
        if (settings.trimOnSave) {
            activity.deleteTrailingSpaces(document, project.name)
        }
    }
}