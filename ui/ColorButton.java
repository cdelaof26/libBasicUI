package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.SpringLayout;
import ui.enums.LabelType;
import utils.LibUtilities;

/**
 * Custom painted JButton
 *
 * @author cristopher
 */
public class ColorButton extends JButton implements ComponentSetup, UIFont {
    /**
     * Button's width
     */
    protected int width = 120;
    
    /**
     * Button's height
     */
    protected int height = 22;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see ColorButton#setUseAppTheme(boolean)
     */
    protected boolean appTheme = false;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see ColorButton#setUseAppColor(boolean) 
     */
    protected boolean appColor = true;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see ColorButton#setUseOnlyAppColor(boolean)
     */
    protected boolean onlyAppColor = false;
    
    /**
     * Condition that determines if the corner of this button should be rounded
     * @see ColorButton#setRoundCorners(boolean)
     * @see UIProperties#buttonRoundRadius
     */
    protected boolean roundCorners = true;
    
    /**
     * Condition that determines if the border is going to be painted<br>
     * Note: If set to true, it can be override-d by {@link ColorButton#paint}
     */
    protected boolean paintBorder = false;
    
    /**
     * Condition that determines if the background and border are going to be painted
     */
    protected boolean paint = true;
    
    /**
     * Paint check if true or mouse is over the component
     */
    protected boolean paintAsHovering = false;
    
    /**
     * Background color
     */
    protected Color BGColor = UIProperties.APP_BGA;
    /**
     * Foreground color
     */
    protected Color FGColor = UIProperties.APP_FG;
    /**
     * Background color when mouse is on top of this button
     */
    protected Color HBGColor = UIProperties.APP_BG_COLOR;
    /**
     * Foreground color when mouse is on top of this button
     */
    protected Color HFGColor = UIProperties.APP_FG_COLOR;
    
    /**
     * Component layout manager
     */
    protected final SpringLayout layout = new SpringLayout();
    
    /**
     * Label that contains button's text
     */
    protected final Label label = new Label(LabelType.BODY);
    
    /**
     * Button's text
     */
    protected String text;
    
    
    /**
     * Creates a new ColorButton with text
     * 
     * @param text the text of the button
     */
    public ColorButton(String text) {
        this.text = text;
        
        initUI();
    }
    
    /**
     * Creates a new ColorButton without text
     */
    public ColorButton() {
        initUI();
    }
    
    @Override
    public final void initUI() {
        setContentAreaFilled(false);
        setBorder(null);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setLayout(layout);
        
        label.setText(text);
        label.setBorder(null);
        
        add(label);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, this);
    }

    @Override
    public void updateUISize() {
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void updateUIFont() {
        label.updateUIFont();
    }

    @Override
    public void updateUITheme() {
        if (appTheme) {
            HBGColor = UIProperties.APP_BG;
            HFGColor = UIProperties.APP_FG;
            
            BGColor = UIProperties.APP_BGA;
            FGColor = UIProperties.APP_FG;
        }
        
        label.setForeground(FGColor);
        
        repaint();
    }

    @Override
    public void updateUIColors() {
        if (appColor) {
            HBGColor = UIProperties.APP_BG_COLOR;
            HFGColor = UIProperties.APP_FG_COLOR;
            
            BGColor = UIProperties.APP_BGA;
            FGColor = UIProperties.APP_FG;
        }
        
        if (onlyAppColor) {
            HBGColor = !UIProperties.usesAccentColors() ? Color.LIGHT_GRAY : UIProperties.APP_BGA_COLOR;
            HFGColor = UIProperties.APP_FG_COLOR;
            
            BGColor = UIProperties.APP_BG_COLOR;
            FGColor = UIProperties.APP_FG_COLOR;
        }
        
        label.setForeground(FGColor);
        
        repaint();
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
        if (paint) {
            Graphics2D g2D = (Graphics2D) g;

            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            Color c;
            
            if ((getModel().isRollover() || paintAsHovering) && isEnabled()) {
                c = HBGColor;
                label.setForeground(HFGColor);
            } else {
                c = BGColor;
                label.setForeground(FGColor);
            }

            paintCustomBorder(g2D, c);
            
            return;
        }
        
        label.setForeground(UIProperties.APP_FG);
    }
    
    private void updateLabelSize() {
        Font f = label.getFont();
        Dimension textDimensions = LibUtilities.getTextDimensions(text == null ? "" : (text + " "), f == null ? UIProperties.APP_BOLD_FONT : f);
        
        if (textDimensions.width > width)
            label.setPreferredSize(new Dimension((int) (UIProperties.getUiScale() * width), (int) Math.ceil(UIProperties.getUiScale() * textDimensions.height)));
        else
            label.setPreferredSize(new Dimension((int) Math.ceil(UIProperties.getUiScale() * textDimensions.width), (int) Math.ceil(UIProperties.getUiScale() * textDimensions.height)));
    }
    
    @Override
    public void setPreferredSize(Dimension preferredSize) {
        width = preferredSize.width;
        height = preferredSize.height;
        
        updateLabelSize();
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        super.setPreferredSize(preferredSize);
    }
    
    /**
     * Changes the font type of the button text<br>
     * Recommended for F type arrangements
     * 
     * @param fontType the font type
     * @see ui.enums.ImageButtonArrangement
     */
    public void setLabelType(LabelType fontType) {
        label.setLabelType(fontType);
        updateLabelSize();
    }

    @Override
    public void setFont(Font font) {
        if (label != null) {
            label.setFont(font);
            updateLabelSize();
        }
    }
    
    /**
     * Draws the button background and border
     * 
     * @param g2D the Graphics2D object to paint in
     * @param fillColor the button's background color
     */
    protected void paintCustomBorder(Graphics2D g2D, Color fillColor) {
        if (paintBorder) {
            g2D.setColor(UIProperties.APP_FG);
            
            if (roundCorners)
                g2D.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.drawRect(0, 0, getWidth(), getHeight());
        }
        
        g2D.setColor(fillColor);

        if (roundCorners)
            g2D.fillRoundRect(2, 2, getWidth() - 3, getHeight() - 3, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
        else
            g2D.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Changes button's visual appearance
     * 
     * @param paintAsHovering if true the button will be painted as if the mouse
     * were hovering on the button even if it isn't
     */
    public void setPaintAsHovering(boolean paintAsHovering) {
        this.paintAsHovering = paintAsHovering;
        repaint();
    }

    /**
     * @param paint if true, background and border will be painted
     */
    public void setPaint(boolean paint) {
        this.paint = paint;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        label.setText(this.text);
        updateLabelSize();
    }
    
    @Override
    public void setFontPointSize(int fontSize) {
        label.setFontPointSize(fontSize);
    }

    @Override
    public void setFontFamily(String fontFamily) {
        label.setFontFamily(fontFamily);
    }

    @Override
    public void setFontBold(boolean boldFont) {
        label.setFontBold(boldFont);
    }

    @Override
    public void setFontItalic(boolean italicFont) {
        label.setFontItalic(italicFont);
    }

    @Override
    public void setFontMonospaced(boolean monospacedFont) {
        label.setFontMonospaced(monospacedFont);
    }

    @Override
    public void useCustomFontColor(boolean customColor) {
        label.useCustomFontColor(customColor);
    }

    @Override
    public void setFontColor(Color fontColor) {
        label.setFontColor(fontColor);
    }
}
