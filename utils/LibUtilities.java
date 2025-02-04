package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import ui.UIProperties;
import ui.Window;

/**
 * General utilities
 * 
 * @author cristopher
 */
public class LibUtilities {
    /**
     * Available preferences for libBasicUI
     */
    public static enum Preferences {
        APPEARANCE, USE_ACCENT_COLORS, PRIMARY_COLOR, SECONDARY_COLOR, FOREGROUND_COLOR, 
        FONT_FAMILY, TITLE_FONT_WIDTH, SUBTITLE_FONT_WIDTH, STANDARD_FONT_WIDTH, UI_SCALE
    }
    
    /**
     * Host architecture
     */
    public static final String OS_ARCH = System.getProperty("os.arch");
    
    /**
     * Host SO name
     */
    public static final String SYSTEM_NAME = System.getProperty("os.name");
    
    /**
     * User home directory
     */
    public static final String USER_HOME = System.getProperty("user.home");
    
    /**
     * Condition to indicate if the host system is not Microsoft NT
     */
    public static final boolean IS_UNIX_LIKE = !SYSTEM_NAME.startsWith("Windows");
    
    /**
     * Condition to indicate if the host system is Apple macOS
     */
    public static final boolean IS_MACOS = SYSTEM_NAME.startsWith("Mac");
    
    /**
     * The default preferences
     */
    public static final HashMap<String, String> DEFAULT_PREFERENCES = new HashMap<>();
    
    /**
     * The default font family
     */
    protected static String fontName = !IS_UNIX_LIKE ? "Segoe UI" : IS_MACOS ? "Helvetica Neue" : "";
    
    /**
     * Initializes lib preferences
     */
    static {
        DEFAULT_PREFERENCES.put(Preferences.APPEARANCE.name(), "Light");
        DEFAULT_PREFERENCES.put(Preferences.USE_ACCENT_COLORS.name(), "true");
        DEFAULT_PREFERENCES.put(Preferences.PRIMARY_COLOR.name(), "62-141-216");
        DEFAULT_PREFERENCES.put(Preferences.SECONDARY_COLOR.name(), "12-91-146");
        DEFAULT_PREFERENCES.put(Preferences.FOREGROUND_COLOR.name(), "255-255-255");
        DEFAULT_PREFERENCES.put(Preferences.FONT_FAMILY.name(), fontName);
        DEFAULT_PREFERENCES.put(Preferences.TITLE_FONT_WIDTH.name(), "24");
        DEFAULT_PREFERENCES.put(Preferences.SUBTITLE_FONT_WIDTH.name(), "18");
        DEFAULT_PREFERENCES.put(Preferences.STANDARD_FONT_WIDTH.name(), "13");
        DEFAULT_PREFERENCES.put(Preferences.UI_SCALE.name(), "1.0");
        
        preferences = DEFAULT_PREFERENCES;
        
        loadDefaultPreferences();
        
        System.out.println("[INFO] libBasicUI v0.0.9");
        System.out.println("[INFO] LibUtilities initialized!");
    }
    
    
    private static final Decoder decoder = Base64.getDecoder();
    private static final FileDialog fileDialog = new FileDialog((JFrame) null, "Select a file", FileDialog.LOAD);
    
    private static final AffineTransform affinetransform = new AffineTransform();     
    private static final FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
    
    /**
     * Modifier key
     */
    public static final int MOD_KEY = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
    private static final File WIN_PATH = FileUtilities.joinPath(USER_HOME, "AppData", "Local", "libBasicUI");
    private static final File UNIX_PATH = FileUtilities.joinPath(USER_HOME, ".libBasicUI");
    
    /**
     * File where libBasicUI will store its preferences, it can be found in:<br>
     * - Windows:<br>
     * <blockquote>C:\Users\USER_NAME\AppData\Local\libBasicUI</blockquote><br>
     * 
     * - macOS:<br>
     * <blockquote>/Users/USER_NAME/.libBasicUI</blockquote><br>
     * 
     * - Linux:<br>
     * <blockquote>/home/USER_NAME/.libBasicUI</blockquote>
     * @see Preferences
     */
    public static final File LIB_PREFERENCES_FILE = !IS_UNIX_LIKE ? WIN_PATH : UNIX_PATH;
    
