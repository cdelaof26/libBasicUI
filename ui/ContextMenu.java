package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import ui.enums.ImageButtonArrangement;
import ui.enums.UIAlignment;

/**
 * Custom painted JPopupMenu
 * 
 * @author cristopher
 */
public class ContextMenu extends JPopupMenu implements ComponentSetup {
    /**
     * Internal container for this ContextMenu
     */
    protected final Panel viewContainer = new Panel();
    
    /**
     * Internal JScrollPane
     */
    protected final JScrollPane viewPanel = new JScrollPane();
    
    /**
     * ContextMenu's width
     */
    protected int width = 120;
    
    /**
     * ContextMenu's height
     */
    protected int height = 66;
    
    /**
     * Total padding in the container
     */
    protected int padding = 0;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see ColorButton#setUseAppTheme(boolean)
     */
    protected boolean appTheme = true;
    
    /**
     * Condition that determines which colors will be used to paint this component
     * @see ColorButton#setUseAppColor(boolean) 
     */
    protected boolean appColor = false;
    
    private final boolean useScrollPane;
    private boolean slimElements = false;
    
    private String selection = "";
    private LinkedList<ImageButton> elements = new LinkedList<>();
    private ImageButtonArrangement elementsArrange = ImageButtonArrangement.LEFT_TEXT_RIGHT_IMAGE;
    
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
            
            viewPanel.getVerticalScrollBar().setUnitIncrement((int) (15 * UIProperties.uiScale));
            viewPanel.getHorizontalScrollBar().setUnitIncrement((int) (15 * UIProperties.uiScale));
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

    /**
     * Sets the width for this component
     * @param width the width
     */
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

    /**
     * @return the text of last clicked option
     */
    public String getSelection() {
        return selection;
    }

    /**
     * Makes selectable elements slim<br>
     * By default, elements are slim if <code>hideOverflow</code> was set to true
     * 
     * @param slimElements if true, the added options will be <code>width=15</code>,
     * otherwise <code>width=30</code>
     */
    public void setSlimElements(boolean slimElements) {
        this.slimElements = slimElements;
        updateOptionsSize();
    }

    /**
     * Changes ImageButton arrange, to be effective it must be settled before 
     * adding options
     * 
     * @param elementsArrange the arrangement for the options
     * @see ImageButtonArrangement
     */
    public void setElementsArrange(ImageButtonArrangement elementsArrange) {
        this.elementsArrange = elementsArrange;
    }
    
    private void updateOptionsSize() {
        int elementHeight = (useScrollPane || slimElements) ? 15 : 30;
        int containerHeight = elementHeight * elements.size() + padding;
        
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
    
    private void addOptionToViewContainer(ImageButton c, boolean addPadding, ActionListener ... actions) {
        if (actions != null) {
            for (ActionListener l : actions)
                c.addActionListener(l);
            if (container instanceof Menu)
                c.addActionListener((Action) -> {
                    Menu.aMenuIsOpened = false;
                });
        }
        
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
     * @param text the option text
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(String text, boolean addPadding, ActionListener ... actions) {
        if (addPadding)
            padding += 10;
        
        ImageButton c = new ImageButton(text, false, elementsArrange);
        c.setRoundCorners(false);
        c.addActionListener((Action) -> {
            selection = text;
            hideMenu();
        });
        
        addOptionToViewContainer(c, addPadding, actions);
    }
    
    /**
     * Adds a new option with bundled in icons or base64 images
     * 
     * @param text the option text
     * @param lightImage image path or base64 string settled when light theme is active
     * @param darkImage image path or base64 string settled when dark theme is active, can be null
     * @param hoverImage image path or base64 string settled when mouse is over the option, can be null
     * @param areImagesBundled flag to indicate if images are bundled in
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(String text, String lightImage, String darkImage, String hoverImage, boolean areImagesBundled, boolean addPadding, ActionListener ... actions) {
        if (addPadding)
            padding += 10;
        
        ImageButton c = new ImageButton(text, false, elementsArrange);
        
        c.setLightThemedImage(lightImage, areImagesBundled, 15, 15);
        c.setDarkThemedImage(darkImage, areImagesBundled, 15, 15);
        c.setHoverImage(hoverImage, areImagesBundled);
        
        c.setRoundCorners(false);
        c.addActionListener((Action) -> {
            selection = text;
            hideMenu();
        });
        
        addOptionToViewContainer(c, addPadding, actions);
    }
    
    /**
     * Adds a new option with BufferedImages
     * 
     * @param text the option text
     * @param lightImage image path or base64 string settled when light theme is active
     * @param darkImage image path or base64 string settled when dark theme is active, can be null
     * @param hoverImage image path or base64 string settled when mouse is over the option, can be null
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(String text, BufferedImage lightImage, BufferedImage darkImage, BufferedImage hoverImage, boolean addPadding, ActionListener ... actions) {
        if (addPadding)
            padding += 10;
        
        ImageButton c = new ImageButton(text, false, elementsArrange);
        
        c.setLightThemedImage(lightImage, 15, 15);
        c.setDarkThemedImage(darkImage, 15, 15);
        c.setHoverImage(hoverImage);
        
        c.setRoundCorners(false);
        c.addActionListener((Action) -> {
            selection = text;
            hideMenu();
        });
        
        addOptionToViewContainer(c, addPadding, actions);
    }
    
    /**
     * Adds a new option
     * 
     * @param text the option text
     * @param lightImage image settled when light theme is active
     * @param darkImage image settled when dark theme is active, can be null
     * @param hoverImage image settled when mouse is over the option, can be null
     * @param addPadding if true, a padding will be added between the new option 
     * and the last (if any)
     * @param actions actions performed by added option, can be null
     */
    public void addOption(String text, File lightImage, File darkImage, File hoverImage, boolean addPadding, ActionListener ... actions) {
        if (addPadding)
            padding += 10;
        
        ImageButton c = new ImageButton(text, false, elementsArrange);
        
        c.setLightThemedImage(lightImage, 15, 15);
        c.setDarkThemedImage(darkImage, 15, 15);
        c.setHoverImage(hoverImage);
        
        c.setRoundCorners(false);
        c.addActionListener((Action) -> {
            selection = text;
            hideMenu();
        });
        
        addOptionToViewContainer(c, addPadding, actions);
    }
    
    /**
     * Removes all options added to the ContextMenu
     */
    public void removeAllOptions() {
        for (ImageButton b : elements)
            viewContainer.remove(b);
        
        elements = new LinkedList<>();
        
        viewContainer.revalidate();
        viewContainer.repaint();
    }
    
    /**
     * Removes the a option given a index
     * @param index the index of the element to remove
     * @throws NoSuchElementException throw if the given index is not valid
     */
    public void removeOption(int index) throws NoSuchElementException {
        ImageButton option = elements.remove(index);
        
        if (elements.size() > index)
            if (index == 0)
                viewContainer.layout.putConstraint(SpringLayout.NORTH, elements.get(index), 0, SpringLayout.NORTH, viewContainer);
            else
                viewContainer.layout.putConstraint(SpringLayout.NORTH, elements.get(index), 0, SpringLayout.SOUTH, elements.get(index - 1));
        
        viewContainer.remove(option);
        updateOptionsSize();
    }
}
