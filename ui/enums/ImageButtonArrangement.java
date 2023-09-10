package ui.enums;

/**
 * Arrange for ImageButton
 * 
 * @author cristopher
 */
public enum ImageButtonArrangement {
    /**
     * This arrange sets the image centered and dimensions of 30x30 for the button
     * if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 20x20<br><br>
     * 
     * <b>This arrangement doesn't support text!</b>
     * 
     * @see ui.UIProperties#uiScale
     */
    ONLY_TINY_IMAGE, 
    /**
     * This arrange sets the image centered and dimensions of 50x50 for the button
     * if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 35x35<br><br>
     * 
     * <b>This arrangement doesn't support text!</b>
     * 
     * @see ui.UIProperties#uiScale
     */
    ONLY_IMAGE, 
    /**
     * This arrange sets the text label horizontally centered and slightly 
     * separated from the bottom, the image is horizontally centered and slightly 
     * up from the vertical center<br><br>
     * 
     * The dimensions for this arrange are of 50x50 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 20x20
     * 
     * @see ui.UIProperties#uiScale
     */
    ONE_WORD_ICON_BUTTON,
    
    
    
    /**
     * This arrange sets the text label at center and the image vertically centered
     * and slightly separated from the left<br><br>
     * 
     * The dimensions for this arrange are of 120x22 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 15x15
     * 
     * @see ui.UIProperties#uiScale
     */
    CENTER_TEXT_LEFT_IMAGE, 
    /**
     * This arrange sets the text label at center and the image vertically centered
     * and slightly separated from the right<br><br>
     * 
     * The dimensions for this arrange are of 120x22 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 15x15
     * 
     * @see ui.UIProperties#uiScale
     */
    CENTER_TEXT_RIGHT_IMAGE, 
    /**
     * This arrange sets the text label slightly separated from the right and 
     * vertically centered, the image is vertically centered and slightly 
     * separated from the left<br><br>
     * 
     * The dimensions for this arrange are of 120x22 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 15x15
     * 
     * @see ui.UIProperties#uiScale
     */
    RIGHT_TEXT_LEFT_IMAGE, 
    /**
     * This arrange sets the text label slightly separated from the left and 
     * vertically centered, the image is vertically centered and slightly 
     * separated from the right<br><br>
     * 
     * The dimensions for this arrange are of 120x22 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 15x15
     * 
     * @see ui.UIProperties#uiScale
     */
    LEFT_TEXT_RIGHT_IMAGE, 
    /**
     * This arrange sets the text label slightly separated from the right and 
     * vertically centered, the image is vertically centered and slightly 
     * separated from the left of the text label<br><br>
     * 
     * The dimensions for this arrange are of 120x22 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 15x15
     * 
     * @see ui.UIProperties#uiScale
     */
    RIGHT_TEXT_RIGHT_IMAGE, 
    /**
     * This arrange sets the text label slightly separated from the left and 
     * vertically centered, the image is vertically centered and slightly 
     * separated from the right of the text label<br><br>
     * 
     * The dimensions for this arrange are of 120x22 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 15x15
     * 
     * @see ui.UIProperties#uiScale
     */
    LEFT_TEXT_LEFT_IMAGE, 
    
    
    
    /**
     * This arrange sets the text label at center and the image vertically centered
     * and slightly separated from the left<br><br>
     * 
     * Note that F-named arrangements don't set the button dimensions automatically,
     * settled dimensions may vary according to the UIScale
     * 
     * @see ui.UIProperties#uiScale
     */
    F_CENTER_TEXT_LEFT_IMAGE, 
    /**
     * This arrange sets the text label at center and the image vertically centered
     * and slightly separated from the right<br><br>
     * 
     * Note that F-named arrangements don't set the button dimensions automatically,
     * settled dimensions may vary according to the UIScale
     * 
     * @see ui.UIProperties#uiScale
     */
    F_CENTER_TEXT_RIGHT_IMAGE, 
    /**
     * This arrange sets the text label slightly separated from the right and 
     * vertically centered, the image is vertically centered and slightly 
     * separated from the left<br><br>
     * 
     * Note that F-named arrangements don't set the button dimensions automatically,
     * settled dimensions may vary according to the UIScale
     * 
     * @see ui.UIProperties#uiScale
     */
    F_RIGHT_TEXT_LEFT_IMAGE, 
    /**
     * This arrange sets the text label slightly separated from the left and 
     * vertically centered, the image is vertically centered and slightly 
     * separated from the right<br><br>
     * 
     * Note that F-named arrangements don't set the button dimensions automatically,
     * settled dimensions may vary according to the UIScale
     * 
     * @see ui.UIProperties#uiScale
     */
    F_LEFT_TEXT_RIGHT_IMAGE, 
    /**
     * This arrange sets the text label slightly separated from the right and 
     * vertically centered, the image is vertically centered and slightly 
     * separated from the left of the text label<br><br>
     * 
     * Note that F-named arrangements don't set the button dimensions automatically,
     * settled dimensions may vary according to the UIScale
     * 
     * @see ui.UIProperties#uiScale
     */
    F_RIGHT_TEXT_RIGHT_IMAGE, 
    /**
     * This arrange sets the text label slightly separated from the left and 
     * vertically centered, the image is vertically centered and slightly 
     * separated from the right of the text label<br><br>
     * 
     * Note that F-named arrangements don't set the button dimensions automatically,
     * settled dimensions may vary according to the UIScale
     * 
     * @see ui.UIProperties#uiScale
     */
    F_LEFT_TEXT_LEFT_IMAGE, 
    
    
    
    /**
     * This arrange sets the text label horizontally centered and slightly 
     * separated from the bottom, the image is horizontally centered and slightly 
     * down from the top<br><br>
     * 
     * The dimensions for this arrange are of 90x50 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 20x20
     * 
     * @see ui.UIProperties#uiScale
     */
    UP_IMAGE, 
    /**
     * This arrange sets the text label horizontally centered and slightly 
     * separated from the top, the image is horizontally centered and slightly 
     * up from the bottom<br><br>
     * 
     * The dimensions for this arrange are of 90x50 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 20x20
     * 
     * @see ui.UIProperties#uiScale
     */
    DOWN_IMAGE,
    /**
     * This arrange sets the text label horizontally centered and slightly 
     * separated from the bottom, the image is horizontally centered and slightly 
     * down from the top<br><br>
     * 
     * The dimensions for this arrange are of 120x80 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 30x30
     * 
     * @see ui.UIProperties#uiScale
     */
    UP_XL_IMAGE, 
    /**
     * This arrange sets the text label horizontally centered and slightly 
     * separated from the top, the image is horizontally centered and slightly 
     * up from the bottom<br><br>
     * 
     * The dimensions for this arrange are of 120x80 if <code>UIScale = 1.0</code><br><br>
     * Recommended image size: 30x30
     * 
     * @see ui.UIProperties#uiScale
     */
    DOWN_XL_IMAGE
}
