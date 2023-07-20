package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JTextField;
import ui.enums.TextAlignment;
import ui.enums.UIAlignment;

/**
 * Color picker for RGB colors
 * 
 * @author cristopher
 */
public class ColorPicker extends Panel {
    private Color selectedColor = new Color(0, 0, 0);
    
    private final JComponent colorPreview = new JComponent() {
        int rwidth = 180, rheight = 30;
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
        
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            g2D.setColor(UIProperties.APP_FG);
            g2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            g2D.setColor(selectedColor);
            g2D.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
        }

        @Override
        public void setPreferredSize(Dimension preferredSize) {
            preferredSize.width = (int) (rwidth * UIProperties.uiScale);
            preferredSize.height = (int) (rheight * UIProperties.uiScale);
            
            super.setPreferredSize(preferredSize);
        }
    };
    
    /**
     * This component it's just to update color preview when sliders are moved<br>
     * I know, it's awful
     */
    private final JTextField updateControlComponent = new JTextField() {
        @Override
        public void setText(String t) {
            updateColor(true);
        }
    };
    
    private final TextField redValueField = new TextField("  r:");
    private final Slider redColorSlider = new Slider(0, 255, redValueField, updateControlComponent);
    
    private final TextField greenValueField = new TextField("  g:");
    private final Slider greenColorSlider = new Slider(0, 255, greenValueField, updateControlComponent);
    
    private final TextField blueValueField = new TextField("  b:");
    private final Slider blueColorSlider = new Slider(0, 255, blueValueField, updateControlComponent);
    
    private final TextField hexColor = new TextField("Hex #");
    
    /**
     * Components to update if color changes
     */
    protected JComponent [] componentsToUpdate;
    
    /**
     * Creates a new color picker
     * @param componentsToUpdate optional components to update when color changes
     */
    public ColorPicker(JComponent ... componentsToUpdate) {
        super(200, 178);
        
        this.componentsToUpdate = componentsToUpdate;
        
        initColorPicker();
        initUI();
    }
    
    private void initColorPicker() {
        colorPreview.setPreferredSize(new Dimension(180, 30));
        
        redValueField.setPreferredSize(new Dimension(50, 22));
        redValueField.setHorizontalAlignment(TextAlignment.CENTER);
        redValueField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateColor(true);
            }
        });
        
        greenValueField.setPreferredSize(new Dimension(50, 22));
        greenValueField.setHorizontalAlignment(TextAlignment.CENTER);
        greenValueField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateColor(true);
            }
        });
        
        blueValueField.setPreferredSize(new Dimension(50, 22));
        blueValueField.setHorizontalAlignment(TextAlignment.CENTER);
        blueValueField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateColor(true);
            }
        });
        
        hexColor.setPreferredSize(new Dimension(180, 22));
        hexColor.setHorizontalAlignment(TextAlignment.CENTER);
        hexColor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                parseColorHex();
                hexToRGB();
            }
        });
        
        add(colorPreview, this, this, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.NORTH, 10);
        add(redValueField, this, colorPreview, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(redColorSlider, this, redValueField, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        add(greenValueField, this, redValueField, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(greenColorSlider, this, greenValueField, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        add(blueValueField, this, greenValueField, UIAlignment.WEST, UIAlignment.WEST, 10, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        add(blueColorSlider, this, blueValueField, UIAlignment.EAST, UIAlignment.EAST, -10, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        
        add(hexColor, this, this, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.SOUTH, UIAlignment.SOUTH, -10);
    }
    
    @Override
    public void updateUISize() {
        if (colorPreview != null)
            colorPreview.setPreferredSize(new Dimension());
        
        super.updateUISize();
    }

    @Override
    public void setUseAppTheme(boolean useAppTheme) {
        throw new UnsupportedOperationException("Unsupported.");
    }

    @Override
    public void setUseAppColor(boolean useAppColor) {
        throw new UnsupportedOperationException("Unsupported.");
    }
    
    private int [] getColorComponents() {
        if (redValueField == null || greenValueField == null || blueValueField == null)
            return null;
        
        String r = redValueField.getText();
        String g = greenValueField.getText();
        String b = blueValueField.getText();
        
        int [] components = {0, 0, 0};
        
        
        if (r.matches("\\d+"))
            components[0] = Integer.parseInt(r);
        else if (!redValueField.placeholderVisible)
            redValueField.setText("");
        
        if (g.matches("\\d+"))
            components[1] = Integer.parseInt(g);
        else if (!greenValueField.placeholderVisible)
            greenValueField.setText("");
        
        if (b.matches("\\d+"))
            components[2] = Integer.parseInt(b);
        else if (!blueValueField.placeholderVisible)
            blueValueField.setText("");
        
        
        if (components[0] > 255) {
            components[0] = 255;
            redValueField.setText("255");
        }
        
        if (components[1] > 255) {
            components[1] = 255;
            greenValueField.setText("255");
        }
        
        if (components[2] > 255) {
            components[2] = 255;
            blueValueField.setText("255");
        }
        
        return components;
    }
    
    private void parseColorRGB() {
        if (redValueField == null || greenValueField == null || blueValueField == null)
            return;
        
        String redFieldText = redValueField.getText().replace(",", "");
        String greenFieldText = greenValueField.getText().replace(",", "");
        String blueFieldText = blueValueField.getText().replace(",", "");
        
        String [] data = null;
        
        if (redFieldText.matches("^[\\d]{1,3} [\\d]{1,3} [\\d]{1,3}$"))
            data = redFieldText.split(" ");
        if (greenFieldText.matches("^[\\d]{1,3} [\\d]{1,3} [\\d]{1,3}$"))
            data = redFieldText.split(" ");
        if (blueFieldText.matches("^[\\d]{1,3} [\\d]{1,3} [\\d]{1,3}$"))
            data = redFieldText.split(" ");
        
        if (data != null) {
            redValueField.setText(data[0]);
            greenValueField.setText(data[1]);
            blueValueField.setText(data[2]);
        }
        
        if (redFieldText.matches("^#?[a-fA-F-0-9]{6}$")) {
            hexColor.setText(redFieldText, true);
            hexToRGB();
        }
        if (greenFieldText.matches("^#?[a-fA-F-0-9]{6}$")) {
            hexColor.setText(greenFieldText, true);
            hexToRGB();
        }
        if (blueFieldText.matches("^#?[a-fA-F-0-9]{6}$")) {
            hexColor.setText(blueFieldText, true);
            hexToRGB();
        }
    }
    
    private void parseColorHex() {
        String hexFieldText = hexColor.getText().replace(",", "");
        
        String [] data = null;
        
        if (hexFieldText.matches("^[\\d]{1,3} [\\d]{1,3} [\\d]{1,3}$"))
            data = hexFieldText.split(" ");
        
        if (data != null) {
            redValueField.setText(data[0]);
            greenValueField.setText(data[1]);
            blueValueField.setText(data[2]);
            
            updateColor(true);
        }
    }
    
    private void updateColor(boolean updateHexColor) {
        parseColorRGB();
        
        int [] components = getColorComponents();
        if (components == null)
            return;
        
        if (updateHexColor)
            RGBToHex(components);
        
        redColorSlider.setValue(components[0], false);
        greenColorSlider.setValue(components[1], false);
        blueColorSlider.setValue(components[2], false);
        
        selectedColor = new Color(components[0], components[1], components[2]);
        if (componentsToUpdate != null)
            for (JComponent c : componentsToUpdate)
                c.setBackground(selectedColor);
        
        colorPreview.repaint();
    }

    /**
     * Get selected color
     * 
     * @return a color
     */
    public Color getSelectedColor() {
        return selectedColor;
    }

    /**
     * Set selected color
     * 
     * @param selectedColor color to set
     * @param updateHexColor true if hex color field should be updated
     */
    public void setSelectedColor(Color selectedColor, boolean updateHexColor) {
        this.selectedColor = selectedColor;
        
        redValueField.setText("" + this.selectedColor.getRed(), true);
        redColorSlider.setValue(this.selectedColor.getRed(), false);
        
        greenValueField.setText("" + this.selectedColor.getGreen(), true);
        greenColorSlider.setValue(this.selectedColor.getGreen(), false);
        
        blueValueField.setText("" + this.selectedColor.getBlue(), true);
        blueColorSlider.setValue(this.selectedColor.getBlue(), false);
        
        updateColor(updateHexColor);
    }
    
    private void RGBToHex(int [] components) {
        String hex = Integer.toHexString(new Color(components[0], components[1], components[2]).getRGB()).toUpperCase().substring(2);
        
        hexColor.setText(hex, true);
    }
    
    private void hexToRGB() {
        String hex = hexColor.getText();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
            hexColor.setText(hex);
        }
        
        if (hex.length() > 6) {
            hexColor.setText(hex.substring(0, 6));
            return;
        }
        
        if (!hex.matches("^[0-9a-fA-F]{0,6}$")) {
            hexColor.setText("");
            return;
        }
        
        int r = 0;
        int g = 0;
        int b = 0;
        
        if (hex.length() >= 2)
            r = Integer.parseInt(hex.substring(0, 2), 16);
        
        if (hex.length() >= 4)
            g = Integer.parseInt(hex.substring(2, 4), 16);
        
        if (hex.length() == 6)
            b = Integer.parseInt(hex.substring(4, 6), 16);
        
        setSelectedColor(new Color(r, g, b), false);
    }
    
    /**
     * Shows this color picker as dialog and returns the selected color
     * 
     * @return a <code>Color</code> or null
     */
    public Color pickColor() {
        Dialog d = new Dialog(width, height + 94);
        d.setResizable(false);
        d.setAlwaysOnTop(true);
        d.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                selectedColor = null;
            }
        });
        
        ColorButton b = new ColorButton("Select color");
        b.setPreferredSize(new Dimension(width - 20, 22));
        b.addActionListener((Action) -> {
            d.hideWindow();
        });
        
        ColorButton c = new ColorButton("Cancel");
        c.setPreferredSize(new Dimension(width - 20, 22));
        c.addActionListener((Action) -> {
            selectedColor = null;
            d.hideWindow();
        });
        
        
        KeyAdapter k = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    b.doClick();
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
                    c.doClick();
            }
        };
        
        redValueField.addKeyListener(k);
        greenValueField.addKeyListener(k);
        blueValueField.addKeyListener(k);
        hexColor.addKeyListener(k);
        
        d.add(this, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.NORTH, 0);
        d.add(b, this, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 0);
        d.add(c, b, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.SOUTH, 10);
        
        d.showWindow();
        d.dispose();
        
        redValueField.removeKeyListener(k);
        greenValueField.removeKeyListener(k);
        blueValueField.removeKeyListener(k);
        hexColor.removeKeyListener(k);
        
        return selectedColor;
    }

    public void setComponentsToUpdate(JComponent ... componentsToUpdate) {
        this.componentsToUpdate = componentsToUpdate;
    }
}
