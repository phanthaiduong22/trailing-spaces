package com.github.phanthaiduong22.trailingspaces.settings

import com.github.phanthaiduong22.trailingspaces.config.PluginConfig
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

/**
 * Configurable for the Trailing Spaces plugin settings.
 * This creates the UI panel that appears in IntelliJ's preferences.
 */
class TrailingSpacesConfigurable : Configurable {
    
    private var settingsComponent: DialogPanel? = null
    private val settings = TrailingSpacesSettings.getInstance()
    
    override fun getDisplayName(): String = PluginConfig.PLUGIN_DISPLAY_NAME
    
    override fun createComponent(): JComponent {
        val panel = panel {
            group("Highlighting Options") {
                row {
                    checkBox("Highlight current line")
                        .bindSelected(settings::highlightCurrentLine)
                        .comment("When enabled, trailing spaces in the currently edited line will always be highlighted. " +
                                "When disabled, trailing spaces in the line being edited will not be highlighted " +
                                "(useful to avoid distracting highlights while typing), but they can still be deleted using the delete command.")
                }
            }
        }
        
        settingsComponent = panel
        return panel
    }
    
    override fun isModified(): Boolean {
        return settingsComponent?.isModified() ?: false
    }
    
    override fun apply() {
        settingsComponent?.apply()
    }
    
    override fun reset() {
        settingsComponent?.reset()
    }
    
    override fun disposeUIResources() {
        settingsComponent = null
    }
} 