package ui;

import ui.enums.LabelType;
import ui.enums.UIAlignment;

/**
 * Dialog to display messages and get input from user
 * 
 * @author cristopher
 */
public class FloatingMessage extends Dialog {
    // Message mode UI elements
    private Label titleLabel;
    private ColorButton agreeButton;
    private TextArea messageBody;
    // End message mode UI elements
    
    public FloatingMessage(int width, int height) {
        super(width, height);
        setResizable(false);
    }
    
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
    
    public void removePreviousElements() {
        if (titleLabel != null) {
            container.remove(titleLabel);
            container.remove(agreeButton);
            container.remove(messageBody);
        }
    }
}
