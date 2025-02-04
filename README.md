# libBasicUI

![Demo](https://github.com/cdelaof26/libBasicUI/blob/main/images/Concept.png?raw=true)

### What is this?

This is a tiny library built on top of Java Swing, it mainly uses _custom-painting_
technics to deliver a modern look and feel while being desktop cross-platform.

### Dependencies 

- Java 8 or newer

### Disclaimer

<pre>
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
</pre>

### Running project

Clone this repo

<pre>
$ git clone https://github.com/cdelaof26/libBasicUI.git
</pre>

Move into project path

<pre>
$ cd libBasicUI
</pre>

#### Compile and run code example

<pre>
# Linux and macOS users
$ javac -classpath . libbasicui/LibBasicUI.java

# Windows users
$ javac -encoding utf8 -classpath . libbasicui/LibBasicUI.java

$ java libbasicui/LibBasicUI
</pre>

#### Pack inside a JAR file

Delete or move `.git` to prevent `jar` command include git files:

<pre>
$ rm -r .git

    or

$ mv .git /destination/path
</pre>

Pack and run
<pre>
# Linux and macOS users
$ javac -classpath . libbasicui/LibBasicUI.java

# Windows users
$ javac -encoding utf8 -classpath . libbasicui/LibBasicUI.java

# Java 8 - any system
$ jar cmvf ./manifest.mf libBasicUI.jar -classpath . libbasicui/LibBasicUI

# Java 11 or newer - any system
$ jar cmvf ./manifest.mf libBasicUI.jar *

# Run demo
# Linux and macOS users
$ java -jar libBasicUI.jar

# Windows users
$ java -Dfile.encoding=UTF-8 -jar libBasicUI.jar
</pre>


### Changelog

### v0.0.9
- Added `UIFont` interface
  - Allows font changes in UI elements such as font family, 
    whether is bold, italic or plain and font point size
  - Created `CUSTOM` entry in `LabelType`
  - Initial support for `Label`, `ColorButton` and `CheckField`
- Created `setFileReplaceableOnSingle()` and `isFileReplaceableOnSingle()`
  in `FileDropArea` class
- Reviewed FFMpeg installation progress
  - Fixed broken installation process
  - Added support for FFplay and FFprobe
  - Improved installation status feedback
- Created `loadFile()` method in `FileUtilities` to easily access
  bundled files in a JAR
- Added `callProcess()` method in `LibUtilities` to call external
  programs
- Fixed some java doc documentation

### v0.0.8
- Improved `FileChooser`
  - Added refresh button
  - Fixed issue with bigger UIScales where this component won't
    show correctly
  - Improved drag & drop selection
- Created `FileSelector`: another way to retrieve files from user.
  This component allows the user to drag & drop files and directories
- Improved `ImageButton` response to different situations
- Created `updateAlignComponent()` method in `Panel`
- Attempt to improve performance in `ProgressBar` on Windows
- Created `ScrollPane` component
- Added `moveObject()` static method to `LibUtilities`. This 
  function moves a object inside an ArrayList given its index and 
  the new one

### v0.0.7
- Added missing Java Docs
- `ColorButton` changes
  - Added `onlyAppColor` property which will only apply accent colors to the button
  - Every button now contains a `Label`, this means text won't overflow
- Added `getSelectedColorHex()` method to `ColorPicker`
- Now all `Menu` share `aMenuIsOpened` property that will
  allow all `ContextMenu`es to be opened when hovering in a `MenuBar`
- Added `FileBrowser` as minimal interface to implement a `FileChooser`,
  this might be used to add a `FileViewer` to another `JComponent`
- Added `getLabelType()` method to `Label`
- Added various `add` methods to `Panel` class to simplify adding components directly
- Fixed colors for `TextField` when `appColor = true` and `UIProperties.accentColors = false`
- Added `Table` component
  - This `JComponent` might contain bugs

### v0.0.6
- `ColorButton` won't highlight if disabled
- Added `removeAllOptions()` and `removeOption()` methods to `ComboBox`
- `FileViewer` will refresh files when selecting a file or directory
- Added 'F-named' arrangements to `ImageButtonArrangement` allowing custom sizes
  for different layouts
- Fixed issue where newly added options to `SelectionPanel` won't update until
  mouse is over
- Added `calculateFitDimension()` to LibUtilities
- Created `FFmpegUtilities` class with many utilities for FFmpeg


### v0.0.5
- Removed `LibUtilities.initLibUtils();` and `UIProperties.initUIProperties();`, 
  initialization is now automatically done when the class is loaded
- Fixed `Window` and `Dialog` height for different platforms
  - **TODO**: Scale font size
- Minimum and maximum values are no longer final in `NumberSelector` class
- Added `editableOnCLick` attribute to `TextField`, setting it to true 
  disables autofocus until the user clicks it
- Setting a `TextField` enabled has the same effect as setting editable
- Added several methods to `FileUtilities`
  - `writeFile()` writes binary files
  - `downloadFile()` downloads files
  - `listFilesInZip()` creates a array with the files inside a zip archive
  - `extractSingleZippedFile()` retrieves a file inside a zip archive without password
  - `extractAllZippedFiles()` retrieves all files inside a zip archive without password
- Added to `LibUtilities`:
  - `OS_ARCH` and `IS_MACOS` properties
  - `replaceLast()` method to replace the last coincidence using regex
  - `splitData()`, `compressStringHashMap()` and `parseProperties()` to facilitate the
    process of reading HashMaps
- Improved and fixed JavaDoc references

### v0.0.4
- Added `paint` attribute to `ColorButton`, setting `paint` to false will disable 
  the hover effect, making the button look like a `Label`
- Added `setElementsArrange()`, `removeOption()` and `removeAllOptions()` methods to 
  `ContextMenu`
- Added `FileChooser`, a basic file chooser using custom painting!
- `ImageButton`:
  - Added `RIGHT_TEXT_RIGHT_IMAGE` and `LEFT_TEXT_LEFT_IMAGE` distributions 
  - Now it's possible to change the `arrangement`
  - Added methods to change image using `BufferedImage`
  - Added method `setImageDimension()`
- Implemented auto increase/decrease mechanism to `NumberSelector` [**WIP**]
  - When up or down buttons are pressed for 400 ms, the value will go up or down.
    Pressing it for more time will accelerate the value change
  - Auto increase/decrease behavior can be disabled by setting `enableValueModifier` 
    to false
- `ProgressBar` changes its UI faster
- Added `visibleBackground` and `fontType` attributes to `TextField`
  - `visibleBackground` sets the background to `null` if false, making
    it look transparent as if it were a `Label`
  - `fontType` allows the `TextField` to have a different fonts
- Moved `joinPath()`, `readFile()` and `writeFile()` methods from `LibUtilities` to 
  `FileUtilities`

### v0.0.3
- Implemented `CheckField`, an easier way to add a checkbox with a label
- Added `componentsToUpdate` to `ColorPicker`
- Added `ONE_WORD_ICON_BUTTON` button scheme to ImageButton among other minor fixes
- Fixed `Menu` items not setting the right foreground color
- Implemented `enabled` property for `NumberSelector`
- Minimum and maximum values are no longer final in `ProgressBar` class
- `Slider` now is capable of modifying `NumberSelector` value
- `TextField` would not longer react to clicks if hover is visible and it's clicked
- New methods added to LibUtilities 
  - `addKeyBindingTo()`
  - `setAlphaToColor()`
- Added a reset button to `UIPreferences` panel

### v0.0.2
- Fixed exception raised when loading preferences containing a custom 
  standard/subtitle font width
- Fixed bug were `ColorButton` and `ImageButton` will use intensively the CPU
- Added option to make elements in `ContextMenu` slim if `hideOverflow = false`
- Added `MenuBar` and `Menu` 
- Changed `ProgressBar` behavior when changing ui appearance
  - Color update will be visible after using `updateUITheme()` or `updateUIColors()`
- Added `getTextDimensions()` to `LibUtilities`

### v0.0.1f
- Fixed unused imports
- Fixed Java 8 compatibility

### v0.0.1
- Initial project
