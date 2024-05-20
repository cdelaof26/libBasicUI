package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import ui.enums.LabelType;
import ui.enums.TextAlignment;

/**
 * Custom painted text field
 * 
 * @author cristopher
 */
public class TextField extends JTextField implements ComponentSetup {
    protected int width = 120, height = 22;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see TextField#setUseAppTheme(boolean)
     */
    protected boolean appTheme = false;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see TextField#setUseAppColor(boolean) 
     */
    protected boolean appColor = true;
    
    
    /**
     * Condition that determines if the corner of this button should be rounded
     * @see TextField#setRoundCorners(boolean)
     * @see UIProperties#buttonRoundRadius
     */
    protected boolean roundCorners = true;
    
    /**
     * Condition that determines if the border is going to be painted
     */
    protected boolean paintBorder = false;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see TextField#setUseOnlyAppColor(boolean)
     */
    protected boolean onlyAppColor = false;
    
    protected boolean visibleBackground = true;
    
    public boolean placeholderVisible;
    private String placeholderText = "";
    
    private LabelType fontType = LabelType.BODY;
    private final boolean editableOnCLick;
    
    
    /**
     * Creates a new TextField with a placeholder text
     * 
     * @param placeholderText the placeholder text
     */
    public TextField(String placeholderText) {
        this.placeholderText = placeholderText;
        this.editableOnCLick = false;
        initUI();
    }
    
    /**
     * Creates a new TextField
     */
    public TextField() {
        this.editableOnCLick = false;
        initUI();
    }
    
    /**
     * Creates a new TextField with a placeholder text
     * 
     * @param placeholderText the placeholder text
     * @param editableOnCLick if true, the TextField will be editable/focusable
     * once the user clicks on it
     */
    public TextField(String placeholderText, boolean editableOnCLick) {
        this.placeholderText = placeholderText;
        this.editableOnCLick = editableOnCLick;
        initUI();
    }
    
