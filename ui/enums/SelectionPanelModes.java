package ui.enums;

/**
 * Operation modes for {@link SelectionPanel}
 * 
 * @author cristopher
 */
public enum SelectionPanelModes {
    /**
     * Labels are placed and are drag-able, text only
     */
    ARRANGE, 
    /**
     * Buttons are placed and are clickable but not drag-able.<br>
     * In this mode the selected option(s) will be highlighted, 
     * text and images are allowed
     */
    SELECTION, 
    /**
     * Buttons are placed and are clickable, not drag-able and will disappear
     * on click. Text and images are allowed
     */
    DISCARD
}
