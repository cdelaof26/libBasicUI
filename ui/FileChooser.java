package ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import ui.enums.FileChooserModal;
import ui.enums.UIAlignment;
import ui.enums.UIFileDisposition;
import ui.filebrowser.FileBrowser;
import ui.filebrowser.FileViewer;
import ui.filebrowser.FileViewerControls;
import ui.filebrowser.FileViewerSidebar;
import utils.FileUtilities;
import utils.LibUtilities;

/**
 * Custom made file chooser
 * 
 * @author cristopher
 */
public class FileChooser extends Dialog implements FileBrowser {
    private final FileViewerSidebar fileViewerSidebar = new FileViewerSidebar(this);
    
    private final FileViewerControls fileViewerControls = new FileViewerControls(this);
    
    private final FileViewer fileSelector = new FileViewer(this);
    
    private final ColorButton chooseButton = new ColorButton("Choose");
    private final ColorButton cancelButton = new ColorButton("Cancel");

    /**
     * Creates a new FileChooser window
     * @see FileChooser#getFile() 
     * @see FileChooser#getFile(java.io.File) 
     * @see FileChooser#getDirectory() 
     * @see FileChooser#getDirectory(java.io.File) 
     */
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
        
        
        fileSelector.listFiles();
        
        chooseButton.addActionListener((Action) -> {
            fileSelector.endSelection();
        });
        cancelButton.addActionListener((Action) -> {
            fileSelector.setSelection(null);
            hideWindow();
        });
        
        
        add(fileViewerSidebar, container, fileSelector, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.SOUTH, UIAlignment.SOUTH, 0);
        
        add(fileViewerControls, fileSelector, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.SOUTH, UIAlignment.NORTH, 0);
        add(fileSelector, chooseButton, UIAlignment.EAST, UIAlignment.EAST, 0, UIAlignment.SOUTH, UIAlignment.NORTH, -10);
        
        add(chooseButton, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.SOUTH, UIAlignment.SOUTH, -10);
        add(cancelButton, chooseButton, UIAlignment.EAST, UIAlignment.WEST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        setDirectory(FileUtilities.USER_HOME);
    }
    
    /**
     * Lists the files in a directory
     * @param directory the directory
     */
    @Override
    public final void setDirectory(File directory) {
        fileSelector.setDirectory(directory);
        fileViewerControls.setDirectory(directory.getAbsolutePath());
        fileViewerControls.refreshParentList(directory);
    }

    /**
     * This method does nothing, please use <code>getFile()</code>, <code>getFiles()</code> or <code>getDirectory()</code>
     * @deprecated
     * @see FileChooser#getFile() 
     * @see FileChooser#getFile(java.io.File) 
     * @see FileChooser#getDirectory() 
     * @see FileChooser#getDirectory(java.io.File) 
     */
    @Override
    @Deprecated
    public void showWindow() { }
    
    @Override
    public File [] listFiles(File path, FilenameFilter filter) {
        return FileUtilities.listFiles(path, filter);
    }

    @Override
    public void endSelection() {
        hideWindow();
    }

    @Override
    public void setDisposition(UIFileDisposition disposition) {
        fileSelector.setDisposition(disposition);
    }

    @Override
    public boolean isVisibleHiddenFiles() {
        return fileSelector.isVisibleHiddenFiles();
    }

    @Override
    public void setVisibleHiddenFiles(boolean visibleHiddenFiles) {
        fileSelector.setVisibleHiddenFiles(visibleHiddenFiles);
    }
    
    /**
     * Creates a new <code>FilenameFilter</code>, this will overwrite any filter settled by 
     * <code>setFilenameFilter()</code><br>
     * This method uses <code>string.contains()</code> to check if <code>File.getName()</code>
     * is part of the strings array<br>
     * Note that this filter will not discriminate directories just files   
     * 
     * @param ignoreCase condition to indicate if case doesn't matter
     * @param strings the allowed extension
     */
    public void setAllowedFileNames(boolean ignoreCase, String ... strings) {
        FilenameFilter filter = (File dir, String name) -> {
            if (FileUtilities.joinPath(dir, name).isDirectory())
                return true;
            
            for (String str : strings)
                if (ignoreCase) {
                    if (name.toLowerCase().contains(str.toLowerCase()))
                        return true;
                } else {
                    if (name.contains(str))
                        return true;
                }
            
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
