package ui;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Scroll pane with TextArea custom painted
 * 
 * @author cristopher
 */
public class TextArea extends JScrollPane implements ComponentSetup {
    private final JTextArea textArea = new JTextArea();
    
    protected int width = -1, height = -1;
    
    protected boolean appTheme = false;
    protected boolean appColor = true;
    
    private boolean monospacedFont = false;
    
    /**
     * Creates a new TextArea given a width and height
     * 
     * @param width
     * @param height 
     */
    public TextArea(int width, int height) {
        this.width = width;
        this.height = height;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        textArea.setBorder(null);
        
        setViewportView(textArea);
        setBorder(null);
        
        getVerticalScrollBar().setUnitIncrement(10);
        getHorizontalScrollBar().setUnitIncrement(10);
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        setPreferredSize(new Dimension(width, height));
        
        getVerticalScrollBar().setUI(new ColorScrollBarUI());
        getHorizontalScrollBar().setUI(new ColorScrollBarUI());
    }

    @Override
    public void updateUIFont() {
        if (!monospacedFont)
            textArea.setFont(UIProperties.APP_FONT);
        else
            textArea.setFont(UIProperties.APP_MONOSPACED_FONT);
    }
    
    @Override
    public void updateUITheme() {
        setBackground(UIProperties.APP_BGA);
        
        getVerticalScrollBar().setUI(new ColorScrollBarUI());
        getHorizontalScrollBar().setUI(new ColorScrollBarUI());
        
        textArea.setBackground(UIProperties.APP_BGA);
        textArea.setForeground(UIProperties.APP_FG);
        
        textArea.setCaretColor(UIProperties.APP_FG);
        if (appTheme) {
            textArea.setSelectedTextColor(UIProperties.APP_FG);
            textArea.setSelectionColor(UIProperties.DIM_TEXT_COLOR);
        }
    }

    @Override
    public void updateUIColors() {
        if (appColor) {
            textArea.setSelectedTextColor(UIProperties.APP_FG_COLOR);
            textArea.setSelectionColor(UIProperties.APP_BGA_COLOR);
        }
    }
    
    @Override
    public void setUseAppTheme(boolean useAppTheme) {
        this.appTheme = useAppTheme;
        this.appColor = !useAppTheme;
        
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void setUseAppColor(boolean useAppColor) {
        this.appColor = useAppColor;
        this.appTheme = !useAppColor;
        
        updateUITheme();
        updateUIColors();
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
        this.width = preferredSize.width;
        this.height = preferredSize.height;
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        super.setPreferredSize(preferredSize);
    }

    public void setUseMonospacedFont(boolean useMonospacedFont) {
        this.monospacedFont = useMonospacedFont;
        updateUIFont();
    }
    
    public void setEditable(boolean b) {
        textArea.setEditable(b);
    }
    
    public void setText(String t) {
        textArea.setText(t);
    }
}
