# libBasicUI

![Demo](https://github.com/cdelaof26/libBasicUI/blob/main/images/Concept.png?raw=true)

### What is this?

This is a tiny library built on top of Java Swing library, it mainly uses _custom-painting_
technics to have a customized look and feel cross-platform. 

### Dependencies 

1. Oracle Java 8 or newer, alternatively OpenJDK 8 or newer

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

### v0.0.3
- Implemented `CheckField`, an easier way to add a checkbox with a label
- Added `componentsToUpdate` to `ColorPicker`
- Added `ONE_WORD_ICON_BUTTON` button scheme to ImageButton among other minor fixes
- Fixed `Menu` items not setting the right foreground color
- Implemented `enabled` property for `NumberSelector`
- Minimum and maximum values are not longer final in `ProgressBar` class
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
