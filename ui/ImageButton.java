package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.SpringLayout;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import utils.LibUtilities;

/**
 * ColorButton with image
 * 
 * @author cristopher
 */
public class ImageButton extends ColorButton {
    private final SpringLayout layout = new SpringLayout();
    
    private final Label textLabel;
    private final Label imageLabel = new Label(LabelType.NONE);
    
    private BufferedImage lightThemedImage;
    private BufferedImage darkThemedImage;
    private BufferedImage hoverImage;
    private int imageWidth;
    private int imageHeight;
    
    private ImageButtonArrangement oldArrangement;
    private ImageButtonArrangement arrangement;
    
    private boolean onlyActionIfImageIsClicked = false;
    
    private boolean paintAsHovering;
    
    /**
     * Creates a new ImageButton without image
     * 
     * @see setImageFile()
     * @param arrangement 
     * @throws IllegalArgumentException if arrangement is not ONLY_IMAGE or ONLY_TINY_IMAGE
     */
    public ImageButton(ImageButtonArrangement arrangement) throws IllegalArgumentException {
        if (arrangement != ImageButtonArrangement.ONLY_IMAGE && arrangement != ImageButtonArrangement.ONLY_TINY_IMAGE)
            throw new IllegalArgumentException("Arrangement must be ONLY_IMAGE or ONLY_TINY_IMAGE");
        
        this.arrangement = arrangement;
        this.textLabel = null;
        
        initImageButton();
    }
    
    /**
     * Creates a new ImageButton with text and no image
     * 
     * @see setImageFile()
     * @param text
     * @param useBoldFont
     * @param arrangement
     * @throws IllegalArgumentException if ONLY_IMAGE or ONLY_TINY_IMAGE is used 
     * as arrangement
     */
    public ImageButton(String text, boolean useBoldFont, ImageButtonArrangement arrangement) throws IllegalArgumentException {
        if (arrangement == ImageButtonArrangement.ONLY_IMAGE || arrangement == ImageButtonArrangement.ONLY_TINY_IMAGE)
            throw new IllegalArgumentException("ONLY_IMAGE and ONLY_TINY_IMAGE doesn't support text");
        
        this.arrangement = arrangement;
        this.textLabel = new Label(useBoldFont ? LabelType.BOLD_BODY : LabelType.BODY, text) {
            @Override
            public void updateUITheme() { }
        };
        
        initImageButton();
    }
    
    private void initImageButton() {
        setLayout(layout);
        
        add(imageLabel);
        
        imageLabel.ifClickedDoClick(this);
        
        if (arrangement != ImageButtonArrangement.ONLY_IMAGE && arrangement != ImageButtonArrangement.ONLY_TINY_IMAGE)
            add(textLabel);
        
        addMouseListener(new HoverListener(false));
        imageLabel.addMouseListener(new HoverListener(true));
        
        updateUISize();
        updateUITheme();
    }

