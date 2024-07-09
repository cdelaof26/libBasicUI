package utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Interface to implement a FilePicker component.<br>
 * This interfaces aims to help in the creation of a component capable of 
 * obtaining files from the user.
 * 
 * @author cristopher
 */
public interface FilePicker {
    /**
     * Shows the file picker and returns the selected file, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters.
     * 
     * @return a <code>File</code> object or null if user canceled
     * @see ui.FilePicker#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File getFile();
    
    /**
     * Shows the file picker and returns the selected files, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters.
     * 
     * @return a <code>File []</code> array object or null if user canceled
     * @see ui.FilePicker#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File [] getFiles();
    
    /**
     * Shows the file picker and returns the selected directory, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters.
     * 
     * @return a <code>File</code> object or null if user canceled
     * @see ui.FilePicker#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File getDirectory();
    
    /**
     * Shows the file picker and returns the selected directories, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters.
     * 
     * @return a <code>File []</code> array object or null if user canceled
     * @see ui.FilePicker#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File [] getDirectories();
    
    /**
     * Shows the file picker and returns the selected file, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters
     * 
     * @param directory the location to show, set to null to start where it was left
     * @return a file or null if user canceled
     * @see ui.FilePicker#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File getFile(File directory);
    
    /**
     * Shows the file picker and returns the selected files, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters
     * 
     * @param directory the location to show, set to null to start where it was left
     * @return a <code>File []</code> array object or null if user canceled
     * @see ui.FilePicker#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File [] getFiles(File directory);
    
    /**
     * Shows the file picker and returns the selected directory, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters
     * 
     * @param directory the location to show, set to null to start where it was left
     * @return a directory or null if user canceled
     * @see ui.FilePicker#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File getDirectory(File directory);
    
    /**
     * Shows the file picker and returns the selected directories, 
     * use <code>setAllowedFileNames()</code> and <code>setFilenameFilter()</code> to add filters
     * 
     * @param directory the location to show, set to null to start where it was left
     * @return a <code>File []</code> array object or null if user canceled
     * @see ui.FilePicker#setAllowedFileNames(boolean, java.lang.String...) 
     * @see ui.FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter) 
     */
    public File [] getDirectories(File directory);
    
    /**
     * Creates a new <code>FilenameFilter</code>, this will overwrite any filter 
     * settled by {@link FilePicker#setFilenameFilter(boolean, java.io.FilenameFilter)}<br>
     * This method uses {@link String#contains(java.lang.CharSequence)} to check if {@link File#getName()}
     * is part of the strings array<br>
     * <b>Note that this filter will not discriminate directories just files</b>
     * 
     * @param ignoreCase condition to indicate if case doesn't matter
     * @param strings the allowed extensions
     */
    public void setAllowedFileNames(boolean ignoreCase, String ... strings);
    
    /**
     * Sets a custom made filter, this will overwrite any filter settled by 
     * {@link FilePicker#setAllowedFileNames(boolean, java.lang.String...)}<br>
     * 
     * @param alwaysAcceptDirectories if true, it will create a new {@link FilenameFilter},
     * this new filter will always allow directories to pass and will only 
     * discriminate files using the given filter
     * @param filter the filename filter
     */
    public void setFilenameFilter(boolean alwaysAcceptDirectories, FilenameFilter filter);
}
