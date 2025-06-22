package com.github.phanthaiduong22.trailingspaces.settings

import com.github.phanthaiduong22.trailingspaces.config.PluginConfig
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent
import javax.swing.JTextField
import com.intellij.ui.ColorPicker
import com.intellij.ui.ColorUtil
import java.awt.Color
import com.intellij.openapi.wm.WindowManager

/**
 * Configurable for the Trailing Spaces plugin settings.
 * This creates the UI panel that appears in IntelliJ's preferences.
 */
class TrailingSpacesConfigurable : Configurable {

    private var settingsComponent: DialogPanel? = null
    private val settings = TrailingSpacesSettings.getInstance()
    private lateinit var colorField: JTextField

    override fun getDisplayName(): String = PluginConfig.PLUGIN_DISPLAY_NAME

    override fun createComponent(): JComponent {
        val panel = panel {
            group("Highlighting Options") {
                row {
                    checkBox("Enable highlighting")
                        .bindSelected(settings::highlightingEnabled)
                        .comment("When disabled, no trailing spaces will be highlighted in any file. " +
                                "You can toggle this setting using the 'Toggle Trailing Spaces' action.")
                }
                row {
                    checkBox("Highlight current line")
                        .bindSelected(settings::highlightCurrentLine)
                        .comment("When enabled, trailing spaces in the currently edited line will always be highlighted. " +
                                "When disabled, trailing spaces in the line being edited will not be highlighted " +
                                "(useful to avoid distracting highlights while typing), but they can still be deleted using the delete command.")
                }
                row("Highlight color:") {
                    colorField = textField()
                        .bindText(settings::highlightColor)
                        .component
                    button("Choose") {
                        val currentColor = try {
                            Color.decode(settings.highlightColor)
                        } catch (e: NumberFormatException) {
                            Color.RED
                        }
                        val parentComponent = WindowManager.getInstance().findVisibleFrame() ?: javax.swing.JFrame()
                        val chosenColor = ColorPicker.showDialog(parentComponent, "Choose Highlight Color", currentColor, false, null, false)
                        chosenColor?.let {
                            val hexColor = ColorUtil.toHex(it)
                            colorField.text = hexColor
                            settings.highlightColor = hexColor
                        }
                    }.comment("Choose a custom color for highlighting trailing spaces. Use HEX format (e.g., #FF0000 for red)")
                }
            }
            group("Automatic Actions") {
                row {
                    checkBox("Trim on save")
                        .bindSelected(settings::trimOnSave)
                        .comment("When enabled, trailing spaces will be automatically deleted when you save a document.")
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