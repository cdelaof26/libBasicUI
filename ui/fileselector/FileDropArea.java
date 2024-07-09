package ui.fileselector;

import ui.*;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import ui.enums.FileChooserModal;
import ui.enums.LabelType;
import ui.enums.UIAlignment;
import utils.LibUtilities;

/**
 * Drag and drop panel
 * 
 * @author cristopher
 */
public class FileDropArea extends ScrollPane {
    private Panel container;
    
    private Icon lightFileIcon = LibUtilities.scaleImage(LibUtilities.readImage("ui/filebrowser/assets/lDocument.png"), 30, 30);
    private Icon darkFileIcon = LibUtilities.scaleImage(LibUtilities.readImage("ui/filebrowser/assets/dDocument.png"), 30, 30);
    private Icon lightDirectoryIcon = LibUtilities.scaleImage(LibUtilities.readImage("ui/filebrowser/assets/lDirectory.png"), 30, 30);
    private Icon darkDirectoryIcon = LibUtilities.scaleImage(LibUtilities.readImage("ui/filebrowser/assets/dDirectory.png"), 30, 30);
    
    private final Label uploadIcon = new Label(LabelType.NONE);
    private final Label uploadInstruction = new Label(LabelType.BODY, "Drag & drop");
    
    
    private ArrayList<ListedUIFile> droppedFiles = new ArrayList<>();
    private ArrayList<String> droppedFileNames = new ArrayList<>();
    
    private boolean moveToBottom = false;
    
    private FileChooserModal mode = FileChooserModal.SINGLE_FILE;
    
    protected ActionListener dropperCallback = null;
    
