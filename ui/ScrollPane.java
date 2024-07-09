package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * JScrollPane with some tweaks
 * 
 * @author cristopher
 */
public class ScrollPane extends JScrollPane implements ComponentSetup {
    protected int width, height;
    
    private boolean enableAutomaticVerticalUnitAssignment = true;
    private boolean enableAutomaticHorizontalUnitAssignment = true;
    
    protected int verticalScrollUnitIncrement = 10;
    protected int horizontalScrollUnitIncrement = verticalScrollUnitIncrement;
    
    private Component viewportViewComponent;

    /**
     * Creates a new ScrollPane without specifying a viewport view component
     * 
     * @see ScrollPanel#setViewportView(java.awt.Component) 
     */
    public ScrollPane() {
        initUI();
    }

    /**
     * Creates a new ScrollPane without specifying a viewport view component 
     * given a width and a height
     * 
     * @param width the width
     * @param height the height
     * @see ScrollPanel#setViewportView(java.awt.Component) 
     */
    public ScrollPane(int width, int height) {
        this.width = width;
        this.height = height;
        initUI();
    }
    
    /**
     * Creates a new ScrollPane with a viewport view component
     * 
     * @param initialViewportViewComponent the component to display
     */
    public ScrollPane(Component initialViewportViewComponent) {
        this.viewportViewComponent = initialViewportViewComponent;
        super.setViewportView(initialViewportViewComponent);
        initUI();
    }
    
    /**
     * Creates a new ScrollPane with a viewport view component, a width and height
     * 
     * @param width the width
     * @param height the height
     * @param initialViewportViewComponent the component to display
     */
    public ScrollPane(int width, int height, Component initialViewportViewComponent) {
        this.width = width;
        this.height = height;
        this.viewportViewComponent = initialViewportViewComponent;
        initUI();
    }
    
    @Override
    public final void initUI() {
        setBorder(null);
    }

    @Override
    public void updateUISize() {
        setPreferredSize(new Dimension(width, height));
        
        if (viewportViewComponent instanceof ComponentSetup)
            ((ComponentSetup) viewportViewComponent).updateUISize();
        
        verticalScrollBar.setUI(new ColorScrollBarUI());
        horizontalScrollBar.setUI(new ColorScrollBarUI());
        
        if (enableAutomaticVerticalUnitAssignment)
            verticalScrollBar.setUnitIncrement((int) (verticalScrollUnitIncrement * UIProperties.getUiScale()));
        
        if (enableAutomaticHorizontalUnitAssignment)
            horizontalScrollBar.setUnitIncrement((int) (horizontalScrollUnitIncrement * UIProperties.getUiScale()));
    }

    @Override
    public void updateUIFont() {
        if (viewportViewComponent instanceof ComponentSetup)
            ((ComponentSetup) viewportViewComponent).updateUIFont();
    }

    @Override
    public void updateUITheme() {
        if (viewportViewComponent instanceof ComponentSetup)
            ((ComponentSetup) viewportViewComponent).updateUITheme();
        
        setBackground(UIProperties.APP_BG_COLOR);

        verticalScrollBar.setUI(new ColorScrollBarUI());
        verticalScrollBar.setUI(new ColorScrollBarUI());
    }

    @Override
    public void updateUIColors() {
        if (viewportViewComponent instanceof ComponentSetup)
            ((ComponentSetup) viewportViewComponent).updateUIColors();
        
    }

    @Override
    public void setUseAppTheme(boolean useAppTheme) {
        if (viewportViewComponent instanceof ComponentSetup)
            ((ComponentSetup) viewportViewComponent).setUseAppTheme(useAppTheme);
    }

    @Override
    public void setUseAppColor(boolean useAppColor) {
        if (viewportViewComponent instanceof ComponentSetup)
            ((ComponentSetup) viewportViewComponent).setUseAppColor(useAppColor);
    }

    @Override
    public void setRoundCorners(boolean roundCorners) {
        if (viewportViewComponent instanceof ComponentSetup)
            ((ComponentSetup) viewportViewComponent).setRoundCorners(roundCorners);
    }

    @Override
    public void setPaintBorder(boolean paintBorder) {
        if (viewportViewComponent instanceof ComponentSetup)
            ((ComponentSetup) viewportViewComponent).setPaintBorder(paintBorder);
    }
    
