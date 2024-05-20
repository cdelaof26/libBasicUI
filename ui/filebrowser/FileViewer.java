package ui.filebrowser;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import ui.ColorScrollBarUI;
import ui.ComponentSetup;
import ui.Panel;
import ui.UIProperties;
import ui.enums.FileChooserModal;
import ui.enums.UIAlignment;
import ui.enums.UIFileDisposition;
import utils.FileUtilities;

/**
 *
 * @author cristopher
 */
public class FileViewer extends JScrollPane implements ComponentSetup {
    private final Panel panel = new Panel() {
        @Override
        public void updateUITheme() {
            if (appTheme)
                setBackground(UIProperties.APP_BGA);

            for (Component c : getComponents())
                if (c instanceof ComponentSetup)
                    ((ComponentSetup) c).updateUITheme();
        }

        @Override
        public int getWidth() {
            return width;
        }
    };
    
    private ArrayList<UIFile> files = new ArrayList<>();
    private UIFileDisposition disposition = UIFileDisposition.LIST_MODE;
    private FileChooserModal mode = FileChooserModal.SINGLE_FILE;
    
    private File directory = FileUtilities.USER_HOME;
    private File selection = null;
    
    private FilenameFilter filter = null;
    private boolean visibleHiddenFiles = false;
    
    /**
     * The width for this viewer
     */
    protected int width = 630;
    
    /**
     * The height for this viewer
     */
    protected int height = 328;
    
    
    private final FileBrowser container;
    
