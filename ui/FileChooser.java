package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import ui.enums.FileChooserModal;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import ui.enums.UIAlignment;
import ui.enums.UIFileDisposition;
import ui.filebrowser.FileViewer;
import ui.filebrowser.UIFile;
import utils.FileUtilities;
import utils.LibUtilities;

/**
 * Custom made file chooser
 * 
 * @author cristopher
 */
public class FileChooser extends Dialog {
    private final Label title = new Label(LabelType.BOLD_TITLE, "Files");
    private final ImageButton homeButton = new ImageButton("Home", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    private final ImageButton desktopButton = new ImageButton("Desktop", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    private final ImageButton documentsButton = new ImageButton("Documents", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    private final ImageButton downloadsButton = new ImageButton("Downloads", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    
    private final ImageButton rootButton = new ImageButton("System", false, ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
    
    private final ImageButton previewModeButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ContextMenu previewModesMenu = new ContextMenu(previewModeButton, false);
    
    private final ImageButton hideFilesToggleButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ImageButton sortButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ImageButton backButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ImageButton nextButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    
    private final ImageButton parentButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    private final ContextMenu parentMenu = new ContextMenu(parentButton, false);
    
    public final TextField directoryField = new TextField();
    
    private final FileViewer fileSelector = new FileViewer(this);
    
    private final ColorButton chooseButton = new ColorButton("Choose");
    private final ColorButton cancelButton = new ColorButton("Cancel");
    
    
    public FileChooser() {
        super(800, 440);
        
        setResizable(false);
        
        LibUtilities.addKeyBindingTo(container, "Hide", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileSelector.setSelection(null);
                hideWindow();
            }
        });
        
        
        homeButton.setPaint(false);
        homeButton.addActionListener((Action) -> {
            setDirectory(FileUtilities.USER_HOME);
        });
        desktopButton.setPaint(false);
        desktopButton.addActionListener((Action) -> {
            setDirectory(FileUtilities.USER_DESKTOP);
        });
        documentsButton.setPaint(false);
        documentsButton.addActionListener((Action) -> {
            setDirectory(FileUtilities.USER_DOCUMENTS);
        });
        downloadsButton.setPaint(false);
        downloadsButton.addActionListener((Action) -> {
            setDirectory(FileUtilities.USER_DOWNLOADS);
        });
        rootButton.setPaint(false);
        rootButton.addActionListener((Action) -> {
            setDirectory(FileUtilities.ROOT_DIRECTORY);
        });
        
        
        previewModesMenu.addOption("Icons", "ui/filebrowser/assets/lIcons.png", "ui/filebrowser/assets/dIcons.png", "ui/filebrowser/assets/dIcons.png", true, false, (Action) -> {
            fileSelector.setDisposition(UIFileDisposition.ICON_MODE);
            
            setButtonImage(previewModeButton, "Icons");
            previewModeButton.updateUITheme();
            previewModeButton.updateUIColors();
        });
        previewModesMenu.addOption("List", "ui/filebrowser/assets/lList.png", "ui/filebrowser/assets/dList.png", "ui/filebrowser/assets/dList.png", true, false, (Action) -> {
            fileSelector.setDisposition(UIFileDisposition.LIST_MODE);
            
            setButtonImage(previewModeButton, "List");
            previewModeButton.updateUITheme();
            previewModeButton.updateUIColors();
        });
        previewModeButton.addActionListener((Action) -> {
            previewModesMenu.show(0, previewModeButton.getPreferredSize().height);
        });
        
        hideFilesToggleButton.addActionListener((Action) -> {
            fileSelector.setVisibleHiddenFiles(!fileSelector.isVisibleHiddenFiles());
            
            if (fileSelector.isVisibleHiddenFiles())
                setButtonImage(hideFilesToggleButton, "Document1");
            else
                setButtonImage(hideFilesToggleButton, "Document");
        });
        
        parentButton.addActionListener((Action) -> {
            parentMenu.show(0, parentButton.getPreferredSize().height);
        });
        
        parentMenu.setElementsArrange(ImageButtonArrangement.LEFT_TEXT_LEFT_IMAGE);
        
        
        setButtonImage(homeButton, "Home");
        setButtonImage(desktopButton, "Desktop");
        setButtonImage(documentsButton, "Document");
        setButtonImage(downloadsButton, "Download");
        
        setButtonImage(rootButton, "Root");
        
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
                        setDirectory(f);
                    else
                        directoryField.setFontType(LabelType.WARNING_LABEL);
                }
            }
        });
        