    @Override
    public void setPreferredSize(Dimension preferredSize) {
        this.width = preferredSize.width;
        this.height = preferredSize.height;
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        super.setPreferredSize(preferredSize);
    }

    @Override
    public void setViewportView(Component view) {
        this.viewportViewComponent = view;
        super.setViewportView(view);
    }

    /**
     * @return if the vertical scroll unit should be updated automatically on 
     * {@link ComponentSetup#updateUISize()} call or not
     */
    public boolean isEnabledAutomaticVerticalUnitAssignment() {
        return enableAutomaticVerticalUnitAssignment;
    }

    /**
     * Changes the automatic adjustment for the vertical scroll unit
     * 
     * @param enableAutomaticVerticalUnitAssignment if true, the vertical scroll 
     * unit will be updated on {@link ComponentSetup#updateUISize()} call
     */
    public void setEnableAutomaticVerticalUnitAssignment(boolean enableAutomaticVerticalUnitAssignment) {
        this.enableAutomaticVerticalUnitAssignment = enableAutomaticVerticalUnitAssignment;
    }

    /**
     * @return if the horizontal scroll unit should be updated automatically on 
     * {@link ComponentSetup#updateUISize()} call or not
     */
    public boolean isEnabledAutomaticHorizontalUnitAssignment() {
        return enableAutomaticHorizontalUnitAssignment;
    }

    /**
     * Changes the automatic adjustment for the horizontal scroll unit
     * 
     * @param enableAutomaticHorizontalUnitAssignment if true, the horizontal 
     * scroll unit will be updated on {@link ComponentSetup#updateUISize()} call
     */
    public void setEnableAutomaticHorizontalUnitAssignment(boolean enableAutomaticHorizontalUnitAssignment) {
        this.enableAutomaticHorizontalUnitAssignment = enableAutomaticHorizontalUnitAssignment;
    }
    
    /**
     * Shortcut for {@link JScrollBar#setUnitIncrement(int)} for the vertical
     * scroll bar, this method takes in account the UI scale.
     * 
     * @param verticalScrollUnitIncrement The scrollbar's unit increment.
     * @see ScrollPane#setEnableAutomaticVerticalUnitAssignment(boolean) 
     */
    public void setVerticalScrollUnitIncrement(int verticalScrollUnitIncrement) {
        this.verticalScrollUnitIncrement = verticalScrollUnitIncrement;
        updateUISize();
    }

    /**
     * Shortcut for {@link JScrollBar#setUnitIncrement(int)} for the horizontal
     * scroll bar, this method takes in account the UI scale.
     * 
     * @param horizontalScrollUnitIncrement The scrollbar's unit increment.
     * @see ScrollPane#setEnableAutomaticHorizontalUnitAssignment(boolean) 
     */
    public void setHorizontalScrollUnitIncrement(int horizontalScrollUnitIncrement) {
        this.horizontalScrollUnitIncrement = horizontalScrollUnitIncrement;
        updateUISize();
    }
    
    /**
     * Shortcut for {@link JScrollBar#setUnitIncrement(int)} for the both the
     * horizontal and vertical scroll bars, this method takes in account the 
     * UI scale.
     * 
     * @param scrollUnitIncrement The scrollbar's unit increment.
     * @see ScrollPane#setEnableAutomaticVerticalUnitAssignment(boolean) 
     * @see ScrollPane#setEnableAutomaticHorizontalUnitAssignment(boolean) 
     */
    public void setScrollUnitIncrement(int scrollUnitIncrement) {
        this.verticalScrollUnitIncrement = scrollUnitIncrement;
        this.horizontalScrollUnitIncrement = scrollUnitIncrement;
        updateUISize();
    }

    /**
     * @return the value for verticalScrollUnitIncrement set by either
     * {@link ScrollPane#setScrollUnitIncrement(int)} or {@link ScrollPane#setVerticalScrollUnitIncrement(int)}
     */
    public int getVerticalScrollUnitIncrement() {
        return verticalScrollUnitIncrement;
    }

    /**
     * @return the value for horizontalScrollUnitIncrement set by either
     * {@link ScrollPane#setScrollUnitIncrement(int)} or {@link ScrollPane#setHorizontalScrollUnitIncrement(int)}
     */
    public int getHorizontalScrollUnitIncrement() {
        return horizontalScrollUnitIncrement;
    }
}
