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

### v0.0.1
- Initial project