    @Override
    public void updateUISize() {
        if (arrangement == null)
            return;
        
        if (oldArrangement == null || oldArrangement != arrangement) {
            switch (arrangement) {
                case ONLY_IMAGE:
                    width = 50;
                    height = width;
                break;
                case ONLY_TINY_IMAGE:
                    width = 30;
                    height = width;
                break;
                case LEFT_TEXT_RIGHT_IMAGE:
                case RIGHT_TEXT_LEFT_IMAGE:
                case CENTER_TEXT_RIGHT_IMAGE:
                case CENTER_TEXT_LEFT_IMAGE:
                    width = 120;
                    height = 22;
                break;
                case UP_IMAGE:
                case DOWN_IMAGE:
                    width = 90;
                    height = 50;
                break;
                case UP_XL_IMAGE:
                case DOWN_XL_IMAGE:
                    width = 120;
                    height = 80;
                break;
                default:
                    throw new UnsupportedOperationException("Unsupported arrangement");
            }

            setPreferredSize(new Dimension(width, height));
            oldArrangement = arrangement;
        }
        
        if (arrangement == ImageButtonArrangement.CENTER_TEXT_RIGHT_IMAGE) {
            layout.putConstraint(SpringLayout.EAST, imageLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, textLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        if (arrangement == ImageButtonArrangement.CENTER_TEXT_LEFT_IMAGE) {
            layout.putConstraint(SpringLayout.WEST, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, textLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        
        
        if (arrangement == ImageButtonArrangement.LEFT_TEXT_RIGHT_IMAGE) {
            layout.putConstraint(SpringLayout.EAST, imageLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.WEST, textLabel, (int) (10 * UIProperties.uiScale), SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, textLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        if (arrangement == ImageButtonArrangement.RIGHT_TEXT_LEFT_IMAGE) {
            layout.putConstraint(SpringLayout.WEST, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.EAST, textLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, textLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        
        
        if (arrangement == ImageButtonArrangement.UP_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, -8, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.NORTH, textLabel, 0, SpringLayout.SOUTH, imageLabel);
        }
        
        if (arrangement == ImageButtonArrangement.UP_XL_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.NORTH, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.NORTH, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.SOUTH, textLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.SOUTH, this);
        }
        
        if (arrangement == ImageButtonArrangement.DOWN_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.SOUTH, textLabel, 0, SpringLayout.NORTH, imageLabel);
        }
        
        if (arrangement == ImageButtonArrangement.DOWN_XL_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.SOUTH, imageLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.SOUTH, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.NORTH, textLabel, (int) (10 * UIProperties.uiScale), SpringLayout.NORTH, this);
        }
        
        if (arrangement == ImageButtonArrangement.ONLY_IMAGE || arrangement == ImageButtonArrangement.ONLY_TINY_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        updateButton();
        
        if (textLabel != null)
            textLabel.updateUISize();
        
        super.updateUISize();
    }

    @Override
    public void updateUIFont() {
        if (textLabel != null)
            textLabel.updateUIFont();
    }

    @Override
    public void updateUITheme() {
        super.updateUITheme();
        updateButton();
    }
    
    @Override
    public void setText(String text) {
        textLabel.setText(text);
    }

    // Idk what the hell
//    @Override
//    public String getText() {
//        if (textLabel != null)
//            return textLabel.getText();
//        
//        return super.getText();
//    }

    /**
     * Use getText_() instead as this will return button's text instead of 
     * internal JLabel which holds actual text settled by setText() method
     * 
     * @return
     * @deprecated
     */
    @Override
    @Deprecated
    public String getText() {
        return super.getText();
    }
    
    
    /**
     * Returns button's text
     * 
     * @return a string
     */
    public String getText_() {
        return textLabel.getText();
    }
    
    private void updateButton() {
        if (textLabel != null)
            if (getModel().isRollover() || paintAsHovering)
                textLabel.setForeground(HFGColor);
            else
                textLabel.setForeground(FGColor);
        
        if (hoverImage != null) 
            if (getModel().isRollover() || paintAsHovering) {
                imageLabel.setIcon(LibUtilities.scaleImage(hoverImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
                return;
            }
        
        if (lightThemedImage != null)
            if (darkThemedImage != null)
                if (UIProperties.isLightThemeActive())
                    imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
                else
                    imageLabel.setIcon(LibUtilities.scaleImage(darkThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
            else
                imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
    }

    @Override
    public void setPaintAsHovering(boolean paintAsHovering) {
        this.paintAsHovering = paintAsHovering;
        updateButton();
    }

    
    
    /**
     * Set an image from a file, the image will be show if dark theme is active<br>
     * 
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageFile
     * @param width use -1 to render the image with its original width
     * @param height use -1 to render the image with its original height
     */
    public void setDarkThemedImage(File imageFile, int width, int height) {
        darkThemedImage = LibUtilities.readImage(imageFile);
        if (darkThemedImage == null)
            return;
        
        imageWidth = (width < 1) ? darkThemedImage.getWidth() : width;
        imageHeight = (height < 1) ? darkThemedImage.getHeight() : height;
        
        width = (int) (imageWidth * UIProperties.uiScale);
        height = (int) (imageHeight * UIProperties.uiScale);
        
        imageLabel.setIcon(LibUtilities.scaleImage(darkThemedImage, width, height));
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
        if (bundledImage)
            darkThemedImage = LibUtilities.readImage(imageData);
        else
            darkThemedImage = LibUtilities.loadBase64StringAsImage(imageData);
        
        if (darkThemedImage == null)
            return;
        
        imageWidth = (width < 1) ? darkThemedImage.getWidth() : width;
        imageHeight = (height < 1) ? darkThemedImage.getHeight() : height;
        
        width = (int) (imageWidth * UIProperties.uiScale);
        height = (int) (imageHeight * UIProperties.uiScale);
        
        imageLabel.setIcon(LibUtilities.scaleImage(darkThemedImage, width, height));
    }
    
    /**
     * Set an image from a file, the image will be show if light theme is active<br>
     * 
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageFile
     * @param width use -1 to render the image with its original width
     * @param height use -1 to render the image with its original height
     */
    public void setLightThemedImage(File imageFile, int width, int height) {
        lightThemedImage = LibUtilities.readImage(imageFile);
        if (lightThemedImage == null)
            return;
        
        imageWidth = (width < 1) ? lightThemedImage.getWidth() : width;
        imageHeight = (height < 1) ? lightThemedImage.getHeight() : height;
        
        width = (int) (imageWidth * UIProperties.uiScale);
        height = (int) (imageHeight * UIProperties.uiScale);
        
        imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, width, height));
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
        if (bundledImage)
            lightThemedImage = LibUtilities.readImage(imageData);
        else
            lightThemedImage = LibUtilities.loadBase64StringAsImage(imageData);
        
        if (lightThemedImage == null)
            return;
        
        imageWidth = (width < 1) ? lightThemedImage.getWidth() : width;
        imageHeight = (height < 1) ? lightThemedImage.getHeight() : height;
        
        width = (int) (imageWidth * UIProperties.uiScale);
        height = (int) (imageHeight * UIProperties.uiScale);
        
        imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, width, height));
    }
    
    /**
     * Set an hover image from a file<br>
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageFile
     */
    public void setHoverImage(File imageFile) {
        hoverImage = LibUtilities.readImage(imageFile);
    }
    
    /**
     * Set an hover image
     * 
     * @param imageData image path or base64 codified data
     * @param bundledImage set true if <code>imageData</code> is a package path
     */
    public void setHoverImage(String imageData, boolean bundledImage) {
        if (bundledImage)
            hoverImage = LibUtilities.readImage(imageData);
        else
            hoverImage = LibUtilities.loadBase64StringAsImage(imageData);
    }

    /**
     * If set true, this button only will be triggered if user clicks on the image<br>
     * Any ActionListener added before changing this won't be affected, <br>
     * in order to add actions use <code>addMouseListener</code> method
     * 
     * @param onlyActionIfImageIsClicked default is false
     */
    public void setOnlyActionIfImageIsClicked(boolean onlyActionIfImageIsClicked) {
        this.onlyActionIfImageIsClicked = onlyActionIfImageIsClicked;
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        if (onlyActionIfImageIsClicked) {
            imageLabel.addMouseListener(l);
            return;
        }
        
        super.addMouseListener(l);
    }

    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        if (onlyActionIfImageIsClicked) {
            imageLabel.removeMouseListener(l);
            return;
        }
        
        super.removeMouseListener(l);
    }
    
    private class HoverListener extends MouseAdapter {
        private final boolean paintAsHover;
        
        public HoverListener(boolean paintAsHover) {
            this.paintAsHover = paintAsHover;
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            if (paintAsHover)
                paintAsHovering = true;
            
            updateButton();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (paintAsHover)
                paintAsHovering = false;
            
            updateButton();
        }
    }
}
