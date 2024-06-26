package ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;
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
            
            int swidth = getPreferredSize().width;
            int sheight = getPreferredSize().height;
            
            int halfWidth = swidth / 2;
            int halfHeight = sheight / 2;
            
            int padding = (int) (7 * UIProperties.uiScale);
            
            g2D.setStroke(new BasicStroke(2 * UIProperties.uiScale));
            g2D.drawLine(padding, halfHeight, swidth - padding, halfHeight);
            g2D.drawLine(halfWidth, padding, halfWidth, sheight - padding);
            
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
            
            int swidth = getPreferredSize().width;
            int sheight = getPreferredSize().height;
            
            int halfHeight = sheight / 2;
            int padding = (int) (7 * UIProperties.uiScale);
            
            g2D.setStroke(new BasicStroke(2 * UIProperties.uiScale));
            g2D.drawLine(padding, halfHeight, swidth - padding, halfHeight);
            
        }
    };
    
    /**
     * The components to update when the value changes<br>
     * Supported JComponents:<br>
     * - {@link JLabel#setText(java.lang.String)}<br>
     * - {@link TextField#setText(java.lang.String)}<br>
     * - {@link JTextComponent#setText(java.lang.String)}<br>
     * - {@link ProgressBar#setValue(int)}<br>
     * - {@link Slider#setValue(int, boolean)}<br>
     */
    protected JComponent [] componentsToUpdate;
    
    private final int defaultValue;
    private int maximumValue;
    private int minimumValue;
    private final int increaseStep;
    private int value;
    
    public boolean enableValueModifier = true;
    
    private int modifiedTimes = 0;
    private boolean pressed = false;
    private boolean increaseValue;
    private final Timer slowValueModificator = new Timer(400, (Action) -> {
        modValue();
    });
    private final Timer normalValueModificator = new Timer(200, (Action) -> {
        modValue();
    });
    private final Timer fastValueModificator = new Timer(50, (Action) -> {
        modValue();
    });
    
    
    private final String regex;

    /**
     * Creates a new NumberSelector with a placeholder value
     * 
     * @param text the field text
     * @param placeholderText the placeholder text
     * @param defaultValue the default value
     * @param minimumValue the minimum value
     * @param maximumValue the maximum value
     * @param increaseStep the increase and decrease step
     * @param componentsToUpdate optional components to update when the value changes
     * @see NumberSelector#componentsToUpdate
     * @throws IllegalArgumentException throw if minimumValue is greater than maximumValue, 
     * defaultValue is less than minimumValue or defaultValue is greater than maximumValue
     */
    public NumberSelector(String text, String placeholderText, int defaultValue, int minimumValue, int maximumValue, int increaseStep, JComponent ... componentsToUpdate) throws IllegalArgumentException {
        super(200, 22);
        
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
    
    /**
     * Creates a new NumberSelector without a placeholder value
     * 
     * @param text the field text
     * @param defaultValue the default value
     * @param minimumValue the minimum value
     * @param maximumValue the maximum value
     * @param increaseStep the increase and decrease step
     * @param componentsToUpdate optional components to update when the value changes
     * @see NumberSelector#componentsToUpdate
     * @throws IllegalArgumentException throw if minimumValue is greater than maximumValue, 
     * defaultValue is less than minimumValue or defaultValue is greater than maximumValue
     */
    public NumberSelector(String text, int defaultValue, int minimumValue, int maximumValue, int increaseStep, JComponent ... componentsToUpdate) throws IllegalArgumentException {
        super(122, 22);
        
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
        
        increaseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                increaseValue = true;
                
                if (enableValueModifier)
                    modValue();
                else
                    modifyValue();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                modifiedTimes = 0;
                
                slowValueModificator.stop();
                normalValueModificator.stop();
                fastValueModificator.stop();
            }
        });
        decreaseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                increaseValue = false;
                
                if (enableValueModifier)
                    modValue();
                else
                    modifyValue();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                modifiedTimes = 0;
                
                slowValueModificator.stop();
                normalValueModificator.stop();
                fastValueModificator.stop();
            }
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
        
        numberField.setPreferredSize(new Dimension((int) (width - (2 * height + (textLabel.getPreferredSize().width / UIProperties.uiScale) + 10)), height));
        increaseButton.setPreferredSize(new Dimension(height, height));
        decreaseButton.setPreferredSize(new Dimension(height, height));
    }
    
    private void modifyValue() {
        if (!numberField.isEditable())
            return;
        
        if (increaseValue) {
            if (increaseStep > 0 && value < maximumValue || increaseStep < 0 && value > minimumValue)
                value += increaseStep;
            else {
                slowValueModificator.stop();
                normalValueModificator.stop();
                fastValueModificator.stop();
            }
        } else {
            if (increaseStep > 0 && value > minimumValue || increaseStep < 0 && value < maximumValue)
                value -= increaseStep;
            else {
                slowValueModificator.stop();
                normalValueModificator.stop();
                fastValueModificator.stop();
            }
        }

        numberField.setText("" + value, true);
        updateValue(true);
        
        modifiedTimes++;
    }
    
    private void modValue() {
        if (!pressed) {
            slowValueModificator.stop();
            normalValueModificator.stop();
            fastValueModificator.stop();
            return;
        }
        
        if (modifiedTimes < 5 && !slowValueModificator.isRunning())
            slowValueModificator.start();
        
        if (modifiedTimes > 5 && modifiedTimes < 20 && !normalValueModificator.isRunning()) {
            slowValueModificator.stop();
            normalValueModificator.start();
        }
        
        if (modifiedTimes > 20 && !fastValueModificator.isRunning()) {
            normalValueModificator.stop();
            fastValueModificator.start();
        }
        
        modifyValue();
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
     * @param preferredSize the new preferred size
     */
    public void setPreferredSize_(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        super.updateUISize();
        
        if (numberField != null) {
            numberField.setPreferredSize(new Dimension((int) (width - (2 * height + (textLabel.getPreferredSize().width / UIProperties.uiScale) + 10)), height));
            increaseButton.setPreferredSize(new Dimension(height, height));
            decreaseButton.setPreferredSize(new Dimension(height, height));
        }
    }
    
    @Override
    public void updateUISize() {
        setPreferredSize_(new Dimension(width, height));
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

    /**
     * Set a value for this NumberSelector
     * 
     * @param value the value for this field
     */
    public void setValue(int value) {
        if (value < minimumValue || value > maximumValue)
            throw new IllegalArgumentException("Value is outside the range [" + minimumValue + ", " + maximumValue + "]");
        
        this.value = value;
        
        numberField.setText("" +  this.value, true);
        updateValue(true);
    }

    public float getValue() {
        return value;
    }
    
    public int getMinimumValue() {
        return minimumValue;
    }

    /**
     * Sets the minimum value for the number selector
     * 
     * @param minimumValue the new minimum value
     * @throws IllegalArgumentException if minimum value is greater than maximum value 
     */
    public void setMinimumValue(int minimumValue) throws IllegalArgumentException {
        if (minimumValue > maximumValue)
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value");
        
        this.minimumValue = minimumValue;
    }

    public int getMaximumValue() {
        return maximumValue;
    }
    
    /**
     * Sets the maximum value for the number selector
     * 
     * @param maximumValue the new maximum value
     * @throws IllegalArgumentException if minimum value is greater than maximum value 
     */
    public void setMaximumValue(int maximumValue) {
        if (minimumValue > maximumValue)
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value");
        
        this.maximumValue = maximumValue;
    }
    
    /**
     * Sets the minimum value to the number selector
     */
    public void setMinimumValue() {
        setValue(minimumValue);
    }
    
    /**
     * Sets the maximum value to the number selector
     */
    public void setMaximumValue() {
        setValue(maximumValue);
    }
    
    /**
     * Makes editable the numberField
     *
     * @param b the editable state
     */
    public void setEditable(boolean b) {
        numberField.setEditable(b);
    }

    @Override
    public void setEnabled(boolean enabled) {
        setEditable(enabled);
    }
}
