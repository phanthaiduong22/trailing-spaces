package com.github.phanthaiduong22.trailingspaces.config

/**
 * Configuration constants for the Trailing Spaces plugin
 */
object PluginConfig {
    
    /**
     * Debounce delay in milliseconds for highlighting updates
     */
    const val HIGHLIGHTING_DEBOUNCE_DELAY_MS = 100L
    
    /**
     * Regular expression pattern for matching trailing spaces
     */
    const val TRAILING_SPACE_PATTERN = "[ \\t]+$"
    
    /**
     * Pattern flags for trailing space matching
     */
    const val PATTERN_FLAGS = java.util.regex.Pattern.MULTILINE
    
    /**
     * Highlighter layer for trailing space highlights
     */
    const val HIGHLIGHTER_LAYER = com.intellij.openapi.editor.markup.HighlighterLayer.WARNING
    
    /**
     * Settings storage file name
     */
    const val SETTINGS_STORAGE_FILE = "trailingSpacesSettings.xml"
    
    /**
     * Plugin display name
     */
    const val PLUGIN_DISPLAY_NAME = "Trailing Spaces"
} 
