package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
     * Writes a binary file
     * 
     * @param inputStream
     * @param outputFile
     * @param closeInputStream
     * @return true if success otherwise false
     */
    public static boolean writeFile(InputStream inputStream, File outputFile, boolean closeInputStream) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {

            byte [] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1)
                fileOutputStream.write(dataBuffer, 0, bytesRead);

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (closeInputStream && inputStream != null)
                try { inputStream.close(); } catch (IOException ex) { }
        }
        
        return false;
    }
    
    /**
     * Downloads a file from a URL, **this will overwrite any file**
     * 
     * @param url
     * @param outputFile where the file should be saved
     * @return true if success otherwise false
     */
    public static boolean downloadFile(String url, File outputFile) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
            return writeFile(in, outputFile, true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Creates an array containing all files and directories inside a zip file
     * 
     * @param inputFile
     * @param includeDirectories
     * @return an array
     */
    public static String [] listFilesInZip(File inputFile, boolean includeDirectories) {
        try {
            if (inputFile.isDirectory())
                return null;
            
            ZipFile zip = new ZipFile(inputFile);
            
            ArrayList<String> files = new ArrayList<>();
            
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                if (!includeDirectories && e.isDirectory())
                    continue;
                
                files.add(e.getName());
            }
            
            return files.toArray(new String[files.size()]);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Extracts a single file from a zip archive
     * 
     * @param inputFile
     * @param relativePath the path of the archive to extract
     * @param outputFile
     * @return true if success otherwise false
     * @throws FileNotFoundException throw if relativePath doesn't exist in the
     * zip file
     * @see FileUtilities#listFilesInZip(java.io.File, boolean) 
     */
    public static boolean extractSingleZippedFile(File inputFile, String relativePath, File outputFile) throws FileNotFoundException {
        try {
            if (inputFile.isDirectory())
                return false;
            
            ZipFile zip = new ZipFile(inputFile);
            ZipEntry e = zip.getEntry(relativePath);
            
            if (e == null)
                throw new FileNotFoundException("Relative path '" + relativePath + "' not found in zip file");
            if (e.isDirectory())
                return false;
            
            return writeFile(zip.getInputStream(e), outputFile, true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Extracts all files from a zip archive
     * 
     * @param inputFile
     * @param outputPath
     * @return true if success otherwise false
     */
    public static boolean extractAllZippedFiles(File inputFile, File outputPath) {
        try {
            if (inputFile.isDirectory())
                return false;
            
            File parentPath = inputFile.getParentFile();
            
            ZipFile zip = new ZipFile(inputFile);
            
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                
                File file = joinPath(parentPath, e.getName());
                
                if (e.isDirectory()) {
                    file.mkdir();
                    continue;
                }
                
                InputStream inputStream = zip.getInputStream(e);
                if (!writeFile(inputStream, file, true))
                    return false;
            }
            
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
