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
    
    public UIFile(int id, File f, UIFileDisposition disposition, FileViewer container) {
        super(f.getName(), false, 
                disposition == UIFileDisposition.COLUMNS_MODE || disposition == UIFileDisposition.LIST_MODE ? 
                        ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE : ImageButtonArrangement.UP_IMAGE
        );
        
        this.file = f;
        
        boolean bigImage = disposition == UIFileDisposition.ICON_MODE;
        if (!bigImage)
            setRoundCorners(false);
        
        int size = bigImage ? 25 : 15;
        
        int imageType = this.file.isFile() && !this.file.isHidden() ? 0 : this.file.isFile() ? 1 : 2;
        
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
