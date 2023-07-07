package ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import ui.enums.LabelType;
import ui.enums.UIAlignment;

/**
 * UI element to select a number given a range
 * 
 * @author cristopher
 */
public class NumberSelector extends Panel {
    private final Label textLabel;
    private final TextField numberField;
    private final ColorButton increaseButton = new ColorButton() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2D = (Graphics2D) g;
        
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
            if (getModel().isRollover() || paintAsHovering)
                g2D.setColor(HFGColor);
            else
                g2D.setColor(FGColor);
            
            int halfWidth = (int) ((width * UIProperties.uiScale) / 2);
            int halfHeight = (int) ((height * UIProperties.uiScale) / 2);
            
            int padding = (int) (7 * UIProperties.uiScale);
            
            g2D.setStroke(new BasicStroke(2));
            g2D.drawLine(padding, halfHeight, (int) ((width * UIProperties.uiScale) - padding), halfHeight);
            g2D.drawLine(halfWidth, padding, halfWidth, (int) ((height * UIProperties.uiScale) - padding));
            
        }
    };
    private final ColorButton decreaseButton = new ColorButton() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2D = (Graphics2D) g;
        
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
            if (getModel().isRollover() || paintAsHovering)
                g2D.setColor(HFGColor);
            else
                g2D.setColor(FGColor);
            
            int halfHeight = (int) ((height * UIProperties.uiScale) / 2);
            int padding = (int) (7 * UIProperties.uiScale);
            
            g2D.setStroke(new BasicStroke(2 * UIProperties.uiScale));
            g2D.drawLine(padding, halfHeight, (int) ((width * UIProperties.uiScale) - padding), halfHeight);
            
        }
    };
    
    protected JComponent [] componentsToUpdate;
    
    private final int defaultValue;
    private final int maximumValue;
    private final int minimumValue;
    private final int increaseStep;
    private int value;
    
    private final String regex;
    
    private boolean initializationEnded = false;
    
    public NumberSelector(String text, String placeholderText, int defaultValue, int minimumValue, int maximumValue, int increaseStep, JComponent ... componentsToUpdate) throws IllegalArgumentException {
        super(200, 22);
        initializationEnded = true;
        
        textLabel = new Label(LabelType.BODY, text);
        numberField = new TextField(placeholderText);
        
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.increaseStep = increaseStep;
        this.componentsToUpdate = componentsToUpdate;
        
        regex = (minimumValue < 0 || maximumValue < 0) ? "^-?[\\d]+$" : "^[\\d]+$";
        
        initNumberSelector();
    }
    
    public NumberSelector(String text, int defaultValue, int minimumValue, int maximumValue, int increaseStep, JComponent ... componentsToUpdate) throws IllegalArgumentException {
        super(122, 22);
        initializationEnded = true;
        
        textLabel = new Label(LabelType.BODY, text);
        numberField = new TextField();
        
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.increaseStep = increaseStep;
        this.componentsToUpdate = componentsToUpdate;
        
        regex = (minimumValue < 0 || maximumValue < 0) ? "^-?[\\d]+$" : "^[\\d]+$";
        
        initNumberSelector();
    }
    
    private void initNumberSelector() {
        if (minimumValue > maximumValue)
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value");
        if (value < minimumValue || value > maximumValue)
            throw new IllegalArgumentException("Value is outside the range [" + minimumValue + ", " + maximumValue + "]");
        
        increaseButton.addActionListener((Action) -> {
            if (increaseStep > 0 && value < maximumValue || increaseStep < 0 && value > minimumValue)
                value += increaseStep;
            
            numberField.setText("" + value, true);
            updateValue(true);
        });
        decreaseButton.addActionListener((Action) -> {
            if (increaseStep > 0 && value > minimumValue || increaseStep < 0 && value < maximumValue)
                value -= increaseStep;
            
            numberField.setText("" + value, true);
            updateValue(true);
        });
        
        numberField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
//                if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE)
//                    return;
                
                updateValue(false);
            }
        });
        
        add(textLabel, this, this, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(numberField, textLabel, this, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(decreaseButton, numberField, this, UIAlignment.WEST, UIAlignment.EAST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(increaseButton, decreaseButton, this, UIAlignment.WEST, UIAlignment.EAST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        numberField.setPreferredSize(new Dimension(width - (2 * height + textLabel.getPreferredSize().width + 10), height));
        increaseButton.setPreferredSize(new Dimension(height, height));
        decreaseButton.setPreferredSize(new Dimension(height, height));
    }
    
    /**
     * Sets the preferred size of this component.<br>
     * Please use setPreferredSize_() for this component as it will update internal
     * components to match the desired size
     * 
     * @param preferredSize
     * @deprecated
     */
    @Override
    @Deprecated
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
    }

    /**
     * Sets the preferred size of this component.
     * 
     * @param preferredSize 
     */
    public void setPreferredSize_(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        
        numberField.setPreferredSize(new Dimension(width - (2 * height + textLabel.getPreferredSize().width + 10), height));
        increaseButton.setPreferredSize(new Dimension(height, height));
        decreaseButton.setPreferredSize(new Dimension(height, height));
    }
    
    private void updateValue(boolean skipVerification) {
        int pvalue = minimumValue;
        
        if (skipVerification) {
            pvalue = Integer.parseInt(numberField.getText());
        } else {
            String newText = numberField.getText();
            if (!newText.isEmpty()) {
                if (newText.matches(regex))
                    pvalue = Integer.parseInt(newText);
            } else {
                pvalue = defaultValue;
            }
            
//            if (newText.matches("^-?[\\d]+\\.?[\\d]+$|^-?[\\d]+$")) {
        }
        
        if (pvalue > maximumValue) {
            numberField.setText("" + maximumValue, true);
            pvalue = maximumValue;
        }

        if (pvalue >= minimumValue && pvalue <= maximumValue) {
            value = pvalue;

            if (componentsToUpdate != null)
                for (JComponent c : componentsToUpdate)
                    if (c instanceof JLabel)
                        ((JLabel) c).setText("" + pvalue);
                    else if (c instanceof TextField)
                        ((TextField) c).setText("" + pvalue, true);
                    else if (c instanceof JTextComponent)
                        ((JTextComponent) c).setText("" + pvalue);
                    else if (c instanceof ProgressBar)
                        ((ProgressBar) c).setValue(pvalue);
                    else if (c instanceof Slider)
                        ((Slider) c).setValue(pvalue, false);
                    else
                        throw new UnsupportedOperationException("Unsuported JComponent");
        }
    }

    public void setValue(int value) {
        if (value < minimumValue || value > maximumValue)
            throw new IllegalArgumentException("Value is outside the range [" + minimumValue + ", " + maximumValue + "]");
        
        this.value = value;
        
        numberField.setText("" +  this.value, true);
    }

    public float getValue() {
        return value;
    }
}
