package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import ui.enums.ImageButtonArrangement;
import ui.enums.UIAlignment;

/**
 * Custom painted JPopupMenu
 * 
 * @author cristopher
 */
public class ContextMenu extends JPopupMenu implements ComponentSetup {
    protected final Panel viewContainer = new Panel();
    protected final JScrollPane viewPanel = new JScrollPane() {
//        @Override
//        public void setPreferredSize(Dimension preferredSize) {
//            preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
//            preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
//
//            super.setPreferredSize(preferredSize);
//        }
    };
    
    protected int width = 120, height = 66;
    
    protected boolean appTheme = true;
    protected boolean appColor = false;
    
    private final boolean useScrollPane;
    
    private String selection = "";
    private final LinkedList<ImageButton> elements = new LinkedList<>();
    
    private final Component container;
    
    /**
     * Creates a new ContextMenu
     * 
     * @param container container
     * @param hideOverflow if true overflow elements will be hidden
     */
    public ContextMenu(Component container, boolean hideOverflow) {
        this.container = container;
        this.useScrollPane = hideOverflow;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        Insets margins = new Insets(0, 0, 0, 0);
        setBorder(new EmptyBorder(margins));
        
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                System.out.println("hello");
//            }
//        });
        
        if (useScrollPane) {
            viewPanel.setBorder(null);
            viewPanel.setViewportView(viewContainer);
            
            add(viewPanel);
        } else {
            
            add(viewContainer);
        }
        
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        if (useScrollPane) {
            viewPanel.setPreferredSize(new Dimension(width, height));
            viewPanel.getVerticalScrollBar().setUI(new ColorScrollBarUI());
            viewPanel.getHorizontalScrollBar().setUI(new ColorScrollBarUI());
            
            viewPanel.getVerticalScrollBar().setUnitIncrement((int) (22 * UIProperties.uiScale));
            viewPanel.getHorizontalScrollBar().setUnitIncrement((int) (22 * UIProperties.uiScale));
        }
        
        updateOptionsSize();
    }

    @Override
    public void updateUIFont() {
        viewContainer.updateUIFont();
    }

    @Override
    public void updateUITheme() {
        setBackground(UIProperties.APP_BGA);
        
        if (useScrollPane) {
            viewPanel.getVerticalScrollBar().setUI(new ColorScrollBarUI());
            viewPanel.getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        }
        
        viewContainer.updateUITheme();
    }

    @Override
    public void updateUIColors() {
        viewContainer.updateUIColors();
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
        width = preferredSize.width;
        height = preferredSize.height;
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        super.setPreferredSize(preferredSize);
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * Makes visible the popup menu
     * 
     * @param x location of the upper left corner
     * @param y location of the upper left corner
     */
    public void show(int x, int y) {
        show(container, x, y);
    }
    
    /**
     * Makes visible the popup menu
     * 
     * @param p location of the upper left corner
     */
    public void show(Point p) {
        show(container, p.x, p.y);
    }

    @Override
    public void show(Component invoker, int x, int y) {
        super.show(invoker, x, y);
    }
    
    /**
     * Makes invisible the popup menu
     */
    public void hideMenu() {
        super.setVisible(false);
    }

    public String getSelection() {
        return selection;
    }
    
    private void updateOptionsSize() {
        int elementHeight = useScrollPane ? 22 : 30;
        int containerHeight = elementHeight * elements.size();
        
        viewContainer.setPreferredSize(new Dimension((containerHeight <= height) ? width : width - 12, containerHeight));
        
        if (!useScrollPane)
            setPreferredSize(new Dimension(width, containerHeight));
        else
            setPreferredSize(new Dimension(width, height));
        
        if (containerHeight > height)
            for (ImageButton c : elements)
                c.setPreferredSize(new Dimension(width - 10, elementHeight));
        else
            for (ImageButton c : elements)
                c.setPreferredSize(new Dimension(width, elementHeight));
        
        viewContainer.revalidate();
        viewContainer.repaint();
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
        ImageButton c = new ImageButton(text, false, ImageButtonArrangement.LEFT_TEXT_RIGHT_IMAGE);
        c.setRoundCorners(false);
        c.addActionListener((Action) -> {
            selection = text;
            hideMenu();
        });
        
        if (actions != null)
            for (ActionListener l : actions)
                c.addActionListener(l);
        
        if (elements.isEmpty())
            viewContainer.add(c, viewContainer, viewContainer, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.NORTH, addPadding ? 10 : 0);
        else
            viewContainer.add(c, viewContainer, elements.getLast(), UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, addPadding ? 10 : 0);
        
        elements.add(c);
        
        updateOptionsSize();
    }
    
    /**
     * Adds a new option with bundled in icons or base64 images
     * 
     * @param text
     * @param lightImage image path or base64 string settled when light theme is active
     * @param darkImage image path or base64 string settled when dark theme is active, can be null
     * @param hoverImage image path or base64 string settled when mouse is over the option, can be null
     * @param areImagesBundled flag to indicate if images are bundled in
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(String text, String lightImage, String darkImage, String hoverImage, boolean areImagesBundled, boolean addPadding, ActionListener ... actions) {
        ImageButton c = new ImageButton(text, false, ImageButtonArrangement.LEFT_TEXT_RIGHT_IMAGE);
        
        c.setLightThemedImage(lightImage, areImagesBundled, 22, 22);
        c.setDarkThemedImage(darkImage, areImagesBundled, 22, 22);
        c.setHoverImage(hoverImage, areImagesBundled);
        
        c.setRoundCorners(false);
        c.addActionListener((Action) -> {
            selection = text;
            hideMenu();
        });
        
        if (actions != null)
            for (ActionListener l : actions)
                c.addActionListener(l);
        
        if (elements.isEmpty())
            viewContainer.add(c, viewContainer, viewContainer, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.NORTH, addPadding ? 10 : 0);
        else
            viewContainer.add(c, viewContainer, elements.getLast(), UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, addPadding ? 10 : 0);
        
        elements.add(c);
        
        updateOptionsSize();
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
        ImageButton c = new ImageButton(text, false, ImageButtonArrangement.LEFT_TEXT_RIGHT_IMAGE);
        
        c.setLightThemedImage(lightImage, 22, 22);
        c.setDarkThemedImage(darkImage, 22, 22);
        c.setHoverImage(hoverImage);
        
        c.setRoundCorners(false);
        c.addActionListener((Action) -> {
            selection = text;
            hideMenu();
        });
        
        if (actions != null)
            for (ActionListener l : actions)
                c.addActionListener(l);
        
        if (elements.isEmpty())
            viewContainer.add(c, viewContainer, viewContainer, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.NORTH, addPadding ? 10 : 0);
        else
            viewContainer.add(c, viewContainer, elements.getLast(), UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, addPadding ? 10 : 0);
        
        elements.add(c);
        
        updateOptionsSize();
    }
}
