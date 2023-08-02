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
import ui.FileChooser;
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
    
    protected FilenameFilter filter = null;
    protected boolean visibleHiddenFiles = false;
    
    
    protected int width = 630;
    protected int height = 328;
    
    
    private final FileChooser container;
    
    
    public FileViewer(FileChooser container) {
        this.container = container;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        setBorder(null);
        setViewportView(panel);
        
        panel.setPreferredSize(new Dimension(width, height));
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
//        getHorizontalScrollBar().setUnitIncrement((int) (22 * UIProperties.getUiScale()));
        
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
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.getUiScale());
        preferredSize.height = (int) (preferredSize.height * UIProperties.getUiScale());
        
        super.setPreferredSize(preferredSize);
    }

    public void setDirectory(File directory) {
        this.directory = directory;
        this.selection = this.directory;
        listFiles();
    }

    public void setFilter(FilenameFilter filter) {
        this.filter = filter;
    }

    public void setDisposition(UIFileDisposition disposition) {
        this.disposition = disposition;
        for (UIFile uif : files)
            uif.setDisposition(disposition);
        
        rearrangeFiles(true);
    }

    public void setMode(FileChooserModal mode) {
        this.mode = mode;
    }
    
    public void setVisibleHiddenFiles(boolean visibleHiddenFiles) {
        this.visibleHiddenFiles = visibleHiddenFiles;
        listFiles();
    }

    public boolean isVisibleHiddenFiles() {
        return visibleHiddenFiles;
    }
    
    private void rearrangeFiles(boolean removePreviousFiles) {
        if (removePreviousFiles)
            for (UIFile uif : files)
                panel.remove(uif);
        
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
    
    public void listFiles() {
        for (UIFile uif : files)
            panel.remove(uif);
        
        files = new ArrayList<>();
        
        File [] listedFiles = FileUtilities.listFiles(directory, filter);
        
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

    public void unselectFiles(int index) {
//        if (multiselector && controlDown)
//            return;

        for (int i = 0; i < files.size(); i++) {
            if (i == index)
                continue;
            
            files.get(i).resetClicks();
        }
    }

    public File getSelection() {
        return selection;
    }
    
    public void setSelection(File selection) {
        this.selection = selection;
    }
    
    public void openSelection() {
        if (selection.isFile() && mode == FileChooserModal.SINGLE_DIRECTORY) {
            selection = null;
            return;
        } else if (selection.isFile())
            container.hideWindow();

        if (selection.isDirectory())
            container.setDirectory(selection);
    }
    
    public void endSelection() {
        if (selection.isFile() && mode == FileChooserModal.SINGLE_FILE) {
            container.hideWindow();
            return;
        }

        if (selection.isDirectory() && mode == FileChooserModal.SINGLE_DIRECTORY) {
            container.hideWindow();
            return;
        }
        
        selection = directory;
    }
}
