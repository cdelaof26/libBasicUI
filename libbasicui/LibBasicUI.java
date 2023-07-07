package libbasicui;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import ui.CheckBox;
import ui.ColorButton;
import ui.ColorPicker;
import ui.ImageButton;
import ui.Label;
import ui.ProgressBar;
import ui.SelectionPanel;
import ui.Slider;
import ui.TextArea;
import ui.TextField;
import ui.enums.UIAlignment;
import ui.UIProperties;
import ui.Window;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import ui.enums.SelectionPanelModes;
import ui.enums.UIOrientation;
import utils.LibUtilities;
import utils.UIPreferences;

/**
 * Class for lib demo
 *
 * @author cristopher
 */
public class LibBasicUI {
    public static void main(String[] args) {
        LibUtilities.initLibUtils();
        UIProperties.initUIProperties();
        
        Demo demo = new Demo();
        LibUtilities.loadPreferences(demo.uiPreferences);
        demo.showWindow();
    }
    
    public static class Demo extends Window {
        private final Label titleLabel = new Label(LabelType.BOLD_TITLE, "This is a title label");
        private final Label subtitleLabel = new Label(LabelType.SUBTITLE, "This is a subtitle label");
        private final Label bodyLabel = new Label(LabelType.BODY, "This is a body label");
        
        private final TextField field = new TextField("  Text Field");
        private final ColorPicker picker = new ColorPicker();
        
        
        private final CheckBox checkBox = new CheckBox(false);
        private final Label checkBoxLabel = new Label(LabelType.BODY, "Indeterminated");
        private final ProgressBar progressBar = new ProgressBar(0, 100, false);
        private final Slider vslider = new Slider(UIOrientation.VERTICAL, 0, 100, progressBar);
        
        
        private final TextArea textArea = new TextArea(200, 200);
        
        
        private final ColorButton preferencesButton = new ColorButton("UI Preferences");
        public final UIPreferences uiPreferences = new UIPreferences(this);
        
        
        public Demo() {
            super(430, 420, true);
            
            setResizable(false);
            
            checkBox.addActionListener((Action) -> {
                progressBar.setIndeterminate(!checkBox.isChecked());
            });
            
            checkBoxLabel.ifClickedDoClick(checkBox);
            progressBar.setPreferredSize(new Dimension(178, 11));
            
            vslider.setPreferredSize(new Dimension(22, 40));
            
            textArea.setText("This is a text area");
            
            preferencesButton.addActionListener((Action) -> {
                uiPreferences.toggleVisibility();
            });
            
            add(titleLabel, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.NORTH, UIAlignment.NORTH, 10);
            add(subtitleLabel, titleLabel, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
            add(bodyLabel, subtitleLabel, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
            
            add(field, bodyLabel, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
            add(picker, field, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
            
            
            add(vslider, progressBar, UIAlignment.WEST, UIAlignment.EAST, 5, UIAlignment.SOUTH, UIAlignment.SOUTH, 0);
            add(checkBoxLabel, checkBox, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
            add(checkBox, progressBar, vslider, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.NORTH, 0);
            add(progressBar, textArea, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.SOUTH, UIAlignment.NORTH, -10);
            
            
            add(textArea, picker, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.NORTH, UIAlignment.NORTH, 0);
            
            add(preferencesButton, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.SOUTH, UIAlignment.SOUTH, -10);
        }
    }
}
