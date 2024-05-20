package ui;

import java.awt.Component;
import javax.swing.JDialog;
import ui.enums.UIAlignment;
import utils.LibUtilities;

/**
 * App window (Dialog)<br>
 * This Window uses SpringLayout to align added components
 * 
 * @author cristopher
 */
public class Dialog extends JDialog implements ComponentSetup {
    /**
     * Dialog width
     */
    protected int width;
    
    /**
     * Dialog height
     */
    protected int height;
    
    /**
     * Internal Panel where all components are added when using implemented <code>add</code> methods
     * @see Dialog#add(java.awt.Component, ui.enums.UIAlignment, ui.enums.UIAlignment, int, ui.enums.UIAlignment, ui.enums.UIAlignment, int) 
     * @see Dialog#add(java.awt.Component, java.awt.Component, ui.enums.UIAlignment, ui.enums.UIAlignment, int, ui.enums.UIAlignment, ui.enums.UIAlignment, int) 
     * @see Dialog#add(java.awt.Component, java.awt.Component, java.awt.Component, ui.enums.UIAlignment, ui.enums.UIAlignment, int, ui.enums.UIAlignment, ui.enums.UIAlignment, int) 
     */
    protected final Panel container = new Panel();
    
    
    /**
     * Create a new program window (JDialog)<br>
     * The default behavior for this window is {@link javax.swing.WindowConstants#HIDE_ON_CLOSE}
     * 
     * @param width the width
     * @param height the height
     */
    public Dialog(int width, int height) {
        this.width = width;
        this.height = height;
        
        initUI();
    }

    @Override
    public final void initUI() {
        setSize(width, height);
        
        setDefaultCloseOperation(Dialog.HIDE_ON_CLOSE);
        setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
//        setAlwaysOnTop(true);
        
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
     * @param width the width
     */
    public void setWidth(int width) {
        this.width = width;
        updateUISize();
    }

    /**
     * Set a new window height
     * 
     * @param height the height
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
