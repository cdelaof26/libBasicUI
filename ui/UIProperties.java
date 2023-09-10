package ui;

import ui.enums.UIAlignment;
import ui.enums.TextAlignment;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultEditorKit;
import utils.LibUtilities;

/**
 * This class contains properties (colors and fonts) for UI<br>
 *
 * @author cristopher
 */
public class UIProperties {
    public static UIProperties properties = null;
    
    private UIProperties() { }
    
    public static UIProperties getInstance() {
        if (properties == null) {
            properties = new UIProperties();
        }
        
        return properties;
    }
    
    public final static int TITLE_BAR_HEIGHT;
    
    /**
     * Initializes UI look and feel
     */
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.exit(1);
        }
        
        JFrame f = new JFrame();
        f.setSize(100, 100);

        JPanel p = new JPanel();
        f.add(p);
        f.setVisible(true);

        TITLE_BAR_HEIGHT = f.getHeight() - p.getSize().height;

        f.setVisible(false);
        f.dispose();
        
        if (LibUtilities.SYSTEM_NAME.startsWith("Mac")) {
            InputMap inputMap = (InputMap) UIManager.get("TextField.focusInputMap");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.META_DOWN_MASK), DefaultEditorKit.selectAllAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.META_DOWN_MASK), DefaultEditorKit.cutAction);
            
            inputMap = (InputMap) UIManager.get("TextArea.focusInputMap");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.META_DOWN_MASK), DefaultEditorKit.selectAllAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.META_DOWN_MASK), DefaultEditorKit.cutAction);
        }
        
        initUIAccentColors();
        initFonts();
        
        System.out.println("[INFO] UIProperties initialized!");
    }
    
    /**
     * Initializes UI fonts
     * 
     * @throws IllegalStateException if LibUtilities has been not initialized
     */
    public static void initFonts() {
        APP_FONT = new Font(LibUtilities.getFontName(), Font.PLAIN, (int) (standardFontSize * uiScale));
        APP_MONOSPACED_FONT = new Font(Font.MONOSPACED, Font.PLAIN, (int) (standardFontSize * uiScale));
        APP_BOLD_FONT = new Font(LibUtilities.getFontName(), Font.BOLD, (int) (standardFontSize * uiScale));
        APP_SUBTITLE_FONT = new Font(LibUtilities.getFontName(), Font.BOLD, (int) (subtitleFontSize * uiScale));
        APP_BOLD_TITLE_FONT = new Font(LibUtilities.getFontName(), Font.BOLD, (int) (titleFontSize * uiScale));
        APP_TITLE_FONT = new Font(LibUtilities.getFontName(), Font.PLAIN, (int) (titleFontSize * uiScale));
    }
    
    /**
     * Initializes UI accent colors
     * 
     * @throws IllegalStateException if LibUtilities has been not initialized
     */
    public static void initUIAccentColors() {
        if (!accentColors) {
            OLD_APP_BG_COLOR = APP_BG_COLOR;
            OLD_APP_BGA_COLOR = APP_BGA_COLOR;
            OLD_APP_FG_COLOR = APP_FG_COLOR;
            
            APP_BG_COLOR = APP_FG;
            APP_BGA_COLOR = APP_FG;
            APP_FG_COLOR = APP_BG;
        } else {
            APP_BG_COLOR = OLD_APP_BG_COLOR;
            APP_BGA_COLOR = OLD_APP_BGA_COLOR;
            APP_FG_COLOR = OLD_APP_FG_COLOR;
        }
    }
    
    public static final Color DIM_TEXT_COLOR = new Color(120, 120, 120);
    
    public static final Color LIGHT_UI_BG = new Color(255, 255, 255);
    public static final Color LIGHT_UI_BGA = new Color(242, 242, 242);
    public static final Color LIGHT_UI_FG = new Color(0, 0, 0);
    public static final Color LIGHT_UI_WARNING_FG = new Color(125, 0, 0);
    
    public static final Color DARK_UI_BG = new Color(0, 0, 0);
    public static final Color DARK_UI_BGA = new Color(40, 40, 40);
    public static final Color DARK_UI_FG = new Color(255, 255, 255);
    public static final Color DARK_UI_WARNING_FG = new Color(255, 166, 33);
    
    
    /**
     * Color for background
     */
    public static Color APP_BG = LIGHT_UI_BG;
    /**
     * Color for background (auxiliary)
     */
    public static Color APP_BGA = LIGHT_UI_BGA;
    /**
     * Foreground app color
     */
    public static Color APP_FG = LIGHT_UI_FG;
    /**
     * Warning foreground app color
     */
    public static Color APP_FGW = LIGHT_UI_WARNING_FG;
    
    private static boolean lightThemeActive = true;
    private static boolean accentColors = true;
    
    
    /**
     * Accent color for background
     */
    public static Color APP_BG_COLOR;
    /**
     * Accent color for background (auxiliary)
     */
    public static Color APP_BGA_COLOR;
    /**
     * Color for accent color foreground
     */
    public static Color APP_FG_COLOR;
    
    public static Color OLD_APP_BG_COLOR;
    public static Color OLD_APP_BGA_COLOR;
    public static Color OLD_APP_FG_COLOR;
    
    /**
     * This value indicates how big the UI should be, the minimum value is 0.5f
     * and the maximum 2.0f, use {@link ui.UIProperties#setUIScale(float)} to
     * change its value
     */
    protected static float uiScale = 1;
    
    
    public static String [] AVAILABLE_FONTS = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    
    
    protected static int standardFontSize = 13;
    protected static int subtitleFontSize = 18;
    protected static int titleFontSize = 24;
    
    public static Font APP_FONT;
    public static Font APP_MONOSPACED_FONT;
    public static Font APP_BOLD_FONT;
    public static Font APP_SUBTITLE_FONT;
    public static Font APP_TITLE_FONT;
    public static Font APP_BOLD_TITLE_FONT;
    
    
    protected static int buttonRoundRadius = 10;
    
    protected static int checkboxSideLength = 22;
    
    protected static int sliderCircleRadius = 6;
    
    protected static int scrollbarWidth = 10;
    
    /**
     * Sets app palette to light colors<br><br>
     * 
     * After calling this, you should call {@link Window}.updateUITheme() and 
     * {@link Window}.updateUIColors() to update app colors
     * 
     */
    public static void setLightColor() {
        APP_BG = LIGHT_UI_BG;
        APP_BGA = LIGHT_UI_BGA;
        APP_FG = LIGHT_UI_FG;
        APP_FGW = LIGHT_UI_WARNING_FG;
        
        if (!accentColors) {
            APP_BG_COLOR = APP_FG;
            APP_BGA_COLOR = APP_FG;
            APP_FG_COLOR = APP_BG;
        }
        
        lightThemeActive = true;
    }
    
    /**
     * Sets app palette to dark colors<br><br>
     * 
     * After calling this, you should call {@link Window}.updateUITheme() and 
     * {@link Window}.updateUIColors() to update app colors
     * 
     */
    public static void setDarkColor() {
        APP_BG = DARK_UI_BG;
        APP_BGA = DARK_UI_BGA;
        APP_FG = DARK_UI_FG;
        APP_FGW = DARK_UI_WARNING_FG;
        
        if (!accentColors) {
            APP_BG_COLOR = APP_FG;
            APP_BGA_COLOR = APP_FG;
            APP_FG_COLOR = APP_BG;
        }
        
        lightThemeActive = false;
    }

    /**
     * Sets light or dark theme<br><br>
     * 
     * After calling this, you should call {@link Window}.updateUITheme() and 
     * {@link Window}.updateUIColors() to update app colors
     * 
     * @param lightTheme if false, dark palette will be set
     */
    public static void setLightThemeActive(boolean lightTheme) {
        UIProperties.lightThemeActive = lightTheme;
        if (UIProperties.lightThemeActive)
            setLightColor();
        else
            setDarkColor();
    }
    
    /**
     * Changes active palette<br><br>
     * 
     * After calling this, you should call {@link Window}.updateUITheme() and 
     * {@link Window}.updateUIColors() to update app colors
     * 
     */
    public static void toggleActiveTheme() {
        setLightThemeActive(!isLightThemeActive());
    }

    public static boolean isLightThemeActive() {
        return lightThemeActive;
    }
    
    public static boolean isDarkThemeActive() {
        return !lightThemeActive;
    }

    public static void setUseAccentColors(boolean accentColors) {
        UIProperties.accentColors = accentColors;
        initUIAccentColors();
    }
    
    public static boolean usesAccentColors() {
        return accentColors;
    }

    /**
     * Changes UI scale.<br>
     * 
     * After changing scale, you should call {@link Window#updateUISize()} and 
     * {@link Window#updateUIFont()} to apply changes<br>
     * Note that Window.updateUISize() and Window.updateUIFont won't change
     * the size/font of UI elements that weren't added to them, such as Dialog 
     * windows and ContextMenues
     * 
     * @param uiScale value between 0.5 and 2.0, default is 1.0
     * @throws IllegalArgumentException if uiScale outside the range [0.5, 2.0]
     */
    public static void setUIScale(float uiScale) throws IllegalArgumentException {
        if (uiScale < 0.5 || uiScale > 2)
            throw new IllegalArgumentException("Value outside range [0.5, 2.0]");
        
        UIProperties.uiScale = uiScale;
        
        buttonRoundRadius = (int) (10 * uiScale);
        checkboxSideLength = (int) (22 * uiScale);
        sliderCircleRadius = (int) (6 * uiScale);
        scrollbarWidth = (int) (10 * uiScale);
        
        initFonts();
    }

    public static float getUiScale() {
        return uiScale;
    }

    public static void setStandardFontSize(int standardFontSize) {
        UIProperties.standardFontSize = standardFontSize;
        UIProperties.initFonts();
    }

    public static int getStandardFontSize() {
        return standardFontSize;
    }

    public static void setSubtitleFontSize(int subtitleFontSize) {
        UIProperties.subtitleFontSize = subtitleFontSize;
        UIProperties.initFonts();
    }

    public static int getSubtitleFontSize() {
        return subtitleFontSize;
    }

    public static void setTitleFontSize(int titleFontSize) {
        UIProperties.titleFontSize = titleFontSize;
        UIProperties.initFonts();
    }

    public static int getTitleFontSize() {
        return titleFontSize;
    }
    
    public static String UIAlignmentToString(UIAlignment align) {
        switch (align) {
            case NORTH:
            return SpringLayout.NORTH;
            case SOUTH:
            return SpringLayout.SOUTH;
            case EAST:
            return SpringLayout.EAST;
            case WEST:
            return SpringLayout.WEST;
            case VERTICAL_CENTER:
            return SpringLayout.VERTICAL_CENTER;
            case HORIZONTAL_CENTER:
            return SpringLayout.HORIZONTAL_CENTER;
        }
        
        throw new UnsupportedOperationException("Not supported.");
    }
    
    public static int TextAlignmentToInt(TextAlignment align) {
        switch (align) {
            case CENTER:
            return SwingConstants.CENTER;
            case LEFT:
            return SwingConstants.LEFT;
            case RIGHT:
            return SwingConstants.RIGHT;
        }
        
        throw new UnsupportedOperationException("Not supported.");
    }
    
    public static String [] PRESET_ACCENT_COLORS_NAME = {
        "Default",
        "Gray",
        "Blue",
        "Aqua blue",
        "Purple",
        "Dark violet",
        "Pink",
        "Cherry blossom",
        "Red",
        "Crimsom red",
        "Orange",
        "Peach",
        "Yellow",
        "Pineapple",
        "Green",
        "Lime"
    };
    
    public static Color [] PRESET_ACCENT_COLORS = {
        // Blue default
        new Color(62, 141, 216),
        new Color(12, 91, 146),
        // gray
        new Color(100, 100, 100),
        new Color(80, 80, 80),
        // blue
        new Color(4, 125, 250),
        new Color(0, 105, 230),
        // Aqua blue
        new Color(10, 161, 221),
        new Color(0, 141, 201),
        // purple
        new Color(111, 91, 223),
        new Color(81, 61, 193),
        // dark violet
        new Color(136, 0, 199),
        new Color(116, 0, 179),
        // pink
        new Color(242, 47, 108),
        new Color(222, 27, 88),
        // cherry blossom
        new Color(235, 146, 169),
        new Color(215, 126, 149),
        // red
        new Color(200, 0, 0),
        new Color(180, 0, 0),
        // crimson red
        new Color(174, 34, 62),
        new Color(154, 14, 42),
        // orange
        new Color(246, 101, 24),
        new Color(226, 81, 4),
        // orange peach
        new Color(253, 151, 67),
        new Color(233, 131, 47),
        // yellow
        new Color(193, 175, 0),
        new Color(173, 155, 0),
        // pineaple yellow
        new Color(234, 173, 0),
        new Color(214, 153, 0),
        // green
        new Color(124, 178, 50),
        new Color(104, 158, 30),
        // lime green
        new Color(164, 176, 46),
        new Color(144, 156, 26)
    };
}
