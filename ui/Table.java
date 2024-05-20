package ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComponent;
import ui.enums.ImageButtonArrangement;
import ui.enums.LabelType;
import ui.enums.TableCellComponent;
import ui.enums.TextAlignment;
import ui.enums.UIAlignment;
import utils.ComponentConstrains;

/**
 * Custom painted Table
 * 
 * @author cristopher
 */
public class Table extends Panel {
    /**
     * Number of columns
     */
    protected int columns;
    
    /**
     * Number of rows
     */
    protected int rows;
    
    /**
     * Minimum number of columns
     */
    protected int minColumns = 1;
    
    /**
     * Minimum number of rows
     */
    protected int minRows = 2;
    
    /**
     * Border width for this table
     */
    protected float borderWidth = 1;
    
    /**
     * If true, head row (first) will be have {@link UIProperties#APP_BG_COLOR} 
     * as background
     */
    protected boolean accentColorHead = true;
    
    /**
     * Space between elements inside the table
     */
    protected int spaceBetween = 1;
    
    protected boolean allowUserToAddColumns = true;
    protected boolean allowUserToAddRows = true;
    
    protected boolean allowUserSwapColumns = true;
    protected boolean allowUserSwapRows = true;
    
    public TableCellComponent newCellComponentType = TableCellComponent.LABEL;
    
    protected int controlButtonWidth = 14;
    private final int swapComponentLength = 10;
    
    
    /**
     * The table head row
     */
    protected ArrayList<String> titles = new ArrayList<>();
    
    private ArrayList<ArrayList<JComponent>> UITable;
    private ArrayList<UIPlaceholder> columnPlaceholders;
    private ArrayList<UIPlaceholder> rowPlaceholders;
    
    
    private final ContextMenu columnContextMenu = new ContextMenu(this, false);
    private final ContextMenu rowContextMenu = new ContextMenu(this, false);
    private int appendIndex;
    
    
    private final Table container = this;
    
    /**
     * Creates a table
     * 
     * @param rows the initial amount of rows
     * @param titles the table head strings
     * @throws IllegalArgumentException if there are not titles or rows is a 
     * negative integer
     */
    public Table(int rows, String ... titles) {
        this.columns = titles.length;
        this.titles.addAll(Arrays.asList(titles));
        this.rows = rows;
        
        initTable();
    }
    
    /**
     * Creates a table
     * 
     * @param columns the initial amount of columns
     * @param rows the initial amount of rows
     * @throws IllegalArgumentException if columns or rows is a negative integer
     */
    public Table(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        
        initTable();
    }

