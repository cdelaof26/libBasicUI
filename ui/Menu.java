package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.SpringLayout;
import ui.enums.LabelType;
import utils.LibUtilities;

/**
 * Color button with deploy-able Context menu
 * 
 * @author cristopher
 */
public class Menu extends ColorButton {
    private final ContextMenu optionsSelector = new ContextMenu(this, false);
    
    private final SpringLayout layout = new SpringLayout();
    private final Label shortcutLabel = new Label(LabelType.BODY);
    
    private boolean initializationEnded = false;
    
    
    /**
     * Creates a new Menu
     * 
     * @param text the menu's text
     */
    public Menu(String text) {
        super(text);
        
        initializationEnded = true;
        initMenuUI();
    }
    
    public final void initMenuUI() {
        setPreferredSize(new Dimension(LibUtilities.getTextDimensions(getText() + "    ", UIProperties.APP_FONT).width, 22));
        setRoundCorners(false);
        
        optionsSelector.setSlimElements(true);
        
        addActionListener((Action) -> {
            optionsSelector.show(0, getPreferredSize().height);
        });
        
        add(shortcutLabel);
        setLayout(layout);
        
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, shortcutLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        if (initializationEnded) {
            optionsSelector.updateUISize();
            shortcutLabel.updateUISize();
            
            layout.putConstraint(SpringLayout.EAST, shortcutLabel, -10, SpringLayout.EAST, this);
        }
        
        super.updateUISize();
    }

    @Override
    public void updateUIFont() {
        if (initializationEnded) {
            optionsSelector.updateUIFont();
            
            shortcutLabel.updateUIFont();
        }
        super.updateUIFont();
    }
    
    @Override
    public void updateUITheme() {
        if (initializationEnded) {
            optionsSelector.updateUITheme();
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
        if (shortcutLabel != null) {
            if (getModel().isRollover() || paintAsHovering) 
                shortcutLabel.setForeground(HFGColor);
            else
                shortcutLabel.setForeground(FGColor);
        }
        
        super.paintComponent(g);
    }

    public void setMenuWidth(int width) {
        optionsSelector.setWidth(width);
        optionsSelector.updateUISize();
    }
    
    /**
     * Adds a new option
     * 
     * @param text
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(String text, boolean addPadding, ActionListener ... actions) {
        optionsSelector.addOption(text, addPadding, actions);
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
        optionsSelector.addOption(text, lightImage, darkImage, hoverImage, areImagesBundled, addPadding, actions);
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
        optionsSelector.addOption(text, lightImage, darkImage, hoverImage, addPadding, actions);
    }
}
