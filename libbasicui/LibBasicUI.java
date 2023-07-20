package libbasicui;

import java.awt.Dimension;
import ui.CheckField;
import ui.ColorButton;
import ui.ColorPicker;
import ui.Label;
import ui.ProgressBar;
import ui.Slider;
import ui.TextArea;
import ui.TextField;
import ui.enums.UIAlignment;
import ui.UIProperties;
import ui.Window;
import ui.enums.LabelType;
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
        
        
        private final CheckField checkField = new CheckField("Indeterminated", false, false);
        private final ProgressBar progressBar = new ProgressBar(0, 100, false);
        private final Slider vslider = new Slider(UIOrientation.VERTICAL, 0, 100, progressBar);
        
        
        private final TextArea textArea = new TextArea(200, 200);
        
        
        private final ColorButton preferencesButton = new ColorButton("UI Preferences");
        public final UIPreferences uiPreferences = new UIPreferences(this);
        
        
        public Demo() {
            super(430, 420, true);
            
            setResizable(false);
            
            checkField.setPreferredSize(new Dimension(178, 22));
            checkField.addActionListener((Action) -> {
                progressBar.setIndeterminate(!checkField.isChecked());
            });
            
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
            add(checkField, progressBar, vslider, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.NORTH, 0);
            add(progressBar, textArea, UIAlignment.WEST, UIAlignment.WEST, 0, UIAlignment.SOUTH, UIAlignment.NORTH, -10);
            
            
            add(textArea, picker, UIAlignment.WEST, UIAlignment.EAST, 10, UIAlignment.NORTH, UIAlignment.NORTH, 0);
            
            add(preferencesButton, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.SOUTH, UIAlignment.SOUTH, -10);
        }
    }
}
