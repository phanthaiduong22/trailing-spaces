<!-- Plugin description -->
# Trailing Spaces

highlight trailing spaces and delete them in a flash!

This plugin is inspired by the [Trailing Space](https://github.com/shardulm94/vscode-trailingspaces) extension for Visual Studio Code and the [Trailing Space](https://github.com/SublimeText/TrailingSpaces) for Sublime Text.

## Synopsis

Jetbrains provides a way to automate deletion of trailing spaces in setting. Depending on your settings, it may be more handy to just highlight them and/or delete them by hand, at any time. This plugin provides just that, and a lot of options to fine-tune the way you want to decimate trailing spaces.

## Usage

### Delete

The main feature you gain from using this plugin is that of deleting all trailing spaces in the currently edited document. In order to use this deletion feature, you may either:

1. **Command Palette**: Press `Ctrl+Shift+A` (or `Cmd+Shift+A` on Mac) and select/type "Trailing Spaces: Delete"

2. **Keyboard Shortcut**: The plugin comes with a default keyboard shortcut `Alt+Shift+T` to delete all trailing spaces at once in the current file!

3. **Menu**: Access the action from the "Edit" menu where "Trailing Spaces: Delete" is available

With these options, you can quickly delete all trailing spaces in your current document whenever needed!

## Settings

Several options are available to customize the plugin's behavior. Those settings are stored persistently and applied globally to all opened documents.

To access the settings: Go to **"File / Settings / Tools / Trailing Spaces"** (or **"Preferences / Tools / Trailing Spaces"** on Mac).

### Highlight Current Line
**Default: true**

Highlighting of trailing spaces in the currently edited line can be annoying: each time you are about to start a new word, the space you type is matched as trailing spaces. The currently edited line can thus be ignored by disabling this option.

When this setting is disabled, trailing spaces in the line being edited will not be highlighted.
<!-- Plugin description end -->