<!-- Plugin description -->

# Trailing Spaces - Jetbrains

highlight trailing spaces and delete them in a flash!

This plugin is inspired by the [Trailing Space](https://github.com/shardulm94/vscode-trailingspaces) extension for
Visual Studio Code and the [Trailing Space](https://github.com/SublimeText/TrailingSpaces) for Sublime Text.

## Synopsis

Jetbrains provides a way to automate deletion of trailing spaces in setting. Depending on your settings, it may be more
handy to just highlight them and/or delete them by hand, at any time. This plugin provides just that, and a lot of
options to fine-tune the way you want to decimate trailing spaces.

## Usage

### Toggle Highlighting

You can toggle highlighting of trailing spaces on and off at any time using:

1. **Menu**: Go to **"Edit / Trailing Spaces / Toggle Highlighting"**
2. **Keyboard Shortcut**: Use `Alt+Shift+D` to instantly toggle highlighting
3. **Settings**: Use the "Enable highlighting" checkbox in the plugin settings

The action text dynamically updates to show whether it will "Enable" or "Disable" highlighting based on the current
state.

### Delete

The main feature you gain from using this plugin is that of deleting all trailing spaces in the currently edited
document. In order to use this deletion feature, you may either:

1. **Command Palette**: Press `Ctrl+Shift+A` (or `Cmd+Shift+A` on Mac) and select/type "Trailing Spaces: Delete"

2. **Keyboard Shortcut**: The plugin comes with a default keyboard shortcut `Alt+Shift+T` to delete all trailing spaces
   at once in the current file!

3. **Menu**: Access the action from **"Edit / Trailing Spaces / Delete"**

With these options, you can quickly delete all trailing spaces in your current document whenever needed!

## Settings

Several options are available to customize the plugin's behavior. Those settings are stored persistently and applied
globally to all opened documents.

To access the settings: Go to **"File / Settings / Tools / Trailing Spaces"** (or **"Preferences / Tools / Trailing
Spaces"** on Mac).

### Enable Highlighting

**Default: true**

This is the master toggle for all trailing space highlighting. When disabled, no trailing spaces will be highlighted in
any file. This setting can also be toggled using the "Toggle Trailing Spaces" action (`Alt+Shift+D`) or via the menu
at "Edit / Trailing Spaces / Toggle Highlighting".

### Highlight Current Line

**Default: true**

Highlighting of trailing spaces in the currently edited line can be annoying: each time you are about to start a new
word, the space you type is matched as trailing spaces. The currently edited line can thus be ignored by disabling this
option.

When this setting is disabled, trailing spaces in the line being edited will not be highlighted (only effective when "
Enable Highlighting" is also enabled).

### Trim On Save

**Default: false**

Setting this to true will ensure trailing spaces are deleted when you save your document. This feature works
independently of the highlighting settings - trailing spaces will be removed on save even if highlighting is disabled.

When enabled, all trailing spaces in the document are automatically removed every time you save the file.

### Custom Highlight Color

**Default: #FF0000 (Red)**

You can customize the color used to highlight trailing spaces by specifying a HEX color value. The plugin provides two
ways to set your preferred color:

1. **Color Picker**: Click the "Choose" button to open a visual color picker dialog where you can select any color
2. **Direct HEX Input**: Enter a HEX color code directly in the text field (supports both `#FF0000` and `FF0000`
   formats)

Examples of HEX color codes:

- `#FF0000` - Red (default)
- `#00FF00` - Green
- `#0000FF` - Blue
- `#FFFF00` - Yellow
- `#FF00FF` - Magenta

The color change applies immediately to all open documents. If an invalid color code is entered, the plugin will fall
back to the default red color and log a warning.
<!-- Plugin description end -->