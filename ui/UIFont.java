package ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;

/**
 * Interface to personalize UI text elements
 * 
 * @author cristopher
 */
public interface UIFont {
    /**
     * Sets the font size for this component.<br>
     * <b>Note</b>: This font size will be automatically resized if the 
     * {@link ui.UIProperties#uiScale} changes. Static font sizes require manual 
     * handling.
     * 
     * @param fontSize the point size
     * @throws IllegalArgumentException if size is negative
     * @see ui.enums.LabelType#NONE
     */
    public void setFontPointSize(int fontSize);
    
    /**
     * Changes the font family for this element
     * @param fontFamily the family's name
     */
    public void setFontFamily(String fontFamily);
    
    /**
     * Makes the text bold
     * @param boldFont if true {@link Font#BOLD} will be applied as style, 
     * otherwise {@link Font#PLAIN}
     */
    public void setFontBold(boolean boldFont);
    
    /**
     * Makes the text italic
     * @param italicFont if true {@link Font#ITALIC} will be applied as style, 
     * this property can be combined with {@link ui.UIFont#setFontBold(boolean)}
     */
    public void setFontItalic(boolean italicFont);
    
    /**
     * Changes the font family to {@link Font#MONOSPACED}
     * @param monospacedFont set to true to apply monospaced font to this component 
     */
    public void setFontMonospaced(boolean monospacedFont);
    
    /**
     * Changes the default foreground color for this component
     * <b>Note</b>: Setting this to true will void any call to {@link JComponent#setForeground(java.awt.Color)}
     * 
     * @param customColor if true, the color settled with {@link ui.UIFont#setFontColor(java.awt.Color)}
     * will be used, otherwise app colors automatically set by calling {@link ui.ComponentSetup#updateUITheme()} 
     * or {@link ui.ComponentSetup#updateUIColors()}
     */
    public void useCustomFontColor(boolean customColor);
    
    /**
     * Sets the color for this component
     * @param fontColor the new color
     * @see ui.UIFont#useCustomFontColor(boolean)
     */
    public void setFontColor(Color fontColor);
}
