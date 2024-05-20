package ui;

import ui.enums.LabelType;
import ui.enums.UIAlignment;

/**
 * Dialog to display messages and get input from user
 * 
 * @author cristopher
 */
public class FloatingMessage extends Dialog {
    private Label titleLabel;
    private ColorButton agreeButton;
    private TextArea messageBody;
    
    /**
     * Creates a new FloatingMessage object given a width and height
     * 
     * @param width the width
     * @param height the height
     * @see FloatingMessage#showMessage(java.lang.String, java.lang.String, boolean) 
     */
    public FloatingMessage(int width, int height) {
        super(width, height);
        setResizable(false);
    }
    
    /**
     * Creates a title label, a text area and a button to display a message<br>
     * 
     * @param msgTitle the message title, this will be displayed at top using a 
     * label configured as {@link LabelType#BOLD_TITLE}
     * @param msgBody the text to be displayed in the TextArea
     * @param useMonospaceFont {@link TextArea#setUseMonospacedFont(boolean)}
     */
    public void showMessage(String msgTitle, String msgBody, boolean useMonospaceFont) {
        removePreviousElements();
        
        titleLabel = new Label(LabelType.BOLD_TITLE, msgTitle);
        agreeButton = new ColorButton("Okay");
        messageBody = new TextArea(width - 20, height - (80 + agreeButton.getPreferredSize().height + 20));
        
        messageBody.setUseMonospacedFont(useMonospaceFont);
        messageBody.setEditable(false);
        messageBody.setText(msgBody);
        
        agreeButton.addActionListener((Action) -> {
            toggleVisibility();
        });
        
        add(titleLabel, UIAlignment.WEST, UIAlignment.WEST, (int) (10 * UIProperties.uiScale), UIAlignment.NORTH, UIAlignment.NORTH, (int) (10 * UIProperties.uiScale));
        add(messageBody, container, titleLabel, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.SOUTH, (int) (10 * UIProperties.uiScale));
        add(agreeButton, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.SOUTH, UIAlignment.SOUTH, (int) (-10 * UIProperties.uiScale));
        
        updateUISize();
        updateUIFont();
        updateUITheme();
        updateUIColors();
        
        showWindow();
    }
    
    private void removePreviousElements() {
        if (titleLabel != null) {
            container.remove(titleLabel);
            container.remove(agreeButton);
            container.remove(messageBody);
        }
    }
}
