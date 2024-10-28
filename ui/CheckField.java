package ui;

import java.awt.Color;
import java.awt.event.ActionListener;
import ui.enums.LabelType;
import ui.enums.UIAlignment;

/**
 * Checkbox with label
 * 
 * @author cristopher
 */
public class CheckField extends Panel implements UIFont {
    private final CheckBox checkBox;
    private final Label textLabel;
    
    /**
     * Creates a new CheckField
     * 
     * @param text the text of this field
     * @param useBoldFont if true, the text will be {@link ui.enums.LabelType#BOLD_BODY}, 
     * otherwise {@link ui.enums.LabelType#BODY}
     * @param checked if it should be checked when created
     */
    public CheckField(String text, boolean useBoldFont, boolean checked) {
        super(120, 22);
        
        checkBox = new CheckBox(checked);
        textLabel = new Label(useBoldFont ? LabelType.BOLD_BODY : LabelType.BODY, text);
        
        initCheckField();
    }

    private void initCheckField() {
        textLabel.ifClickedDoClick(checkBox);
        
        add(checkBox, this, this, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        add(textLabel, checkBox, checkBox, UIAlignment.WEST, UIAlignment.EAST, 5, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
    }
    
    /**
     * Adds an ActionListener to the checkbox
     * 
     * @param l the action
     */
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
     * @param checked the checkbox status
     */
    public void setChecked(boolean checked) {
        checkBox.checked = checked;
        repaint();
    }

    @Override
    public void setFontPointSize(int fontSize) {
        textLabel.setFontPointSize(fontSize);
    }

    @Override
    public void setFontFamily(String fontFamily) {
        textLabel.setFontFamily(fontFamily);
    }

    @Override
    public void setFontBold(boolean boldFont) {
        textLabel.setFontBold(boldFont);
    }

    @Override
    public void setFontItalic(boolean italicFont) {
        textLabel.setFontItalic(italicFont);
    }

    @Override
    public void setFontMonospaced(boolean monospacedFont) {
        textLabel.setFontMonospaced(monospacedFont);
    }

    @Override
    public void useCustomFontColor(boolean customColor) {
        textLabel.useCustomFontColor(customColor);
    }

    @Override
    public void setFontColor(Color fontColor) {
        textLabel.setFontColor(fontColor);
    }
}
