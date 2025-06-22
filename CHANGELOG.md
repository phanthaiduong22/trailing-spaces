# Changelog

All notable changes to the Trailing Spaces plugin will be documented in this file.

## [Unreleased]
### Added
- **Real-time highlighting** of trailing spaces in all open documents
- **Toggle highlighting functionality** to enable/disable trailing space highlighting
  - Available via Edit → Trailing Spaces → Toggle Highlighting menu
  - Default keyboard shortcut: `Alt+Shift+D`
  - Dynamic action text that shows current state (Enable/Disable Highlighting)
  - Instantly updates all open editors when toggled
- **Delete action** to remove all trailing spaces in the current document
  - Available via Command Palette (`Ctrl+Shift+A` / `Cmd+Shift+A`) - search "Trailing Spaces: Delete"
  - Available via Edit → Trailing Spaces → Delete menu
  - Default keyboard shortcut: `Alt+Shift+T`
- **Custom highlight color support**
  - Choose any color for highlighting trailing spaces using HEX format (e.g., #FF0000)
  - Color picker dialog for visual color selection
  - Direct HEX input field for precise color specification
  - Default color: Red (#FF0000)
- **Persistent settings**
  - Located at File → Settings → Tools → Trailing Spaces (Windows/Linux)
  - Located at Preferences → Tools → Trailing Spaces (Mac)
- **Enable Highlighting setting** (default: enabled)
  - Global setting to enable/disable all trailing space highlighting
  - Can be toggled via settings panel or toggle action
- **Highlight Current Line setting** (default: enabled)
  - When enabled: Always highlight trailing spaces in the currently edited line
  - When disabled: Skip highlighting in the current line to avoid distraction while typing
- **Trim On Save setting** (default: disabled)
  - When enabled: Automatically deletes all trailing spaces when saving a document
- **Smart highlighting logic** with debounced updates for performance
