package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import ui.enums.LabelType;
import ui.enums.TextAlignment;
import javax.swing.JLabel;

/**
 * Custom painted JLabel
 *
 * @author cristopher
 */
public class Label extends JLabel implements ComponentSetup {
    private LabelType labelType;
    
    /**
     * Creates a new Label without text
     * 
     * @param labelType 
     */
    public Label(LabelType labelType) {
        this.labelType = labelType;
        
        initUI();
    }

    /**
     * Creates a new Label with text
     * 
     * @param labelType
     * @param text 
     */
    public Label(LabelType labelType, String text) {
        super(text);
        
        this.labelType = labelType;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        setTextAlignment(TextAlignment.CENTER);
        
        updateUIFont();
        updateUITheme();
    }

    @Override
    public void updateUISize() {
        updateUIFont();
    }

    @Override
    public void updateUIFont() {
        switch (labelType) {
            case TITLE:
                setFont(UIProperties.APP_TITLE_FONT);
            break;
            case BOLD_TITLE:
                setFont(UIProperties.APP_BOLD_TITLE_FONT);
            break;
            case SUBTITLE:
                setFont(UIProperties.APP_SUBTITLE_FONT);
            break;
            case BODY:
                setFont(UIProperties.APP_FONT);
            break;
            case BOLD_BODY:
                setFont(UIProperties.APP_BOLD_FONT);
            break;
            case WARNING_LABEL:
                setFont(UIProperties.APP_BOLD_FONT);
            break;
        }
    }

    @Override
    public void updateUITheme() {
        if (labelType == LabelType.WARNING_LABEL)
            setForeground(UIProperties.APP_FGW);
        else
            setForeground(UIProperties.APP_FG);
    }

    @Override
    public void updateUIColors() { }
    
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

    /**
     * Changes font type
     * 
     * @param labelType 
     */
    public void setLabelType(LabelType labelType) {
        this.labelType = labelType;
        updateUIFont();
    }

    /**
     * Set text alignment
     * 
     * @param textAlignment
     */
    public void setTextAlignment(TextAlignment textAlignment) {
        setHorizontalAlignment(UIProperties.TextAlignmentToInt(textAlignment));
    }
    
    /**
     * Adds mouse listener to this label. If clicked, the component C will be clicked
     * 
     * @param c JButton or ColorButton (for hover effect)
     */
    public void ifClickedDoClick(JComponent c) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((JButton) c).doClick(1);
                c.requestFocus();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (c instanceof ColorButton) {
                    ((ColorButton) c).paintAsHovering = true;
                    ((ColorButton) c).repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (c instanceof ColorButton) {
                    ((ColorButton) c).paintAsHovering = false;
                    ((ColorButton) c).repaint();
                }
            }
        });
    }
}
