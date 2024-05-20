package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import ui.enums.SelectionPanelModes;
import ui.enums.UIAlignment;

/**
 * SelectionPanel is an UI element to select something, discard options or arrange
 * options order
 * 
 * @author cristopher
 */
public class SelectionPanel extends JScrollPane implements ComponentSetup {
    protected final Panel viewContainer = new Panel() {
        @Override
        public void updateUITheme() {
            if (appTheme)
                setBackground(UIProperties.APP_BGA);
        
            for (Component c : getComponents())
                if (c instanceof ComponentSetup)
                    ((ComponentSetup) c).updateUITheme();
        }
    };
    
    protected int width = -1, height = -1;
    
    private LinkedList<JComponent> elements = new LinkedList<>();
    private ArrayList<MouseListener> discardActions = new ArrayList<>();
    
    private final ImageButtonArrangement buttonArragement;
    
    private final SelectionPanelModes mode;
    
    private File lightImageFile;
    private File darkImageFile;
    private File hoverImageFile;
    
    private String lightImageData;
    private boolean isBundledLightImage;
    
    private String darkImageData;
    private boolean isBundledDarkImage;
    
    private String hoverImageData;
    private boolean isBundledHoverImage;
    
    private int imageWidth;
    private int imageHeight;
    
    
    protected int lastIndexHighlighted = -1;
    protected String selectedOption = "";
    
    
    /**
     * Creates a new selection panel, 
     * the mode for this is {@link SelectionPanelModes}.ARRANGE
     * 
     * @param width the width
     * @param height the height
     */
    public SelectionPanel(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");

//        this.width = width;
//        this.height = height;
//        this.buttonArragement = null;
//        this.mode = SelectionPanelModes.ARRANGE;
//                
//        initUI();
    }
    
    /**
     * Creates a new selection panel with image<br>
     * To add the image use <code>panel.setImage();</code>
     * 
     * @param width the width
     * @param height the height
     * @param buttonArragement the arrangement for buttons
     * @param mode the selection mode
     * @see SelectionPanelModes
     * @throws IllegalArgumentException if buttonArragement isn't *_RIGHT_IMAGE or
     * *_LEFT_IMAGE and if mode is ARRANGE
     */
    public SelectionPanel(int width, int height, ImageButtonArrangement buttonArragement, SelectionPanelModes mode) throws IllegalArgumentException {
        if (buttonArragement != ImageButtonArrangement.LEFT_TEXT_RIGHT_IMAGE && buttonArragement != ImageButtonArrangement.RIGHT_TEXT_LEFT_IMAGE && 
                buttonArragement != ImageButtonArrangement.CENTER_TEXT_RIGHT_IMAGE && buttonArragement != ImageButtonArrangement.CENTER_TEXT_LEFT_IMAGE)
            throw new IllegalArgumentException("Only RIGHT_IMAGE and LEFT_IMAGE are allowed");
        
        if (mode == SelectionPanelModes.ARRANGE)
            throw new IllegalArgumentException("ARRANGE selection mode doesn't support images");
        
        this.width = width;
        this.height = height;
        this.buttonArragement = buttonArragement;
        this.mode = mode;
        
        initUI();
    }
    
    /**
     * Creates a new selection panel without image<br>
     * 
     * @param width the width
     * @param height the height
     * @param mode the selection mode
     * @see SelectionPanelModes
     */
    public SelectionPanel(int width, int height, SelectionPanelModes mode) {
        this.width = width;
        this.height = height;
        this.buttonArragement = ImageButtonArrangement.CENTER_TEXT_RIGHT_IMAGE;
        this.mode = mode;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        setViewportView(viewContainer);
        setBorder(null);
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        setPreferredSize(new Dimension(width, height));
        
        getVerticalScrollBar().setUI(new ColorScrollBarUI());
        getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        
        getVerticalScrollBar().setUnitIncrement((int) (22 * UIProperties.uiScale));
        getHorizontalScrollBar().setUnitIncrement((int) (22 * UIProperties.uiScale));
        
        updateOptionsSize();
    }

