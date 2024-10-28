package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import ui.enums.LabelType;
import ui.enums.TextAlignment;
import javax.swing.JLabel;
import utils.LibUtilities;

/**
 * Custom painted JLabel
 *
 * @author cristopher
 */
public class Label extends JLabel implements ComponentSetup, UIFont {
    private LabelType fontType;
    
    private int fontSize = UIProperties.standardFontSize;
    private String fontFamily = LibUtilities.getFontName();
    private boolean boldFont = false;
    private boolean italicFont = false;
    private boolean monospacedFont = false;
    private boolean customColor = false;
    private Color fontColor = null;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see Label#setUseAppTheme(boolean)
     */
    protected boolean appTheme = true;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see Label#setUseAppColor(boolean) 
     */
    protected boolean appColor = false;
    
    /**
     * Creates a new Label without text
     * 
     * @param fontType the font to be used
     * @see ui.enums.LabelType
     */
    public Label(LabelType fontType) {
        this.fontType = fontType;
        
        initUI();
    }
    
    /**
     * Creates a new Label with text
     * 
     * @param fontType the font to be used
     * @param text the text
     * @see ui.enums.LabelType
     */
    public Label(LabelType fontType, String text) {
        super(text);
        
        this.fontType = fontType;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        setTextAlignment(TextAlignment.CENTER);
        
        updateUIFont();
        updateUITheme();
    }

    @Override
    public void updateUISize() {
        updateUIFont();
    }

    @Override
    public void updateUIFont() {
        switch (fontType) {
            case TITLE:
                setFont(UIProperties.APP_TITLE_FONT);
            break;
            case BOLD_TITLE:
                setFont(UIProperties.APP_BOLD_TITLE_FONT);
            break;
            case SUBTITLE:
                setFont(UIProperties.APP_SUBTITLE_FONT);
            break;
            case BODY:
                setFont(UIProperties.APP_FONT);
            break;
            case BOLD_BODY:
                setFont(UIProperties.APP_BOLD_FONT);
            break;
            case WARNING_LABEL:
                setFont(UIProperties.APP_BOLD_FONT);
            break;
            case CUSTOM:
                int fontStyle;
                if (!boldFont && !italicFont)
                    fontStyle = Font.PLAIN;
                else if (boldFont && italicFont)
                    fontStyle = Font.BOLD | Font.ITALIC;
                else
                    fontStyle = boldFont ? Font.BOLD : Font.ITALIC;
                
                Font f = new Font(monospacedFont ? Font.MONOSPACED : fontFamily, fontStyle, (int) (fontSize * UIProperties.uiScale));
                setFont(f);
            break;
        }
    }

    @Override
    public void updateUITheme() {
        if (appTheme)
            if (fontType == LabelType.WARNING_LABEL)
                setForeground(UIProperties.APP_FGW);
            else
                setForeground(UIProperties.APP_FG);
    }

    @Override
    public void updateUIColors() {
        if (appColor)
            if (fontType == LabelType.WARNING_LABEL)
                setForeground(UIProperties.APP_FGW);
            else
                setForeground(UIProperties.APP_FG_COLOR);
    }
    
    @Override
    public void setUseAppTheme(boolean useAppTheme) {
        this.appTheme = useAppTheme;
        this.appColor = !useAppTheme;
        
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void setUseAppColor(boolean useAppColor) {
        this.appColor = useAppColor;
        this.appTheme = !useAppColor;
        
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void setRoundCorners(boolean roundCorners) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPaintBorder(boolean paintBorder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void validateFontType() {
        assert fontType == LabelType.CUSTOM;
    }
    
    @Override
    public void setFontPointSize(int fontSize) {
        if (fontSize < 0)
            throw new IllegalArgumentException("Size cannot be negative");
        
        validateFontType();
        
        this.fontSize = fontSize;
        updateUIFont();
    }

    @Override
    public void setFontFamily(String fontFamily) {
        validateFontType();
        
        this.fontFamily = fontFamily;
        updateUIFont();
    }

    @Override
    public void setFontBold(boolean boldFont) {
        validateFontType();
        
        this.boldFont = boldFont;
        updateUIFont();
    }

    @Override
    public void setFontItalic(boolean italicFont) {
        validateFontType();
        
        this.italicFont = italicFont;
        updateUIFont();
    }

    @Override
    public void setFontMonospaced(boolean monospacedFont) {
        validateFontType();
        
        this.monospacedFont = monospacedFont;
        updateUIFont();
    }
    
    @Override
    public void useCustomFontColor(boolean customColor) {
        validateFontType();
        
        this.customColor = customColor;
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void setFontColor(Color fontColor) {
        validateFontType();
        
        this.customColor = true;
        this.fontColor = fontColor;
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void setForeground(Color fg) {
        if (customColor)
            fg = fontColor;
        
        super.setForeground(fg);
    }
    
    /**
     * Changes font type
     * 
     * @param fontType the font to be used
     * @see ui.enums.LabelType
     */
    public void setLabelType(LabelType fontType) {
        this.fontType = fontType;
        updateUIFont();
    }

    /**
     * @return the font type this Label uses
     */
    public LabelType getLabelType() {
        return fontType;
    }

    /**
     * Sets the text alignment
     * 
     * @param textAlignment the position of the text
     * @see ui.enums.TextAlignment
     */
    public void setTextAlignment(TextAlignment textAlignment) {
        setHorizontalAlignment(UIProperties.TextAlignmentToInt(textAlignment));
    }
    
    /**
     * Adds mouse listener to this label. If clicked, the component C will be clicked
     * 
     * @param c JButton or ColorButton (for hover effect)
     */
    public void ifClickedDoClick(JComponent c) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((JButton) c).doClick(1);
                c.requestFocus();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (c instanceof ColorButton) {
                    ((ColorButton) c).paintAsHovering = true;
                    ((ColorButton) c).repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (c instanceof ColorButton) {
                    ((ColorButton) c).paintAsHovering = false;
                    ((ColorButton) c).repaint();
                }
            }
        });
    }
    
    /**
     * Adds mouse listener to this label which will do click a given button
     * 
     * @param b the JButton
     * @param highlightButton if true the button will be highlight if it's 
     * a {@link ColorButton}
     */
    public void ifClickedDoClick(JButton b, boolean highlightButton) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((JButton) b).doClick(1);
                b.requestFocus();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (b instanceof ColorButton && highlightButton) {
                    ((ColorButton) b).paintAsHovering = true;
                    ((ColorButton) b).repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (b instanceof ColorButton && highlightButton) {
                    ((ColorButton) b).paintAsHovering = false;
                    ((ColorButton) b).repaint();
                }
            }
        });
    }
}
