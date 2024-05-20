package utils;

import java.awt.Component;
import ui.enums.UIAlignment;

/**
 * Required fields to add a component to another<br>
 * 
 * This class is meant to update distance between components once placed when
 * UI is rescaled
 * 
 * @see ui.Panel
 * @author cristopher
 */
public class ComponentConstrains {
    /**
     * Component to add
     */
    public Component c;
    
    /**
     * Component to align c with in x
     */
    public Component cx;
    
    /**
     * Component to align c with in y
     */
    public Component cy;
    
    /**
     * c side that aligns with cx
     */
    public UIAlignment csx;
    
    /**
     * cx side that aligns with c
     */
    public UIAlignment cxs;
    
    /**
     * Distance between c and cx
     */
    public int xPad;
    
    /**
     * c side that aligns with cy
     */
    public UIAlignment csy;
    
    /**
     * cy side that aligns with c
     */
    public UIAlignment cys;
    
    /**
     * Distance between c and cy
     */
    public int yPad;

    /**
     * Class to track all components involved in JComponent adding to a Panel
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
    public ComponentConstrains(Component c, Component cx, Component cy, UIAlignment csx, UIAlignment cxs, int xPad, UIAlignment csy, UIAlignment cys, int yPad) {
        this.c = c;
        this.cx = cx;
        this.cy = cy;
        this.csx = csx;
        this.cxs = cxs;
        this.xPad = xPad;
        this.csy = csy;
        this.cys = cys;
        this.yPad = yPad;
    }
}
