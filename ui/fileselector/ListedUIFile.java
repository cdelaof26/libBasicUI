package ui.fileselector;

import java.awt.image.BufferedImage;
import java.io.File;
import ui.ImageButton;
import ui.enums.ImageButtonArrangement;
import ui.enums.TextAlignment;
import utils.LibUtilities;

/**
 * Creates a button with the name of a file as a list element
 * 
 * @author cristopher
 */
public class ListedUIFile extends ImageButton {
    public final static BufferedImage L_DELETE = LibUtilities.readImage("ui/filebrowser/assets/lDelete.png");
    public final static BufferedImage D_DELETE = LibUtilities.readImage("ui/filebrowser/assets/dDelete.png");
    
    protected final File f;
    
    /**
     * Creates a ImageButton with <code>Delete</code> icon and the name of the 
     * file
     * @param f the file
     */
    public ListedUIFile(File f) {
        super(f.getName(), false, ImageButtonArrangement.F_LEFT_TEXT_LEFT_IMAGE);
        this.f = f;
        
        label.setTextAlignment(TextAlignment.LEFT);
        setOnlyActionIfImageIsClicked(true);
        setLightThemedImage(L_DELETE, 18, 18);
        setDarkThemedImage(D_DELETE, 18, 18);
        setHoverImage(D_DELETE);
        setBorderPainted(false);
        setRoundCorners(false);
        setUseOnlyAppColor(true);
    }
}
