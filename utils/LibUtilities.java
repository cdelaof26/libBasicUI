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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
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
    private static boolean init = false;
    
    /**
     * Available preferences for libBasicUI
     */
    public enum Preferences {
        APPEARANCE, USE_ACCENT_COLORS, PRIMARY_COLOR, SECONDARY_COLOR, FOREGROUND_COLOR, 
        FONT_FAMILY, TITLE_FONT_WIDTH, SUBTITLE_FONT_WIDTH, STANDARD_FONT_WIDTH, UI_SCALE
    }
    
    /**
     * Initializes lib preferences
     */
    public static void initLibUtils() {
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
        
        init = true;
        
        System.out.println("[INFO] libBasicUI v0.0.4");
        System.out.println("[INFO] LibUtilities initialized!");
    }
    
    public static boolean isInit() {
        return init;
    }
    
    
    private static final Decoder decoder = Base64.getDecoder();
    private static final FileDialog fileDialog = new FileDialog((JFrame) null, "Select a file", FileDialog.LOAD);
    
    private static final AffineTransform affinetransform = new AffineTransform();     
    private static final FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
    
    public static final int MOD_KEY = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
    public static final String SYSTEM_NAME = System.getProperty("os.name");
    public static final String USER_HOME = System.getProperty("user.home");
    public static final boolean IS_UNIX_LIKE = !SYSTEM_NAME.startsWith("Windows");
    
    
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
    
    public static final HashMap<String, String> DEFAULT_PREFERENCES = new HashMap<>();
    protected static HashMap<String, String> preferences = new HashMap<>();
    
    
    protected static String fontName = !IS_UNIX_LIKE ? "Segoe UI" : SYSTEM_NAME.startsWith("Mac") ? "Helvetica Neue" : "";
    
    
    /**
     * Sets library's font, you should call {@link Window}.updateUIFont() to apply
     * changes
     * 
     * @param fontName
     */
    public static void setFontName(String fontName) {
        LibUtilities.fontName = fontName;
        UIProperties.initFonts();
    }

    /**
     * @return string containing the current font name
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
     * @param imagePath
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
     * @param imagePath
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
     * @param image
     * @param width
     * @param height
     * @return an ImageIcon
     */
    public static ImageIcon scaleImage(BufferedImage image, int width, int height) {
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
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
     * @param text
     * @param font
     * @return a new dimension
     * @see FontRenderContext
     */
    public static Dimension getTextDimensions(String text, Font font) {
        Rectangle2D r2D = font.getStringBounds(text, frc);
        
        return new Dimension((int) r2D.getWidth(), (int) r2D.getHeight());
    }
    
    
    /**
     * Adds a keybinding to the c component, by default key bindings added with
     * this method will be invoked WHEN_IN_FOCUSED_WINDOW
     * @param c
     * @param name
     * @param keySequense
     * @param action 
     */
    public static void addKeyBindingTo(JComponent c, String name, String keySequense, AbstractAction action) { 
        c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keySequense), name);
        c.getActionMap().put(name, action);
    }
    
    /**
     * Adds a keybinding to the c component
     * @param c
     * @param name
     * @param keySequense
     * @param onlyWhenFocused if true, JComponent.WHEN_FOCUSED is settled 
     * otherwise WHEN_IN_FOCUSED_WINDOW
     * @param action 
     */
    public static void addKeyBindingTo(JComponent c, String name, String keySequense, boolean onlyWhenFocused, AbstractAction action) { 
        c.getInputMap(onlyWhenFocused ? JComponent.WHEN_FOCUSED : JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keySequense), name);
        c.getActionMap().put(name, action);
    }
    
    /**
     * Adds a keybinding to the c component, by default key bindings added with
     * this method will be invoked WHEN_IN_FOCUSED_WINDOW
     * @param c
     * @param name
     * @param keySequense
     * @param action 
     */
    public static void addKeyBindingTo(JComponent c, String name, KeyStroke keySequense, AbstractAction action) { 
        c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keySequense, name);
        c.getActionMap().put(name, action);
    }
    
    /**
     * Adds a keybinding to the c component
     * @param c
     * @param name
     * @param keySequense
     * @param onlyWhenFocused if true, JComponent.WHEN_FOCUSED is settled 
     * otherwise WHEN_IN_FOCUSED_WINDOW
     * @param action 
     */
    public static void addKeyBindingTo(JComponent c, String name, KeyStroke keySequense, boolean onlyWhenFocused, AbstractAction action) { 
        c.getInputMap(onlyWhenFocused ? JComponent.WHEN_FOCUSED : JComponent.WHEN_IN_FOCUSED_WINDOW).put(keySequense, name);
        c.getActionMap().put(name, action);
    }
    
    /**
     * Sets alpha value to a <code>c</code> color
     * 
     * @param c
     * @param alpha 
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
     * @deprecated please use <code>FileUtils.joinPath()<code> instead
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
     * @deprecated please use <code>FileUtils.joinPath()<code> instead
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
     * @see ui.filebrowser.FileChooser
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
     * Creates a string containing the color components<br>
     * The format is: red-green-blue
     * 
     * @param c
     * @return 
     */
    public static String colorToString(Color c) {
        return "" + c.getRed() + "-" + c.getGreen() + "-" + c.getBlue();
    }
    
    private static void setPreference(String key, String value) throws IllegalArgumentException {
        Preferences keyParse = Preferences.valueOf(key);
        
        boolean isColor = value.matches("[\\d]{1,3}-[\\d]{1,3}-[\\d]{1,3}");
        int [] colorData = new int[3];
        if (isColor) {
            String [] colorUData = value.split("-");
            for (int i = 0; i < 3; i++)
                colorData[i] = Integer.parseInt(colorUData[i]);
        }
        
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
                if (!isColor)
                    throw new IllegalArgumentException("Data '" + value + "' is not a color");
                UIProperties.OLD_APP_BG_COLOR = new Color(colorData[0], colorData[1], colorData[2]);
                UIProperties.APP_BG_COLOR = UIProperties.OLD_APP_BG_COLOR;
            break;
            
            case SECONDARY_COLOR:
                if (!isColor)
                    throw new IllegalArgumentException("Data '" + value + "' is not a color");
                UIProperties.OLD_APP_BGA_COLOR = new Color(colorData[0], colorData[1], colorData[2]);
                UIProperties.APP_BGA_COLOR = UIProperties.OLD_APP_BGA_COLOR;
            break;
            
            case FOREGROUND_COLOR:
                if (!isColor)
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
    
    private static void parsePreferences() throws IllegalArgumentException {
        DEFAULT_PREFERENCES.forEach(
            (key, value) -> {
                if (!preferences.containsKey(key))
                    preferences.put(key, value);
            }
        );
        
        preferences.forEach(
            (key, value) -> setPreference(key, value)
        );
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
        
        data = data.replace("{", "").replace("}", "");
        
        String [] sdata = data.split(",");
        
        preferences = new HashMap<>();
        for (String p : sdata) {
            String [] dat = p.replaceAll("^ +", "").split("=");
            if (dat.length != 2)
                continue;
            
            preferences.put(dat[0], dat[1]);
        }
        
        try {
            parsePreferences();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            
            loadDefaultPreferences();
            
            return false;
        }
        
        if (preferencesWindow != null)
            preferencesWindow.updatePreferences();
        
        return true;
    }
}
