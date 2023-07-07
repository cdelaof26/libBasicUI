package utils;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import ui.CheckBox;
import ui.ColorButton;
import ui.ColorPicker;
import ui.ComboBox;
import ui.Dialog;
import ui.Label;
import ui.NumberSelector;
import ui.UIProperties;
import ui.Window;
import ui.enums.LabelType;
import ui.enums.UIAlignment;

/**
 * Graphical window to manage look and feel of the library
 *
 * @author cristopher
 */
public class UIPreferences extends Dialog {
    private final Label title = new Label(LabelType.BOLD_TITLE, "UI Preferences");
    private final Label colorsSubtitle = new Label(LabelType.SUBTITLE, "Color");
    private final Label fontSubtitle = new Label(LabelType.SUBTITLE, "Font");
    private final Label uiSubtitle = new Label(LabelType.SUBTITLE, "UI");
    
    private final ColorPicker colorPicker = new ColorPicker();
    
    private final ComboBox themeOptions = new ComboBox("Appearance", UIProperties.isLightThemeActive() ? "Light" : "Dark", false);
    private final CheckBox useAccentColorsCheckButton = new CheckBox(true);
    private final Label useAccentColorsLabel = new Label(LabelType.BODY, "Use accent colors");
    private final ColorButton selectorAccentColor1 = new ColorButton("Select primary color");
    private final ColorButton selectorAccentColor2 = new ColorButton("Select secondary color");
    private final ColorButton selectorAccentColor3 = new ColorButton("Select foreground color");
    private final ComboBox colorPresets = new ComboBox("Preset", "", true);
    
    
    private final ComboBox fontOptions = new ComboBox("Font", LibUtilities.getFontName(), true);
    
    private final JLabel titleFontUpdater;
    private final JLabel subtitleFontUpdater;
    private final JLabel fontUpdater;
    
