package ui;

import ui.enums.UIAlignment;
import java.awt.Component;
import javax.swing.JFrame;
import utils.LibUtilities;

/**
 * App window<br>
 * This Window uses SpringLayout to align added components
 * 
 * @author cristopher
 */
public class Window extends JFrame implements ComponentSetup {
    private final boolean exitOnClose;
    
    private int width, height;
    
    protected final Panel container = new Panel();

    /**
     * Create a new program window (JFrame)
     * 
     * @param width
     * @param height
     * @param exitOnClose if true, app will exit otherwise it will be hidden
     */
    public Window(int width, int height, boolean exitOnClose) {
        this.width = width;
        this.height = height;
        this.exitOnClose = exitOnClose;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        setSize(width, height);
        
        setDefaultCloseOperation(exitOnClose ? JFrame.EXIT_ON_CLOSE: JFrame.HIDE_ON_CLOSE);
        
        add(container);
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        setSize(width, height);
        container.updateUISize();
    }

    @Override
    public void updateUIFont() {
        container.updateUIFont();
    }

    @Override
    public void updateUITheme() {
        container.updateUITheme();
    }

    @Override
    public void updateUIColors() {
        container.updateUIColors();
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        
        if (!LibUtilities.IS_UNIX_LIKE) {
            // Windows fix
            width += 10;
        }
        
        width = (int) (width * UIProperties.uiScale);
        height = (int) (height * UIProperties.uiScale);
        
        super.setSize(width, height + (isUndecorated() ? 0 : UIProperties.TITLE_BAR_HEIGHT));
        
        setLocationRelativeTo(null);
    }

    /**
     * Make window visible
     */
    public void showWindow() {
        updateUISize();
        setVisible(true);
    }
    
    /**
     * Make window invisible
     */
    public void hideWindow() {
        setVisible(false);
    }
    
    /**
     * Toggle window visibility<br>
     * If visible, it becomes invisible<br>
     * If invisible, it becomes visible
     */
    public void toggleVisibility() {
        setVisible(!isVisible());
    }
    
    /**
     * Set a new window width
     * 
     * @param width the new width
     */
    public void setWidth(int width) {
        this.width = width;
        updateUISize();
    }

    /**
     * Set a new window height
     * 
     * @param height the new height
     */
    public void setHeight(int height) {
        this.height = height;
        updateUISize();
    }
    
    /**
     * Add a new component to this Window<br>
     * Alignment is with this window container. See {@link Panel}.
     * 
     * @param c is the component to add
     * @param csx is the side of c that will aligned with cx
     * @param cxs is the side of cx that will used to align c
     * @param xPad is the space between c and cx
     * @param csy is the side of c that will aligned with cy
     * @param cys is the side of cy that will used to align c
     * @param yPad is the space between c and cy 
     */
    public void add(Component c, UIAlignment csx, UIAlignment cxs, int xPad, UIAlignment csy, UIAlignment cys, int yPad) {
        container.add(c, container, container, csx, cxs, xPad, csy, cys, yPad);
    }
    
    /**
     * Add a new component to this Window<br>
     * Alignment is with given component c1
     * 
     * @param c is the component to add
     * @param c1 is the component to align c with
     * @param csx is the side of c that will aligned with cx
     * @param cxs is the side of cx that will used to align c
     * @param xPad is the space between c and cx
     * @param csy is the side of c that will aligned with cy
     * @param cys is the side of cy that will used to align c
     * @param yPad is the space between c and cy 
     */
    public void add(Component c, Component c1, UIAlignment csx, UIAlignment cxs, int xPad, UIAlignment csy, UIAlignment cys, int yPad) {
        container.add(c, c1, c1, csx, cxs, xPad, csy, cys, yPad);
    }
    
    /**
     * Add a new component to this Window<br>
     * 
     * @param c is the component to add
     * @param cx is the component to align with in x
     * @param cy is the component to align with in y
     * @param csx is the side of c that will aligned with cx
     * @param cxs is the side of cx that will used to align c
     * @param xPad is the space between c and cx
     * @param csy is the side of c that will aligned with cy
     * @param cys is the side of cy that will used to align c
     * @param yPad is the space between c and cy 
     */
    public void add(Component c, Component cx, Component cy, UIAlignment csx, UIAlignment cxs, int xPad, UIAlignment csy, UIAlignment cys, int yPad) {
        container.add(c, cx, cy, csx, cxs, xPad, csy, cys, yPad);
    }

    @Override
    public void setUseAppTheme(boolean useAppTheme) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setUseAppColor(boolean useAppColor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRoundCorners(boolean roundCorners) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPaintBorder(boolean paintBorder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