    private void initTable() {
        if (columns <= 0)
            throw new IllegalArgumentException("This table must contain at least one column");
        
        if (rows < 0)
            throw new IllegalArgumentException("Rows cannot be a negative number");
        
        UITable = new ArrayList<>(columns);
        columnPlaceholders = new ArrayList<>(columns);
        rowPlaceholders = new ArrayList<>(rows);
        
        for (int j = 0; j < columns; j++) {
            UIPlaceholder column = new UIPlaceholder(true, j);
            addInitialComponent(column, j == 0, true);
            columnPlaceholders.add(column);
        }
        
        for (int i = 0; i < rows; i++) {
            UIPlaceholder row = new UIPlaceholder(false, i);
            addInitialComponent(row, i == 0, false);
            rowPlaceholders.add(row);
        }
        
        for (int i = 0; i < columns; i++) {
            ArrayList<JComponent> row = new ArrayList<>(rows);
            
            for (int j = 0; j < rows; j++) {
                Label c = new Label(j == 0 ? LabelType.BOLD_BODY : LabelType.BODY, j == 0 ? titles.get(i) : "");
                c.setUseAppColor(j == 0);
                
                if (j == 0)
                    columnPlaceholders.get(i).setPreferredSize(c.getPreferredSize());
                
                row.add(c);
                add(c, columnPlaceholders.get(i), rowPlaceholders.get(j), UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
            }
            
            UITable.add(row);
        }
        
//        ((Label) UITable.get(2).get(1)).setLabelType(LabelType.BOLD_BODY);
        
        columnContextMenu.setElementsArrange(ImageButtonArrangement.CENTER_TEXT_LEFT_IMAGE);
        columnContextMenu.setWidth(130);
        columnContextMenu.addOption("Append to the right", false, (Action) -> {
            addColumn(appendIndex + 1);
            appendIndex = -10;
        });
        columnContextMenu.addOption("Append to the left", false, (Action) -> {
            addColumn(appendIndex);
            appendIndex = -10;
        });
        
        rowContextMenu.setElementsArrange(ImageButtonArrangement.CENTER_TEXT_LEFT_IMAGE);
        rowContextMenu.setWidth(100);
        rowContextMenu.addOption("Append above", false, (Action) -> {
            addRow(appendIndex);
            appendIndex = -10;
        });
        rowContextMenu.addOption("Append below", false, (Action) -> {
            addRow(appendIndex + 1);
            appendIndex = -10;
        });

        externalComponents.add(columnContextMenu);
        externalComponents.add(rowContextMenu);
    
        updateUISize();
    }

    @Override
    public void updateUISize() {
        width = getRequiredExtraSpace(true);
        height = getRequiredExtraSpace(false);
        
        if (columnPlaceholders != null) {
            for (int j = 0; j < columnPlaceholders.size(); j++)
                width += columnPlaceholders.get(j).getPreferredSize().width + spaceBetween;

            for (int i = 0; i < rowPlaceholders.size(); i++)
                height += rowPlaceholders.get(i).getPreferredSize().height + spaceBetween;
        }
        
        super.updateUISize();
    }

    @Override
    public void updateUIColors() {
        super.updateUIColors();
        
        if (columnPlaceholders != null) {
            for (UIPlaceholder uip : columnPlaceholders)
                uip.setBackground(appTheme ? UIProperties.APP_BG_COLOR : UIProperties.APP_FG_COLOR);

            for (UIPlaceholder uip : rowPlaceholders)
                uip.setBackground(appTheme ? UIProperties.APP_BG_COLOR : UIProperties.APP_FG_COLOR);
        }
    }
    
    @Override
    protected void paintChildren(Graphics g) {
        Dimension preferredSize = getPreferredSize();
        
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        g2D.setStroke(new BasicStroke(borderWidth));
        
        double halfSpace = spaceBetween / 2;
        
        double initialX = columnPlaceholders.get(0).getX() - halfSpace;
        double initialY = columnPlaceholders.get(0).getY() + (allowUserSwapColumns ? swapComponentLength : 0);
        
        if (accentColorHead) {
            g2D.setColor(UIProperties.APP_BGA_COLOR);
            g2D.fill(new Rectangle2D.Double(initialX, initialY, preferredSize.width - borderWidth - initialX, rowPlaceholders.get(0).getPreferredSize().height + spaceBetween));
        }
        
        super.paintChildren(g);
        
        g2D.setColor(UIProperties.APP_FG);
        
        for (int j = 1; j < columns; j++) {
            int x = columnPlaceholders.get(j).getX();
            g2D.draw(new Line2D.Double(x - halfSpace, initialY, x - halfSpace, preferredSize.height));
        }
        
        for (int i = 1; i < rows; i++) {
            int y = rowPlaceholders.get(i).getY();
            g2D.draw(new Line2D.Double(initialX, y - halfSpace, preferredSize.width, y - halfSpace));
        }
        
//        initialX = rowPlaceholders.get(0).getX();
//        initialY += swapComponentLength;
        
        g2D.draw(new Rectangle2D.Double(initialX, initialY, preferredSize.width - borderWidth - initialX, preferredSize.height - borderWidth - initialY));
    }
    
    private int getRequiredExtraSpace(boolean width) {
        if (width)
            return (allowUserToAddRows ? controlButtonWidth : 0) + (allowUserSwapRows ? swapComponentLength : 0);
        
        return (allowUserToAddColumns ? controlButtonWidth : 0) + (allowUserSwapColumns ? swapComponentLength : 0);
    }
    
    private void addInitialComponent(JComponent c, boolean initial, boolean column) {
        if (column) {
            if (!initial) {
                add(c, columnPlaceholders.get(columnPlaceholders.size() - 1), UIAlignment.WEST, UIAlignment.EAST, spaceBetween, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
                return;
            }
            
            int xMargin = getRequiredExtraSpace(true);
            int yMargin = getRequiredExtraSpace(false);
            if (allowUserToAddColumns)
                xMargin += spaceBetween / 2;
            if (allowUserSwapColumns)
                yMargin -= swapComponentLength;

            add(c, UIAlignment.WEST, UIAlignment.WEST, xMargin, UIAlignment.NORTH, UIAlignment.NORTH, yMargin);
            
            return;
        }
        
        if (initial)
            add(c, columnPlaceholders.get(0), UIAlignment.EAST, UIAlignment.WEST, -spaceBetween / 2, UIAlignment.NORTH, UIAlignment.SOUTH, spaceBetween / 2);
        else
            add(c, rowPlaceholders.get(rowPlaceholders.size() - 1), UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.SOUTH, spaceBetween);
    }
    
    private JComponent createCell(int column, int row, String text, TableCellComponent type) {
        switch (type) {
            case LABEL:
            return new Label(row == 0 ? LabelType.BOLD_BODY : LabelType.BODY, text);
            
            case TEXT_FIELD:
                TextField c = new TextField();
                c.setPreferredSize(new Dimension(
                        columnPlaceholders.get(column).getPreferredSize().width - controlButtonWidth / 2, 
                        rowPlaceholders.get(row).getPreferredSize().height - controlButtonWidth / 2
                ));
                c.setRoundCorners(false);
                c.setHorizontalAlignment(TextAlignment.CENTER);
                c.setText(text, true);
                c.setUseOnlyAppColor(row == 0 && accentColorHead);
                if (row == 0)
                    c.setFontType(LabelType.BOLD_BODY);
                else
                    c.setVisibleBackground(false);
            return c;
            
            default:
            return null;
        }
    }
    
    private void verifyRange(int column, int row) {
        if (column >= columns || row >= rows || column < 0 || row < 0)
            throw new IllegalArgumentException("Invalid position: (" + column + ", " + row + ")");
    }
    
    private void verifyAppendRange(int column, int row) {
        if (column > columns || row > rows || column < 0 || row < 0)
            throw new IllegalArgumentException("Invalid position: (" + column + ", " + row + ")");
    }
    
    /**
     * Sets the {@link Table#allowUserSwapRows} property which will allow 
     * (or not) the user to change swap the rows of this table
     * 
     * @param allowUserSwapRows if true, the table will update itself to show 
     * drag-able UI components
     */
    public void setAllowUserSwapRows(boolean allowUserSwapRows) {
        this.allowUserSwapRows = allowUserSwapRows;
        
        updateAlignComponent(columnPlaceholders.get(0), getRequiredExtraSpace(true), getRequiredExtraSpace(false));
        updateUISize();
    }
    
    /**
     * Sets the {@link Table#allowUserSwapColumns} property which will allow (or 
     * not) the user to change swap the columns of this table
     * 
     * @param allowUserSwapColumns if true, the table will update itself to show 
     * drag-able UI components
     */
    public void setAllowUserSwapColumns(boolean allowUserSwapColumns) {
        this.allowUserSwapColumns = allowUserSwapColumns;
        
        updateAlignComponent(columnPlaceholders.get(0), getRequiredExtraSpace(true), getRequiredExtraSpace(false));
        updateUISize();
    }

    /**
     * Sets the {@link Table#allowUserToAddRows} property which will allow or 
     * not the user to add rows to this table
     * 
     * @param allowUserToAddRows if true, the table will update itself to 
     * show buttons
     */
    public void setAllowUserToAddRows(boolean allowUserToAddRows) {
        this.allowUserToAddRows = allowUserToAddRows;
        
        updateAlignComponent(columnPlaceholders.get(0), getRequiredExtraSpace(true), getRequiredExtraSpace(false));
        updateUISize();
    }
    
    /**
     * Sets the {@link Table#allowUserToAddColumns} property which will allow or 
     * not the user to add columns to this table
     * 
     * @param allowUserToAddColumns if true, the table will update itself to 
     * show buttons
     */
    public void setAllowUserToAddColumns(boolean allowUserToAddColumns) {
        this.allowUserToAddColumns = allowUserToAddColumns;
        
        updateAlignComponent(columnPlaceholders.get(0), getRequiredExtraSpace(true), getRequiredExtraSpace(false));
        updateUISize();
    }
    
    /**
     * Changes the JComponent for a cell in the table
     * 
     * @param column the cell's column
     * @param row the cell's row
     * @param preserveText if true, any text will be preserved, otherwise it 
     * will be replaced with ""
     * @param type the new JComponent to place
     */
    public void setCellType(int column, int row, boolean preserveText, TableCellComponent type) {
        verifyRange(column, row);
        String text = "";
        
        JComponent c = UITable.get(column).get(row);
        if (preserveText) {
            if (c instanceof Label)
                text = ((Label) c).getText();
            else if (c instanceof TextField)
                text = ((TextField) c).getText();
        }
        
        remove(c);
        
        c = createCell(column, row, text, type);
        
        UITable.get(column).set(row, c);
        
        add(c, columnPlaceholders.get(column), rowPlaceholders.get(row), UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
    }
    
    /**
     * Returns the type of a cell
     * 
     * @param column the cell's column
     * @param row the cell's row
     * @return the type or null
     */
    public TableCellComponent getCellType(int column, int row) {
        verifyRange(column, row);
        JComponent c = UITable.get(column).get(row);
        
        if (c instanceof Label)
            return TableCellComponent.LABEL;
        
        if (c instanceof TextField)
            return TableCellComponent.TEXT_FIELD;
        
        return null;
    }
    
    /**
     * Sets the JComponent for an entire column
     * 
     * @param column the column
     * @param preserveText if true, any text will be preserved, otherwise it 
     * will be replaced with ""
     * @param type the new JComponent to place
     */
    public void setColumnType(int column, boolean preserveText, TableCellComponent type) {
        for (int i = 0; i < rows; i++)
            setCellType(column, i, preserveText, type);
    }
    
    /**
     * Sets the JComponent for an entire row
     * 
     * @param row the row
     * @param preserveText if true, any text will be preserved, otherwise it 
     * will be replaced with ""
     * @param type the new JComponent to place
     */
    public void setRowType(int row, boolean preserveText, TableCellComponent type) {
        for (int j = 0; j < columns; j++)
            setCellType(j, row, preserveText, type);
    }
    
    /**
     * Sets the JComponent for the entire table
     * 
     * @param preserveText if true, any text will be preserved, otherwise it 
     * will be replaced with ""
     * @param type the new JComponent to place
     */
    public void setTableType(boolean preserveText, TableCellComponent type) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                setCellType(j, i, preserveText, type);
    }
    
    /**
     * Sets the text for a row
     * 
     * @param column the cell's column
     * @param row the cell's row
     * @param content the text
     */
    public void setCellContent(int column, int row, String content) {
        verifyRange(column, row);
        
        JComponent c = UITable.get(column).get(row);
        
        if (c instanceof Label)
            ((Label) c).setText(content);
        
        if (c instanceof TextField)
            ((TextField) c).setText(content);
    }
    
    /**
     * Returns the text in a cell
     * @param column the cell's column
     * @param row the cell's row
     * @return the text
     */
    public String getCellContent(int column, int row) {
        verifyRange(column, row);
        
        JComponent c = UITable.get(column).get(row);
        
        if (c instanceof Label)
            return ((Label) c).getText();
        
        if (c instanceof TextField)
            return ((TextField) c).getText();
        
        return "";
    }
    
    /**
     * Sets the same text to the entire table
     * @param content the text
     */
    public void setTableContent(String content) {
        for (int i = 0; i < columns; i++)
            for (int j = 0; j < rows; j++)
                setCellContent(i, j, content);
    }
    
    /**
     * Sets text to the entire table
     * @param content an matrix containing all strings for the table
     * @throws IllegalArgumentException if:<br>
     * 1. There are not any columns or rows<br>
     * 2. The amount of columns in content is greater than {@link Table#columns}<br>
     * 3. The amount of rows in content is greater than {@link Table#rows}
     */
    public void setTableContent(String [][] content) {
        if (content.length == 0)
            throw new IllegalArgumentException("There must be at least one column");
        
        if (content[0].length == 0)
            throw new IllegalArgumentException("There must be at least one row");
        
        if (content.length > columns)
            throw new IllegalArgumentException("The amount of columns given "
                    + "(" + content.length + ") cannot fit on this table (" + columns + ")");
        
        if (content[0].length > rows)
            throw new IllegalArgumentException("The amount of rows given "
                    + "(" + content[0].length + ") cannot fit on this table (" + rows + ")");
        
        for (int i = 0; i < content.length; i++)
            for (int j = 0; j < content[i].length; j++)
                setCellContent(i, j, content[i][j]);
    }
    
    /**
     * Returns all the content on the table as an matrix
     * @return the content as matrix
     */
    public String [][] getTableContent() {
        String [][] content = new String[columns][rows];
        
        for (int i = 0; i < columns; i++)
            for (int j = 0; j < rows; j++)
                content[i][j] = getCellContent(i, j);
        
        return content;
    }

    /**
     * Sets {@link Table#accentColorHead} property
     * @param accentColorHead if true, first row background will be {@link UIProperties#APP_BGA_COLOR}
     */
    public void setAccentColorHead(boolean accentColorHead) {
        this.accentColorHead = accentColorHead;
        for (int i = 0; i < columns; i++) {
            JComponent c = UITable.get(i).get(0);
            if (c instanceof TextField) {
                ((TextField) c).setUseOnlyAppColor(this.accentColorHead);
                ((TextField) c).setVisibleBackground(this.accentColorHead);
            }
        }
        repaint();
    }

    /**
     * @return value of accentColorHead
     */
    public boolean isAccentColorHead() {
        return accentColorHead;
    }
    
    /**
     * @return the number of columns
     */
    public int getAmountOfColumns() {
        return columns;
    }

    /**
     * @return the number of rows
     */
    public int getAmountOfRows() {
        return rows;
    }
    
    /**
     * Appends a new column given an index
     * @param index the index
     */
    public void addColumn(int index) {
        if (index == -1)
            index = columns;
        
        verifyAppendRange(index, 0);
        
        UIPlaceholder column = new UIPlaceholder(true, index);
        
        columnPlaceholders.add(index, column);
        
        if (index == 0) {
            int xMargin = getRequiredExtraSpace(true);
            int yMargin = getRequiredExtraSpace(false);
            if (allowUserToAddColumns)
                xMargin += spaceBetween / 2;
            if (allowUserSwapColumns)
                yMargin -= swapComponentLength;

            add(column, UIAlignment.WEST, UIAlignment.WEST, xMargin, UIAlignment.NORTH, UIAlignment.NORTH, yMargin);
            updateAlignComponent(rowPlaceholders.get(index), column, column);
            
            remove(columnPlaceholders.get(index + 1));
            add(columnPlaceholders.get(index + 1), column, UIAlignment.WEST, UIAlignment.EAST, spaceBetween, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        } else {
            add(column, columnPlaceholders.get(index - 1), UIAlignment.WEST, UIAlignment.EAST, spaceBetween, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
            
            if (index + 1 < columnPlaceholders.size())
                updateAlignComponent(columnPlaceholders.get(index + 1), column, column);
        }
        
        columns++;
        
        ArrayList<JComponent> columnElements = new ArrayList<>(rows);
        for (int j = 0; j < rows; j++) {
            JComponent c = createCell(index, j, "", newCellComponentType);
            columnElements.add(c);
            add(c, columnPlaceholders.get(index), rowPlaceholders.get(j), UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
        }
        UITable.add(index, columnElements);
        
//        if (allowUserToSetColumnTitle)
//            setColumnType(index, false, TableCellComponent.TEXT_FIELD);
        
        for (int i = 0; i < columns; i++)
            columnPlaceholders.get(i).index = i;
        
        revalidate();
        repaint();
        updateUISize();
    }
   
    /**
     * Appends a new column to the table
     * @param atTheBeginning if false, the new column will be appended at the end
     */
    public void addColumn(boolean atTheBeginning) {
        addColumn(atTheBeginning ? 0 : -1);
    }
    
    /**
     * Removes a column given an index, note that this might do not anything if
     * {@link Table#columns} is less or equals {@link Table#minColumns}
     * @param index the index
     */
    public void delColumn(int index) {
        if (columns <= minColumns)
            return;
        
        if (index == -1)
            index = columns;
        
        verifyRange(index, 0);
        
        UIPlaceholder column = columnPlaceholders.remove(index);
        
        remove(column);
        for (JComponent c : UITable.remove(index))
            remove(c);
        
        if (index - 1 > -1 && index < columnPlaceholders.size())
            updateAlignComponent(columnPlaceholders.get(index), columnPlaceholders.get(index - 1), columnPlaceholders.get(index - 1));
        else if (index - 1 == -1) {
            index = 0;
            remove(columnPlaceholders.get(index));
            
            int xMargin = getRequiredExtraSpace(true);
            int yMargin = getRequiredExtraSpace(false);
            if (allowUserToAddColumns)
                xMargin += spaceBetween / 2;
            if (allowUserSwapColumns)
                yMargin -= swapComponentLength;

            add(columnPlaceholders.get(index), UIAlignment.WEST, UIAlignment.WEST, xMargin, UIAlignment.NORTH, UIAlignment.NORTH, yMargin);
            updateAlignComponent(rowPlaceholders.get(index), columnPlaceholders.get(index), columnPlaceholders.get(index));
        }
        
        columns--;
        for (int i = 0; i < columns; i++)
            columnPlaceholders.get(i).index = i;
        
        revalidate();
        repaint();
        updateUISize();
    }
    
    /**
     * Appends a new row given an index
     * @param index the index
     */
    public void addRow(int index) {
        if (index == -1)
            index = rows;
        
        verifyAppendRange(0, index);
        
        UIPlaceholder row = new UIPlaceholder(false, index);
        
        rowPlaceholders.add(index, row);
        
        if (index == 0) {
            add(row, columnPlaceholders.get(0), UIAlignment.EAST, UIAlignment.WEST, -spaceBetween / 2, UIAlignment.NORTH, UIAlignment.SOUTH, spaceBetween / 2);
            
            remove(rowPlaceholders.get(index + 1));
            add(rowPlaceholders.get(index + 1), row, UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.SOUTH, spaceBetween);
        } else {
            add(row, rowPlaceholders.get(index - 1), UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.NORTH, UIAlignment.SOUTH, spaceBetween);
            
            if (index + 1 < rowPlaceholders.size())
                updateAlignComponent(rowPlaceholders.get(index + 1), row, row);
        }
        
        rows++;
        
        for (int i = 0; i < columns; i++) {
            JComponent c = createCell(i, index, "", newCellComponentType);
            UITable.get(i).add(index, c);
            add(c, columnPlaceholders.get(i), rowPlaceholders.get(index), UIAlignment.HORIZONTAL_CENTER, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.VERTICAL_CENTER, UIAlignment.VERTICAL_CENTER, 0);
            
            if (index + 1 < rows)
                setCellType(i, index + 1, true, getCellType(i, index + 1));
        }
        
        for (int j = 0; j < rows; j++)
            rowPlaceholders.get(j).index = j;
        
        revalidate();
        repaint();
        updateUISize();
    }
    
    /**
     * Appends a new row to the table
     * @param atTheBeginning if false, the new column will be appended at the end
     */
    public void addRow(boolean atTheBeginning) {
        addRow(atTheBeginning ? 0 : -1);
    }
    
    /**
     * Removes a row given an index, note that this might do not anything if
     * {@link Table#rows} is less or equals {@link Table#minRows}
     * @param index the index
     */
    public void delRow(int index) {
        if (rows <= minRows)
            return;
        
        if (index == -1)
            index = rows;
        
        verifyRange(0, index);
        
        UIPlaceholder row = rowPlaceholders.remove(index);
        
        remove(row);
        for (int i = 0; i < columns; i++)
            remove(UITable.get(i).remove(index));
        
        if (index - 1 > -1 && index < rowPlaceholders.size())
            updateAlignComponent(rowPlaceholders.get(index), rowPlaceholders.get(index - 1), rowPlaceholders.get(index - 1));
        else if (index - 1 == -1) {
            index = 0;
            remove(rowPlaceholders.get(index));
            
            add(rowPlaceholders.get(index), columnPlaceholders.get(0), UIAlignment.EAST, UIAlignment.WEST, -spaceBetween / 2, UIAlignment.NORTH, UIAlignment.SOUTH, spaceBetween / 2);
            for (int i = 0; i < columns; i++)
                setCellType(i, index, true, getCellType(i, index));
        }
        
        rows--;
        for (int j = 0; j < rows; j++)
            rowPlaceholders.get(j).index = j;
        
        revalidate();
        repaint();
        updateUISize();
    }
    
    /**
     * Changes the position of a column given a new index and the old index.<br>
     * This might be still buggy.
     * @param index the old index
     * @param newIndex the new index
     */
    public void swapColumn(int index, int newIndex) {
        verifyRange(index, 0);
        verifyRange(newIndex, 0);
        
        if (index == newIndex)
            return;
        
        if (index < newIndex) {
            int tmpIndex = index;
            index = newIndex;
            newIndex = tmpIndex;
        }
        
        UIPlaceholder columnPlaceholder = columnPlaceholders.get(index);
        UIPlaceholder columnPlaceholder1 = columnPlaceholders.get(newIndex);
        ArrayList<JComponent> column = UITable.get(index);
        
        ComponentConstrains cc1 = getComponentConstrains(columnPlaceholder1);
        ComponentConstrains cc = getComponentConstrains(columnPlaceholder);
        
        columnPlaceholders.set(index, columnPlaceholder1);
        UITable.set(index, UITable.get(newIndex));
        if (index + 1 < columns)
            updateAlignComponent(columnPlaceholders.get(index + 1), columnPlaceholder1, columnPlaceholder1);
        
        setComponentConstrains(columnPlaceholder1, cc);
        
        columnPlaceholders.set(newIndex, columnPlaceholder);
        UITable.set(newIndex, column);
        setComponentConstrains(columnPlaceholder, cc1);
        if (newIndex + 1 < columns)
            updateAlignComponent(columnPlaceholders.get(newIndex + 1), columnPlaceholder, columnPlaceholder);
        
        revalidate();
        repaint();
    }
    
    private class UIPlaceholder extends JComponent implements ComponentSetup {
        private int width, height;
        private int index;
        private ColorButton addButton;
        private ColorButton delButton;
        private final boolean columnType;

        private boolean dragging = false;
        private boolean paintAsDraggable = false;
        
        
        public UIPlaceholder(boolean columnType, int index) {
            this.columnType = columnType;
            this.index = index;
            
            initUI();
        }

        @Override
        public final void initUI() {
            width = columnType ? controlButtonWidth * 2 : swapComponentLength;
            height = !columnType ? controlButtonWidth * 2 : swapComponentLength;
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    paintAsDraggable = allowUserSwapColumns && columnType || allowUserSwapRows && !columnType;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (dragging)
                        return;
                    
                    paintAsDraggable = false;
                    repaint();
                }
            });
            
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!allowUserSwapColumns && columnType || !allowUserSwapRows && !columnType)
                        return;
                    
                    dragging = true;
                    
                    Point p = e.getPoint();
                    Dimension preferredSize = getPreferredSize();
                    p.x += getLocation().x - preferredSize.width / 2;
                    p.y += getLocation().y - preferredSize.height / 2;
                    if (columnType)
                        p.y = getLocation().y;
                    else
                        p.x = getLocation().x;
                    
                    setLocation(p);
                    
                    for (int k = 0; k < (columnType ? rows : columns); k++) {
                        JComponent c;
                        if (columnType) {
                            c = UITable.get(index)
                                    .get(k);
                            p.y = rowPlaceholders.get(k).getLocation().y + spaceBetween;
                        } else {
                            c = UITable.get(k)
                                    .get(index);
                            p.x = columnPlaceholders.get(k).getLocation().x + spaceBetween;
                        }
                        c.setLocation(p);
                    }
                    
                    
                    container.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    dragging = false;
                    paintAsDraggable = false;
                    repaint();
                }
            });
            
