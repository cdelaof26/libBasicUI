package ui;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import ui.enums.UIAlignment;

/**
 * Custom menu bar
 * 
 * @author cristopher
 */
public class MenuBar extends Panel {
    private final ArrayList<Menu> menus = new ArrayList<>();
    
    public MenuBar(int width, int height) {
        super(width, height);
    }

    @Override
    public void updateUITheme() {
        if (appTheme)
            setBackground(UIProperties.APP_BGA);
        
        for (Component c : getComponents())
            if (c instanceof ComponentSetup)
                ((ComponentSetup) c).updateUITheme();
    }
    
    /**
     * Creates a new input for this menu bar
     * 
     * @param text
     * @return the index of the new menu
     */
    public int addMenu(String text) {
        Menu m = new Menu(text);
        
        if (menus.isEmpty())
            add(m, this, this, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        else
            add(m, menus.get(menus.size() - 1), this, UIAlignment.WEST, UIAlignment.EAST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        menus.add(m);
        
        return menus.size() - 1;
    }
    
    /**
     * Sets the width for a menu
     * 
     * @param menuIndex
     * @param width 
     */
    public void setMenuWidth(int menuIndex, int width) {
        menus.get(menuIndex).setMenuWidth(width);
    }
    
    /**
     * Adds a new option
     * 
     * @param menuIndex
     * @param text
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(int menuIndex, String text, boolean addPadding, ActionListener ... actions) {
        menus.get(menuIndex).addOption(text, addPadding, actions);
    }
    
    /**
     * Adds a new option
     * 
     * @param menuIndex
     * @param text
     * @param lightImage base64 image settled when light theme is active
     * @param darkImage base64 image settled when dark theme is active, can be null
     * @param hoverImage base64 image settled when mouse is over the option, can be null
     * @param areImagesBundled flag to indicate if images are bundled in
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(int menuIndex, String text, String lightImage, String darkImage, String hoverImage, boolean areImagesBundled, boolean addPadding, ActionListener ... actions) {
        menus.get(menuIndex).addOption(text, lightImage, darkImage, hoverImage, areImagesBundled, addPadding, actions);
    }
    
    /**
     * Adds a new option
     * 
     * @param menuIndex
     * @param text
     * @param lightImage image settled when light theme is active
     * @param darkImage image settled when dark theme is active, can be null
     * @param hoverImage image settled when mouse is over the option, can be null
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(int menuIndex, String text, File lightImage, File darkImage, File hoverImage, boolean addPadding, ActionListener ... actions) {
        menus.get(menuIndex).addOption(text, lightImage, darkImage, hoverImage, addPadding, actions);
    }
}
