package com.github.phanthaiduong22.trailingspaces.listeners

import com.github.phanthaiduong22.trailingspaces.startup.TrailingSpacesActivity
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.project.Project

class TrailingSpacesEditorFactoryListener(
    private val project: Project,
    private val activity: TrailingSpacesActivity
) : EditorFactoryListener {

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        if (editor.project == project) {
            activity.highlightTrailingSpaces(editor)
        }
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        val editor = event.editor
        activity.cancelDebounceJob(editor)
        activity.clearHighlighters(editor)
    }
}