            addButton = createAndSetupButton(true);
            delButton = createAndSetupButton(false);
            
            if (columnType) {
                container.add(addButton, this, UIAlignment.WEST, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.SOUTH, UIAlignment.NORTH, 0);
                container.add(delButton, this, UIAlignment.EAST, UIAlignment.HORIZONTAL_CENTER, 0, UIAlignment.SOUTH, UIAlignment.NORTH, 0);
            } else {
                container.add(addButton, this, UIAlignment.EAST, UIAlignment.WEST, 0, UIAlignment.NORTH, UIAlignment.VERTICAL_CENTER, 0);
                container.add(delButton, this, UIAlignment.EAST, UIAlignment.WEST, 0, UIAlignment.SOUTH, UIAlignment.VERTICAL_CENTER, 0);
            }
        }

        @Override
        public void updateUISize() {
            setPreferredSize(new Dimension(width, height));
        }

        @Override
        public void updateUIFont() { }

        @Override
        public void updateUITheme() { }

        @Override
        public void updateUIColors() {
            setBackground(UIProperties.APP_BG_COLOR);
        }

        @Override
        public void setUseAppTheme(boolean useAppTheme) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setUseAppColor(boolean useAppColor) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setRoundCorners(boolean roundCorners) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setPaintBorder(boolean paintBorder) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setPreferredSize(Dimension preferredSize) {
            if (columnType && preferredSize.width < controlButtonWidth * 2)
                preferredSize.width = controlButtonWidth * 2;
            if (!columnType && preferredSize.height < controlButtonWidth * 2)
                preferredSize.height = controlButtonWidth * 2;
            
            if (columnType)
                preferredSize.height = allowUserSwapColumns ? swapComponentLength : 0;
            else
                preferredSize.width = allowUserSwapRows ? swapComponentLength : 0;
            
            this.width = preferredSize.width;
            this.height = preferredSize.height;

            preferredSize.width = (int) ((preferredSize.width + (columnType ? controlButtonWidth : 0)) * UIProperties.uiScale);
            preferredSize.height = (int) (preferredSize.height * UIProperties.uiScale);
//            preferredSize.height = (int) ((preferredSize.height + (!columnType ? controlButtonWidth : 0)) * UIProperties.uiScale);

            super.setPreferredSize(preferredSize);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Dimension preferredDimension = getPreferredSize();
            
            g.setColor(getBackground());
            g.fillRect(0, 0, preferredDimension.width, preferredDimension.height);
            
            if (columnType && paintAsDraggable) {
                g.setColor(appTheme ? UIProperties.APP_FG_COLOR : UIProperties.APP_BG);
                
                int halfSwap = swapComponentLength / 2;
                int margin = (int) (preferredDimension.width * 0.2);
                g.drawLine(margin, halfSwap - 2, preferredDimension.width - margin, halfSwap - 2);
                g.drawLine(margin, halfSwap, preferredDimension.width - margin, halfSwap);
                g.drawLine(margin, halfSwap + 2, preferredDimension.width - margin, halfSwap + 2);
            } else if (!columnType && paintAsDraggable) {
                g.setColor(appTheme ? UIProperties.APP_FG_COLOR : UIProperties.APP_BG);
                
                int halfSwap = swapComponentLength / 2;
                int margin = (int) (preferredDimension.height * 0.2);
                g.drawLine(halfSwap - 2, margin, halfSwap - 2, preferredDimension.height - margin);
                g.drawLine(halfSwap, margin, halfSwap, preferredDimension.height - margin);
                g.drawLine(halfSwap + 2, margin, halfSwap + 2, preferredDimension.height - margin);
            }
        }
        
        private ColorButton createAndSetupButton(boolean add) {
            String text = add ? "+" : "-";
            ColorButton button = new ColorButton();
            button.setPreferredSize(new Dimension(controlButtonWidth, controlButtonWidth));
            button.setRoundCorners(false);
            button.setPaint(false);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (allowUserToAddColumns && columnType && (index >= minColumns || add))
                        button.setText(text);
                    if (allowUserToAddRows && !columnType && (index >= minRows || add))
                        button.setText(text);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setText("");
                }
            });
            
            if (columnType)
                button.addActionListener((Action) -> {
                    if (allowUserToAddColumns && add) {
                        appendIndex = index;
                        columnContextMenu.show(getX(), getY());
                    } else if (allowUserToAddColumns && !add)
                        delColumn(index);
                });
            else
                button.addActionListener((Action) -> {
                    if (allowUserToAddRows && add) {
                        appendIndex = index;
                        rowContextMenu.show(getX(), getY());
                    } else if (allowUserToAddRows && !add)
                        delRow(index);
                });

            return button;
        }
    }
}
