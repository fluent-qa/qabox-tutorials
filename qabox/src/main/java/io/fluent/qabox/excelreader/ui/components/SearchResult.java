package io.fluentqa.qaplugins.excelreader.ui.components;


import java.util.LinkedList;
import java.util.List;

/**
 * @author piercebrands
 * @since 2.1.1
 */
public class SearchResult {
    private final List<Cell> cellsList = new LinkedList<>();

    public int getSize() {
        return cellsList.size();
    }

    public List<Cell> add(Cell cell) {
        cellsList.add(cell);
        return cellsList;
    }

    public void clear() {
        cellsList.clear();
    }

    public boolean isEmpty() {
        return cellsList.isEmpty();
    }

    public int getCellIndex(Cell cell) {
        for (int i = 0; i < cellsList.size(); i++) {
            if (cell.row == cellsList.get(i).row &&
                    cell.column == cellsList.get(i).column) {
                return i;
            }
        }
        return -1;
    }

    public Cell getIndexCell(int index) {
        return cellsList.get(index);
    }

    public static class Cell {
        private final int row;
        private final int column;
        private final DiffType diffType;

        public Cell(int row, int column, DiffType diffType) {
            this.row = row;
            this.column = column;
            this.diffType = diffType;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public DiffType getDiffType() {
            return diffType;
        }

        public enum DiffType {
            /**
             * modified cell
             */
            MODIFIED,

            /**
             * inserted cell
             */
            INSERTED,

            /**
             * common fill
             */
            UN_KNOW,
        }
    }
}
