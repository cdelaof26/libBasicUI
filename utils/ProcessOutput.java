package utils;

import java.io.File;

/**
 * Class to read and manage some properties for an ProcessBuilder call
 * @author cristopher
 * @see utils.LibUtilities#callProcess(utils.ProcessOutput, java.lang.String...) 
 * @see utils.FFmpegUtilities#callFFUtil(utils.ProcessOutput, utils.enums.FFUtil, java.lang.String...) 
 */
public class ProcessOutput {
    /**
     * Flag which indicates if the process is running
     */
    protected boolean running = false;
    
    /**
     * Flag which indicates if the process should be destroyed or not, similar
     * to <code>destroyProcessCalls</code> but this will only stop this process.
     * @see utils.LibUtilities#destroyProcessCalls
     */
    protected boolean haltProcess = false;
    
    /**
     * ProcessBuilder's output data (stdout and stderr)
     * @see utils.ProcessOutput#includeStderrData
     */
    protected String data = "";

    /**
     * ProcessBuilder's exit code
     */
    protected int exitCode = -1;
    
    /**
     * This defines whether the stderr (error data) will be reported in the 
     * output or not, by default this is true.
     */
    protected boolean includeStderrData = true;

    /**
     * The executed command<br>
     * On error "Error while running" will be appended to the start of this 
     * String
     */
    protected String commandData;
    
    /**
     * Current Working Directory. This is the directory in which the process 
     * will be called from. It can be null.<br>
     * For Windows systems, it's recommended to use the full path for the 
     * program instead of setting this.<br>
     * <pre>
     * $ ffmpeg.exe arg1 arg2 ...             # Not recommended
     * $ C:\path\to\ffmpeg.exe arg1 arg2 ...  # Recommended
     * </pre>
     * @see ProcessBuilder#directory(java.io.File) 
     */
    public File cwd = null;

    /**
     * Flag to indicate if the output will be printed using println
     */
    public boolean printDataOnNonZeroExitCode = true;

    /**
     * Flag to indicate if InterruptedException will be thrown on a non-zero
     * exit code
     */
    public boolean throwExceptionOnNonZeroExitCode = true;

    /**
     * Creates a new ProcessOutput object
     * 
     * @see utils.LibUtilities#callProcess(utils.ProcessOutput, java.lang.String...) 
     * @see utils.ProcessOutput#cwd
     * @see utils.ProcessOutput#data
     * @see utils.ProcessOutput#setIncludeStderrData(boolean) 
     * @see utils.ProcessOutput#setHaltProcess(boolean) 
     */
    public ProcessOutput() { }

    /**
     * Creates a new ProcessOutput object given a cwd file
     * @param cwd the Current Work Directory in which the ProcessBuilder will be
     * spawned on
     * 
     * @see utils.LibUtilities#callProcess(utils.ProcessOutput, java.lang.String...) 
     * @see utils.ProcessOutput#cwd
     * @see utils.ProcessOutput#data
     * @see utils.ProcessOutput#setIncludeStderrData(boolean) 
     * @see utils.ProcessOutput#setHaltProcess(boolean) 
     */
    public ProcessOutput(File cwd) {
        this.cwd = cwd;
    }
    
    /**
     * Changes the behavior for the process
     * @param includeStderrData if true, stderr data will be appended to {@link utils.ProcessOutput#data}
     * @see utils.ProcessOutput#includeStderrData
     * @throws IllegalStateException if it's called while the process is running
     */
    public void setIncludeStderrData(boolean includeStderrData) {
        if (running)
            throw new IllegalStateException("This cannot be settled while the process is running");
        
        this.includeStderrData = includeStderrData;
    }

    /**
     * Sets a stop flag for the process
     * @param haltProcess if true, the process will be destroyed as soon as 
     * possible
     * @throws IllegalStateException if <code>haltProcess = false</code> it's 
     * called while the process is running and was previously settled to <code>true</code>
     */
    public void setHaltProcess(boolean haltProcess) {
        if (this.running && this.haltProcess && !haltProcess)
            throw new IllegalStateException("This cannot be settled back to false while it's running");
        
        this.haltProcess = haltProcess;
    }
    
    /**
     * @return the output from the process, this will be updated in real time
     */
    public String getData() {
        return data;
    }

    /**
     * @return the process exit status code
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * @return the command and its parameters in a String
     */
    public String getCommandData() {
        return commandData;
    }

    /**
     * @return whether the process is still running
     */
    public boolean isRunning() {
        return running;
    }
}
