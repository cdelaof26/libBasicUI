package utils.enums;

/**
 * Installation steps and errors for FFmpeg utilities
 * 
 * @author cristopher
 */
public enum InstallationStatus {
    /**
     * {@link utils.FFmpegUtilities#downloadFFmpeg()} has not been called yet
     */
    NONE, 
    /**
     * The download is being prepared
     */
    PREPARING, 
    /**
     * The download is ongoing
     */
    DOWNLOADING, 
    /**
     * An error occurred while downloading
     */
    DOWNLOAD_ERROR, 
    /**
     * The downloaded files are being decompressed
     */
    EXTRACTING, 
    /**
     * An error occurred while extracting the archive
     */
    EXTRACTION_ERROR, 
    /**
     * The downloaded file is not longer accessible
     */
    FILE_NOT_FOUND, 
    /**
     * Archives are getting deleted
     */
    CLEANING, 
    /**
     * {@link utils.FFmpegUtilities#checkFFUtilsInstallation()} is running
     */
    VERIFYING_INSTALLATION
}
