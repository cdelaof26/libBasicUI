package ui.filebrowser;

import ui.ImageButton;
import ui.Label;
import ui.Panel;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import ui.enums.UIAlignment;
import utils.FileUtilities;

/**
 * Panel containing several button to move around "frequent" places, such as
 * Home, Desktop, Document among others.
 * 
 * @author cristopher
 */
public class FileViewerSidebar extends Panel {
    private final Label title = new Label(LabelType.BOLD_TITLE, "Files");
    private final ImageButton homeButton = new ImageButton("Home", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    private final ImageButton desktopButton = new ImageButton("Desktop", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    private final ImageButton documentsButton = new ImageButton("Documents", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    private final ImageButton downloadsButton = new ImageButton("Downloads", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    
    private final ImageButton rootButton = new ImageButton("System", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    
    /**
     * Creates a new FileViewerSidebar
     * @param container an object that implements {@link FileBrowser}
     */
    public FileViewerSidebar(FileBrowser container) {
        super(160, 400);
        
        homeButton.setPaint(false);
        homeButton.addActionListener((Action) -> {
            container.setDirectory(FileUtilities.USER_HOME);
        });
        desktopButton.setPaint(false);
        desktopButton.addActionListener((Action) -> {
            container.setDirectory(FileUtilities.USER_DESKTOP);
        });
        documentsButton.setPaint(false);
        documentsButton.addActionListener((Action) -> {
            container.setDirectory(FileUtilities.USER_DOCUMENTS);
        });
        downloadsButton.setPaint(false);
        downloadsButton.addActionListener((Action) -> {
            container.setDirectory(FileUtilities.USER_DOWNLOADS);
        });
        rootButton.setPaint(false);
        rootButton.addActionListener((Action) -> {
            container.setDirectory(FileUtilities.ROOT_DIRECTORY);
        });
        
        setButtonImage(homeButton, "Home");
        setButtonImage(desktopButton, "Desktop");
        setButtonImage(documentsButton, "Document");
        setButtonImage(downloadsButton, "Download");
        
        setButtonImage(rootButton, "Root");
        
        installComponents();
    }

    private void installComponents() {
        add(title, UIAlignment.WEST, UIAlignment.WEST, 20, UIAlignment.NORTH, UIAlignment.NORTH, 20);
        add(homeButton, title, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(desktopButton, homeButton, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(documentsButton, desktopButton, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(downloadsButton, documentsButton, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(rootButton, downloadsButton, this, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.SOUTH, UIAlignment.SOUTH, 0);
    }
    
    private void setButtonImage(ImageButton ib, String name) {
        ib.setLightThemedImage("ui/filebrowser/assets/l" + name + ".png", true, 15, 15);
        ib.setDarkThemedImage("ui/filebrowser/assets/d" + name + ".png", true, 15, 15);
        ib.setHoverImage("ui/filebrowser/assets/d" + name + ".png", true);
    }
}
