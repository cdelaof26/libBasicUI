package ui;

import ui.enums.UIAlignment;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import utils.ComponentConstrains;

/**
 * Window's container, based in JPanel<br>
 * Uses SpringLayout as layout manager
 * 
 * @author cristopher
 */
public class Panel extends JPanel implements ComponentSetup {
    protected final SpringLayout layout = new SpringLayout();
    
    protected int width = -1, height = -1;
    
    protected boolean appTheme = true;
    protected boolean appColor = false;
    
    /**
     * If true, calls all methods on {@link ComponentSetup} interface when a 
     * component is added<br>
     * Keep in mind that this might slower down adding components
     */
    public boolean updateOnJComponentAdded = true;
    
    private final ArrayList<ComponentConstrains> componentConstraints = new ArrayList<>();
    
    /**
     * External components to update when methods in {@link ComponentSetup} are 
     * called.<br>
     * Components such {@link Window}, {@link Dialog}, {@link ContextMenu} and 
     * its subclasses.<br><br>
     * 
     * This is intended to facilitate the update of external components without
     * having to override any methods in {@link ComponentSetup} interface.
     */
    public final ArrayList<ComponentSetup> externalComponents = new ArrayList<>();
    
    
    /**
     * Creates a new Panel given a width and height
     * 
     * @param width the width
     * @param height the height
     */
    public Panel(int width, int height) {
        this.width = width;
        this.height = height;
        
        initUI();
    }

    /**
     * Creates a new Panel without specifying its dimensions
     */
    public Panel() {
        initUI();
    }
    
