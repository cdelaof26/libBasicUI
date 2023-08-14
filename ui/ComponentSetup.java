package ui;

/**
 * Interface for most UI elements
 * 
 * @author cristopher
 */
public interface ComponentSetup {
    /**
     * Initializes UI component
     */
    public void initUI();
    
    /**
     * Changes UI dimensions
     */
    public void updateUISize();
    
    /**
     * Updates UI font
     */
    public void updateUIFont();
    
    /**
     * Changes theme colors if applicable
     */
    public void updateUITheme();
    
    /**
     * Changes accent colors if applicable
     */
    public void updateUIColors();
    
    /**
     * Updates component color to {@link ui.UIProperties#APP_BG}
     * 
     * @param useAppTheme if true, <code>useAppColor</code> becomes false
     */
    public void setUseAppTheme(boolean useAppTheme);

    /**
     * Updates component color to {@link ui.UIProperties#APP_BG_COLOR}
     * 
     * @param useAppColor if true, <code>useAppTheme</code> becomes false
     */
    public void setUseAppColor(boolean useAppColor);

    /**
     * Specifies if component should have rounded corners
     * 
     * @param roundCorners if true, the component will be painted with rounded 
     * corners
     */
    public void setRoundCorners(boolean roundCorners);

    /**
     * Specifies if component should have a border
     * 
     * @param paintBorder if true, the component will be painted with a border
     */
    public void setPaintBorder(boolean paintBorder);
}
