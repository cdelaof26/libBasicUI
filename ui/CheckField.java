package ui;

import java.awt.event.ActionListener;
import ui.enums.LabelType;
import ui.enums.UIAlignment;

/**
 * Checkbox with label
 * 
 * @author cristopher
 */
public class CheckField extends Panel {
    private final CheckBox checkBox;
    private final Label textLabel;
    
    /**
     * Creates a new CheckField
     * 
     * @param text
     * @param bold
     * @param checked if it should be checked when created
     */
    public CheckField(String text, boolean bold, boolean checked) {
        super(120, 22);
        
        checkBox = new CheckBox(checked);
        textLabel = new Label(bold ? LabelType.BOLD_BODY : LabelType.BODY, text);
        
        initCheckField();
    }

    private void initCheckField() {
        textLabel.ifClickedDoClick(checkBox);
        
        add(checkBox, this, this, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(textLabel, checkBox, checkBox, UIAlignment.WEST, UIAlignment.EAST, 5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
    }
    
    public void addActionListener(ActionListener l) {
        checkBox.addActionListener(l);
    }
    
    /**
     * @return true or false
     */
    public boolean isChecked() {
        return checkBox.checked;
    }

    /**
     * Changes visual appearance of the checkbox
     * @param checked 
     */
    public void setChecked(boolean checked) {
        checkBox.checked = checked;
        repaint();
    }
}