    @Override
    public final void initUI() {
        setLayout(layout);
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        if (width != -1 && height != -1)
            setPreferredSize(new Dimension(width, height));
        
        for (Component c : getComponents())
            if (c instanceof ComponentSetup)
                ((ComponentSetup) c).updateUISize();
        
        for (ComponentSetup c : externalComponents)
            c.updateUISize();
        
        for (ComponentConstrains ac : componentConstraints) {
            layout.putConstraint(UIProperties.UIAlignmentToString(ac.csx), ac.c, (int) (ac.xPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(ac.cxs), ac.cx);
            layout.putConstraint(UIProperties.UIAlignmentToString(ac.csy), ac.c, (int) (ac.yPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(ac.cys), ac.cy);
        }
    }

    @Override
    public void updateUIFont() {
        for (Component c : getComponents())
            if (c instanceof ComponentSetup)
                ((ComponentSetup) c).updateUIFont();
        
        for (ComponentSetup c : externalComponents)
            c.updateUIFont();
    }

    @Override
    public void updateUITheme() {
        if (appTheme)
            setBackground(UIProperties.APP_BG);
        
        for (Component c : getComponents())
            if (c instanceof ComponentSetup)
                ((ComponentSetup) c).updateUITheme();
        
        for (ComponentSetup c : externalComponents)
            c.updateUITheme();
    }

    @Override
    public void updateUIColors() {
        if (appColor)
            setBackground(UIProperties.APP_BG_COLOR);
        
        for (Component c : getComponents())
            if (c instanceof ComponentSetup)
                ((ComponentSetup) c).updateUIColors();
        
        for (ComponentSetup c : externalComponents)
            c.updateUIColors();
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
        this.width = preferredSize.width;
        this.height = preferredSize.height;
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        super.setPreferredSize(preferredSize);
    }

    private int findAddedComponent(Component c) {
        for (int i = 0; i < componentConstraints.size(); i++)
            if (componentConstraints.get(i).c == c)
                return i;
        
        return -1;
    }
    
    /**
     * Returns the layout constrains associated to a {@link Component} c
     * @param c the component
     * @return an {@link ComponentConstrains} object or null if the c component 
     * was not added using implemented <code>add</code> methods.
     * 
     * @see Panel#add(java.awt.Component, ui.enums.UIAlignment, ui.enums.UIAlignment, int, ui.enums.UIAlignment, ui.enums.UIAlignment, int) 
     * @see Panel#add(java.awt.Component, java.awt.Component, ui.enums.UIAlignment, ui.enums.UIAlignment, int, ui.enums.UIAlignment, ui.enums.UIAlignment, int) 
     * @see Panel#add(java.awt.Component, java.awt.Component, java.awt.Component, ui.enums.UIAlignment, ui.enums.UIAlignment, int, ui.enums.UIAlignment, ui.enums.UIAlignment, int) 
     */
    public ComponentConstrains getComponentConstrains(Component c) {
        int index = findAddedComponent(c);
        if (index == -1)
            return null;
        
        return componentConstraints.get(index);
    }
    
    /**
     * Replaces the constraints or a component c.
     * @param c the components
     * @param constrains the new constraints
     * @return false if the c component is not part of any constraint, otherwise 
     * true
     */
    public boolean setComponentConstrains(Component c, ComponentConstrains constrains) {
        int index = findAddedComponent(c);
        if (index == -1)
            return false;
        
        ComponentConstrains cc = new ComponentConstrains(c, constrains.cx, constrains.cy, constrains.csx, constrains.cxs, constrains.xPad, constrains.csy, constrains.cys, constrains.yPad);
        if (c == constrains.cx)
            System.out.println("c == cx");
        if (c == constrains.cy)
            System.out.println("c == cy");
        
        componentConstraints.set(index, cc);
        
        layout.putConstraint(UIProperties.UIAlignmentToString(cc.csx), cc.c, (int) (cc.xPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cc.cxs), cc.cx);
        layout.putConstraint(UIProperties.UIAlignmentToString(cc.csy), cc.c, (int) (cc.yPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cc.cys), cc.cy);
        
        return true;
    }
    
    @Override
    public void remove(Component comp) {
        int componentIndex = findAddedComponent(comp);
        if (componentIndex != -1)
            componentConstraints.remove(componentIndex);
        
        layout.removeLayoutComponent(comp);
        super.remove(comp);
    }
    
    /**
     * Changes the elements associated to the component c
     * 
     * @param c the component
     * @param cx the new component to align c in x
     * @param cy the new component to align c in y
     * @see Panel#add(java.awt.Component, java.awt.Component, java.awt.Component, ui.enums.UIAlignment, ui.enums.UIAlignment, int, ui.enums.UIAlignment, ui.enums.UIAlignment, int) 
     */
    public void updateAlignComponent(Component c, Component cx, Component cy) {
        int componentIndex = findAddedComponent(c);
        if (componentIndex == -1)
            return;
        
        ComponentConstrains cc = componentConstraints.get(componentIndex);
        cc.cx = cx;
        cc.cy = cy;
        
        layout.putConstraint(UIProperties.UIAlignmentToString(cc.csx), cc.c, (int) (cc.xPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cc.cxs), cc.cx);
        layout.putConstraint(UIProperties.UIAlignmentToString(cc.csy), cc.c, (int) (cc.yPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cc.cys), cc.cy);
    }
    
    /**
     * Changes the padding associated to the component c
     * 
     * @param c the component
     * @param xPad the new padding in x
     * @param yPad the new padding in y
     */
    public void updateAlignComponent(Component c, int xPad, int yPad) {
        int componentIndex = findAddedComponent(c);
        if (componentIndex == -1)
            return;
        
        ComponentConstrains cc = componentConstraints.get(componentIndex);
        cc.xPad = xPad;
        cc.yPad = yPad;
        
        layout.putConstraint(UIProperties.UIAlignmentToString(cc.csx), cc.c, (int) (cc.xPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cc.cxs), cc.cx);
        layout.putConstraint(UIProperties.UIAlignmentToString(cc.csy), cc.c, (int) (cc.yPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cc.cys), cc.cy);
    }

    private Thread addThread = null;
    private int addCalls = 0;
    
    @Override
    public Component add(Component comp) {
        if (updateOnJComponentAdded && addThread == null) {
            addThread = new Thread(() -> {
                addCalls = 1;
                while (addCalls > 0) {
                    try { Thread.sleep(100); } catch (InterruptedException ex) { }
                    addCalls--;
                }
                
                SwingUtilities.invokeLater(() -> {
                    updateUISize();
                    updateUIFont();
                    updateUITheme();
                    updateUIColors();
                });
                
                addThread = null;
            });
            
            addThread.start();
        } else if (updateOnJComponentAdded)
            addCalls++;
        
//        if (updateOnJComponentAdded) {
//            updateUISize();
//            updateUIFont();
//            updateUITheme();
//            updateUIColors();
//        }
        
        return super.add(comp);
    }
    
    /**
     * Add a new component to this Panel<br>
     * This method uses {@link SpringLayout} as layout manager
     * 
     * @param c is the component to add
     * @param cx is the component to align with in x
     * @param cy is the component to align with in y
     * @param csx is the side of c that will aligned with cx
     * @param cxs is the side of cx that will used to align c
     * @param xPad is the space between c and cx
     * @param cys is the side of cy that will used to align c
     * @param csy is the side of c that will aligned with cy
     * @param yPad is the space between c and cy 
     */
    public void add(Component c, Component cx, Component cy, UIAlignment csx, UIAlignment cxs, int xPad, UIAlignment csy, UIAlignment cys, int yPad) {
        add(c);
        
        componentConstraints.add(new ComponentConstrains(c, cx, cy, csx, cxs, xPad, csy, cys, yPad));
        
        layout.putConstraint(UIProperties.UIAlignmentToString(csx), c, (int) (xPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cxs), cx);
        layout.putConstraint(UIProperties.UIAlignmentToString(csy), c, (int) (yPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cys), cy);
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
        add(c, this, this, csx, cxs, xPad, csy, cys, yPad);
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
        add(c, c1, c1, csx, cxs, xPad, csy, cys, yPad);
    }
}
