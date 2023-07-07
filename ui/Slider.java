package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import ui.enums.UIOrientation;

/**
 * Custom made slider
 *
 * @author cristopher
 */
public class Slider extends JComponent implements ComponentSetup {
    
    protected int rwidth = 120, rheight = 22;
    
    protected int width = 120, height = 22;
    
    protected boolean appTheme = false;
    protected boolean appColor = true;
    
    /**
     * Background color
     */
    protected Color BGColor = UIProperties.APP_BG;
    
    /**
     * Slider drag-able circle color
     */
    protected Color FGColor = UIProperties.APP_BG_COLOR;
    
    private final UIOrientation orientation;
    
    /**
     * Value when slider is at left
     */
    private final int minimumValue;
    
    /**
     * Value when slider is at right
     */
    private final int maximumValue;
    
    /**
     * Current slider value
     */
    private int value;
    
    /**
     * Drag-able circle position
     */
    private int position = 0;
    
    /**
     * Components to update if slider moves
     */
    protected JComponent [] componentsToUpdate;
    
    /**
     * Creates a horizontal slider
     * 
     * @param minimumValue
     * @param maximumValue
     * @param componentToUpdate optional components to update when slider moves
     */
    public Slider(int minimumValue, int maximumValue, JComponent ... componentToUpdate) throws IllegalArgumentException {
        this.orientation = UIOrientation.HORIZONTAL;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.componentsToUpdate = componentToUpdate;
        
        initUI();
    }
    
    /**
     * Creates a new slider
     * 
     * @param orientation component orientation
     * @param minimumValue
     * @param maximumValue
     * @param componentToUpdate optional components to update when slider moves
     */
    public Slider(UIOrientation orientation, int minimumValue, int maximumValue, JComponent ... componentToUpdate) throws IllegalArgumentException {
        this.orientation = orientation;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.componentsToUpdate = componentToUpdate;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        if (minimumValue > maximumValue)
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value");
        
        value = minimumValue;
        
        if (orientation == UIOrientation.VERTICAL) {
            width = 22;
            height = 120;
            position = height - (UIProperties.sliderCircleRadius * 2) - 2;
        }
        
        setBorder(null);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int coord = (orientation == UIOrientation.HORIZONTAL) ? e.getX() : e.getY();
                moveCircle(coord, true, true);
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int coord = (orientation == UIOrientation.HORIZONTAL) ? e.getX() : e.getY();
                moveCircle(coord, true, true);
            }
        });
        
        updateUISize();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        width = rwidth;
        height = rheight;
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void updateUIFont() { }

    @Override
    public void updateUITheme() {
        BGColor = UIProperties.APP_BG;
        if (appTheme)
            FGColor = UIProperties.APP_FG;
        
        repaint();
    }

    @Override
    public void updateUIColors() {
        if (appColor)
            FGColor = UIProperties.APP_BG_COLOR;
        
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPaintBorder(boolean paintBorder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (UIProperties.uiScale == 1f) {
            rwidth = preferredSize.width;
            rheight = preferredSize.height;
        }
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        this.width = preferredSize.width;
        this.height = preferredSize.height;
        
        setValue(value, false);
        
        super.setPreferredSize(preferredSize);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        g2D.setColor(BGColor);
        g2D.fillRect(0, 0, width, height);
        
        int trackHalf = ((orientation == UIOrientation.HORIZONTAL) ? height : width) / 2;
        
        g2D.setColor(UIProperties.APP_FG);
        g2D.setStroke(new BasicStroke(2 * UIProperties.uiScale));
        if (orientation == UIOrientation.HORIZONTAL)
            g2D.drawLine(4, trackHalf, width - 4, trackHalf);
        else
            g2D.drawLine(trackHalf, 4, trackHalf, height - 4);
        
        
        g2D.setColor(FGColor);
        if (orientation == UIOrientation.HORIZONTAL)
            g2D.fillOval(position, trackHalf - UIProperties.sliderCircleRadius, UIProperties.sliderCircleRadius * 2, UIProperties.sliderCircleRadius * 2);
        else
            g2D.fillOval(trackHalf - UIProperties.sliderCircleRadius, position, UIProperties.sliderCircleRadius * 2, UIProperties.sliderCircleRadius * 2);
    }
    
    private void moveCircle(int coord, boolean focus, boolean overwriteValue) {
        if (focus)
            requestFocus();

        position = coord - UIProperties.sliderCircleRadius;
        
        if (orientation == UIOrientation.HORIZONTAL) {
            int trackWidth = width - (UIProperties.sliderCircleRadius * 2) - 2;
            
            if (position < 2) {
                position = 2;
                if (overwriteValue)
                    value = minimumValue;
            } else if (position > trackWidth) {
                position = trackWidth;
                if (overwriteValue)
                    value = maximumValue;
            } else if (overwriteValue)
                value = position * maximumValue / trackWidth;
            
        } else {
            int trackHeight = (height - (UIProperties.sliderCircleRadius * 2) - 2);
            
            if (position < 2) {
                position = 2;
                if (overwriteValue)
                    value = maximumValue;
            } else if (position > trackHeight) { 
                position = trackHeight;
                if (overwriteValue)
                    value = minimumValue;
            } else if (overwriteValue)
                value = (trackHeight - position) * maximumValue / trackHeight;
        }
        
        if (componentsToUpdate != null && overwriteValue)
            for (JComponent c : componentsToUpdate)
                if (c instanceof JLabel)
                    ((JLabel) c).setText("" + value);
                else if (c instanceof TextField)
                    ((TextField) c).setText("" + value, true);
                else if (c instanceof JTextComponent)
                    ((JTextComponent) c).setText("" + value);
                else if (c instanceof ProgressBar)
                    ((ProgressBar) c).setValue(value);
                else if (c instanceof Slider)
                    ((Slider) c).setValue(value, false);
                else
                    throw new UnsupportedOperationException("Unsuported JComponent");

        repaint();
    }

    /**
     * Set a value for this slider
     * @param value the value
     * @param focus set if slider should be focused if value changes
     * @throws IllegalArgumentException if value is outside given range
     */
    public void setValue(int value, boolean focus) throws IllegalArgumentException {
        if (value < minimumValue || value > maximumValue)
            throw new IllegalArgumentException("Value is outside the range [" + minimumValue + ", " + maximumValue + "]");
        
        this.value = value;
        
        int coord;
        
        if (orientation == UIOrientation.HORIZONTAL) {
            coord = value * (width - 4) / maximumValue;
        } else {
            coord = (maximumValue - value) * height / maximumValue;
        }
        
        // TODO: Fix visual inconsistences with vertical slider
        //       Slider(0, 100).setValue(50) doesn't look at the exact middle
        
        moveCircle(coord, focus, false);
    }

    public void setComponentsToUpdate(JComponent ... componentsToUpdate) {
        this.componentsToUpdate = componentsToUpdate;
    }
}
