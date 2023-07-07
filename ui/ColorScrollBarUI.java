package ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Custom UI for scroll-bars
 * 
 * @author cristopher
 */
public class ColorScrollBarUI extends BasicScrollBarUI {
    private final Color customTrackColor;
    private final Color barColor;

    /**
     * Creates a new ScrollBar
     */
    public ColorScrollBarUI() {
        this.customTrackColor = UIProperties.APP_BGA;
        this.barColor = UIProperties.DIM_TEXT_COLOR;
    }

    @Override
    protected void configureScrollBarColors() {
        thumbColor = barColor;
        trackColor = customTrackColor;
        scrollbar.setBorder(null);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return noButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return noButton();
    }

    @Override
    protected void installComponents() {
        scrollBarWidth = UIProperties.scrollbarWidth;
        super.installComponents();
    }

    private JButton noButton() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0, 0));
        b.setMaximumSize(new Dimension(0, 0));
        
        return b;
    }
}