    /**
     * User-defined preferences
     */
    protected static HashMap<String, String> preferences = new HashMap<>();
    
    
    /**
     * This is the replacement string. It's used to replace strings inside other,
     * mainly used by {@link utils.LibUtilities#compressStringHashMap(java.lang.String)}
     */
    public static final String REPLACEMENT_STR = "DATA_RPL";
    
    /**
     * Setting this to true will call {@link Process#destroy()} in all running 
     * instances of {@link utils.LibUtilities#callProcess(utils.ProcessOutput, java.lang.String...)}
     */
    public static boolean destroyProcessCalls = false;
    
    
    /**
     * Sets library's font, you should call {@link Window#updateUIFont()} to apply
     * changes
     * 
     * @param fontName the font family name
     */
    public static void setFontName(String fontName) {
        LibUtilities.fontName = fontName;
        UIProperties.initFonts();
    }

    /**
     * @return the current font name
     */
    public static String getFontName() {
        return fontName;
    }
    
    /**
     * Loads an image from a file<br>
     * Note that this method only supports .png, .jpeg and .jpg file types<br><br>
     * 
     * After getting the BufferedImage use scaleImage() to create an icon for Labels
     * 
     * @param imagePath the file
     * @return a BufferedImage or null
     */
    public static BufferedImage readImage(File imagePath) {
        if (imagePath == null)
            return null;
        
        if (!imagePath.exists() || !imagePath.canRead())
            return null;
        
        try {
            return ImageIO.read(imagePath);
        } catch (IOException | OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            System.gc();
            Runtime.getRuntime().freeMemory();
        }
        
        return null;
    }
    
