package ui;

import utils.FilePicker;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import ui.enums.FileChooserModal;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import ui.enums.UIAlignment;
import ui.fileselector.FileDropArea;

/**
 * Custom made file selector which works by dragging and dropping files
 * 
 * @author cristopher
 */
public class FileSelector extends Panel implements FilePicker {
    private final Label title = new Label(LabelType.SUBTITLE, "Drop your file");
    
    private final ColorButton submitButton = new ColorButton("Cancel");
    private final ImageButton removeAllButton = new ImageButton(ImageButtonArrangement.ONLY_TINY_IMAGE);
    
    private FileDropArea fileDropArea;
    private final Dialog dialog;
    
    private final boolean showAsDialog;
    
    /**
     * Creates a new FileSelector given a width and a height
     * 
     * @param width the width
     * @param height the height
     * @param showAsDialog if true, this component will be shown in a dialog
     * when calling <code>getFile()</code> or others. <b>If showAsDialog is true,
     * this panel will be empty.</b>
     * @see FileSelector#getFile() 
     * @see FileSelector#getFiles() 
     * @see FileSelector#getDirectory() 
     * @see FileSelector#getDirectories() 
     */
    public FileSelector(int width, int height, boolean showAsDialog) {
        super(width, height);
        this.showAsDialog = showAsDialog;
        
        if (width < 200 || height < 200)
            throw new IllegalArgumentException("The minimum size is 200 for both width and height");
        
        dialog = new Dialog(width, height);
        dialog.setResizable(false);
        
//        LibUtilities.addKeyBindingTo(dialog.container, "Hide", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dialog.hideWindow();
//            }
//        });
        
        fileDropArea = new FileDropArea(width - 20, height - 10 * 4 - 30 - title.getPreferredSize().height);
        fileDropArea.setDropperCallback((Action) -> {
            boolean isSomethingSelected = fileDropArea.isThereASelection();
            
            submitButton.setUseOnlyAppColor(isSomethingSelected);
            submitButton.setText(isSomethingSelected ? "Submit" : "Cancel");
        });
        
        submitButton.setPreferredSize(new Dimension(width - 30 - 30, 30));
        if (showAsDialog)
            submitButton.addActionListener((Action) -> {
                dialog.hideWindow();
            });
        
        removeAllButton.setLightThemedImage("ui/filebrowser/assets/lTrash.png", true, 20, 20);
        removeAllButton.setDarkThemedImage("ui/filebrowser/assets/dTrash.png", true, 20, 20);
        removeAllButton.setHoverImage("ui/filebrowser/assets/dTrash.png", true);
        
        removeAllButton.addActionListener((Action) -> {
            fileDropArea.removeAllItems();
        });
        
        if (showAsDialog) {
            dialog.container.add(title, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.NORTH, UIAlignment.NORTH, 10);
            dialog.container.add(fileDropArea, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);

            dialog.container.add(submitButton, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.SOUTH, UIAlignment.SOUTH, -10);
            dialog.container.add(removeAllButton, fileDropArea, submitButton, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        } else {
            add(title, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.NORTH, UIAlignment.NORTH, 10);
            add(fileDropArea, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);

            add(submitButton, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.SOUTH, UIAlignment.SOUTH, -10);
            add(removeAllButton, fileDropArea, submitButton, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        }
    }
    
    private Object getSelection(String titleText, FileChooserModal mode) {
        boolean multipleSelectionAllowed = mode.toString().contains("MULTIPLE");
        
        title.setText(titleText);
        fileDropArea.setMode(mode);
        removeAllButton.setVisible(multipleSelectionAllowed);
        submitButton.setPreferredSize(new Dimension(width + (multipleSelectionAllowed ? -60 : -20), 30));
        
        if (showAsDialog)
            dialog.showWindow();
        
        File [] selection = fileDropArea.getSelection();
        if (!multipleSelectionAllowed)
            return selection == null ? null : selection[0];
        
        return selection;
    }
    
    @Override
    public File getFile() {
        return (File) getSelection("Drop your file", FileChooserModal.SINGLE_FILE);
    }

    @Override
    public File[] getFiles() {
        return (File[]) getSelection("Drop your files", FileChooserModal.MULTIPLE_FILES);
    }

    @Override
    public File getDirectory() {
        return (File) getSelection("Drop your directory", FileChooserModal.SINGLE_DIRECTORY);
    }

    @Override
    public File[] getDirectories() {
        return (File[]) getSelection("Drop your directories", FileChooserModal.MULTIPLE_DIRECTORIES);
    }
    
    /**
     * Adds an ActionListener to the submit button
     * @param l the ActionListener
     */
    public void addSubmitActionListener(ActionListener l) {
        submitButton.addActionListener(l);
    }

    @Override
    public File getFile(File directory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File[] getFiles(File directory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File getDirectory(File directory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File[] getDirectories(File directory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAllowedFileNames(boolean ignoreCase, String... strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFilenameFilter(boolean alwaysAcceptDirectories, FilenameFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
