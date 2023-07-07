package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/**
 * Custom painted JButton
 *
 * @author cristopher
 */
public class ColorButton extends JButton implements ComponentSetup {
    protected int width = 120, height = 22;
    
    protected boolean appTheme = false;
    protected boolean appColor = true;
    protected boolean roundCorners = true;
    protected boolean paintBorder = false;
    
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
     * Creates a new ColorButton with text
     * 
     * @param text 
     */
    public ColorButton(String text) {
        super(text);
        
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
        setFont(UIProperties.APP_FONT);
    }

    @Override
    public void updateUITheme() {
        if (appTheme) {
            HBGColor = UIProperties.APP_BG;
            HFGColor = UIProperties.APP_FG;
        }
        
        BGColor = UIProperties.APP_BGA;
        FGColor = UIProperties.APP_FG;
        
        repaint();
    }

    @Override
    public void updateUIColors() {
        if (appColor) {
            HBGColor = UIProperties.APP_BG_COLOR;
            HFGColor = UIProperties.APP_FG_COLOR;
        }
        
        BGColor = UIProperties.APP_BGA;
        FGColor = UIProperties.APP_FG;
        
        repaint();
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
        
        Color c;
        
        if (getModel().isRollover() || paintAsHovering) {
            c = HBGColor;
            setForeground(HFGColor);
        } else {
            c = BGColor;
            setForeground(FGColor);
        }

        paintCustomBorder(g2D, c);
        
        super.paintComponent(g);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        width = preferredSize.width;
        height = preferredSize.height;
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        super.setPreferredSize(preferredSize);
    }
    
    /**
     * Draws rounded rectangle
     * 
     * @param g2D 
     * @param fillColor 
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
     * Paints as if mouse were hovering over the button or not
     * 
     * @param paintAsHovering
     */
    public void setPaintAsHovering(boolean paintAsHovering) {
        this.paintAsHovering = paintAsHovering;
        repaint();
    }
}