    /**
     * Loads an image from a file bundled in the JAR app<br>
     * 
     * After getting the BufferedImage use scaleImage() to create an icon for Labels
     * 
     * @param imagePath the absolute package path to the resource
     * @return a BufferedImage or null
     */
    public static BufferedImage readImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty())
            return null;
        
        URL path = ClassLoader.getSystemClassLoader().getResource(imagePath);
        
        try {
            return ImageIO.read(path);
        } catch (IllegalArgumentException | IOException ex) {
            ex.printStackTrace();
        } finally {
            System.gc();
            Runtime.getRuntime().freeMemory();
        }
        
        return null;
    }
    
    
    /**
     * Creates an image from base64 valid data<br><br>
     * 
     * After getting the BufferedImage use scaleImage() to create an icon for Labels
     * 
     * @param data base64 string
     * @return an ImageIcon or null if string <code>data</code> is not base64
     */
    public static BufferedImage loadBase64StringAsImage(String data) {
        if (data == null)
            return null;

        try {
            byte [] imageData = decoder.decode(data);
            return ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IllegalArgumentException | IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Scales a buffered image
     * 
     * @param image the buffered image
     * @param width the new width
     * @param height the new height
     * @return an ImageIcon
     */
    public static ImageIcon scaleImage(BufferedImage image, int width, int height) {
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    
    /**
     * Creates a dimension bigger or smaller based of <code>d</code> 
     * to fit it in <code>containerDimension</code> while preserving the aspect 
     * ratio
     * 
     * @param d the dimension to fit in <code>containerDimension</code>
     * @param containerDimension the container dimension
     * @return a new dimension based of <code>d</code>
     */
    public static Dimension calculateFitDimension(Dimension d, Dimension containerDimension) {
        double ratio = (double) d.height / (double) d.width;
        int width = containerDimension.width;
        int height = (int) (width * ratio);
        
        if (width <= containerDimension.width && height <= containerDimension.height)
            return new Dimension(width, height);
        
        ratio = (double) d.width / (double) d.height;
        height = containerDimension.height;
        width = (int) (height * ratio);
        
        return new Dimension(width, height);
    }
    
    /**
     * Converts a base64 string into an InputStream
     * 
     * @param data base64 string
     * @return an InputStream
     * @throws IllegalArgumentException if string <code>data</code> is not base64
     */
    public static InputStream base64StringToInputStream(String data) {
        return new ByteArrayInputStream(decoder.decode(data));
    }
    
    /**
     * Calculates the size of a string given a font
     * 
     * @param text the string
     * @param font the font
     * @return a new dimension
     * @see FontRenderContext
     */
    public static Dimension getTextDimensions(String text, Font font) {
        Rectangle2D r2D = font.getStringBounds(text, frc);
        
        return new Dimension((int) Math.floor(r2D.getWidth()), (int) Math.floor(r2D.getHeight()));
    }
    
    
    /**
     * Adds a keybinding to the c component, by default key bindings added with
     * this method will be invoked WHEN_IN_FOCUSED_WINDOW
     * @param c the component
     * @param name the name for this keybinding
     * @param keySequence the key sequence
     * @param action the action to be performed
     */
    public static void addKeyBindingTo(JComponent c, String name, String keySequence, AbstractAction action) { 
        c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keySequence), name);
        c.getActionMap().put(name, action);
    }
    
    /**
     * Adds a keybinding to the c component
     * @param c the component
     * @param name the name for this keybinding
     * @param keySequence the key sequence
     * @param onlyWhenFocused if true, JComponent.WHEN_FOCUSED is settled 
     * otherwise WHEN_IN_FOCUSED_WINDOW
     * @param action the action to be performed
     */
    public static void addKeyBindingTo(JComponent c, String name, String keySequence, boolean onlyWhenFocused, AbstractAction action) { 
        c.getInputMap(onlyWhenFocused ? JComponent.WHEN_FOCUSED : JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keySequence), name);
        c.getActionMap().put(name, action);
    }
    
    /**
     * Adds a keybinding to the c component, by default key bindings added with
     * this method will be invoked WHEN_IN_FOCUSED_WINDOW
     * @param c the component
     * @param name the name for this keybinding
     * @param keySequence the key sequence
     * @param action the action to be performed
     */
    public static void addKeyBindingTo(JComponent c, String name, KeyStroke keySequence, AbstractAction action) { 
        c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keySequence, name);
        c.getActionMap().put(name, action);
    }
    
    /**
     * Adds a keybinding to the c component
     * @param c the component
     * @param name the name for this keybinding
     * @param keySequence the key sequence
     * @param onlyWhenFocused if true, JComponent.WHEN_FOCUSED is settled 
     * otherwise WHEN_IN_FOCUSED_WINDOW
     * @param action the action to be performed
     */
    public static void addKeyBindingTo(JComponent c, String name, KeyStroke keySequence, boolean onlyWhenFocused, AbstractAction action) { 
        c.getInputMap(onlyWhenFocused ? JComponent.WHEN_FOCUSED : JComponent.WHEN_IN_FOCUSED_WINDOW).put(keySequence, name);
        c.getActionMap().put(name, action);
    }
    
    /**
     * Sets alpha value to a <code>c</code> color
     * 
     * @param c the color
     * @param alpha the new alpha value in the range [0 - 255]
     * @return the <code>c</code> with alpha channel or null if c is null
     */
    public static Color setAlphaToColor(Color c, int alpha) {
        if (c == null)
            return null;
        
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
    
    /**
     * Joins a string path
     * 
     * @param path the base
     * @param paths the strings to concatenate
     * @return a file within a pathname composed by <code>path</code> and <code>paths</code>
     * @deprecated please use <code>FileUtils.joinPath()</code> instead
     * @see FileUtilities#joinPath(java.lang.String, java.lang.String[])
     */
    @Deprecated
    public static File joinPath(String path, String ... paths) {
        return FileUtilities.joinPath(path, paths);
    }
    
    /**
     * Joins a file path
     * 
     * @param f the base
     * @param paths the strings to concatenate
     * @return a file within a pathname composed by <code>f</code> and <code>paths</code>
     * @deprecated please use <code>FileUtils.joinPath()</code> instead
     * @see FileUtilities#joinPath(java.io.File, java.lang.String[])
     */
    @Deprecated
    public static File joinPath(File f, String ... paths) {
        return FileUtilities.joinPath(f.getAbsolutePath(), paths);
    }
    
    /**
     * Displays a FileDialog to pick a file from user
     * 
     * @param path the path where the file dialog will be located, set to null 
     * to use the last location
     * @param selectDirectory 
     * @see FileDialog
     * @return <code>File</code> or null
     * @deprecated please use <code>FileChooser</code> instead
     * @see ui.FileChooser
     */
    @Deprecated
    public static File getFile(File path, boolean selectDirectory) {
        fileDialog.setFile(selectDirectory ? "*." : null);
        
        if (path != null)
            fileDialog.setDirectory(path.getAbsolutePath());
        
        fileDialog.setTitle("Select a file");
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        String data = fileDialog.getFile();
        if (data == null)
            return null;
        
        return FileUtilities.joinPath(fileDialog.getDirectory(), data);
    }
    
    /**
     * Displays a FileDialog to pick files from user
     * 
     * @param path the path where the file dialog will be located, set to null 
     * to use the last location
     * @see FileDialog
     * @return <code>File</code> array or null
     * @deprecated please use <code>FileChooser</code> instead
     * @see ui.FileChooser
     */
    @Deprecated
    public static File [] getFiles(File path) {
        if (path != null)
            fileDialog.setDirectory(path.getAbsolutePath());
        
        fileDialog.setTitle("Select file(s)");
        fileDialog.setMultipleMode(true);
        fileDialog.setVisible(true);
        return fileDialog.getFiles();
    }
    
    
    /**
     * Reads a plain text file
     * 
     * @param file file path
     * @return an string of file contents
     * @deprecated please use <code>FileUtils.readFile()</code> instead
     * @see FileUtilities#readFile
     */
    @Deprecated
    public static String readFile(File file) {
        return FileUtilities.readFile(file);
    }
    
    /**
     * Writes a plain text file
     * 
     * @param file file path
     * @param data string to write
     * @return true if success otherwise false
     * @deprecated please use <code>FileUtils.writeFile()</code> instead
     * @see FileUtilities#writeFile
     */
    @Deprecated
    public static boolean writeFile(File file, String data) {
        return FileUtilities.writeFile(file, data);
    }
    
    /**
     * Moves an object inside an ArrayList to a new index.<br>
     * Note this won't do anything if <br>
     * - <code>index == newIndex</code><br>
     * - index is less than 0<br>
     * - newIndex is less than 0<br>
     * - index is greater than array.size()<br>
     * - newIndex is greater than array.size()<br>
     * 
     * @param array the {@link ArrayList}
     * @param object the object to move
     * @param index the old index
     * @param newIndex the new index
     */
    public static void moveObject(ArrayList array, Object object, int index, int newIndex) {
        if (index == newIndex)
            return;
        
        if (index < 0 || newIndex < 0)
            return;
        if (index >= array.size() || newIndex >= array.size())
            return;
        
        if (index > newIndex) {
            array.remove(index);
            array.add(newIndex, object);
        } else {
            array.add(newIndex + 1, object);
            array.remove(index);
        }
    }
    
    /**
     * Creates a string containing the color components<br>
     * 
     * @param c the color
     * @return a string red-green-blue or an empty string if c is null
     */
    public static String colorToString(Color c) {
        if (c == null)
            return "";
        
        return "" + c.getRed() + "-" + c.getGreen() + "-" + c.getBlue();
    }
    
    /**
     * Extracts the components of a string color without alpha<br>
     * The format is the string must have is red-green-blue
     * 
     * @param value the string to extract info
     * @return a int[3] array or null if value is not a formatted color
     * @see LibUtilities#colorToString(java.awt.Color) 
     */
    public static int [] getColorData(String value) {
        boolean isColor = value.matches("[\\d]{1,3}-[\\d]{1,3}-[\\d]{1,3}");
        int [] colorData = null;
        if (isColor) {
            colorData = new int[3];
            
            String [] colorUData = value.split("-");
            for (int i = 0; i < 3; i++)
                colorData[i] = Integer.parseInt(colorUData[i]);
        }
        
        return colorData;
    }
    
    /**
     * Replaces the last coincidence inside an string
     * 
     * @param s the string to replace in
     * @param regex the regular expression
     * @param replacement the replacement
     * @return the string with the replacement
     */
    public static String replaceLast(String s, String regex, String replacement) {
        return s.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }

    /**
     * Extracts and splits data from a string.<br><br>
     * For example:<br>
     * <pre>
     *      dataID            = "DATA_RLP0"
     *      replacedStringIDs = ["DATA_RLP0", "DATA_RPL1"]
     *      replacedStrings   = ["PROPERTY2_2_1=9.9, PROPERTY2_2_2=true", "PROPERTY2_1=100, PROPERTY2_2=DATA_RPL0"]
     * </pre>
     * 
     * This method will return <code>["PROPERTY2_2_1=9.9", "PROPERTY2_2_2=true"]</code> and 
     * will remove <code>DATA_RLP0</code> from <code>replacedStringIDs</code> and
     * <code>"PROPERTY2_2_1=9.9, PROPERTY2_2_2=true"</code> from <code>replacedStrings</code><br>
     * 
     * @param dataID the identifier to get, for example DATA_RPL0
     * @param replacedStringIDs the list with the identifiers
     * @param replacedStrings the list with the replacements
     * @return an array containing all properties found, null if <code>dataID</code>
     * is not in <code>replacedStringIDs</code> or <code>new String[0]</code> 
     * if no data is found
     * @see utils.LibUtilities#REPLACEMENT_STR
     * @see utils.LibUtilities#compressStringHashMap(java.lang.String) 
     * @see utils.LibUtilities#parseProperties(java.lang.String) 
     */    
    public static String [] splitData(String dataID, LinkedList<String> replacedStringIDs, LinkedList<String> replacedStrings) {
//        System.out.println("splitData looking for " + lineFormID);
        
        int dataIndex = replacedStringIDs.indexOf(dataID);
        if (dataIndex == -1)
            return null;
        
        String data = replacedStrings.get(dataIndex).replace("{", "").replace("}", "");
        
        replacedStringIDs.remove(dataIndex);
        replacedStrings.remove(dataIndex);
        
        if (data.isEmpty())
            return new String[0];
        
        return data.split(", ");
    }
    
    /**
     * "Compresses" a string containing multiple hash maps to make it more manageable<br><br>
     * For example we have the string <code>s</code>:<br>
     * <pre>s={PROPERTY0=Hello, PROPERTY1=GRAY, PROPERTY2={PROPERTY2_1=100, PROPERTY2_2={PROPERTY2_2_1=9.9, PROPERTY2_2_2=true}}}</pre><br>
     * This method will return an array containing three elements<br>
     * <ul>
     * <li><code>String s</code> - the string with the replacements
     * <li><code>LinkedList-String- replacedStringIDs</code> - a list with the 
     * identifiers of the replacements
     * <li><code>LinkedList-String- replacedStrings</code> - a list with the 
     * replacements
     * </ul><br>
     * 
     * The result will be something like<br>
     * <pre>
     *      array[0] = "{PROPERTY0=Hello, PROPERTY1=GRAY, PROPERTY2=DATA_RPL1}"
     *      array[1] = ["DATA_RLP0", "DATA_RPL1"]
     *      array[2] = ["PROPERTY2_2_1=9.9, PROPERTY2_2_2=true", "PROPERTY2_1=100, PROPERTY2_2=DATA_RPL0"]
     * </pre>
     * 
     * @param s the HashMap string to "compress"
     * @return an array containing the processed string, a LinkedList containing
     * the identifiers of each string replaced and another LinkedList containing
     * the replacements. Both list have the same size
     * @see utils.LibUtilities#REPLACEMENT_STR
     * @see utils.LibUtilities#parseProperties(java.lang.String) 
     */
    public static Object [] compressStringHashMap(String s) {
        LinkedList<Integer> bracketOpeningIndexes = new LinkedList<>();
        LinkedList<Integer> bracketClosingIndexes = new LinkedList<>();

        for (int i = s.length() - 1; i > -1; i--)
            if (s.charAt(i) == '{')
                bracketOpeningIndexes.add(i);
            
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == '}')
                bracketClosingIndexes.add(i + 1);
        
        if (bracketOpeningIndexes.size() != bracketClosingIndexes.size())
            return null;
        
        
        LinkedList<String> replacedStringIDs = new LinkedList<>();
        LinkedList<String> replacedStrings = new LinkedList<>();

        int i = 0;
        while (bracketOpeningIndexes.size() > 1) {
//            System.out.println("\ni = " + i);
//            System.out.println("opening indexes " + Arrays.toString(bracketOpeningIndexes.toArray(new Integer[bracketOpeningIndexes.size()])));
//            System.out.println("closing indexes " + Arrays.toString(bracketClosingIndexes.toArray(new Integer[bracketClosingIndexes.size()])));
        
            int first = bracketOpeningIndexes.removeFirst();
            int closingBigger = first;
            int modifyIndex = -1;
            
            for (int j = 0; j < bracketClosingIndexes.size(); j++) {
                int index = bracketClosingIndexes.get(j);
                if (first < index) {
                    bracketClosingIndexes.remove(j);
                    modifyIndex = j;
                    closingBigger = index;
                    break;
                }
            }
            
//            System.out.println("first = " + first);
//            System.out.println("closingBigger = " + closingBigger);
//            System.out.println("modifyIndex = " + modifyIndex);
            
            String id = REPLACEMENT_STR + i;
            String originalString = s.substring(first, closingBigger);
//            System.out.println("replacement " + id);
//            System.out.println("subs " + originalString);
            
            replacedStringIDs.add(id);
            replacedStrings.add(originalString);
            
            s = replaceLast(s, originalString.replace("{", "\\{").replace("}", "\\}"), id);
            int len = originalString.length() - id.length();
            
            for (int j = modifyIndex; j < bracketClosingIndexes.size(); j++)
                bracketClosingIndexes.set(j, bracketClosingIndexes.get(j) - len);

            i++;
            
//            System.out.println("result  " +s);
//            System.out.println("slen " + s.length());
//            System.out.println("");
        }
        
        return new Object[]{s, replacedStringIDs, replacedStrings};
    }
    
    /**
     * Creates a simple HashMap from a string<br><br>
     * To parse more complex HashMaps follow the next steps:<br>
     * <ol>
     * <li> "Compress" the string with {@link utils.LibUtilities#compressStringHashMap(java.lang.String)}
     * <li> Convert the string given by the last step using this method
     * <li> Create the objects, if you find a property with {@link utils.LibUtilities#REPLACEMENT_STR}, 
     * decompress it by using {@link utils.LibUtilities#splitData(java.lang.String, java.util.LinkedList, java.util.LinkedList)} 
     * and parse it, repeat this step until all properties are loaded
     * </ol>
     * 
     * @param data the string to process, this method is meant for simple HashMaps, 
     * it's likely that it won't work if the data contains another HashMap
     * @return the data as HashMap
     */
    public static HashMap<String, String> parseProperties(String data) {
        if (data.contains("{"))
            data = data.replace("{", "").replace("}", "");
        
        String [] splitedData = data.replace("\n", "").split(", ");
        HashMap<String, String> parsedData = new HashMap<>();
        
        for (String s : splitedData) {
            String [] ssplit = s.split("=");
            if (ssplit.length != 2)
                continue;
            
            parsedData.put(ssplit[0], ssplit[1]);
        }
        
        return parsedData;
    }
    
    private static void setPreference(String key, String value) throws IllegalArgumentException {
        Preferences keyParse;
        
        try {
            keyParse = Preferences.valueOf(key);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return;
        }
        
        int [] colorData = getColorData(value);
        
        boolean isInteger = value.matches("[\\d]+");
        boolean isAPositiveNumber = value.matches("[\\d]+.[\\d]+");
        
        switch (keyParse) {
            case APPEARANCE:
                UIProperties.setLightThemeActive(value.equalsIgnoreCase("Light"));
            break;
            
            case USE_ACCENT_COLORS:
                UIProperties.setUseAccentColors(Boolean.parseBoolean(value));
            break;
            
            case PRIMARY_COLOR:
                if (colorData == null)
                    throw new IllegalArgumentException("Data '" + value + "' is not a color");
                UIProperties.OLD_APP_BG_COLOR = new Color(colorData[0], colorData[1], colorData[2]);
                UIProperties.APP_BG_COLOR = UIProperties.OLD_APP_BG_COLOR;
            break;
            
            case SECONDARY_COLOR:
                if (colorData == null)
                    throw new IllegalArgumentException("Data '" + value + "' is not a color");
                UIProperties.OLD_APP_BGA_COLOR = new Color(colorData[0], colorData[1], colorData[2]);
                UIProperties.APP_BGA_COLOR = UIProperties.OLD_APP_BGA_COLOR;
            break;
            
            case FOREGROUND_COLOR:
                if (colorData == null)
                    throw new IllegalArgumentException("Data '" + value + "' is not a color");
                UIProperties.OLD_APP_FG_COLOR = new Color(colorData[0], colorData[1], colorData[2]);
                UIProperties.APP_FG_COLOR = UIProperties.OLD_APP_FG_COLOR;
            break;
            
            case FONT_FAMILY:
                setFontName(value);
            break;
            
            case TITLE_FONT_WIDTH:
                if (!isInteger)
                    throw new IllegalArgumentException("Data '" + value + "' is not an integer");
                UIProperties.setTitleFontSize(Integer.parseInt(value));
            break;
            
            case SUBTITLE_FONT_WIDTH:
                if (!isInteger)
                    throw new IllegalArgumentException("Data '" + value + "' is not an integer");
                UIProperties.setSubtitleFontSize(Integer.parseInt(value));
            break;
            
            case STANDARD_FONT_WIDTH:
                if (!isInteger)
                    throw new IllegalArgumentException("Data '" + value + "' is not an integer");
                UIProperties.setStandardFontSize(Integer.parseInt(value));
            break;
            
            case UI_SCALE:
                if (!isAPositiveNumber)
                    throw new IllegalArgumentException("Data '" + value + "' is not a positive number");
                UIProperties.setUIScale(Float.parseFloat(value));
            break;
        }
    }
    
    private static void compilePreferences() {
        String oldAppBGColor = preferences.get(Preferences.PRIMARY_COLOR.name());
        String oldAppBGAColor = preferences.get(Preferences.SECONDARY_COLOR.name());
        String oldAppFGColor = preferences.get(Preferences.FOREGROUND_COLOR.name());
        
        preferences = new HashMap<>();
        
        preferences.put(Preferences.APPEARANCE.name(), UIProperties.isLightThemeActive() ? "Light" : "Dark");
        preferences.put(Preferences.USE_ACCENT_COLORS.name(), "" + UIProperties.usesAccentColors());
        preferences.put(Preferences.PRIMARY_COLOR.name(), UIProperties.usesAccentColors() ? colorToString(UIProperties.APP_BG_COLOR) : oldAppBGColor);
        preferences.put(Preferences.SECONDARY_COLOR.name(), UIProperties.usesAccentColors() ? colorToString(UIProperties.APP_BGA_COLOR) : oldAppBGAColor);
        preferences.put(Preferences.FOREGROUND_COLOR.name(), UIProperties.usesAccentColors() ? colorToString(UIProperties.APP_FG_COLOR) : oldAppFGColor);
        preferences.put(Preferences.FONT_FAMILY.name(), fontName);
        preferences.put(Preferences.TITLE_FONT_WIDTH.name(), "" + UIProperties.getTitleFontSize());
        preferences.put(Preferences.SUBTITLE_FONT_WIDTH.name(), "" + UIProperties.getSubtitleFontSize());
        preferences.put(Preferences.STANDARD_FONT_WIDTH.name(), "" + UIProperties.getStandardFontSize());
        preferences.put(Preferences.UI_SCALE.name(), "" + UIProperties.getUiScale());
    }
    
    /**
     * This will save all UI preferences inside a file
     * @return true if success
     * @see Preferences
     * @see LIB_PREFERENCES_FILE
     */
    public static boolean savePreferences() {
        if (!LIB_PREFERENCES_FILE.exists())
            try {
                LIB_PREFERENCES_FILE.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        
        if (!LIB_PREFERENCES_FILE.canWrite())
            return false;
        
        compilePreferences();
        
        return FileUtilities.writeFile(LIB_PREFERENCES_FILE, preferences.toString());
    }
    
    /**
     * This will restore to default all UI preferences
     * @see Preferences
     */
    public static void loadDefaultPreferences() {
        DEFAULT_PREFERENCES.forEach(
            (key, value) -> setPreference(key, value)
        );

        preferences = DEFAULT_PREFERENCES;
    }
    
    /**
     * Loads library preferences such as fonts, colors and selected size
     * 
     * @param preferencesWindow preferences window is needed to update the main 
     * window and any other component, it can be null
     * @return true if preferences were loaded and settled
     * @see Preferences
     */
    public static boolean loadPreferences(UIPreferences preferencesWindow) {
        if (!LIB_PREFERENCES_FILE.exists()) {
            loadDefaultPreferences();
            if (preferencesWindow != null)
                preferencesWindow.updatePreferences();
            
            return savePreferences();
        }
        
        if (!LIB_PREFERENCES_FILE.canRead())
            return false;
        
        String data = FileUtilities.readFile(LIB_PREFERENCES_FILE).replace("\n", "");
        if (data.replace(" ", "").isEmpty())
            return false;
        
        preferences = parseProperties(data);
        
        try {
            preferences.forEach(
                (key, value) -> setPreference(key, value)
            );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            
            loadDefaultPreferences();
            
            return false;
        }
        
        if (preferencesWindow != null)
            preferencesWindow.updatePreferences();
        
        return true;
    }
    
    /**
     * This method creates a ProcessBuilder to run a program.<br>
     * Some examples for this:
     * <pre>
     * $ foo bar bar1                 # For Unix-like OSes (program is on PATH)
     * $ /path/to/foo bar bar1        # Unix-like (local program, requires absolute path)
     * $ C:\path\to\foo.exe bar bar1  # Windows (only absolute path)
     * </pre>
     * <b>Note: this method will block the thread that called it.</b>
     * 
     * @param objectData the object in which the output and some other 
     * parameters are managed
     * @param args the full command to execute (all arguments must be separated)
     * @throws IOException
     * @throws InterruptedException 
     * @see utils.ProcessOutput#cwd
     * @see utils.LibUtilities#destroyProcessCalls
     * @see utils.ProcessOutput#haltProcess
     */
    public static void callProcess(ProcessOutput objectData, String ... args) throws IOException, InterruptedException {
        objectData.commandData = Arrays.toString(args);
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        
        processBuilder.redirectErrorStream(objectData.includeStderrData);
        if (objectData.cwd != null)
            processBuilder.directory(objectData.cwd);
        
        objectData.running = true;
        Process process = processBuilder.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        objectData.data = "";
        String line;
        while (process.isAlive() && !(destroyProcessCalls || objectData.haltProcess))
            while (reader.ready() && !(destroyProcessCalls || objectData.haltProcess)) {
                line = reader.readLine();
                if (line == null)
                    break;

                objectData.data += line + "\n";
            }
        
        if (reader.ready() && !(destroyProcessCalls || objectData.haltProcess)) {
            line = reader.readLine();
            if (line != null)
                objectData.data += line + "\n";
        }
        
        if (destroyProcessCalls || objectData.haltProcess)
            process.destroy();
        
        objectData.exitCode = process.waitFor();
        objectData.running = false;
        if (objectData.exitCode != 0) {
            objectData.commandData = "Error while running '" + objectData.commandData + "'";
            
            if (objectData.printDataOnNonZeroExitCode)
                System.out.println("  Output:\n" + objectData.data);
            
            if (objectData.throwExceptionOnNonZeroExitCode)
                throw new InterruptedException(objectData.commandData);
        }
    }
}
