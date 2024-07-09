package ui.filebrowser;

import java.io.File;
import java.io.FilenameFilter;
import ui.enums.UIFileDisposition;

/**
 * Interface to implement a FileBrowser
 *
 * @author cristopher
 */
public interface FileBrowser {
    /**
     * Request the files in a path given a {@link FilenameFilter}
     * @param path the path
     * @param filter the filename filter
     * @return a list of elements matching the filter criteria
     */
    public File [] listFiles(File path, FilenameFilter filter);
    
    /**
     * Updates the UI to display new files
     */
    public void refreshListedFiles();
    
    /**
     * Sets the directory that is currently seen
     * 
     * @param directory the directory
     */
    public void setDirectory(File directory);
    
    /**
     * Ends the selection of files
     */
    public void endSelection();
    
    /**
     * Changes the arrangement for the icons in a {@link FileViewer} component
     * @param disposition the arrangement
     */
    public void setDisposition(UIFileDisposition disposition);
    
    /**
     * @return whether hidden files are visible or not
     */
    public boolean isVisibleHiddenFiles();
    
    /**
     * Sets the visibility for hidden files
     * @param visibleHiddenFiles if true, hidden files will be visible and 
     * selectable (if applies)
     */
    public void setVisibleHiddenFiles(boolean visibleHiddenFiles);
}
