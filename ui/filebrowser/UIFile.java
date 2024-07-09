package ui.filebrowser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import ui.ImageButton;
import ui.enums.ImageButtonArrangement;
import ui.enums.UIFileDisposition;
import utils.FileUtilities;
import utils.LibUtilities;

/**
 * Creates a button with the name of a file with an icon
 * 
 * @author cristopher
 */
public class UIFile extends ImageButton {
    public final static BufferedImage L_DOCUMENT = LibUtilities.readImage("ui/filebrowser/assets/lDocument.png");
    public final static BufferedImage D_DOCUMENT = LibUtilities.readImage("ui/filebrowser/assets/dDocument.png");
    
    public final static BufferedImage L_DOCUMENT_1 = LibUtilities.readImage("ui/filebrowser/assets/lDocument1.png");
    public final static BufferedImage D_DOCUMENT_1 = LibUtilities.readImage("ui/filebrowser/assets/dDocument1.png");
    
    public final static BufferedImage L_DIRECTORY = LibUtilities.readImage("ui/filebrowser/assets/lDirectory.png");
    public final static BufferedImage D_DIRECTORY = LibUtilities.readImage("ui/filebrowser/assets/dDirectory.png");
    
    private final File file;
    private int clicks = 0;
    
    /**
     * Creates a new UIFile
     * 
     * @param id the index of the element
     * @param f the file which points at
     * @param disposition the UIFileDisposition
     * @param container the container
     */
    public UIFile(int id, File f, UIFileDisposition disposition, FileViewer container) {
        super(f.getName(), false, 
                disposition == UIFileDisposition.COLUMNS_MODE || disposition == UIFileDisposition.LIST_MODE ? 
                        ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE : ImageButtonArrangement.UP_IMAGE
        );
        
        this.file = f;
        
        int imageType = this.file.isFile() && !this.file.isHidden() ? 0 : this.file.isFile() ? 1 : 2;
        
        initUIFile(id, imageType, disposition, container);
    }
    
    /**
     * Creates a new UIFile given a image id<br>
     * 0: File not hidden<br>
     * 1: Hidden file<br>
     * 2: Directory
     * 
     * @param id the index of the element
     * @param f the file which points at
     * @param imageType the image to use
     * @param disposition the UIFileDisposition
     * @param container the container
     * @throws IllegalArgumentException if imageType is not in the range [0, 2]
     */
    public UIFile(int id, File f, int imageType, UIFileDisposition disposition, FileViewer container) {
        super(f.getName(), false, 
                disposition == UIFileDisposition.COLUMNS_MODE || disposition == UIFileDisposition.LIST_MODE ? 
                        ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE : ImageButtonArrangement.UP_IMAGE
        );
        
        if (imageType < 0 || imageType > 2)
            throw new IllegalArgumentException("Invalid imageType = " + imageType);
        
        this.file = f;
        
        initUIFile(id, imageType, disposition, container);
    }
    
    private void initUIFile(int id, int imageType, UIFileDisposition disposition, FileViewer container) {
        boolean bigImage = disposition == UIFileDisposition.ICON_MODE;
        if (!bigImage)
            setRoundCorners(false);
        
        int size = bigImage ? 25 : 15;
        
        BufferedImage lightThemedImage = imageType == 0 ? L_DOCUMENT : imageType == 1 ? L_DOCUMENT_1 : L_DIRECTORY;
        BufferedImage darkThemedImage = imageType == 0 ? D_DOCUMENT : imageType == 1 ? D_DOCUMENT_1 : D_DIRECTORY;
        
        setLightThemedImage(lightThemedImage, size, size);
        setDarkThemedImage(darkThemedImage, size, size);
        setHoverImage(darkThemedImage);
        
        addActionListener((Action) -> {
            clicks++;
            if (clicks == 1) {
                setPaintAsHovering(true);
                container.setSelection(file);
                container.unselectFiles(id);
                return;
            }
            
            clicks = 0;
            container.openSelection();
        });
    }

    public File getFile() {
        return file;
    }
    
    public File [] listFiles(FilenameFilter filter) {
        return FileUtilities.listFiles(file, filter);
    }
    
    public void setDisposition(UIFileDisposition disposition) {
        boolean bigImage = disposition == UIFileDisposition.ICON_MODE;
        setRoundCorners(bigImage);
        
        int size = bigImage ? 25 : 15;
        
        setImageDimension(size, size);
        
        setArrangement(
                disposition == UIFileDisposition.COLUMNS_MODE || disposition == UIFileDisposition.LIST_MODE ? 
                        ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE : ImageButtonArrangement.UP_IMAGE
        );
        
        revalidate();
        repaint();
    }
    
    public void resetClicks() {
        clicks = 0;
        setPaintAsHovering(false);
    }
}
