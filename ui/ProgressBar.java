package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.Timer;
import ui.enums.UIOrientation;

/**
 *
 * @author cristopher
 */
public class ProgressBar extends JComponent implements ComponentSetup {
    protected int rwidth = 120, rheight = 11;
    
    protected int width = 120, height = 11;
    
    protected boolean appTheme = false;
    protected boolean appColor = true;
    protected boolean roundCorners = true;
    protected boolean paintBorder = false;
    
    /**
     * Background color
     */
    protected Color BGColor = UIProperties.APP_BG;
    
    /**
     * Progress bar color
     */
    protected Color FGColor = UIProperties.APP_FG_COLOR;
    
    private final UIOrientation orientation;
    
    /**
     * Value when progress bar is empty
     */
    private final int minimumValue;
    
    /**
     * Value when progress bar is full
     */
    private final int maximumValue;
    
    /**
     * Progress bar position
     */
    private int position = 0;
    
    private int oldPosition = 0;
    
    private final boolean exitIndeterminate;
    
    private int positionCopy = 0;
    private int indeterminatedPosition = 2;
    private int barWidth = 40;
    private int currentBarLength = 0;
    private boolean barWidthAccomplished = false;
    private final boolean [] animationRunning = {true, false};
    private boolean animationStarting = true;
    private boolean indeterminatedEnded = true;
    private boolean indeterminate = false;
    
    private Timer progressUpdater = new Timer(5, (Action) -> {
        repaint();
    });
    
    private Timer indeterminatedUpdater = new Timer(7, (Action) -> {
        repaint();
    });
    
    
    /**
     * Creates a new progress bar
     * 
     * @param minimumValue
     * @param maximumValue 
     * @param exitIndeterminate if true, when setting a value indeterminate 
     * mode is exited automatically
     */
    public ProgressBar(int minimumValue, int maximumValue, boolean exitIndeterminate) throws IllegalArgumentException {
        this.orientation = UIOrientation.HORIZONTAL;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.exitIndeterminate = exitIndeterminate;
        
        initUI();
    }
    
    /**
     * Creates a new progress bar
     * 
     * @param orientation component orientation
     * @param minimumValue
     * @param maximumValue 
     * @param exitIndeterminate if true, when setting a value indeterminate 
     * mode is exited automatically
     */
    public ProgressBar(UIOrientation orientation, int minimumValue, int maximumValue, boolean exitIndeterminate) throws IllegalArgumentException {
        this.orientation = orientation;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.exitIndeterminate = exitIndeterminate;
        
        initUI();
    }
    
    @Override
    public final void initUI() {
        if (minimumValue > maximumValue)
            throw new IllegalArgumentException("Minimum value cannot be greather than maximum value");
        
        if (orientation == UIOrientation.VERTICAL) {
            width = 11;
            height = 120;
            rwidth = width;
            rheight = height;
            
            position = height - 2;
            oldPosition = position;
            indeterminatedPosition = height;
            positionCopy = height;
        }
        
        setBorder(null);
        
        updateUISize();
        updateUITheme();
        updateUIColors();
    }

    @Override
    public void updateUISize() {
        width = rwidth;
        height = rheight;
        setPreferredSize(new Dimension(width, height));
        
        boolean reRunProgressUpdater = progressUpdater.isRunning();
        boolean reRunIndeterminatedUpdater = indeterminatedUpdater.isRunning();
        
        progressUpdater.stop();
        indeterminatedUpdater.stop();
        
        progressUpdater = new Timer(UIProperties.uiScale <= 1f ? (int) (7 - (UIProperties.uiScale * 2)) : (int) (7 - (UIProperties.uiScale * 3)), (Action) -> {
            repaint();
        });
    
        indeterminatedUpdater = new Timer(UIProperties.uiScale <= 1f ? (int) (9 - (UIProperties.uiScale * 2)) : (int) (9 - (UIProperties.uiScale * 3)), (Action) -> {
            repaint();
        });
        
        if (reRunProgressUpdater)
            progressUpdater.start();
        
        if (reRunIndeterminatedUpdater)
            indeterminatedUpdater.start();
    }

    @Override
    public void updateUIFont() { }

    @Override
    public void updateUITheme() {
        BGColor = UIProperties.APP_BG;
        if (appTheme)
            FGColor = UIProperties.APP_FG;
    }

