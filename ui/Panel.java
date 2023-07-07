package ui;

import ui.enums.UIAlignment;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import utils.AddedComponent;

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
    
    private final ArrayList<AddedComponent> addedComponents = new ArrayList<>();
    
    
    /**
     * Creates a new JPanel given a width and height
     * 
     * @param width
     * @param height 
     */
    public Panel(int width, int height) {
        this.width = width;
        this.height = height;
        
        initUI();
    }

    /**
     * Creates a new JPanel without specifying its dimensions
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
        
        for (AddedComponent ac : addedComponents) {
            layout.putConstraint(UIProperties.UIAlignmentToString(ac.csx), ac.c, (int) (ac.xPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(ac.cxs), ac.cx);
            layout.putConstraint(UIProperties.UIAlignmentToString(ac.csy), ac.c, (int) (ac.yPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(ac.cys), ac.cy);
        }
    }

    @Override
    public void updateUIFont() {
        for (Component c : getComponents())
            if (c instanceof ComponentSetup)
                ((ComponentSetup) c).updateUIFont();
    }

    @Override
    public void updateUITheme() {
        if (appTheme)
            setBackground(UIProperties.APP_BG);
        
        for (Component c : getComponents())
            if (c instanceof ComponentSetup)
                ((ComponentSetup) c).updateUITheme();
    }

    @Override
    public void updateUIColors() {
        if (appColor)
            setBackground(UIProperties.APP_BG_COLOR);
        
        for (Component c : getComponents())
            if (c instanceof ComponentSetup)
                ((ComponentSetup) c).updateUIColors();
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

    @Override
    public void remove(Component comp) {
        for (int i = 0; i < addedComponents.size(); i++)
            if (addedComponents.get(i).c == comp) {
                addedComponents.remove(i);
                break;
            }
        
        super.remove(comp);
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
        
        addedComponents.add(new AddedComponent(c, cx, cy, csx, cxs, xPad, csy, cys, yPad));
        
        layout.putConstraint(UIProperties.UIAlignmentToString(csx), c, (int) (xPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cxs), cx);
        layout.putConstraint(UIProperties.UIAlignmentToString(csy), c, (int) (yPad * UIProperties.uiScale), UIProperties.UIAlignmentToString(cys), cy);
    }
}
