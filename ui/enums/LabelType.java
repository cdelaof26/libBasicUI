package ui.enums;

/**
 * Font type for label
 *
 * @author cristopher
 */
public enum LabelType {
    /**
     * Plain text, size = 24
     */
    TITLE, 
    /**
     * Bold text, size = 24
     */
    BOLD_TITLE, 
    /**
     * Bold text, size = 18
     */
    SUBTITLE, 
    /**
     * Plain text, size = 13
     */
    BODY, 
    /**
     * Bold text, size = 13
     */
    BOLD_BODY, 
    /**
     * Plain text, size = 13, wine colored in light mode otherwise orange
     */
    WARNING_LABEL, 
    /**
     * Font, size, family and style can be customized
     * @see ui.UIFont
     */
    CUSTOM,
    /**
     * No font will be set automatically allowing for a better manual handling
     */
    NONE
}