    private final NumberSelector titleFontSizeSelector;
    private final NumberSelector subtitleFontSizeSelector;
    private final NumberSelector fontSizeSelector;
    
    
    private final JLabel uiScaleUpdater;
    private final NumberSelector uiScale;
    
    
    private final Window mainWindow;
    
    
    public UIPreferences(Window mainWindow) {
        super(450, 365);
        
        this.mainWindow = mainWindow;
        
        setResizable(false);
        
        themeOptions.setPreferredSize(new Dimension(210, 22));
        themeOptions.addOption("Light", "utils/assets/lSun.png", "utils/assets/dSun.png", "utils/assets/dSun.png", true, false, (Action) -> {
            UIProperties.setLightColor();
            mainWindow.updateUITheme();
            mainWindow.updateUIColors();
            updateUITheme();
            updateUIColors();
            
            LibUtilities.savePreferences();
        });
        themeOptions.addOption("Dark", "utils/assets/lMoon.png", "utils/assets/dMoon.png", "utils/assets/dMoon.png", true, false, (Action) -> {
            UIProperties.setDarkColor();
            mainWindow.updateUITheme();
            mainWindow.updateUIColors();
            updateUITheme();
            updateUIColors();
            
            LibUtilities.savePreferences();
        });
        
        useAccentColorsCheckButton.addActionListener((Action) -> {
            UIProperties.setUseAccentColors(!useAccentColorsCheckButton.isChecked());
            mainWindow.updateUIColors();
            mainWindow.updateUITheme();
            updateUIColors();
            updateUITheme();
            
            LibUtilities.savePreferences();
        });
        useAccentColorsLabel.ifClickedDoClick(useAccentColorsCheckButton);
        
        selectorAccentColor1.setPreferredSize(new Dimension(210, 22));
        selectorAccentColor1.addActionListener((Action) -> {
            colorPicker.setSelectedColor(UIProperties.OLD_APP_BG_COLOR, true);
            Color c = colorPicker.pickColor();
            if (c == null)
                return;
            
            useAccentColorsCheckButton.setChecked(true);
            UIProperties.setUseAccentColors(true);
            UIProperties.APP_BG_COLOR = c;
            UIProperties.OLD_APP_BG_COLOR = c;
            
            mainWindow.updateUIColors();
            mainWindow.updateUITheme();
            updateUIColors();
            updateUITheme();

            LibUtilities.savePreferences();
        });
        
        selectorAccentColor2.setPreferredSize(new Dimension(210, 22));
        selectorAccentColor2.addActionListener((Action) -> {
            colorPicker.setSelectedColor(UIProperties.OLD_APP_BGA_COLOR, true);
            Color c = colorPicker.pickColor();
            if (c == null)
                return;
            
            useAccentColorsCheckButton.setChecked(true);
            UIProperties.setUseAccentColors(true);
            UIProperties.APP_BGA_COLOR = c;
            UIProperties.OLD_APP_BGA_COLOR = c;
            
            mainWindow.updateUIColors();
            mainWindow.updateUITheme();
            updateUIColors();
            updateUITheme();

            LibUtilities.savePreferences();
        });
        
        selectorAccentColor3.setPreferredSize(new Dimension(210, 22));
        selectorAccentColor3.addActionListener((Action) -> {
            colorPicker.setSelectedColor(UIProperties.OLD_APP_FG_COLOR, true);
            Color c = colorPicker.pickColor();
            if (c == null)
                return;
            
            useAccentColorsCheckButton.setChecked(true);
            UIProperties.setUseAccentColors(true);
            UIProperties.APP_FG_COLOR = c;
            UIProperties.OLD_APP_FG_COLOR = c;
            
            mainWindow.updateUIColors();
            mainWindow.updateUITheme();
            updateUIColors();
            updateUITheme();

            LibUtilities.savePreferences();
        });
        
        colorPresets.setPreferredSize(new Dimension(210, 22));
        for (int i = 0; i < UIProperties.PRESET_ACCENT_COLORS_NAME.length; i++) {
            final int j = i;
            colorPresets.addOption(UIProperties.PRESET_ACCENT_COLORS_NAME[j], false, (Action) -> {
                UIProperties.APP_BG_COLOR = UIProperties.PRESET_ACCENT_COLORS[j * 2];
                UIProperties.APP_BGA_COLOR = UIProperties.PRESET_ACCENT_COLORS[(j * 2) + 1];
                UIProperties.APP_FG_COLOR = UIProperties.DARK_UI_FG;
                UIProperties.OLD_APP_BG_COLOR = UIProperties.APP_BG_COLOR;
                UIProperties.OLD_APP_BGA_COLOR = UIProperties.APP_BGA_COLOR;
                UIProperties.OLD_APP_FG_COLOR = UIProperties.APP_FG_COLOR;
                
                useAccentColorsCheckButton.setChecked(true);
                UIProperties.setUseAccentColors(true);
                
                mainWindow.updateUIColors();
                mainWindow.updateUITheme();
                updateUIColors();
                updateUITheme();

                LibUtilities.savePreferences();
            });
        }
        
        
        fontOptions.setPreferredSize(new Dimension(210, 22));
        for (String name : UIProperties.AVAILABLE_FONTS)
            fontOptions.addOption(name, false, (Action) -> {
                LibUtilities.setFontName(name);
                mainWindow.updateUIFont();
                updateUIFont();
                
                LibUtilities.savePreferences();
            });
        
        
        titleFontUpdater = new JLabel() {
            @Override
            public void setText(String text) {
                if (text.isEmpty())
                    return;
                UIProperties.setTitleFontSize(Integer.parseInt(text));
                mainWindow.updateUIFont();
                updateUIFont();
                
                LibUtilities.savePreferences();
            }
        };
        titleFontSizeSelector = new NumberSelector("Title width", "  24pt", 24, 16, 100, 1, titleFontUpdater);
        titleFontSizeSelector.setPreferredSize_(new Dimension(210, 22));
        
        subtitleFontUpdater = new JLabel() {
            @Override
            public void setText(String text) {
                if (text.isEmpty())
                    return;
                UIProperties.setSubtitleFontSize(Integer.parseInt(text));
                mainWindow.updateUIFont();
                updateUIFont();
                
                LibUtilities.savePreferences();
            }
        };
        subtitleFontSizeSelector = new NumberSelector("Subtitle width", "  18pt", 18, 10, 94, 1, subtitleFontUpdater);
        subtitleFontSizeSelector.setPreferredSize_(new Dimension(210, 22));
        
        fontUpdater = new JLabel() {
            @Override
            public void setText(String text) {
                if (text.isEmpty())
                    return;
                UIProperties.setStandardFontSize(Integer.parseInt(text));
                mainWindow.updateUIFont();
                updateUIFont();
                
                LibUtilities.savePreferences();
            }
        };
        fontSizeSelector = new NumberSelector("Font width", "  13pt", 13, 5, 89, 1, fontUpdater);
        fontSizeSelector.setPreferredSize_(new Dimension(210, 22));
        
        
        uiScaleUpdater = new JLabel() {
            @Override
            public void setText(String text) {
                if (text.isEmpty())
                    return;
                UIProperties.setUIScale(Float.parseFloat(text) / 100f);
                mainWindow.updateUIFont();
                mainWindow.updateUISize();
                updateUIFont();
                updateUISize();
                
                LibUtilities.savePreferences();
            }
        };
        uiScale = new NumberSelector("UI scale", "  100%", 100, 50, 200, 10, uiScaleUpdater);
        uiScale.setPreferredSize_(new Dimension(430, 22));
        
        
        add(title, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.NORTH, UIAlignment.NORTH, 10);
        add(colorsSubtitle, title, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 5);
        add(themeOptions, colorsSubtitle, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(useAccentColorsCheckButton, themeOptions, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(useAccentColorsLabel, useAccentColorsCheckButton, UIAlignment.WEST, UIAlignment.EAST, 5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(selectorAccentColor1, themeOptions, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(selectorAccentColor2, selectorAccentColor1, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(selectorAccentColor3, selectorAccentColor1, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(colorPresets, selectorAccentColor3, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        add(fontSubtitle, selectorAccentColor3, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(fontOptions, fontSubtitle, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(titleFontSizeSelector, fontOptions, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(subtitleFontSizeSelector, fontOptions, UIAlignment.EAST, UIAlignment.EAST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(fontSizeSelector, subtitleFontSizeSelector, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        add(uiSubtitle, subtitleFontSizeSelector, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(uiScale, uiSubtitle, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
    }

    @Override
    public void updateUISize() {
        if (colorPicker != null)
            colorPicker.updateUISize();
        
        super.updateUISize();
    }

    @Override
    public void updateUIFont() {
        if (colorPicker != null)
            colorPicker.updateUIFont();
        
        super.updateUIFont();
    }
    
    @Override
    public void updateUITheme() {
        if (colorPicker != null)
            colorPicker.updateUITheme();
        
        super.updateUITheme();
    }

    @Override
    public void updateUIColors() {
        if (colorPicker != null)
            colorPicker.updateUIColors();
        
        super.updateUIColors();
    }
    
    public void updatePreferences() {
        themeOptions.setText(UIProperties.isLightThemeActive() ? "Light" : "Dark");
        useAccentColorsCheckButton.setChecked(UIProperties.usesAccentColors());
        fontOptions.setText(LibUtilities.getFontName());
        if (UIProperties.getTitleFontSize() != Integer.parseInt(LibUtilities.DEFAULT_PREFERENCES.get(LibUtilities.Preferences.TITLE_FONT_WIDTH.name())))
            titleFontSizeSelector.setValue(UIProperties.getTitleFontSize());
        if (UIProperties.getSubtitleFontSize()!= Integer.parseInt(LibUtilities.DEFAULT_PREFERENCES.get(LibUtilities.Preferences.SUBTITLE_FONT_WIDTH.name())))
            titleFontSizeSelector.setValue(UIProperties.getSubtitleFontSize());
        if (UIProperties.getStandardFontSize()!= Integer.parseInt(LibUtilities.DEFAULT_PREFERENCES.get(LibUtilities.Preferences.STANDARD_FONT_WIDTH.name())))
            titleFontSizeSelector.setValue(UIProperties.getStandardFontSize());
        if (UIProperties.getUiScale() < 1 || UIProperties.getUiScale() > 1)
            uiScale.setValue((int) (UIProperties.getUiScale() * 100));
        
        mainWindow.updateUISize();
        mainWindow.updateUIFont();
        mainWindow.updateUITheme();
        mainWindow.updateUIColors();
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }
}
