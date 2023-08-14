package ui;

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
    
    protected boolean appTheme = false;
    protected boolean appColor = true;
    protected boolean roundCorners = true;
    protected boolean paintBorder = false;
    
    protected boolean visibleBackground = true;
    
    public boolean placeholderVisible;
    private String placeholderText = "";
    
    private LabelType fontType = LabelType.BODY;
    private final boolean editableOnCLick;
    
    
    /**
     * Creates a new TextField with a placeholder text
     * 
     * @param placeholderText 
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
     * @param placeholderText 
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
        if (appColor) {
            setSelectedTextColor(UIProperties.APP_FG_COLOR);
            setSelectionColor(UIProperties.APP_BGA_COLOR);
        }
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
     * @param fontType 
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
     * @param t
     * @param updateColor 
     */
    public void setText(String t, boolean updateColor) {
        if (updateColor)
            togglePlaceholderTextVisibility();
        
        super.setText(t);
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
     * @param placeholderText 
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