    @Override
    public void updateUIFont() {
        viewContainer.updateUIFont();
    }

    @Override
    public void updateUITheme() {
        setBackground(UIProperties.APP_BGA);
        
        getVerticalScrollBar().setUI(new ColorScrollBarUI());
        getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        
        viewContainer.updateUITheme();
    }

    @Override
    public void updateUIColors() {
        viewContainer.updateUIColors();
    }

    @Override
    public void setUseAppTheme(boolean useAppTheme) {
        viewContainer.setUseAppTheme(useAppTheme);
    }

    @Override
    public void setUseAppColor(boolean useAppColor) {
        viewContainer.setUseAppColor(useAppColor);
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
     * Set an image from a file, the image will be show if dark theme is active<br>
     * 
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageFile the file
     * @param width use -1 to render the image with its original width
     * @param height use -1 to render the image with its original height
     */
    public void setDarkThemedImage(File imageFile, int width, int height) {
        this.darkImageFile = imageFile;
        if (this.darkImageFile != null)
            this.darkImageData = null;
        
        this.imageWidth = width;
        this.imageHeight = height;
        
        for (JComponent c : elements)
            if (c instanceof ImageButton)
                ((ImageButton) c).setDarkThemedImage(this.darkImageFile, this.imageWidth, this.imageHeight);
    }
    
    /**
     * Set an image from a base64 String, the image will be show if dark theme
     * is active
     * 
     * @param imageData image path or base64 codified data
     * @param bundledImage set true if <code>imageData</code> is a package path
     * @param width use -1 to render the image with its original width
     * @param height use -1 to render the image with its original height
     */
    public void setDarkThemedImage(String imageData, boolean bundledImage, int width, int height) {
        this.darkImageData = imageData;
        if (this.darkImageData != null)
            this.darkImageFile = null;
        
        this.isBundledDarkImage = bundledImage;
        this.imageWidth = width;
        this.imageHeight = height;
        
        for (JComponent c : elements)
            if (c instanceof ImageButton)
                ((ImageButton) c).setDarkThemedImage(this.darkImageData, this.isBundledDarkImage, this.imageWidth, this.imageHeight);
    }
    
    /**
     * Set an image from a file, the image will be show if light theme is active<br>
     * 
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageFile the file
     * @param width use -1 to render the image with its original width
     * @param height use -1 to render the image with its original height
     */
    public void setLightThemedImage(File imageFile, int width, int height) {
        this.lightImageFile = imageFile;
        if (this.lightImageFile != null)
            this.lightImageData = null;
        
        this.imageWidth = width;
        this.imageHeight = height;
        
        for (JComponent c : elements)
            if (c instanceof ImageButton)
                ((ImageButton) c).setLightThemedImage(this.lightImageFile, this.imageWidth, this.imageHeight);
    }
    
    /**
     * Set an image from a base64 String, the image will be show if light theme
     * is active
     * 
     * @param imageData image path or base64 codified data
     * @param bundledImage set true if <code>imageData</code> is a package path
     * @param width use -1 to render the image with its original width
     * @param height use -1 to render the image with its original height
     */
    public void setLightThemedImage(String imageData, boolean bundledImage, int width, int height) {
        this.lightImageData = imageData;
        if (this.lightImageData != null)
            this.lightImageFile = null;
        
        this.isBundledLightImage = bundledImage;
        this.imageWidth = width;
        this.imageHeight = height;
        
        for (JComponent c : elements)
            if (c instanceof ImageButton)
                ((ImageButton) c).setLightThemedImage(this.lightImageData, this.isBundledLightImage, this.imageWidth, this.imageHeight);
    }
    
    /**
     * Set an image from a file<br>
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageHoverFile the file
     */
    public void setHoverImage(File imageHoverFile) {
        this.hoverImageFile = imageHoverFile;
        if (this.hoverImageData != null)
            this.hoverImageData = null;
        
        for (JComponent c : elements)
            if (c instanceof ImageButton)
                ((ImageButton) c).setHoverImage(this.hoverImageFile);
    }
    
    /**
     * Set an hover image
     * 
     * @param imageHoverData image path or base64 codified data
     * @param bundledImage set true if <code>imageData</code> is a package path
     */
    public void setHoverImage(String imageHoverData, boolean bundledImage) {
        this.hoverImageData = imageHoverData;
        if (this.hoverImageFile != null)
            this.hoverImageFile = null;
        
        this.isBundledHoverImage = bundledImage;
        
        for (JComponent c : elements)
            if (c instanceof ImageButton)
                ((ImageButton) c).setHoverImage(this.hoverImageData, this.isBundledHoverImage);
    }
    
    private void updateOptionsSize() {
        int containerHeight = elements.size() * 22;
        
        viewContainer.setPreferredSize(new Dimension((containerHeight <= height) ? width : width - 10, containerHeight));
        
        if (mode != SelectionPanelModes.ARRANGE) {
            if (containerHeight > height)
                for (JComponent c : elements)
                    ((ImageButton) c).setPreferredSize(new Dimension(width - 10, 22));
            else
                for (JComponent c : elements)
                    ((ImageButton) c).setPreferredSize(new Dimension(width, 22));
        }
        
        viewContainer.revalidate();
        viewContainer.repaint();
    }
    
    public void disableAllOptions() {
        for (JComponent c : elements)
            c.setEnabled(false);
    }
    
    public void enableAllOptions() {
        for (JComponent c : elements)
            c.setEnabled(true);
    }
    
    public void toggleAllOptions() {
        if (!elements.isEmpty()) {
            boolean enable = !elements.getFirst().isEnabled();
            
            for (JComponent c : elements)
                c.setEnabled(enable);
        }
    }
    
    public boolean hasOptions() {
        return !elements.isEmpty();
    }
    
    public void unselectOption() {
        if (lastIndexHighlighted != -1) {
            ImageButton button = (ImageButton) elements.get(lastIndexHighlighted);
            button.setPaintAsHovering(false);
        }
        
        selectedOption = "";
    }
    
    private void highlightOption(int index) {
        lastIndexHighlighted = index;
        ImageButton button = (ImageButton) elements.get(index);
        button.setPaintAsHovering(true);
    }

    public String getSelectedOption() {
        return selectedOption;
    }
    
    public void removeAllOptions() {
        for (JComponent c : elements)
            viewContainer.remove(c);
        
        elements = new LinkedList<>();
        discardActions = new ArrayList<>();
        
        viewContainer.revalidate();
        viewContainer.repaint();
    }
    
    public void removeOption(int index) {
        JComponent c = elements.remove(index);
        discardActions.remove(index);
        
        if (elements.size() > index)
            if (index == 0)
                viewContainer.layout.putConstraint(SpringLayout.NORTH, elements.get(index), 0, SpringLayout.NORTH, viewContainer);
            else
                viewContainer.layout.putConstraint(SpringLayout.NORTH, elements.get(index), 0, SpringLayout.SOUTH, elements.get(index - 1));
        
        if (mode == SelectionPanelModes.DISCARD)
            for (int i = index; i < elements.size(); i++) {
                MouseListener l = discardActions.remove(index);
                ImageButton cb = (ImageButton) elements.get(i);
                cb.removeMouseListener(l);
                
                final int j = i;
                l = new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        removeOption(j);
                    }
                };
                
                cb.addMouseListener(l);
                discardActions.add(l);
            }
        
