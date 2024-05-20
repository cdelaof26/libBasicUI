package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.SpringLayout;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import ui.enums.TextAlignment;
import utils.LibUtilities;

/**
 * ColorButton with image
 * 
 * @author cristopher
 */
public class ImageButton extends ColorButton {
    private final Label imageLabel = new Label(LabelType.NONE);
    
    private BufferedImage lightThemedImage;
    private BufferedImage darkThemedImage;
    private BufferedImage hoverImage;
    private int imageWidth;
    private int imageHeight;
    
    private ImageButtonArrangement oldArrangement;
    private ImageButtonArrangement arrangement;
    
    private boolean onlyActionIfImageIsClicked = false;
    
    private boolean updatingColors = false;
    
    /**
     * Creates a new ImageButton without image
     * 
     * @param arrangement the way to arrange the icon and text label
     * @throws IllegalArgumentException if arrangement is not ONLY_IMAGE or ONLY_TINY_IMAGE
     * @see ui.ImageButton#setLightThemedImage(java.awt.image.BufferedImage, int, int) 
     * @see ui.ImageButton#setLightThemedImage(java.io.File, int, int)
     * @see ui.ImageButton#setLightThemedImage(java.lang.String, boolean, int, int) 
     * @see ui.ImageButton#setDarkThemedImage(java.awt.image.BufferedImage, int, int) 
     * @see ui.ImageButton#setDarkThemedImage(java.io.File, int, int)
     * @see ui.ImageButton#setDarkThemedImage(java.lang.String, boolean, int, int) 
     * @see ui.ImageButton#setHoverImage(java.awt.image.BufferedImage) 
     * @see ui.ImageButton#setHoverImage(java.io.File) 
     * @see ui.ImageButton#setHoverImage(java.lang.String, boolean)
     */
    public ImageButton(ImageButtonArrangement arrangement) throws IllegalArgumentException {
        if (arrangement != ImageButtonArrangement.ONLY_IMAGE && arrangement != ImageButtonArrangement.ONLY_TINY_IMAGE)
            throw new IllegalArgumentException("Arrangement must be ONLY_IMAGE or ONLY_TINY_IMAGE");
        
        this.arrangement = arrangement;
        
        initImageButton();
    }
    
    /**
     * Creates a new ImageButton with text and no image
     * 
     * @param text the text of the button 
     * @param useBoldFont if true, the text will be {@link ui.enums.LabelType#BOLD_BODY}, 
     * otherwise {@link ui.enums.LabelType#BODY}
     * @param arrangement the way to arrange the icon and text label
     * @throws IllegalArgumentException if ONLY_IMAGE or ONLY_TINY_IMAGE is used 
     * as arrangement
     * @see ui.ImageButton#setLightThemedImage(java.awt.image.BufferedImage, int, int) 
     * @see ui.ImageButton#setLightThemedImage(java.io.File, int, int)
     * @see ui.ImageButton#setLightThemedImage(java.lang.String, boolean, int, int) 
     * @see ui.ImageButton#setDarkThemedImage(java.awt.image.BufferedImage, int, int) 
     * @see ui.ImageButton#setDarkThemedImage(java.io.File, int, int)
     * @see ui.ImageButton#setDarkThemedImage(java.lang.String, boolean, int, int) 
     * @see ui.ImageButton#setHoverImage(java.awt.image.BufferedImage) 
     * @see ui.ImageButton#setHoverImage(java.io.File) 
     * @see ui.ImageButton#setHoverImage(java.lang.String, boolean)
     */
    public ImageButton(String text, boolean useBoldFont, ImageButtonArrangement arrangement) throws IllegalArgumentException {
        super(text);
        
        if (arrangement == ImageButtonArrangement.ONLY_IMAGE || arrangement == ImageButtonArrangement.ONLY_TINY_IMAGE)
            throw new IllegalArgumentException("ONLY_IMAGE and ONLY_TINY_IMAGE don't support text");
        
        this.arrangement = arrangement;
        
        initImageButton();
    }
    
    private void initImageButton() {
        add(imageLabel);
        
        imageLabel.ifClickedDoClick(this);
        
        addMouseListener(new HoverListener(false));
        imageLabel.addMouseListener(new HoverListener(true));
        
        addActionListener((Action) -> {
            updateButton();
        });
        
        if (arrangement.name().startsWith("LEFT"))
            label.setTextAlignment(TextAlignment.LEFT);
        else if (arrangement.name().startsWith("RIGHT"))
            label.setTextAlignment(TextAlignment.RIGHT);
        
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
                case ONE_WORD_ICON_BUTTON:
                    width = 50;
                    height = width;
                break;
                case LEFT_TEXT_LEFT_IMAGE:
                case RIGHT_TEXT_RIGHT_IMAGE:
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
            }

