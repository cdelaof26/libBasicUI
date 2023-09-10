package ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.NoSuchElementException;
import javax.swing.SpringLayout;
import ui.enums.LabelType;

/**
 * Custom painted JComboBox for strings
 *
 * @author cristopher
 */
public class ComboBox extends ColorButton {
    private final ContextMenu optionsSelector;
    
    private final SpringLayout layout = new SpringLayout();
    private final Label textLabel;
    private final Label selectionLabel;
    private final Label iconLabel = new Label(LabelType.NONE, "H") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
        
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            g2D.setColor(UIProperties.DIM_TEXT_COLOR);
            g2D.setStroke(new BasicStroke(1.5f * UIProperties.uiScale));
            
            int halfWidth = getPreferredSize().width / 2;
            int height = getPreferredSize().height;
            
            int yPadding = (int) (7 * UIProperties.uiScale);
            int yEndPadding = (int) (9 * UIProperties.uiScale);
            int separation = (int) (3 * UIProperties.uiScale);
            
            g2D.drawLine(halfWidth, yPadding, halfWidth - separation, yEndPadding);
            g2D.drawLine(halfWidth, yPadding, halfWidth + separation, yEndPadding);
            
            g2D.drawLine(halfWidth, height - yPadding, halfWidth - separation, height - yEndPadding);
            g2D.drawLine(halfWidth, height - yPadding, halfWidth + separation, height - yEndPadding);
        }
    };
    
    private boolean initializationEnded = false;
    
    
    /**
     * Creates a new ComboBox
     * 
     * @param text indicates for what is this combo box
     * @param selected indicates what is the default selection
     * @param hideOverflow if the excess of options should be hid
     */
    public ComboBox(String text, String selected, boolean hideOverflow) {
        textLabel = new Label(LabelType.BODY, text);
        selectionLabel = new Label(LabelType.BODY, selected);
        optionsSelector = new ContextMenu(this, hideOverflow);
        
        initializationEnded = true;
        initComboUI();
    }
    
    public final void initComboUI() {
        setPreferredSize(new Dimension(180, 22));
        setUseAppTheme(true);
        
        addActionListener((Action) -> {
            optionsSelector.show(0, getPreferredSize().height);
        });
        
        add(textLabel);
        add(selectionLabel);
        add(iconLabel);
        setLayout(layout);
        
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, textLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, selectionLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, iconLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        
        layout.putConstraint(SpringLayout.EAST, selectionLabel, 0, SpringLayout.WEST, iconLabel);
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        if (initializationEnded) {
            optionsSelector.setWidth(width);
            optionsSelector.updateUISize();
            
            textLabel.updateUISize();
            selectionLabel.updateUISize();
            
            iconLabel.setPreferredSize(new Dimension((int) (22 * UIProperties.uiScale), (int) (22 * UIProperties.uiScale)));
            
            layout.putConstraint(SpringLayout.WEST, textLabel, (int) (10 * UIProperties.uiScale), SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.EAST, iconLabel, (int) (-5 * UIProperties.uiScale), SpringLayout.EAST, this);
        }
        
        super.updateUISize();
    }

    @Override
    public void updateUIFont() {
        if (initializationEnded) {
            optionsSelector.updateUIFont();
            
            textLabel.updateUIFont();
            selectionLabel.updateUIFont();
        }
        super.updateUIFont();
    }
    
    @Override
    public void updateUITheme() {
        if (initializationEnded) {
            optionsSelector.updateUITheme();
            
            selectionLabel.setForeground(UIProperties.DIM_TEXT_COLOR);
        }
        super.updateUITheme();
    }

    @Override
    public void updateUIColors() {
        if (initializationEnded) {
            optionsSelector.updateUIColors();
        }
        super.updateUIColors();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (textLabel != null) {
            if (getModel().isRollover() || paintAsHovering) 
                textLabel.setForeground(HFGColor);
            else
                textLabel.setForeground(FGColor);
        }
        
        super.paintComponent(g);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        
        if (initializationEnded) {
            optionsSelector.setWidth(width);
            optionsSelector.updateUISize();
        }
    }
    
    @Override
    public void setText(String text) {
        selectionLabel.setText(text);
    }

    /**
     * Use getText_() instead as this will return button's text instead of 
     * internal JLabel which holds actual text settled by setText() method
     * 
     * @return the button's text
     * @deprecated
     */
    @Override
    @Deprecated
    public String getText() {
        return super.getText();
    }
    
    /**
     * Returns button's text
     * 
     * @return a string
     */
    public String getText_() {
        return selectionLabel.getText();
    }
    
    private ActionListener [] addAction(String text, ActionListener [] actions) {
        if (actions == null) {
            return new ActionListener[]{
                (Action) -> {
                    setText(text);
                }
            };
        }
        
        ActionListener [] actionsCopy = actions;
        actions = new ActionListener[actions.length + 1];
        
        System.arraycopy(actionsCopy, 0, actions, 0, actionsCopy.length);
        
        actions[actionsCopy.length] = (Action) -> {
            setText(text);
        };
        
        return actions;
    }
    
    /**
     * Adds a new option
     * 
     * @param text
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null or empty
     */
    public void addOption(String text, boolean addPadding, ActionListener ... actions) {
        optionsSelector.addOption(text, addPadding, addAction(text, actions));
    }
    
    /**
     * Adds a new option
     * 
     * @param text
     * @param lightImage base64 image settled when light theme is active
     * @param darkImage base64 image settled when dark theme is active, can be null
     * @param hoverImage base64 image settled when mouse is over the option, can be null
     * @param areImagesBundled flag to indicate if images are bundled in
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(String text, String lightImage, String darkImage, String hoverImage, boolean areImagesBundled, boolean addPadding, ActionListener ... actions) {
        optionsSelector.addOption(text, lightImage, darkImage, hoverImage, areImagesBundled, addPadding, addAction(text, actions));
    }
    
    /**
     * Adds a new option
     * 
     * @param text
     * @param lightImage image settled when light theme is active
     * @param darkImage image settled when dark theme is active, can be null
     * @param hoverImage image settled when mouse is over the option, can be null
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(String text, File lightImage, File darkImage, File hoverImage, boolean addPadding, ActionListener ... actions) {
        optionsSelector.addOption(text, lightImage, darkImage, hoverImage, addPadding, addAction(text, actions));
    }
    
    /**
     * Removes all options added to the ComboBox
     */
    public void removeAllOptions() {
        optionsSelector.removeAllOptions();
    }
    
    /**
     * Removes the a option given a index
     * @param index the index of the element to remove
     * @throws NoSuchElementException 
     */
    public void removeOption(int index) throws NoSuchElementException {
        optionsSelector.removeOption(index);
    }
    
    /**
     * Makes selectable elements slim<br>
     * By default, elements are slim if <code>hideOverflow</code> was set to true
     * 
     * @param slimElements if true, the added options will be <code>width=15</code>,
     * otherwise <code>width=30</code>
     */
    public void setSlimElements(boolean slimElements) {
        optionsSelector.setSlimElements(slimElements);
    }
}
