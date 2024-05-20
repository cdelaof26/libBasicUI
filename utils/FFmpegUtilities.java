package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utilities for FFmpeg.<br><br>
 * 
 * NOTE THAT FFmpeg integrity is NOT checked whatsoever by the utilities in this class, 
 * so it is possible run malicious software by using this library.<br><br>
 * 
 * I'm NOT responsible of any DAMAGE, you have been warned.
 * 
 * @author cristopher
 */
public class FFmpegUtilities {
    /**
     * Directory where all FFmpeg and processed files will be saved in Microsoft NT systems
     */
    public static final File WIN_PATH = FileUtilities.joinPath(LibUtilities.USER_HOME, "AppData", "Local", "libBasicUIFFmpeg");
    
    /**
     * Directory where all FFmpeg and processed files will be saved in UNIX like systems
     */
    public static final File UNIX_PATH = FileUtilities.joinPath(LibUtilities.USER_HOME, ".libBasicUIFFmpeg");
    
    /**
     * The path where FFmpeg and processed files will be saved
     */
    public static final File LIB_FFMPEG_DIRECTORY = !LibUtilities.IS_UNIX_LIKE ? WIN_PATH : UNIX_PATH;
    
    /**
     * The executable path
     */
    public static final File FFMPEG_PATH = FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, LibUtilities.IS_UNIX_LIKE ? "ffmpeg" : "ffmpeg.exe");
    
    /**
     * The command to invoke FFmpeg, {@link utils.FFmpegUtilities#FFMPEG_PATH} is the
     * CWD for downloaded copies using this library
     */
    public static String ffmpegCommand;
    
    /**
     * Indicates if FFmpeg is in {@link utils.FFmpegUtilities#LIB_FFMPEG_DIRECTORY} or
     * it's installed on PATH
     */
    public static boolean isFFmpegInstalled;
    
    /**
     * The downloading status for FFmpeg
     * @see utils.FFmpegUtilities#downloadFFmpeg() 
     */
    public static String downloadStatus = "";
    
    
    static {
        if (!LIB_FFMPEG_DIRECTORY.exists())
            LIB_FFMPEG_DIRECTORY.mkdir();
        
        checkFFmpegInstallation();
    }
    
    /**
     * This method checks if FFmpeg is installed or not, doesn't return anything
     * instead <code>isFFmpegInstalled</code> flag is going to be changed<br><br>
     * 
     * <ul>
     *     <li><code>ffmpegCommand = "ffmpeg"</code> if the OS is Unix like and it is on PATH</li>
     *     <li><code>ffmpegCommand = "./ffmpeg"</code> if the OS is Unix like and it is NOT on PATH</li>
     *     <li><code>ffmpegCommand = "Drive:\Path\ffmpeg.exe"</code> if the OS is Windows</li>
     * </ul>
     * @see utils.FFmpegUtilities#isFFmpegInstalled
     */
    public static void checkFFmpegInstallation() {
        Process process = null;
        
        try {
            process = Runtime.getRuntime().exec("ffmpeg", null);
            process.waitFor();
            
            isFFmpegInstalled = true;
        } catch (IOException | InterruptedException ex) {
            isFFmpegInstalled = false;
        } finally {
            if (process != null)
                process.destroy();
        }
        
        ffmpegCommand = isFFmpegInstalled && LibUtilities.IS_UNIX_LIKE ? "ffmpeg" : LibUtilities.IS_UNIX_LIKE ? "./ffmpeg" : FFMPEG_PATH.getAbsolutePath();
        if (!isFFmpegInstalled)
            isFFmpegInstalled = FFMPEG_PATH.exists();
    }
    
    /**
     * Creates a copy of the FFmpeg executable in <code>LIB_FFMPEG_DIRECTORY</code>
     * 
     * @param f the FFmpeg executable
     * @return true if FFmpeg was copied and it ran successfully
     * @see utils.FFmpegUtilities#LIB_FFMPEG_DIRECTORY
     */
    public static boolean copyFFmpeg(File f) {
        try {
            if (!FileUtilities.writeFile(new FileInputStream(f), FFMPEG_PATH, true))
                return false;
            
            FFMPEG_PATH.setExecutable(true);
            
            checkFFmpegInstallation();
            
            return isFFmpegInstalled;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    private static boolean extractFFmpeg(String relativePath) {
        Process process = null;
        
        try {
            process = Runtime.getRuntime().exec("tar xf ffmpeg.tar.xz " + relativePath, null, LIB_FFMPEG_DIRECTORY);
            int exitCode = process.waitFor();
            
            process = Runtime.getRuntime().exec("mv " + relativePath + " ./", null, LIB_FFMPEG_DIRECTORY);
            exitCode += process.waitFor();
            
            String path = relativePath.split("/")[0];
            process = Runtime.getRuntime().exec("rm -fr " + path, null, LIB_FFMPEG_DIRECTORY);
            exitCode += process.waitFor();
            
            return exitCode == 0;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            if (process != null)
                process.destroy();
        }
        
        return false;
    }
    
    /**
     * Retrieves a copy of FFmpeg from internet, compatible OSes:
     * <ul>
     *     <li>Windows x86-64</li>
     *     <li>Windows ARM64 (uses x86-64 build)</li>
     *     <li>Linux x86-64</li>
     *     <li>Linux ARM64</li>
     *     <li>macOS x86-64</li>
     *     <li>macOS ARM64 (uses x86-64 build)</li>
     * </ul>
     * 
     * Default links
     * <ul>
     *     <li>Windows and Linux: <a href="https://github.com/BtbN/FFmpeg-Builds/releases">BtbN/FFmpeg-Builds (GitHub)</a></li>
     *     <li>macOS:             <a href="https://evermeet.cx/ffmpeg/get/zip">https://evermeet.cx/ffmpeg</a></li>
     * </ul>
     * 
     * <br>
     * <b>Note:</b> Download and extraction process will be updated through 
     * {@link utils.FFmpegUtilities#downloadStatus}
     * 
     * @return true if FFmpeg was downloaded, copied and it ran successfully
     */
    public static boolean downloadFFmpeg() {
        String url;
        String zippedPath;
        boolean isZip = false;
        
        downloadStatus = "Preparing";
        
        if (!LibUtilities.IS_UNIX_LIKE) {
            url = "https://github.com/BtbN/FFmpeg-Builds/releases/download/latest/ffmpeg-n6.0-latest-win64-gpl-6.0.zip";
            zippedPath = "ffmpeg-n6.0-latest-win64-gpl-6.0/bin/ffmpeg.exe";
            isZip = true;
        } else if (LibUtilities.IS_MACOS) {
            url = "https://evermeet.cx/ffmpeg/get/zip";
            zippedPath = "ffmpeg";
            isZip = true;
        } else {
            if (LibUtilities.OS_ARCH.contains("aarch64")) {
                url = "https://github.com/BtbN/FFmpeg-Builds/releases/latest/download/ffmpeg-n6.0-latest-linuxarm64-gpl-6.0.tar.xz";
                zippedPath = "ffmpeg-n6.0-latest-linuxarm64-gpl-6.0/bin/ffmpeg";
            } else {
                url = "https://github.com/BtbN/FFmpeg-Builds/releases/latest/download/ffmpeg-n6.0-latest-linux64-gpl-6.0.tar.xz";
                zippedPath = "ffmpeg-n6.0-latest-linux64-gpl-6.0/bin/ffmpeg";
            }
        }
        
        File downloadPath = FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, "ffmpeg" + (isZip ? ".zip" : ".tar.xz"));
        
        downloadStatus = "Downloading";
        if (!FileUtilities.downloadFile(url, downloadPath))
            return false;
        
        downloadStatus = "Extracting";
        if (isZip)
            try {
                if (!FileUtilities.extractSingleZippedFile(downloadPath, zippedPath, FFMPEG_PATH))
                    return false;
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return false;
            }
        else
            if (!extractFFmpeg(zippedPath))
                return false;
        
        downloadStatus = "Cleaning";
        downloadPath.delete();
        
        if (LibUtilities.IS_UNIX_LIKE)
            FFMPEG_PATH.setExecutable(true);
        
        downloadStatus = "Checking";
        checkFFmpegInstallation();
        
        return isFFmpegInstalled;
    }

    /**
     * Opens a ProcessBuilder to invoke FFmpeg.<br>
     * No need to include <code>ffmpegCommand</code> in <code>args</code> parameter
     * as it is always added
     * 
     * @param args the arguments passed to FFmpeg
     * @return the output including error data
     * @throws IOException if I/O error occurs
     * @throws InterruptedException if exit code is not 0
     */
    public static String callFFmpeg(String ... args) throws IOException, InterruptedException {
        String [] arguments = new String[args.length + 1];
        arguments[0] = ffmpegCommand;
        
        System.arraycopy(args, 0, arguments, 1, args.length);
        
        ProcessBuilder processBuilder = new ProcessBuilder(arguments);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(LIB_FFMPEG_DIRECTORY);
        Process process = processBuilder.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        String data = "";
        String line;
        while (process.isAlive())
            while (reader.ready()) {
                line = reader.readLine();
                if (line == null)
                    break;

                data += line + "\n";
            }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.out.println("Output:\n" + data);
            throw new InterruptedException("Error while running '" + Arrays.toString(arguments) + "'");
        }
        
        return data;
    }
    
    /**
     * Writes inside a file all extensions supported by FFmpeg
     * 
     * @param inputExtensions if true <code>common_i_extensions</code> file will
     * be written with all input extensions otherwise <code>common_o_extensions</code>
     * will be created
     * @param overwrite if true any file will be written again, otherwise the 
     * process will be canceled
     * @see utils.FFmpegUtilities#getSupportedExtensions(boolean) 
     */
    public static void findCommonExtensions(boolean inputExtensions, boolean overwrite) {
        String filename = "common_i_extensions";
        if (!inputExtensions)
            filename = "common_o_extensions";
        
        File file = FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, filename);
        if (file.exists() && !overwrite)
            return;
            
        String searchBy = inputExtensions ? "-demuxers" : "-muxers";
        String subSearchBy = inputExtensions ? "demuxer=" : "muxer=";
        String de_muxRegex = inputExtensions ? "[ ]+[D][ ]+" : "[ ]+[E][ ]+";
        
        try {
            String [] de_muxers = callFFmpeg("-hide_banner", "-loglevel", "error", searchBy).split("\n");
            
            ArrayList<String> preCommonExtensions = new ArrayList<>();
            ArrayList<String> commonExtensions = new ArrayList<>();
            
            for (String de_muxer : de_muxers) {
                String tmp = de_muxer;
                de_muxer = de_muxer.replaceAll(de_muxRegex, "");
                if (tmp.equals(de_muxer))
                    continue;
                
                de_muxer = de_muxer.replaceAll("[ |,]+.+", "");
                preCommonExtensions.addAll(
                    Arrays.asList(
                        callFFmpeg("-hide_banner", "-loglevel", "error", "-h", subSearchBy + de_muxer).split("\n")
                    )
                );
            }
            
            for (String output : preCommonExtensions) {
                String tmp = output;
                output = output.replaceAll("[ ]+Common[ ]extensions:[ ]", "");
                if (tmp.equals(output))
                    continue;
                
                output = output.replace(".", "");
                if (output.contains(","))
                    commonExtensions.addAll(Arrays.asList(output.split(",")));
                else
                    commonExtensions.add(output);
            }
            
            String data = commonExtensions.toString().replace("[", "").replace("]", "");
            FileUtilities.writeFile(file, data);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Retrieves all extensions supported by FFmpeg.<br>
     * 
     * This won't output anything if <code>findCommonExtensions()</code> wasn't
     * called before
     * 
     * @param inputExtensions indicates what type of extensions are going to be
     * read
     * @return an array containing the extensions or null
     * @see utils.FFmpegUtilities#findCommonExtensions(boolean, boolean) 
     */
    public static String [] getSupportedExtensions(boolean inputExtensions) {
        String filename = "common_i_extensions";
        if (!inputExtensions)
            filename = "common_o_extensions";
            
        String fileData = FileUtilities.readFile(FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, filename));
        if (fileData.isEmpty())
            return null;
        
        return fileData.replace("\n", "").split(", ");
    }
}