    @Override
    public void updateUIColors() {
        if (appColor)
            FGColor = UIProperties.APP_BG_COLOR;
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
        this.roundCorners = roundCorners;
        repaint();
    }

    @Override
    public void setPaintBorder(boolean paintBorder) {
        this.paintBorder = paintBorder;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        if (paintBorder) {
            g2D.setColor(UIProperties.APP_FG);
            
            if (roundCorners)
                g2D.drawRoundRect(1, 1, getWidth() - 2, height - 2, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.drawRect(1, 1, getWidth() - 2, height - 2);
        }
        
        g2D.setColor(UIProperties.APP_BGA);

        if (roundCorners)
            g2D.fillRoundRect(2, 2, getWidth() - 3, height - 3, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
        else
            g2D.fillRect(2, 2, getWidth() - 3, height - 3);
        
        
        g2D.setColor(FGColor);
        
        if (!indeterminate && indeterminatedEnded)
            paintProgressBar(g2D);
        else
            if (orientation == UIOrientation.HORIZONTAL)
                paintProgressBarIndeterminatedHorizontal(g2D);
            else
                paintProgressBarIndeterminatedVertical(g2D);
    }
    
    private void paintProgressBar(Graphics2D g2D) {
        if (orientation == UIOrientation.HORIZONTAL) {
            if (roundCorners)
                g2D.fillRoundRect(2, 2, oldPosition - 3, height - 3, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.fillRect(2, 2, oldPosition - 3, height - 3);
            
            if (oldPosition < position)
                oldPosition++;
            else
                oldPosition--;
        } else {
            if (roundCorners)
                g2D.fillRoundRect(2, height - (height - 2 - oldPosition), width - 2, height - 2 - oldPosition, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.fillRect(2, height - (height - 2 - oldPosition), width - 2, height - 2 - oldPosition);
            
            if (oldPosition < position)
                oldPosition++;
            else
                oldPosition--;
        }

        if (oldPosition == position)
            progressUpdater.stop();
    }
    
    private void paintProgressBarIndeterminatedHorizontal(Graphics2D g2D) {
        if (positionCopy > 0) {
            if (roundCorners)
                g2D.fillRoundRect(2, 2, positionCopy - 3, height - 3, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.fillRect(2, 2, positionCopy - 3, height - 3);
            
            positionCopy--;
            
            return;
        }
        
        if (roundCorners)
            g2D.fillRoundRect(indeterminatedPosition, 2, currentBarLength, height - 3, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
        else
            g2D.fillRect(indeterminatedPosition, 2, currentBarLength, height - 3);
        
        if (animationStarting && !indeterminate) {
            indeterminatedUpdater.stop();
            progressUpdater.stop();
            
            oldPosition = 0;
            
            if (!progressUpdater.isRunning())
                progressUpdater.start();
            
            indeterminatedEnded = true;
            return;
        }
        
        if (animationRunning[0])
            if (currentBarLength < barWidth && !barWidthAccomplished) {
                animationStarting = false;
                
                currentBarLength++;
            } else if (indeterminatedPosition + barWidth < width - 3) {
                barWidthAccomplished = true;
                indeterminatedPosition++;
                
                if (!progressUpdater.isRunning())
                    progressUpdater.start();
                if (indeterminatedUpdater.isRunning())
                    indeterminatedUpdater.stop();
            } else if (indeterminatedPosition < width - 3) {
                indeterminatedPosition++;
                currentBarLength--;
                
                if (progressUpdater.isRunning())
                    progressUpdater.stop();
                if (!indeterminatedUpdater.isRunning())
                    indeterminatedUpdater.start();
            } else {
                animationRunning[0] = false;
                animationRunning[1] = true;
                barWidthAccomplished = false;
            }
        
        if (animationRunning[1])
            if (currentBarLength < barWidth && !barWidthAccomplished) {
                indeterminatedPosition--;
                currentBarLength++;
            } else if (indeterminatedPosition > 2) {
                barWidthAccomplished = true;
                indeterminatedPosition--;
                
                if (!progressUpdater.isRunning())
                    progressUpdater.start();
                if (indeterminatedUpdater.isRunning())
                    indeterminatedUpdater.stop();
            } else if (currentBarLength > 0) {
                currentBarLength--;
                
                if (progressUpdater.isRunning())
                    progressUpdater.stop();
                if (!indeterminatedUpdater.isRunning())
                    indeterminatedUpdater.start();
            } else {
                animationRunning[0] = true;
                animationRunning[1] = false;
                barWidthAccomplished = false;
                animationStarting = true;
            }
    }

    private void paintProgressBarIndeterminatedVertical(Graphics2D g2D) {
        if (positionCopy < height - 2) {
            if (roundCorners)
                g2D.fillRoundRect(2, positionCopy, width - 2, height - positionCopy, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
            else
                g2D.fillRect(2, positionCopy, width - 2, currentBarLength);
            
            positionCopy++;
            
            return;
        }
        
        if (roundCorners)
            g2D.fillRoundRect(2, indeterminatedPosition, width - 2, currentBarLength, UIProperties.buttonRoundRadius, UIProperties.buttonRoundRadius);
        else
            g2D.fillRect(2, indeterminatedPosition, width - 2, currentBarLength);
        
        if (animationStarting && !indeterminate) {
            indeterminatedUpdater.stop();
            progressUpdater.stop();
            
            oldPosition = height - 2;
            
            if (!progressUpdater.isRunning())
                progressUpdater.start();
            
            indeterminatedEnded = true;
            return;
        }
        
        if (animationRunning[0])
            if (currentBarLength < barWidth && !barWidthAccomplished) {
                animationStarting = false;
                
                currentBarLength++;
                indeterminatedPosition--;
            } else if (indeterminatedPosition > 2) {
                barWidthAccomplished = true;
                indeterminatedPosition--;
                
                if (!progressUpdater.isRunning())
                    progressUpdater.start();
                if (indeterminatedUpdater.isRunning())
                    indeterminatedUpdater.stop();
            } else if (currentBarLength > 0) {
                currentBarLength--;
                
                if (progressUpdater.isRunning())
                    progressUpdater.stop();
                if (!indeterminatedUpdater.isRunning())
                    indeterminatedUpdater.start();
            } else {
                animationRunning[0] = false;
                animationRunning[1] = true;
                barWidthAccomplished = false;
            }
        
        if (animationRunning[1])
            if (currentBarLength < barWidth && !barWidthAccomplished) {
                currentBarLength++;
            } else if (indeterminatedPosition + barWidth < height - 2) {
                barWidthAccomplished = true;
                indeterminatedPosition++;
                
                if (!progressUpdater.isRunning())
                    progressUpdater.start();
                if (indeterminatedUpdater.isRunning())
                    indeterminatedUpdater.stop();
            } else if (currentBarLength > 0) {
                indeterminatedPosition++;
                currentBarLength--;
                
                if (progressUpdater.isRunning())
                    progressUpdater.stop();
                if (!indeterminatedUpdater.isRunning())
                    indeterminatedUpdater.start();
            } else {
                animationRunning[0] = true;
                animationRunning[1] = false;
                barWidthAccomplished = false;
                animationStarting = true;
            }
    }
    
    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (UIProperties.uiScale == 1f) {
            rwidth = preferredSize.width;
            rheight = preferredSize.height;
        }
        
        preferredSize.width = (int) (preferredSize.width * UIProperties.uiScale);
        preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
        
        this.width = preferredSize.width;
        this.height = preferredSize.height;
        
        if (orientation == UIOrientation.HORIZONTAL && width <= 40)
            barWidth = (int) (width * 0.4);
        else
            barWidth = 40;
        
        if (orientation == UIOrientation.VERTICAL && height <= 40)
            barWidth = (int) (height * 0.4);
        else
            barWidth = 40;
        
        barWidth = (int) (barWidth * UIProperties.uiScale);
        
        super.setPreferredSize(preferredSize);
    }

    /**
     * Set a value for this progress bar
     * 
     * @param value the value
     */
    public void setValue(int value) {
        // TODO: Fix bug with UIProperties.uiScale = 1.9f; when setting a new value
        
        if (orientation == UIOrientation.HORIZONTAL)
            position = value * width / maximumValue;
        else
            position = (maximumValue - value) * height / maximumValue;
        
        if (exitIndeterminate && indeterminate)
            setIndeterminate(false);
        
        if (indeterminate)
            return;
        
        if (!progressUpdater.isRunning())
            progressUpdater.start();
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        
        if (!indeterminatedEnded)
            return;
        
        if (indeterminate) {
            positionCopy = position;
            indeterminatedEnded = false;
            if (!indeterminatedUpdater.isRunning())
                indeterminatedUpdater.start();
        }
    }
}
