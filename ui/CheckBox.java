package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Custom painted checkbox button
 *
 * @author cristopher
 */
public class CheckBox extends ColorButton {
    /**
     * Current checkbox state
     */
    protected boolean checked;
    
    /**
     * Creates a new Checkbox
     * 
     * @param checked if it should be checked when created
     */
    public CheckBox(boolean checked) {
        this.checked = checked;
        
        width = UIProperties.checkboxSideLength;
        height = UIProperties.checkboxSideLength;
        
        paintBorder = false;
        
        addActionListener((Action) -> {
            this.checked = !this.checked;
            repaint();
        });
        
        initUI();
    }
    
    protected void paintCheck(Graphics2D g2D, boolean mouseIsHovering) {
        if (mouseIsHovering && !checked)
            g2D.setColor(Color.GRAY);
        else if (checked)
            g2D.setColor(FGColor);
        else
            return;
        
        g2D.setStroke(new BasicStroke(3 * UIProperties.uiScale));

        int size = UIProperties.checkboxSideLength;

        g2D.drawLine((int) (size - 18 * UIProperties.uiScale), (int) (size - 8 * UIProperties.uiScale), (int) (size - 14 * UIProperties.uiScale), (int) (size - 4 * UIProperties.uiScale));
        g2D.drawLine((int) (size - 14 * UIProperties.uiScale), (int) (size - 4 * UIProperties.uiScale), (int) (size - 4 * UIProperties.uiScale), (int) (size - 16 * UIProperties.uiScale));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g.setColor(BGColor);
        
        paintCustomBorder(g2D, BGColor);
        paintCheck(g2D, getModel().isRollover() || paintAsHovering);
    }

    /**
     * @return true or false
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Changes visual appearance of button
     * @param checked 
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
        repaint();
    }
}
