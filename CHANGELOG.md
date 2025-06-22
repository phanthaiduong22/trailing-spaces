# Changelog

All notable changes to the Trailing Spaces plugin will be documented in this file.

## [Unreleased]

### Added
- **Real-time highlighting** of trailing spaces in all open documents
- **Delete action** to remove all trailing spaces in the current document
  - Available via Command Palette (`Ctrl+Shift+A` / `Cmd+Shift+A`) - search "Trailing Spaces: Delete"
  - Default keyboard shortcut: `Alt+Shift+T`
  - Accessible from Edit menu
- **Persistent settings**
  - Located at File → Settings → Tools → Trailing Spaces (Windows/Linux)
  - Located at Preferences → Tools → Trailing Spaces (Mac)
- **Highlight Current Line setting** (default: enabled)
  - When enabled: Always highlight trailing spaces in the currently edited line
  - When disabled: Skip highlighting in the current line to avoid distraction while typing
- **Smart highlighting logic** with debounced updates for performance