            layout.removeLayoutComponent(imageLabel);
            layout.removeLayoutComponent(label);
            
            oldArrangement = arrangement;
        }
        
        if (arrangement == ImageButtonArrangement.ONLY_IMAGE || arrangement == ImageButtonArrangement.ONLY_TINY_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        if (arrangement == ImageButtonArrangement.ONE_WORD_ICON_BUTTON) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, (int) (-6 * UIProperties.uiScale), SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.SOUTH, label, (int) (-3 * UIProperties.uiScale), SpringLayout.SOUTH, this);
        }
        
        
        
        
        if (arrangement == ImageButtonArrangement.CENTER_TEXT_LEFT_IMAGE || arrangement == ImageButtonArrangement.F_CENTER_TEXT_LEFT_IMAGE) {
            layout.putConstraint(SpringLayout.WEST, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        if (arrangement == ImageButtonArrangement.CENTER_TEXT_RIGHT_IMAGE || arrangement == ImageButtonArrangement.F_CENTER_TEXT_RIGHT_IMAGE) {
            layout.putConstraint(SpringLayout.EAST, imageLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        if (arrangement == ImageButtonArrangement.RIGHT_TEXT_LEFT_IMAGE || arrangement == ImageButtonArrangement.F_RIGHT_TEXT_LEFT_IMAGE) {
            layout.putConstraint(SpringLayout.WEST, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.EAST, label, (int) (-10 * UIProperties.uiScale), SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        if (arrangement == ImageButtonArrangement.LEFT_TEXT_RIGHT_IMAGE || arrangement == ImageButtonArrangement.F_LEFT_TEXT_RIGHT_IMAGE) {
            layout.putConstraint(SpringLayout.EAST, imageLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.WEST, label, (int) (10 * UIProperties.uiScale), SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        if (arrangement == ImageButtonArrangement.RIGHT_TEXT_RIGHT_IMAGE || arrangement == ImageButtonArrangement.F_RIGHT_TEXT_RIGHT_IMAGE) {
            layout.putConstraint(SpringLayout.EAST, imageLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.EAST, label, (int) (-10 * UIProperties.uiScale), SpringLayout.WEST, imageLabel);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        if (arrangement == ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE || arrangement == ImageButtonArrangement.F_LEFT_TEXT_LEFT_IMAGE) {
            layout.putConstraint(SpringLayout.WEST, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, 0, SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.WEST, label, (int) (10 * UIProperties.uiScale), SpringLayout.EAST, imageLabel);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, this);
        }
        
        
        
        
        if (arrangement == ImageButtonArrangement.UP_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, (int) (-8 * UIProperties.uiScale), SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.SOUTH, imageLabel);
        }
        
        if (arrangement == ImageButtonArrangement.DOWN_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.VERTICAL_CENTER, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.NORTH, imageLabel);
        }
        
        if (arrangement == ImageButtonArrangement.UP_XL_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.NORTH, imageLabel, (int) (10 * UIProperties.uiScale), SpringLayout.NORTH, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.SOUTH, label, (int) (-10 * UIProperties.uiScale), SpringLayout.SOUTH, this);
        }
        
        if (arrangement == ImageButtonArrangement.DOWN_XL_IMAGE) {
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, imageLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.SOUTH, imageLabel, (int) (-10 * UIProperties.uiScale), SpringLayout.SOUTH, this);
            
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
            layout.putConstraint(SpringLayout.NORTH, label, (int) (10 * UIProperties.uiScale), SpringLayout.NORTH, this);
        }
        
        updateButton();
        
        super.updateUISize();
    }
    
    @Override
    public void updateUITheme() {
        updatingColors = true;
        
        super.updateUITheme();
        updateButton();
    }
    
    @Override
    public void updateUIColors() {
        updatingColors = true;
        
        super.updateUIColors();
        updateButton();
    }
    
    /**
     * Returns button's text
     * 
     * @return a string
     * @deprecated please, use {@link ui.ColorButton#getText()} method
     */
    @Deprecated
    public String getText_() {
        return super.getText();
    }
    
    private void updateButton() {
        if (!paint) {
            label.setForeground(FGColor);
            
            if (lightThemedImage != null)
                if (darkThemedImage != null)
                    if (UIProperties.isLightThemeActive())
                        imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
                    else
                        imageLabel.setIcon(LibUtilities.scaleImage(darkThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
                else
                    imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
            
            return;
        }
        
        // TODO
        // Fix colors when theme is changed if update is through a mouse listener
        
        if (updatingColors) {
            label.setForeground(FGColor);
            
            if (lightThemedImage != null)
                if (darkThemedImage != null)
                    if (UIProperties.isLightThemeActive())
                        imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
                    else
                        imageLabel.setIcon(LibUtilities.scaleImage(darkThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
                else
                    imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, (int) (imageWidth * UIProperties.uiScale), (int) (imageHeight * UIProperties.uiScale)));
                
            
            repaint();
            updatingColors = false;
            
            return;
        }
        
        if (!isEnabled())
            return;
        
        
        if (getModel().isRollover() || paintAsHovering)
            label.setForeground(HFGColor);
        else
            label.setForeground(FGColor);
        
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
        super.setPaintAsHovering(paintAsHovering);
        updateButton();
    }

    /**
     * Sets a new arrangement for this button
     * 
     * @param arrangement the new arrangement
     */
    public void setArrangement(ImageButtonArrangement arrangement) {
        this.arrangement = arrangement;
        updateUISize();
    }
    
    /**
     * Sets an image from a file, the image will be show if dark theme is active<br>
     * 
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageFile the file
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
     * Sets an image from a base64 String or a package path, the image will be 
     * show if dark theme is active
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
     * Sets an image from a BufferedImage, the image will be show if dark theme 
     * is active
     * 
     * @param image the image
     * @param width use -1 to render the image with its original width
     * @param height use -1 to render the image with its original height
     */
    public void setDarkThemedImage(BufferedImage image, int width, int height) {
        darkThemedImage = image;
        if (darkThemedImage == null)
            return;
        
        imageWidth = (width < 1) ? darkThemedImage.getWidth() : width;
        imageHeight = (height < 1) ? darkThemedImage.getHeight() : height;
        
        width = (int) (imageWidth * UIProperties.uiScale);
        height = (int) (imageHeight * UIProperties.uiScale);
        
        imageLabel.setIcon(LibUtilities.scaleImage(darkThemedImage, width, height));
    }
    
    /**
     * Sets an image from a file, the image will be show if light theme is active<br>
     * 
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageFile the file
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
     * Sets an image from a base64 String or a package path, the image will be 
     * show if light theme is active
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
     * Sets an image from a BufferedImage, the image will be show if light theme 
     * is active
     * 
     * @param image the image
     * @param width use -1 to render the image with its original width
     * @param height use -1 to render the image with its original height
     */
    public void setLightThemedImage(BufferedImage image, int width, int height) {
        lightThemedImage = image;
        if (lightThemedImage == null)
            return;
        
        imageWidth = (width < 1) ? lightThemedImage.getWidth() : width;
        imageHeight = (height < 1) ? lightThemedImage.getHeight() : height;
        
        width = (int) (imageWidth * UIProperties.uiScale);
        height = (int) (imageHeight * UIProperties.uiScale);
        
        imageLabel.setIcon(LibUtilities.scaleImage(lightThemedImage, width, height));
    }
    
    /**
     * Sets the hover image from a file<br>
     * Note that this method only supports .png, .jpeg and .jpg file types
     * 
     * @param imageFile the file
     */
    public void setHoverImage(File imageFile) {
        hoverImage = LibUtilities.readImage(imageFile);
    }
    
    /**
     * Sets the hover image
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
     * Sets the hover image from a BufferedImage
     * 
     * @param image the image
     */
    public void setHoverImage(BufferedImage image) {
        hoverImage = image;
    }

    /**
     * Changes the dimension for all added images
     * 
     * @param width the width
     * @param height the height
     */
    public void setImageDimension(int width, int height) {
        if (width < 0 || height < 0)
            return;
        
        imageWidth = width;
        imageHeight = height;
        
        updateButton();
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
            if (updatingColors)
                return;
            
            if (paintAsHover)
                paintAsHovering = true;
            
            updateButton();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (updatingColors)
                return;
            
            if (paintAsHover)
                paintAsHovering = false;
            
            updateButton();
        }
    }
}