    private final DropTarget dropperAdapter = new DropTarget(this, new DropTargetAdapter() {
        @Override
        public void drop(DropTargetDropEvent event) {
            event.acceptDrop(DnDConstants.ACTION_COPY);
            Transferable transaction = event.getTransferable();

            if (!transaction.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                return;

            try {
                List<?> data = (List<?>) transaction.getTransferData(DataFlavor.javaFileListFlavor);

                for (Object o : data) {
                    if (!(o instanceof File))
                        continue;
                    
                    File f = (File) o;
                    
                    if (!canAcceptFile(f))
                        continue;
                    
                    String absPath = f.getAbsolutePath();
                    if (droppedFileNames.contains(absPath))
                        continue;
                    
                    ListedUIFile luif = new ListedUIFile(f);
                    luif.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            removeItem(absPath);
                        }
                    });
                    
                    droppedFileNames.add(absPath);
                    droppedFiles.add(luif);
                }
                
                updateList();
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
            }
        }
    });
    
    /**
     * Creates a Panel with drag and drop capabilities given a width and a height
     * @param width the width
     * @param height the height
     */
    public FileDropArea(int width, int height) {
        super(width, height);
        
        initFileSelector();
    }
    
    private void initFileSelector() {
        container = new Panel(width, height) {
            @Override
            public void updateUISize() {
                if (uploadIcon != null) {
                    int size = (int) (30 * UIProperties.getUiScale());
                    lightFileIcon = LibUtilities.scaleImage(LibUtilities.readImage("ui/filebrowser/assets/lDocument.png"), size, size);
                    darkFileIcon = LibUtilities.scaleImage(LibUtilities.readImage("ui/filebrowser/assets/dDocument.png"), size, size);
                    lightDirectoryIcon = LibUtilities.scaleImage(LibUtilities.readImage("ui/filebrowser/assets/lDirectory.png"), size, size);
                    darkDirectoryIcon = LibUtilities.scaleImage(LibUtilities.readImage("ui/filebrowser/assets/dDirectory.png"), size, size);
                }
                super.updateUISize();
            }
            
            @Override
            public void updateUITheme() {
                if (uploadIcon != null) {
                    Icon lightIcon = mode.toString().contains("FILE") ? lightFileIcon : lightDirectoryIcon;
                    Icon darkIcon = mode.toString().contains("FILE") ? darkFileIcon : darkDirectoryIcon;
                    
                    uploadIcon.setIcon(UIProperties.isLightThemeActive() ? lightIcon : darkIcon);
                }

                super.updateUITheme();

                setBackground(UIProperties.APP_BGA);
            }
        };
        
        getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            if (moveToBottom) {
                moveToBottom = false;
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        
        setViewportView(container);
        setDropTarget(dropperAdapter);
        setScrollUnitIncrement(30);
        
        container.add(uploadIcon, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, -15);
        container.add(uploadInstruction, uploadIcon, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
    }
    
    private boolean canAcceptFile(File f) {
        switch (mode) {
            case SINGLE_FILE:
            return f.isFile() && droppedFiles.isEmpty();
            
            case MULTIPLE_FILES:
            return f.isFile();
            
            case SINGLE_DIRECTORY:
            return f.isDirectory() && droppedFiles.isEmpty();
            
            case MULTIPLE_DIRECTORIES:
            return f.isDirectory();
        }
        
        return false;
    }
    
    private void updateList() {
        if (dropperCallback != null)
            dropperCallback.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        
        if (!droppedFiles.isEmpty()) {
            container.updateAlignComponent(uploadIcon, UIAlignment.SOUTH, UIAlignment.SOUTH, false);
            container.updateAlignComponent(uploadIcon, 0, -40);
        } else {
            container.updateAlignComponent(uploadIcon, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, false);
            container.updateAlignComponent(uploadIcon, 0, -15);
        }
        
        int containerHeight = droppedFiles.size() * 30 + 90;
        int containerWidth = width + (containerHeight > height ? -10 : 0);
        
        for (int i = 0; i < droppedFiles.size(); i++) {
            ListedUIFile luif = droppedFiles.get(i);
            container.remove(luif);
            luif.setPreferredSize(new Dimension(containerWidth, 30));
            
            if (i == 0) 
                container.add(luif, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.NORTH, 0);
            else
                container.add(luif, droppedFiles.get(i - 1), UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 0);
        }
        
        moveToBottom = true;
        
        container.setPreferredSize(new Dimension(containerWidth, containerHeight));
        container.revalidate();
        container.repaint();
    }
    
    private void removeItem(String filePath) {
        int index = droppedFileNames.indexOf(filePath);
        droppedFileNames.remove(index);
        container.remove(droppedFiles.remove(index));
        
        updateList();
    }
    
    /**
     * Removes all dropped items on the panel
     */
    public void removeAllItems() {
        while (!droppedFiles.isEmpty()) {
            droppedFileNames.remove(0);
            container.remove(droppedFiles.remove(0));
        }
        
        updateList();
    }

    /**
     * Changes the selection mode
     * @param mode the new selection mode
     */
    public void setMode(FileChooserModal mode) {
        this.mode = mode;
        updateUITheme();
    }
    
    /**
     * Retrieves all dropped files into a File[] object
     * @return an array of files or null if there's none
     */
    public File [] getSelection() {
        if (droppedFiles.isEmpty())
            return null;
        
        File [] selection = new File[droppedFiles.size()];
        
        for (int i = 0; i < droppedFiles.size(); i++)
            selection[i] = droppedFiles.get(i).f;
        
        droppedFileNames = new ArrayList<>();
        droppedFiles = new ArrayList<>();
        
        return selection;
    }
    
    /**
     * @return true there is dropped files
     */
    public boolean isThereASelection() {
        return !droppedFiles.isEmpty();
    }

    /**
     * Sets the dropperCallback, this will be called when a element is dropped or
     * all items are removed
     * 
     * @param dropperCallback the ActionListener callback
     * @see FileDropArea#removeAllItems()
     */
    public void setDropperCallback(ActionListener dropperCallback) {
        this.dropperCallback = dropperCallback;
    }
}
