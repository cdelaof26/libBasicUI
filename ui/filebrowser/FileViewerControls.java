package ui.filebrowser;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import ui.ContextMenu;
import ui.ImageButton;
import ui.Panel;
import ui.TextField;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import ui.enums.UIAlignment;
import ui.enums.UIFileDisposition;
import utils.FileUtilities;
import utils.LibUtilities;

/**
 *
 * @author cristopher
 */
public class FileViewerControls extends Panel {
    private final ImageButton previewModeButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ContextMenu previewModesMenu = new ContextMenu(previewModeButton, false);
    
    private final ImageButton hideFilesToggleButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ImageButton sortButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ImageButton backButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ImageButton nextButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    
    private final ImageButton parentButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    public final ContextMenu parentMenu = new ContextMenu(parentButton, false);
    
    public final TextField directoryField = new TextField();

    private final FileBrowser container;
    
    public FileViewerControls(FileBrowser container) {
        super(630, 40);
        this.container = container;
        
        previewModesMenu.addOption("Icons", "ui/filebrowser/assets/lIcons.png", "ui/filebrowser/assets/dIcons.png", "ui/filebrowser/assets/dIcons.png", true, false, (Action) -> {
            container.setDisposition(UIFileDisposition.ICON_MODE);
            
            setButtonImage(previewModeButton, "Icons");
            previewModeButton.updateUITheme();
            previewModeButton.updateUIColors();
        });
        previewModesMenu.addOption("List", "ui/filebrowser/assets/lList.png", "ui/filebrowser/assets/dList.png", "ui/filebrowser/assets/dList.png", true, false, (Action) -> {
            container.setDisposition(UIFileDisposition.LIST_MODE);
            
            setButtonImage(previewModeButton, "List");
            previewModeButton.updateUITheme();
            previewModeButton.updateUIColors();
        });
        previewModeButton.addActionListener((Action) -> {
            previewModesMenu.show(0, previewModeButton.getPreferredSize().height);
        });
        
        hideFilesToggleButton.addActionListener((Action) -> {
            container.setVisibleHiddenFiles(!container.isVisibleHiddenFiles());
            
            if (container.isVisibleHiddenFiles())
                setButtonImage(hideFilesToggleButton, "Document1");
            else
                setButtonImage(hideFilesToggleButton, "Document");
        });
        
        parentButton.addActionListener((Action) -> {
            parentMenu.show(0, parentButton.getPreferredSize().height);
        });
        
        parentMenu.setElementsArrange(ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
        
        setButtonImage(previewModeButton, "List");
        setButtonImage(hideFilesToggleButton, "Document");
        setButtonImage(sortButton, "Sort");
        
        setButtonImage(backButton, "Back");
        setButtonImage(nextButton, "Next");
        setButtonImage(parentButton, "Up");
        
        
        directoryField.setText(LibUtilities.USER_HOME, true);
        directoryField.setPreferredSize(new Dimension(360, 22));
        directoryField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                directoryField.setFontType(LabelType.BODY);
                
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    File f = new File(directoryField.getText());
                    if (f.isDirectory())
                        container.setDirectory(f);
                    else
                        directoryField.setFontType(LabelType.WARNING_LABEL);
                }
            }
        });
        
        externalComponents.add(previewModesMenu);
        externalComponents.add(parentMenu);
        
        installComponents();
    }
    
    private void installComponents() {
        add(previewModeButton, this, directoryField, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(hideFilesToggleButton, previewModeButton, UIAlignment.WEST, UIAlignment.EAST, 5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(sortButton, hideFilesToggleButton, UIAlignment.WEST, UIAlignment.EAST, 5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        add(backButton, nextButton, UIAlignment.EAST, UIAlignment.WEST, -5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(nextButton, parentButton, UIAlignment.EAST, UIAlignment.WEST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(parentButton, directoryField, UIAlignment.EAST, UIAlignment.WEST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(directoryField, this, UIAlignment.EAST, UIAlignment.EAST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
    }
    
    private void setButtonImage(ImageButton ib, String name) {
        ib.setLightThemedImage("ui/filebrowser/assets/l" + name + ".png", true, 15, 15);
        ib.setDarkThemedImage("ui/filebrowser/assets/d" + name + ".png", true, 15, 15);
        ib.setHoverImage("ui/filebrowser/assets/d" + name + ".png", true);
    }

    /**
     * Sets the path to be displayed in the directory field
     * @param path the path
     */
    public void setDirectory(String path) {
        directoryField.setText(path);
    }
    
    /**
     * Changes all inputs in the parent button (up arrow icon) given a new path
     * @param path the path
     */
    public void refreshParentList(File path) {
        parentMenu.removeAllOptions();
        
        for (File f : FileUtilities.getFileParents(path))
            parentMenu.addOption(f.getParentFile() == null ? f.getAbsolutePath() : f.getName(), UIFile.L_DIRECTORY, UIFile.D_DIRECTORY, UIFile.D_DIRECTORY, false, (Action) -> {
                directoryField.setText(f.getAbsolutePath());
                container.setDirectory(f.getAbsoluteFile());
            });
    }
}