        viewContainer.remove(c);
        
        updateOptionsSize();
    }
    
    public void addOption(String text, ActionListener action) {
        if (mode == SelectionPanelModes.ARRANGE)
            throw new UnsupportedOperationException("ARRANGE doesn't support ActionListener");
        
        ImageButton c = new ImageButton(text, false, buttonArragement);
        c.setPreferredSize(new Dimension(width, 22));
        c.setRoundCorners(false);
        
        
        if (this.lightImageFile != null)
            c.setLightThemedImage(lightImageFile, imageWidth, imageHeight);
        else if (this.lightImageData != null)
            c.setLightThemedImage(lightImageData, isBundledLightImage, imageWidth, imageHeight);
        
        if (this.darkImageFile != null)
            c.setDarkThemedImage(darkImageFile, imageWidth, imageHeight);
        else if (this.darkImageData != null)
            c.setDarkThemedImage(darkImageData, isBundledDarkImage, imageWidth, imageHeight);
        
        
        if (this.hoverImageFile != null)
            c.setHoverImage(hoverImageFile);
        else if (this.hoverImageData != null)
            c.setHoverImage(hoverImageData, isBundledHoverImage);
        
        c.addActionListener(action);
        if (mode == SelectionPanelModes.DISCARD) {
            c.setOnlyActionIfImageIsClicked(true);
            int i = elements.size();
            
            MouseAdapter l = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    removeOption(i);
                }
            };
            
            c.addMouseListener(l);
            discardActions.add(l);
        } else if (mode == SelectionPanelModes.SELECTION) {
            int i = elements.size();
            
            c.addActionListener((Action) -> {
                unselectOption();
                selectedOption = c.getText_();
                highlightOption(i);
            });
        }
        
        if (elements.isEmpty())
            viewContainer.add(c, viewContainer, viewContainer, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.NORTH, 0);
        else
            viewContainer.add(c, viewContainer, elements.getLast(), UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 0);
        
        elements.add(c);
        ((ComponentSetup) c).updateUISize();
        ((ComponentSetup) c).updateUIFont();
        ((ComponentSetup) c).updateUITheme();
        ((ComponentSetup) c).updateUIColors();
        
        updateOptionsSize();
    }
    
    public void addOption(String text) {
        JComponent c;
        if (mode == SelectionPanelModes.ARRANGE)
            c = new Label(LabelType.BODY, text);
        else {
            c = new ImageButton(text, false, buttonArragement);
            if (this.lightImageFile != null)
                ((ImageButton) c).setLightThemedImage(lightImageFile, imageWidth, imageHeight);
            else if (this.lightImageData != null)
                ((ImageButton) c).setLightThemedImage(lightImageData, isBundledLightImage, imageWidth, imageHeight);

            if (this.darkImageFile != null)
                ((ImageButton) c).setDarkThemedImage(darkImageFile, imageWidth, imageHeight);
            else if (this.darkImageData != null)
                ((ImageButton) c).setDarkThemedImage(darkImageData, isBundledDarkImage, imageWidth, imageHeight);
            
            if (this.hoverImageFile != null)
                ((ImageButton) c).setHoverImage(hoverImageFile);
            else if (this.hoverImageData != null)
                ((ImageButton) c).setHoverImage(hoverImageData, isBundledHoverImage);
        }
        
        c.setPreferredSize(new Dimension(width, 22));
        
        if (c instanceof ImageButton && mode == SelectionPanelModes.DISCARD) {
            int i = elements.size();
            
            ((ImageButton) c).setOnlyActionIfImageIsClicked(true);
            ((ImageButton) c).setRoundCorners(false);
            
            MouseAdapter l = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    removeOption(i);
                }
            };
            
            ((ImageButton) c).addMouseListener(l);
            discardActions.add(l);
        } else if (c instanceof ImageButton && mode == SelectionPanelModes.SELECTION) {
            int i = elements.size();
            
            ImageButton b = (ImageButton) c;
            b.setRoundCorners(false);
            
            b.addActionListener((Action) -> {
                unselectOption();
                selectedOption = b.getText_();
                highlightOption(i);
            });
        }
        
        if (elements.isEmpty())
            viewContainer.add(c, viewContainer, viewContainer, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.NORTH, 0);
        else
            viewContainer.add(c, viewContainer, elements.getLast(), UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 0);
        
        elements.add(c);
        ((ComponentSetup) c).updateUISize();
        ((ComponentSetup) c).updateUIFont();
        ((ComponentSetup) c).updateUITheme();
        ((ComponentSetup) c).updateUIColors();
        
        updateOptionsSize();
    }
}