        fileSelector.listFiles();
        
        chooseButton.addActionListener((Action) -> {
            fileSelector.endSelection();
        });
        cancelButton.addActionListener((Action) -> {
            fileSelector.setSelection(null);
            hideWindow();
        });
        
        
        
        add(title, UIAlignment.WEST, UIAlignment.WEST, 20, UIAlignment.NORTH, UIAlignment.NORTH, 20);
        add(homeButton, title, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(desktopButton, homeButton, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(documentsButton, desktopButton, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(downloadsButton, documentsButton, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        
        add(rootButton, downloadsButton, fileSelector, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.SOUTH, UIAlignment.SOUTH, 0);
        
        add(previewModeButton, fileSelector, directoryField, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(hideFilesToggleButton, previewModeButton, UIAlignment.WEST, UIAlignment.EAST, 5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(sortButton, hideFilesToggleButton, UIAlignment.WEST, UIAlignment.EAST, 5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        add(backButton, nextButton, UIAlignment.EAST, UIAlignment.WEST, -5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(nextButton, parentButton, UIAlignment.EAST, UIAlignment.WEST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(parentButton, directoryField, UIAlignment.EAST, UIAlignment.WEST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(directoryField, fileSelector, UIAlignment.EAST, UIAlignment.EAST, 0, UIAlignment.SOUTH, UIAlignment.NORTH, -10);
        
        add(fileSelector, chooseButton, UIAlignment.EAST, UIAlignment.EAST, 0, UIAlignment.SOUTH, UIAlignment.NORTH, -10);
        
        add(chooseButton, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.SOUTH, UIAlignment.SOUTH, -10);
        add(cancelButton, chooseButton, UIAlignment.EAST, UIAlignment.WEST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        setDirectory(FileUtilities.USER_HOME);
    }

    @Override
    public void updateUISize() {
        if (previewModesMenu != null) {
            previewModesMenu.updateUISize();
            parentMenu.updateUISize();
        }
        
        super.updateUISize();
    }

    @Override
    public void updateUIFont() {
        if (previewModesMenu != null) {
            previewModesMenu.updateUIFont();
            parentMenu.updateUIFont();
        }
        
        super.updateUIFont();
    }

    @Override
    public void updateUITheme() {
        if (previewModesMenu != null) {
            previewModesMenu.updateUITheme();
            parentMenu.updateUITheme();
        }
        
        super.updateUITheme();
    }

    @Override
    public void updateUIColors() {
        if (previewModesMenu != null) {
            previewModesMenu.updateUIColors();
            parentMenu.updateUIColors();
        }
        
        super.updateUIColors();
    }
    
    private void setButtonImage(ImageButton ib, String name) {
        ib.setLightThemedImage("ui/filebrowser/assets/l" + name + ".png", true, 15, 15);
        ib.setDarkThemedImage("ui/filebrowser/assets/d" + name + ".png", true, 15, 15);
        ib.setHoverImage("ui/filebrowser/assets/d" + name + ".png", true);
    }
    
    /**
     * Moves the current looking location to a given directory
     * 
     * @param directory 
     */
    public final void setDirectory(File directory) {
        fileSelector.setDirectory(directory);
        
        directoryField.setText(directory.getAbsolutePath());
        
        parentMenu.removeAllOptions();
        
        for (File f : FileUtilities.getFileParents(directory))
            parentMenu.addOption(f.getParentFile() == null ? f.getAbsolutePath() : f.getName(), UIFile.L_DIRECTORY, UIFile.D_DIRECTORY, UIFile.D_DIRECTORY, false, (Action) -> {
                directoryField.setText(f.getAbsolutePath());
                setDirectory(f.getAbsoluteFile());
            });
        
        parentMenu.updateUITheme();
        parentMenu.updateUIColors();
    }

    /**
     * This method does nothing, please use getFile, getFiles or getDirectory
     * @deprecated
     */
    @Override
    @Deprecated
    public void showWindow() { }
    
    /**
     * Creates a new <code>FilenameFilter</code>, this will overwrite any filter settled by 
     * <code>setFilenameFilter()</code><br>
     * This method uses <code>string.contains()</code> to check if <code>File.getName()</code>
     * is part of the strings array<br>
     * Note that this filter will not discriminate directories just files   
     * 
     * @param ignoreCase
     * @param strings the allowed extension
     */
    public void setAllowedFileNames(boolean ignoreCase, String ... strings) {
        FilenameFilter filter = (File dir, String name) -> {
            if (FileUtilities.joinPath(dir, name).isDirectory())
                return true;
            
            for (String str : strings)
                if (ignoreCase)
                    if (name.toLowerCase().contains(str.toLowerCase()))
                        return true;
                else
                    if (name.contains(str))
                        return true;
            
            return false;
        };
        
        fileSelector.setFilter(filter);
    }
    
    /**
     * Sets a custom made filter, this will overwrite any filter settled by 
     * <code>setAllowedFileNames()</code>
     * 
     * @param alwaysAcceptDirectories if true, it will create a new <code>FilenameFilter</code>,
     * this new filter will always allow directories to pass and will only 
     * discriminate files using the given filter
     * @param filter the filename filter
     */
    public void setFilenameFilter(boolean alwaysAcceptDirectories, FilenameFilter filter) {
        if (alwaysAcceptDirectories) {
            FilenameFilter dirFilter = (File dir, String name1) -> {
                if (FileUtilities.joinPath(dir, name1).isDirectory())
                    return true;
                
                return filter.accept(dir, name1);
            };
            
            fileSelector.setFilter(dirFilter);
            return;
        }
        
        fileSelector.setFilter(filter);
    }
    
    /**
     * Shows the file chooser and returns the selected file, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters
     * 
     * @param directory the location to show, set to null to start where it was left
     * @return a file or null if user canceled
     * @see ui.FileChooser#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FileChooser#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File getFile(File directory) {
        if (directory != null)
            setDirectory(directory);
        
        fileSelector.setMode(FileChooserModal.SINGLE_FILE);
        fileSelector.listFiles();
        chooseButton.setText("Choose file");
        
        super.showWindow();
        
        dispose();
        
        return fileSelector.getSelection();
    }
    
    /**
     * Shows the file chooser and returns the selected file, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters
     * 
     * @return a file or null if user canceled
     * @see ui.FileChooser#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FileChooser#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File getFile() {
        return getFile(null);
    }
    
    /**
     * Shows the file chooser and returns the selected directory, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters
     * 
     * @param directory the location to show, set to null to start where it was left
     * @return a directory or null if user canceled
     * @see ui.FileChooser#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FileChooser#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File getDirectory(File directory) {
        if (directory != null)
            setDirectory(directory);
        
        fileSelector.setMode(FileChooserModal.SINGLE_DIRECTORY);
        fileSelector.listFiles();
        chooseButton.setText("Choose directory");
        
        super.showWindow();
        
        dispose();
        
        return fileSelector.getSelection();
    }
    
    /**
     * Shows the file chooser and returns the selected directory, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters
     * 
     * @return a directory or null if user canceled
     * @see ui.FileChooser#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FileChooser#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File getDirectory() {
        return getDirectory(null);
    }
}