    /**
     * Creates a new FileViewer
     * @param container a object to manage file selections
     */
    public FileViewer(FileBrowser container) {
        this.container = container;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        setBorder(null);
        setViewportView(panel);
        
        panel.setPreferredSize(new Dimension(width, height));
        panel.updateOnJComponentAdded = false;
        panel.setDropTarget(new DropTarget(panel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent event) {
                event.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transaction = event.getTransferable();

                if (!transaction.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                    return;

                try {
                    List<?> data = (List<?>) transaction.getTransferData(DataFlavor.javaFileListFlavor);
                    
                    if (data.size() == 1) {
                        Object o = data.get(0);
                        
                        if (!(o instanceof File))
                            return;
                        
                        File f = (File) o;
                        if (f.getParentFile() != null)
                            setDirectory(f.getParentFile());
                        else
                            setDirectory(FileUtilities.ROOT_DIRECTORY);

                        setSelection(f);
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        }));
        
        updateUISize();
    }

    @Override
    public void updateUISize() {
        setPreferredSize(new Dimension(width, height));
        
        getVerticalScrollBar().setUI(new ColorScrollBarUI());
        getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        
        int increment = disposition == UIFileDisposition.LIST_MODE ? 22 : 60;
        
        getVerticalScrollBar().setUnitIncrement((int) (increment * UIProperties.getUiScale()));
        
        panel.updateUISize();
    }

    @Override
    public void updateUIFont() {
        panel.updateUIFont();
    }

    @Override
    public void updateUITheme() {
        getVerticalScrollBar().setUI(new ColorScrollBarUI());
        getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        
        panel.updateUITheme();
    }

    @Override
    public void updateUIColors() {
        panel.updateUIColors();
    }

    @Override
    public void setUseAppTheme(boolean useAppTheme) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setUseAppColor(boolean useAppColor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRoundCorners(boolean roundCorners) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPaintBorder(boolean paintBorder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        width = preferredSize.width;
        height = preferredSize.height;
        
        rearrangeFiles(false);
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.getUiScale());
        preferredSize.height = (int) (preferredSize.height * UIProperties.getUiScale());
        
        super.setPreferredSize(preferredSize);
    }

    /**
     * Changes the directory and lists all files inside
     * @param directory the path
     */
    public void setDirectory(File directory) {
        this.directory = directory;
        this.selection = this.directory;
        listFiles();
    }

    /**
     * Changes the {@link FilenameFilter} for this component, this won't refresh 
     * already listed files
     * @param filter the new filter
     * @see FileViewer#listFiles() 
     */
    public void setFilter(FilenameFilter filter) {
        this.filter = filter;
    }

    /**
     * Rearranges UI to change how files are displayed
     * 
     * @param disposition the new disposition
     */
    public void setDisposition(UIFileDisposition disposition) {
        this.disposition = disposition;
        for (UIFile uif : files)
            uif.setDisposition(disposition);
        
        rearrangeFiles(true);
    }

    /**
     * Changes the selection mode, this won't refresh already listed files
     * @param mode the new selection mode
     * @see FileViewer#listFiles() 
     */
    public void setMode(FileChooserModal mode) {
        this.mode = mode;
    }
    
    /**
     * Changes what files will be listed, this method invokes {@link FileViewer#listFiles()}
     * 
     * @param visibleHiddenFiles if true, hidden files will be listed
     */
    public void setVisibleHiddenFiles(boolean visibleHiddenFiles) {
        this.visibleHiddenFiles = visibleHiddenFiles;
        listFiles();
    }

    /**
     * @return whether the hidden files are listed or not
     */
    public boolean isVisibleHiddenFiles() {
        return visibleHiddenFiles;
    }
    
    /**
     * Removes all UIFile elements added
     * @param repaint if true, repaint and revalidate methods will be called
     */
    public void removeAllUIFiles(boolean repaint) {
        for (UIFile uif : files)
            panel.remove(uif);
        
        if (repaint) {
            repaint();
            revalidate();
        }
    }
    
    /**
     * Initializes UIFiles array list
     */
    public void emptyUIFiles() {
        files = new ArrayList<>();
    }
    
    /**
     * Updates files on the component
     * @param removePreviousFiles if true, previous {@link UIFile}s will be removed
     */
    public void rearrangeFiles(boolean removePreviousFiles) {
        if (removePreviousFiles)
            removeAllUIFiles(false);
        
        int containerHeight = 0;
        
        switch (disposition) {
            case LIST_MODE:
                containerHeight = files.size() * 22;
            break;
            case ICON_MODE:
                containerHeight = (int) (Math.ceil(files.size() / 6f) * 60) + 10;
            break;
        }
        
        for (int i = 0; i < files.size(); i++) {
            UIFile uif = files.get(i);
            
            if (disposition == UIFileDisposition.LIST_MODE)
                if (containerHeight > height)
                    uif.setPreferredSize(new Dimension(width - 10, 22));
                else
                    uif.setPreferredSize(new Dimension(width, 22));
            
            switch (disposition) {
                case LIST_MODE:
                    if (i == 0)
                        panel.add(uif, panel, panel, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.NORTH, 0);
                    else
                        panel.add(uif, panel, files.get(i - 1), UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 0);
                break;
                case ICON_MODE:
                    if (i == 0)
                        panel.add(uif, panel, panel, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.NORTH, UIAlignment.NORTH, 10);
                    else if (i % 6 == 0)
                        panel.add(uif, files.get(i - 6), files.get(i - 1), UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
                    else
                        panel.add(uif, files.get(i - 1), files.get(i - 1), UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
                break;
            }
        }
        
        if (containerHeight > height)
            panel.setPreferredSize(new Dimension(width - 10, containerHeight));
        else
            panel.setPreferredSize(new Dimension(width, containerHeight));
        
        getVerticalScrollBar().setValue(0);
        
        panel.updateUITheme();
        panel.updateUIColors();
        
        revalidate();
        repaint();
    }
    
    /**
     * Updates all selectable files and directories
     */
    public void listFiles() {
        removeAllUIFiles(false);
        emptyUIFiles();
        
        File [] listedFiles = container.listFiles(directory, filter);
        
        if (listedFiles != null)
            for (File f : listedFiles) {
                if (!visibleHiddenFiles && f.isHidden())
                    continue;
                
                if (f.isFile() && mode == FileChooserModal.SINGLE_DIRECTORY)
                    continue;
                
                files.add(new UIFile(files.size(), f, disposition, this));
            }
        
        rearrangeFiles(false);
    }
    
    /**
     * Appends a new UIFile to the internal ArrayList for listing
     * @param uif the UIFile
     */
    public void addUIFile(UIFile uif) {
        files.add(uif);
    }
    
    /**
     * @return the number of UIFiles inside the internal ArrayList
     */
    public int getAmountOfUIFiles() {
        return files.size();
    }

    /**
     * @return the UIFileDisposition
     */
    public UIFileDisposition getDisposition() {
        return disposition;
    }

    /**
     * @return the mode of operation for this FileViewer
     */
    public FileChooserModal getMode() {
        return mode;
    }

    /**
     * @return the current directory
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * @return the FilenameFilter
     */
    public FilenameFilter getFilter() {
        return filter;
    }

    /**
     * Deselects a file given its index. This is not meant to be used outside 
     * the FileViewer-UIFile implementation
     * 
     * @param index the index
     */
    protected void unselectFiles(int index) {
//        if (multiselector && controlDown)
//            return;

        for (int i = 0; i < files.size(); i++) {
            if (i == index)
                continue;
            
            files.get(i).resetClicks();
        }
    }

    /**
     * @return the selected file or directory
     */
    public File getSelection() {
        return selection;
    }
    
    /**
     * Sets a file to be the selection
     * @param selection the file or directory
     */
    public void setSelection(File selection) {
        this.selection = selection;
    }
    
    /**
     * Opens the selection depending on the mode and the selected item
     * - If <code>selection</code> is a file and <code>mode</code> = {@link FileChooserModal#SINGLE_DIRECTORY}<br>
     *   then nothing is done
     * - If <code>selection</code> is a file then {@link FileBrowser#endSelection()} method is called<br>
     * - If <code>selection</code> is a directory then {@link FileBrowser#setDirectory(java.io.File)} method is called
     */
    public void openSelection() {
        if (selection.isFile() && mode == FileChooserModal.SINGLE_DIRECTORY) {
            selection = null;
            return;
        } else if (selection.isFile())
            container.endSelection();

        if (selection.isDirectory())
            container.setDirectory(selection);
    }
    
    /**
     * Ends the selection by calling {@link FileBrowser#endSelection()} 
     * if conditions are meet:
     * <br><br>- <code>selection</code> must not be null<br>
     * - <code>selection</code> is a file and mode = {@link FileChooserModal#SINGLE_FILE} or<br>
     * - <code>selection</code> is a directory and mode = {@link FileChooserModal#SINGLE_DIRECTORY}<br>
     */
    public void endSelection() {
        if (selection == null)
            return;

        if (selection.isFile() && mode == FileChooserModal.SINGLE_FILE) {
            container.endSelection();
            return;
        }

        if (selection.isDirectory() && mode == FileChooserModal.SINGLE_DIRECTORY) {
            container.endSelection();
            return;
        }
        
        selection = directory;
    }
}
