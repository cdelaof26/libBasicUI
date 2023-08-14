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
public class AddedComponent {
    public final Component c;
    public final Component cx;
    public final Component cy;
    public final UIAlignment csx;
    public final UIAlignment cxs;
    public final int xPad;
    public final UIAlignment csy;
    public final UIAlignment cys;
    public final int yPad;

    public AddedComponent(Component c, Component cx, Component cy, UIAlignment csx, UIAlignment cxs, int xPad, UIAlignment csy, UIAlignment cys, int yPad) {
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
