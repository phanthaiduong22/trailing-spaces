package com.github.phanthaiduong22.trailingspaces.settings

import com.github.phanthaiduong22.trailingspaces.config.PluginConfig
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Settings state for the Trailing Spaces plugin.
 * This class stores the plugin configuration and persists it across IDE sessions.
 */
@Service(Service.Level.APP)
@State(
    name = "TrailingSpacesSettings",
    storages = [Storage(PluginConfig.SETTINGS_STORAGE_FILE)]
)
class TrailingSpacesSettings : PersistentStateComponent<TrailingSpacesSettings.State> {
    
    private var state = State()
    
    data class State(
        var highlightCurrentLine: Boolean = true,
        var highlightingEnabled: Boolean = true,
        var trimOnSave: Boolean = false,
        var highlightColor: String = PluginConfig.DEFAULT_HIGHLIGHT_COLOR
    )
    
    override fun getState(): State = state
    
    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, this.state)
    }
    
    /**
     * Whether to highlight trailing spaces in the currently edited line
     */
    var highlightCurrentLine: Boolean
        get() = state.highlightCurrentLine
        set(value) {
            state.highlightCurrentLine = value
        }

    /**
     * Whether highlighting is globally enabled
     */
    var highlightingEnabled: Boolean
        get() = state.highlightingEnabled
        set(value) {
            state.highlightingEnabled = value
        }

    /**
     * Whether to automatically trim trailing spaces on save
     */
    var trimOnSave: Boolean
        get() = state.trimOnSave
        set(value) {
            state.trimOnSave = value
        }
    
    /**
     * Custom highlight color in HEX format
     */
    var highlightColor: String
        get() = state.highlightColor
        set(value) {
            state.highlightColor = value
        }
    
    companion object {
        @JvmStatic
        fun getInstance(): TrailingSpacesSettings {
            return ApplicationManager.getApplication().getService(TrailingSpacesSettings::class.java)
        }
    }
} 