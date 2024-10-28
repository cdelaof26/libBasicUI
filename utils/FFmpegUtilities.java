package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import utils.enums.FFUtil;
import utils.enums.InstallationStatus;

/**
 * Utilities for FFmpeg, FFplay and FFprobe.<br><br>
 * 
 * <b>Note:</b> the integrity is NOT checked by any of the utilities in this 
 * class, that means that is possible run malicious software by using this library.<br><br>
 * 
 * I'm NOT responsible for any damage.
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
     * The path where FF-utilities and processed files will be saved
     */
    public static final File LIB_FFMPEG_DIRECTORY = !LibUtilities.IS_UNIX_LIKE ? WIN_PATH : UNIX_PATH;
    
    /**
     * The FFmpeg executable path
     */
    public static final File FFMPEG_PATH = FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, LibUtilities.IS_UNIX_LIKE ? "ffmpeg" : "ffmpeg.exe");
    
    /**
     * The FFplay executable path
     */
    public static final File FFPLAY_PATH = FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, LibUtilities.IS_UNIX_LIKE ? "ffplay" : "ffplay.exe");
    
    /**
     * The FFprobe executable path
     */
    public static final File FFPROBE_PATH = FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, LibUtilities.IS_UNIX_LIKE ? "ffprobe" : "ffprobe.exe");
    
    /**
     * The command to invoke FFmpeg, {@link utils.FFmpegUtilities#FFMPEG_PATH} is the
     * CWD for downloaded copies using this library
     */
    public static String ffmpegCommand;
    
    /**
     * The command to invoke FFplay, {@link utils.FFmpegUtilities#FFMPEG_PATH} is the
     * CWD for downloaded copies using this library
     */
    public static String ffplayCommand;
    
    /**
     * The command to invoke FFplay, {@link utils.FFmpegUtilities#FFMPEG_PATH} is the
     * CWD for downloaded copies using this library
     */
    public static String ffprobeCommand;
    
    /**
     * Indicates if FFmpeg is in {@link utils.FFmpegUtilities#LIB_FFMPEG_DIRECTORY} or
     * it's installed on PATH
     */
    public static boolean isFFmpegInstalled;
    
    /**
     * Indicates if FFplay is in {@link utils.FFmpegUtilities#LIB_FFMPEG_DIRECTORY} or
     * it's installed on PATH
     */
    public static boolean isFFplayInstalled;
    
    /**
     * Indicates if FFprobe is in {@link utils.FFmpegUtilities#LIB_FFMPEG_DIRECTORY} or
     * it's installed on PATH
     */
    public static boolean isFFprobeInstalled;
    
    /**
     * The downloading status for FFmpeg
     * @see utils.FFmpegUtilities#downloadFFmpeg() 
     * @see utils.FFmpegUtilities#installStatus
     */
    @Deprecated
    public static String downloadStatus = "";
    
    /**
     * The downloading status for FFmpeg
     * @see utils.FFmpegUtilities#downloadFFmpeg() 
     */
    public static InstallationStatus installStatus = InstallationStatus.NONE;
    
    static {
        if (!LIB_FFMPEG_DIRECTORY.exists())
            LIB_FFMPEG_DIRECTORY.mkdir();
        
        checkFFUtilsInstallation();
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
     * @see utils.FFmpegUtilities#checkFFUtilInstallation(utils.enums.FFUtil) 
     * @see utils.FFmpegUtilities#checkFFUtilsInstallation() 
     */
    public static void checkFFmpegInstallation() {
        checkFFUtilInstallation(FFUtil.FFMPEG);
    }
    
    /**
     * This method checks whether FFmpeg, FFplay or FFprobe are installed or not, 
     * doesn't return anything instead <code>isFFmpegInstalled</code>, <code>isFFplayInstalled</code>
     * and <code>isFFprobeInstalled</code> flags are going to be updated<br><br>
     * 
     * @param utility which tool is going to look after
     * @see utils.FFmpegUtilities#isFFmpegInstalled
     * @see utils.FFmpegUtilities#isFFplayInstalled
     * @see utils.FFmpegUtilities#isFFprobeInstalled
     */
    public static void checkFFUtilInstallation(FFUtil utility) {
        Process process = null;
        
        String command = utility == FFUtil.FFMPEG ? "ffmpeg" : utility == FFUtil.FFPLAY ? "ffplay" : "ffprobe";
        
        try {
            process = Runtime.getRuntime().exec(command, null);
            process.waitFor();
            
            switch (utility) {
                case FFMPEG: isFFmpegInstalled = true; break;
                case FFPLAY: isFFplayInstalled = true; break;
                case FFPROBE: isFFprobeInstalled = true; break;
            }
        } catch (IOException | InterruptedException ex) {
            switch (utility) {
                case FFMPEG: isFFmpegInstalled = false; break;
                case FFPLAY: isFFplayInstalled = false; break;
                case FFPROBE: isFFprobeInstalled = false; break;
            }
        } finally {
            if (process != null)
                process.destroy();
        }
        
        boolean flag = utility == FFUtil.FFMPEG ? isFFmpegInstalled : 
                       utility == FFUtil.FFPLAY ? isFFplayInstalled : 
                       isFFprobeInstalled;
        
        String localCommand = "./" + command;
        String absoluteCommand = utility == FFUtil.FFMPEG ? FFMPEG_PATH.getAbsolutePath() : 
                                 utility == FFUtil.FFPLAY ? FFPLAY_PATH.getAbsolutePath() : 
                                 FFPROBE_PATH.getAbsolutePath();
        
        String globalCommand = flag && LibUtilities.IS_UNIX_LIKE ? command : 
                               LibUtilities.IS_UNIX_LIKE ? localCommand : 
                               absoluteCommand;
        
        switch (utility) {
            case FFMPEG: ffmpegCommand = globalCommand; break;
            case FFPLAY: ffplayCommand = globalCommand; break;
            case FFPROBE: ffprobeCommand = globalCommand; break;
        }
        
        if (!flag)
            switch (utility) {
                case FFMPEG: isFFmpegInstalled = FFMPEG_PATH.exists(); break;
                case FFPLAY: isFFplayInstalled = FFPLAY_PATH.exists(); break;
                case FFPROBE: isFFprobeInstalled = FFPROBE_PATH.exists(); break;
            }
    }
    
    /**
     * @return isFFmpegInstalled and isFFplayInstalled and isFFprobeInstalled
     * @see FFmpegUtilities#isFFmpegInstalled
     * @see FFmpegUtilities#isFFplayInstalled
     * @see FFmpegUtilities#isFFprobeInstalled
     */
    public static boolean areFFUtilsInstalled() {
        return isFFmpegInstalled && isFFplayInstalled && isFFprobeInstalled;
    }
    
    /**
     * This method checks whether FFUtils (FFmpeg, FFplay and FFprobe) are 
     * installed or not.
     * @return if the three tools are installed on PATH or locally
     * 
     * @see utils.FFmpegUtilities#isFFmpegInstalled
     * @see utils.FFmpegUtilities#isFFplayInstalled
     * @see utils.FFmpegUtilities#isFFprobeInstalled
     * @see utils.FFmpegUtilities#checkFFUtilInstallation(utils.enums.FFUtil) 
     */
    public static boolean checkFFUtilsInstallation() {
        checkFFUtilInstallation(FFUtil.FFMPEG);
        checkFFUtilInstallation(FFUtil.FFPLAY);
        checkFFUtilInstallation(FFUtil.FFPROBE);
        
        return areFFUtilsInstalled();
    }
    
    /**
     * Creates a copy of the FFmpeg executable in <code>LIB_FFMPEG_DIRECTORY</code>
     * 
     * @param f the FFmpeg executable
     * @return true if FFmpeg was copied and it ran successfully
     * @see utils.FFmpegUtilities#LIB_FFMPEG_DIRECTORY
     * @see utils.FFmpegUtilities#copyFFUtil(java.io.File, utils.enums.FFUtil) 
     */
    public static boolean copyFFmpeg(File f) {
        return copyFFUtil(f, FFUtil.FFMPEG);
    }
    
    /**
     * Creates a copy of any FF-utility executable in <code>LIB_FFMPEG_DIRECTORY</code>
     * 
     * @param f the executable
     * @param utility which tool is going to be copied
     * @return true if the file was copied and it ran successfully
     * @see utils.FFmpegUtilities#LIB_FFMPEG_DIRECTORY
     */
    public static boolean copyFFUtil(File f, FFUtil utility) {
        try {
            if (!FileUtilities.writeFile(new FileInputStream(f), FFMPEG_PATH, true))
                return false;
            
            if (LibUtilities.IS_UNIX_LIKE)
                switch (utility) {
                    case FFMPEG: FFMPEG_PATH.setExecutable(true); break;
                    case FFPLAY: FFPLAY_PATH.setExecutable(true); break;
                    case FFPROBE: FFPROBE_PATH.setExecutable(true); break;
                }
            
            checkFFUtilInstallation(utility);
            
            return utility == FFUtil.FFMPEG ? isFFmpegInstalled : 
                   utility == FFUtil.FFPLAY ? isFFplayInstalled : 
                   isFFprobeInstalled;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    private static boolean extractFFPackage(String [] relativePaths) {
        Process process = null;
        
        try {
            int exitCode = 0;
            String path = "";
            
            for (String relativePath : relativePaths) {
                if (path.isEmpty())
                    path = relativePath.split("/")[0];
                
                process = Runtime.getRuntime().exec("tar xf ffmpeg_package.tar.xz " + relativePath, null, LIB_FFMPEG_DIRECTORY);
                exitCode = process.waitFor();

                process = Runtime.getRuntime().exec("mv " + relativePath + " ./", null, LIB_FFMPEG_DIRECTORY);
                exitCode += process.waitFor();
            }
            
            process = Runtime.getRuntime().exec("rm -fr " + path, null, LIB_FFMPEG_DIRECTORY);
            exitCode += process.waitFor();
            
            return exitCode == 0;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            installStatus = InstallationStatus.FILE_NOT_FOUND;
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
     * {@link utils.FFmpegUtilities#installStatus}<br><br>
     * <b>Note:</b> {@link utils.FFmpegUtilities#downloadStatus} is deprecated
     * 
     * @return true if FFUtils were downloaded, copied and ran successfully
     */
    public static boolean downloadFFmpeg() {
        String url = "";
        String [] urls = null;
        
        String basePath;
        String [] zippedPaths;
        boolean isZip = true;
        
        File [] outputPaths = {
            FFMPEG_PATH, FFPLAY_PATH, FFPROBE_PATH
        };
        
        downloadStatus = "Preparing";
        installStatus = InstallationStatus.PREPARING;
        
        if (!LibUtilities.IS_UNIX_LIKE) {
            url = "https://github.com/BtbN/FFmpeg-Builds/releases/download/latest/ffmpeg-master-latest-win64-gpl.zip";
            
            basePath = "ffmpeg-master-latest-win64-gpl/bin/";
            zippedPaths = new String[] {
                basePath + "ffmpeg.exe",
                basePath + "ffplay.exe",
                basePath + "ffprobe.exe"
            };
        } else if (LibUtilities.IS_MACOS) {
            urls = new String[] {
                "https://evermeet.cx/ffmpeg/getrelease/zip",
                "https://evermeet.cx/ffmpeg/getrelease/ffplay/zip",
                "https://evermeet.cx/ffmpeg/getrelease/ffprobe/zip"
            };
            
            zippedPaths = new String[] {
                "ffmpeg", "ffplay", "ffprobe"
            };
        } else {
            isZip = false;
            
            if (LibUtilities.OS_ARCH.contains("aarch64")) {
                url = "https://github.com/BtbN/FFmpeg-Builds/releases/latest/download/ffmpeg-master-latest-linuxarm64-gpl.tar.xz";
                basePath = "ffmpeg-master-latest-linuxarm64-gpl/bin/";
            } else {
                url = "https://github.com/BtbN/FFmpeg-Builds/releases/latest/download/ffmpeg-master-latest-linux64-gpl.tar.xz";
                basePath = "ffmpeg-master-latest-linux64-gpl/bin/";
            }
            
            zippedPaths = new String[] {
                basePath + "ffmpeg",
                basePath + "ffplay",
                basePath + "ffprobe"
            };
        }
        
        
        downloadStatus = "Downloading";
        installStatus = InstallationStatus.DOWNLOADING;
        
        File [] downloadPaths = null;
        File downloadPath = null;
        
        if (LibUtilities.IS_MACOS) {
            downloadPaths = new File[] {
                FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, "ffmpeg.zip"),
                FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, "ffplay.zip"),
                FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, "ffprobe.zip")
            };
            
            for (int i = 0; i < urls.length; i++)
                if (!FileUtilities.downloadFile(urls[i], downloadPaths[i])) {
                    installStatus = InstallationStatus.DOWNLOAD_ERROR;
                    return false;
                }
        } else {
            downloadPath = FileUtilities.joinPath(LIB_FFMPEG_DIRECTORY, "ffmpeg_package" + (isZip ? ".zip" : ".tar.xz"));
            if (!FileUtilities.downloadFile(url, downloadPath)) {
                installStatus = InstallationStatus.DOWNLOAD_ERROR;
                return false;
            }
        }
        
        
        downloadStatus = "Extracting";
        installStatus = InstallationStatus.EXTRACTING;
        if (isZip)  // Windows & macOS
            try {
                for (int i = 0; i < zippedPaths.length; i++)
                    if (!FileUtilities.extractSingleZippedFile(LibUtilities.IS_MACOS ? downloadPaths[i] : downloadPath, zippedPaths[i], outputPaths[i])) {
                        installStatus = InstallationStatus.EXTRACTION_ERROR;
                        return false;
                    }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                installStatus = InstallationStatus.FILE_NOT_FOUND;
                return false;
            }
        else  // Linux & Unix
            if (!extractFFPackage(zippedPaths)) {
                installStatus = InstallationStatus.EXTRACTION_ERROR;
                return false;
            }
        
        
        downloadStatus = "Cleaning";
        installStatus = InstallationStatus.CLEANING;
        
        if (downloadPath != null)
            downloadPath.delete();
        if (downloadPaths != null)
            for (File path : downloadPaths)
                path.delete();
        
        if (LibUtilities.IS_UNIX_LIKE) {
            FFMPEG_PATH.setExecutable(true);
            FFPLAY_PATH.setExecutable(true);
            FFPROBE_PATH.setExecutable(true);
        }
        
        
        downloadStatus = "Checking";
        installStatus = InstallationStatus.VERIFYING_INSTALLATION;
        
        return checkFFUtilsInstallation();
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
     * @see utils.FFmpegUtilities#callFFUtil(utils.ProcessOutput, utils.enums.FFUtil, java.lang.String...) 
     */
    public static String callFFmpeg(String ... args) throws IOException, InterruptedException {
        ProcessOutput output = new ProcessOutput();
        callFFUtil(output, FFUtil.FFMPEG, args);
        return output.data;
    }
    
    /**
     * Opens a ProcessBuilder to invoke a FFUtility<br>
     * 
     * @param objectData object in which the output and error data will 
     * be written to
     * @param utility the tool to invoke
     * @param args the CLI arguments for the tool
     * @throws IOException if I/O error occurs
     * @throws InterruptedException if exit code is not 0
     * @see utils.LibUtilities#destroyProcessCalls
     * @see utils.ProcessOutput#haltProcess
     */
    public static void callFFUtil(ProcessOutput objectData, FFUtil utility, String ... args) throws IOException, InterruptedException {
        String [] arguments = new String[args.length + 1];
        
        arguments[0] = utility == FFUtil.FFMPEG ? ffmpegCommand :
                       utility == FFUtil.FFPLAY ? ffplayCommand :
                       ffprobeCommand;
        
        System.arraycopy(args, 0, arguments, 1, args.length);
        
        LibUtilities.callProcess(objectData, arguments);
    }
    
    /**
     * Writes in a file all extensions supported by FFmpeg
     * 
     * @param inputExtensions if true, <code>common_i_extensions</code> file will
     * be written with all input extensions otherwise <code>common_o_extensions</code>
     * will be created (output extensions)
     * @param overwrite if true, any file will be overwritten, otherwise the 
     * operation will be canceled
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
     * This will not output anything if <code>findCommonExtensions()</code> 
     * has not been called before
     * 
     * @param inputExtensions if true, the result will be input file extensions
     * supported by FFmpeg, otherwise the output ones
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
