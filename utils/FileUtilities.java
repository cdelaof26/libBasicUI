package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Utilities to manage files
 * 
 * @author cristopher
 */
public class FileUtilities {
    public static final String SYSTEM_ANCHOR = System.getProperty("file.separator");
    public static final File USER_HOME = new File(LibUtilities.USER_HOME);
    
    public static final File USER_DESKTOP = joinPath(LibUtilities.USER_HOME, "Desktop");
    public static final File USER_DOCUMENTS = joinPath(LibUtilities.USER_HOME, "Documents");
    public static final File USER_DOWNLOADS = joinPath(LibUtilities.USER_HOME, "Downloads");
    
    public static final File ROOT_DIRECTORY = new File(LibUtilities.IS_UNIX_LIKE ? "/" : System.getenv("SystemDrive"));
    
    
    
    /**
     * Joins a string path
     * 
     * @param path the base
     * @param paths the strings to concatenate
     * @return a file within a pathname composed by <code>path</code> and <code>paths</code>
     */
    public static File joinPath(String path, String ... paths) {
        if (path.endsWith(SYSTEM_ANCHOR))
            path = path.substring(0, path.length());
        
        for (String p : paths)
            path += SYSTEM_ANCHOR + p;
        
        return new File(path);
    }
    
    /**
     * Joins a file path
     * 
     * @param f the base
     * @param paths the strings to concatenate
     * @return a file within a pathname composed by <code>f</code> and <code>paths</code>
     */
    public static File joinPath(File f, String ... paths) {
        return joinPath(f.getAbsolutePath(), paths);
    }
    
    /**
     * Reads a plain text file
     * 
     * @param file file path
     * @return an string of file contents
     */
    public static String readFile(File file) {
        if (!file.exists() || !file.canRead())
            return "";
        
        String data = "";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready())
                data += bufferedReader.readLine() + "\n";
            
            return data;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return "";
    }
    
    /**
     * Writes a plain text file
     * 
     * @param file file path
     * @param data string to write
     * @return true if success otherwise false
     */
    public static boolean writeFile(File file, String data) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(data);
            
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Lists files given a directory, this method will check if <code>path</code>
     * is a directory and it's readable
     * 
     * @param path
     * @return an File array or null if <code>path</code> not a directory or is 
     * not readable
     */
    public static File [] listFiles(File path) {
        if (!path.isDirectory())
            return null;
        if (!path.canRead())
            return null;
        
        return path.listFiles();
    }
    
    /**
     * Lists files given a directory, this method will check if <code>path</code>
     * is a directory and it's readable
     * 
     * @param path
     * @param filter the filename filter, it can be null
     * @return an File array or null if <code>path</code> not a directory or is 
     * not readable
     */
    public static File [] listFiles(File path, FilenameFilter filter) {
        if (!path.isDirectory())
            return null;
        if (!path.canRead())
            return null;
        
        if (filter == null)
            return path.listFiles();
        
        return path.listFiles(filter);
    }
    
    public static File [] getFileParents(File path) {
        ArrayList<File> ancestors = new ArrayList<>();
        
        File parent = path.getParentFile();
        
        while (parent != null) {
            ancestors.add(parent);
            File f = parent.getParentFile();
            
            parent = f;
        }
        
        return ancestors.toArray(new File[ancestors.size()]);
    }
}