    /**
     * Creates a new TextField
     * @param editableOnCLick if true, the TextField will be editable/focusable
     * once the user clicks on it
     */
    public TextField(boolean editableOnCLick) {
        this.editableOnCLick = editableOnCLick;
        
        initUI();
    }
    
    
    @Override
    public final void initUI() {
        setBorder(null);
        setOpaque(false);
        
        if (editableOnCLick) {
            setFocusable(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setFocusable(true);
                    requestFocus();
                }
            });
        }
        
        placeholderVisible = !placeholderText.equals("");
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!isEditable())
                    return;
                
                if (getText().equals(placeholderText)) {
                    togglePlaceholderTextVisibility();
                    setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!isEditable())
                    return;
                
                if (getText().isEmpty()) {
                    togglePlaceholderTextVisibility();
                }
            }
        });
        
        if (!placeholderText.isEmpty()) {
            setForeground(UIProperties.DIM_TEXT_COLOR);
            setText(placeholderText);
        }
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        setPreferredSize(new Dimension(width, height));
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
        }
    }

    @Override
    public void updateUITheme() {
        if (onlyAppColor) {
            if (!getText().equals(placeholderText))
                if (fontType == LabelType.WARNING_LABEL)
                    setForeground(UIProperties.APP_FGW);
                else
                    setForeground(UIProperties.APP_FG_COLOR);
            
            if (visibleBackground)
                setBackground(UIProperties.APP_BGA_COLOR);
            else
                setBackground(null);
            
            setCaretColor(UIProperties.APP_FG_COLOR);
            
            return;
        }
        
        if (!getText().equals(placeholderText))
            if (fontType == LabelType.WARNING_LABEL)
                setForeground(UIProperties.APP_FGW);
            else
                setForeground(UIProperties.APP_FG);
        else
            setBackground(UIProperties.DIM_TEXT_COLOR);
        
        if (visibleBackground)
            setBackground(UIProperties.APP_BGA);
        else
            setBackground(null);
        
        setCaretColor(UIProperties.APP_FG);
        if (appTheme) {
            setSelectedTextColor(UIProperties.APP_FG);
            setSelectionColor(UIProperties.DIM_TEXT_COLOR);
        }
    }

    @Override
    public void updateUIColors() {
        if (appColor || onlyAppColor) {
            setSelectedTextColor(UIProperties.APP_FG_COLOR);
            setSelectionColor(!UIProperties.usesAccentColors() ? Color.LIGHT_GRAY : onlyAppColor ? UIProperties.APP_BG_COLOR : UIProperties.APP_BGA_COLOR);
        }
    }
    
    @Override
    public void setUseAppTheme(boolean useAppTheme) {
        this.appTheme = useAppTheme;
        this.appColor = !useAppTheme;
        this.onlyAppColor = !useAppTheme;
        
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void setUseAppColor(boolean useAppColor) {
        this.appColor = useAppColor;
        this.appTheme = !useAppColor;
        this.onlyAppColor = !useAppColor;
        
        updateUITheme();
        updateUIColors();
    }
    
    /**
     * Changes the ColorButton aspect
     * @param onlyAppColor if true, only accent color will be used to paint 
     * this component
     * @see UIProperties#APP_BGA_COLOR
     * @see UIProperties#APP_BG_COLOR
     * @see UIProperties#APP_FG_COLOR
     */
    public void setUseOnlyAppColor(boolean onlyAppColor) {
        this.onlyAppColor = onlyAppColor;
        this.appColor = !onlyAppColor;
        this.appTheme = !onlyAppColor;
        
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void setRoundCorners(boolean roundCorners) {
        this.roundCorners = roundCorners;
        repaint();
    }

    @Override
    public void setPaintBorder(boolean paintBorder) {
        this.paintBorder = paintBorder;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        if (paintBorder) {
            g2D.setColor(getForeground());
            
            if (roundCorners)
                g2D.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
        }
        
        g2D.setColor(getBackground());

        if (paintBorder) {
            if (roundCorners)
                g2D.fillRoundRect(2, 2, getWidth() - 3, getHeight() - 3, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.fillRect(2, 2, getWidth() - 3, getHeight() - 3);
        } else {
            if (roundCorners)
                g2D.fillRoundRect(0, 0, getWidth(), getHeight(), UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.fillRect(0, 0, getWidth(), getHeight());
        }
        
        super.paintComponent(g);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        this.width = preferredSize.width;
        this.height = preferredSize.height;
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        super.setPreferredSize(preferredSize);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEditable(enabled);
    }
    
    /**
     * Changes font type
     * 
     * @param fontType the new font type
     */
    public void setFontType(LabelType fontType) {
        this.fontType = fontType;
        updateUIFont();
        updateUITheme();
    }

    public void setVisibleBackground(boolean visibleBackground) {
        this.visibleBackground = visibleBackground;
        updateUITheme();
    }

    /**
     * Sets the text for this component
     * 
     * @param t the text
     * @param updateColor if true, the text will change from GRAY to 
     * {@link UIProperties#APP_FG} if it was not already
     */
    public void setText(String t, boolean updateColor) {
        super.setText(t);
        
        if (updateColor)
            togglePlaceholderTextVisibility();
    }
    
    private void togglePlaceholderTextVisibility() {
        placeholderVisible = getText().isEmpty() && !placeholderText.isEmpty();
        
        if (placeholderVisible) {
            setForeground(UIProperties.DIM_TEXT_COLOR);
            setText(placeholderText);
        } else {
            setForeground(UIProperties.APP_FG);
        }
    }

    /**
     * Change placeholder text for this TextField
     * 
     * @param placeholderText the new placeholder text
     */
    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
        
        togglePlaceholderTextVisibility();
    }

    /**
     * Get the placeholder text
     * @return string
     */
    public String getPlaceholderText() {
        return placeholderText;
    }

    public void setHorizontalAlignment(TextAlignment alignment) {
        super.setHorizontalAlignment(UIProperties.TextAlignmentToInt(alignment));
    }
}
