package io.fluentqa.qaplugins.excelreader.excel;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.UIUtil;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;
import io.fluentqa.qaplugins.excelreader.ui.actions.*;
import io.fluentqa.qaplugins.excelreader.ui.components.*;
import io.fluentqa.qaplugins.excelreader.util.*;

import javax.swing.*;
import javax.swing.table.TableRowSorter;

/**
 * @author piercebrands
 * @since 2.1.1
 */
public class DataFilter {

    private static final DataFilter INSTANCE = new DataFilter();

    private FilterModel filterModel;
    private MatchModel matchModel;
    private int columnIndex;
    private ExcelReaderMainPanel mainPanel;

    private DataFilter() {

    }

    public void setMainPanel(ExcelReaderMainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public static DataFilter getInstance() {
        return INSTANCE;
    }

    public void setModel(FilterModel filterModel,
                         MatchModel matchModel) {
        this.filterModel = filterModel;
        this.matchModel = matchModel;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void searchInCsv(String insertValue, JBTable dataTable) {
        searchInTable(dataTable, insertValue);
    }

    public void searchInXlsOrXlsx(String insertValue, JBTabbedPane sheetPane) {
        JBScrollPane scrollPane = (JBScrollPane) sheetPane.
                getSelectedComponent();
        if (scrollPane == null) {
            return;
        }
        JBTable table = (JBTable) scrollPane.getViewport().getComponent(0);
        searchInTable(table, insertValue);
    }

    private void searchInTable(JBTable table, String insertValue) {
        MyStatusTextAction statusTextAction = mainPanel.getStatusTextAction();
        if (StrUtil.isEmpty(insertValue)) {
            TableUtil.resetToDefaultStyle(table);
            statusTextAction.setText("0 results");
            statusTextAction.setColor(JBColor.BLACK);
            table.setRowSorter(null);
            return;
        }

        // The SQL Model only show the matched rows, looks like row filter
        if (mainPanel.getMatchModel() == MatchModel.SQL) {
            if (StrUtil.isEmpty(insertValue)) {
                return;
            }
            MyTableModel model = (MyTableModel)table.getModel();
            TableRowSorter<MyTableModel> sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
            RowFilter<MyTableModel, Integer> searchFilter =
                    new RowFilter<>() {
                        @Override
                        public boolean include(Entry<? extends MyTableModel,
                                ? extends Integer> entry) {
                            MyTableModel model = entry.getModel();
                            int index = entry.getIdentifier().intValue();
                            return model.containsRow(index, insertValue,
                                    mainPanel.getSqlPopup(),
                                    mainPanel.getStatusTextAction());
                        }
                    };
            sorter.setRowFilter(searchFilter);
            int rowCount = table.getRowCount();
            mainPanel.getStatusTextAction().setText(rowCount +
                    (rowCount > 1 ? " rows" : " row"));
            mainPanel.getStatusTextAction().setColor(UIUtil.getLabelForeground());
            return;
        }
        table.setRowSorter(null);
        // Set the searchModel
        setModel(mainPanel.getFilterModel(), mainPanel.getMatchModel());
        // Set the columnIndex
        if (mainPanel.getFilterModel() == FilterModel.DEFINED_COLUMN) {
            MyTableModel model = (MyTableModel) table.getModel();
            int columnIndex = model.getColumnIndex(mainPanel.getFilterAction().selectedValue());
            DataFilter.getInstance().setColumnIndex(columnIndex);
        }

        // Highlight the value
        TableUtil.setSearchColor(table, insertValue);
        // Toolbar status value
        dealSearchResult(table, insertValue);

        SearchResult searchResult = mainPanel.getSearchResult();
        if (searchResult.isEmpty()) {
            statusTextAction.setText("0 results");
            statusTextAction.setColor(UIUtil.getErrorForeground());
        } else {
            statusTextAction.setText(searchResult.getSize() + " results");
            statusTextAction.setColor(UIUtil.getLabelForeground());
        }
        table.updateUI();
    }

    private void dealSearchResult(JBTable table, String findStr) {
        SearchResult searchResult = mainPanel.getSearchResult();
        MyTableModel model = (MyTableModel) table.getModel();
        int rows = model.getRowCount();
        int columns = model.getColumnCount();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Object valueAt = model.getValueAt(i, j);
                if (valueAt == null || StrUtil.isEmpty(valueAt.toString()) || StrUtil.isEmpty(findStr)) {
                    continue;
                }
                boolean matched = dealSearchModel(findStr, valueAt.toString(), j);
                if (matched) {
                    searchResult.add(new SearchResult.Cell(i, j, SearchResult.Cell.DiffType.UN_KNOW));
                }
            }
        }
    }

    /**
     * The filter model of match
     *
     * @see MyFilterAction
     */
    public enum FilterModel {
        // Search in the all table
        ANYWHERE,

        // Search in the defined column
        DEFINED_COLUMN
    }

    /**
     * The match rule
     *
     * @see MyToggleMatchCaseAction
     * @see MyToggleWordsAction
     * @see MyToggleRegexAction
     */
    public enum MatchModel {
        // Default match rule
        NONE,

        // Match the case
        MATCH_CASE,

        // Whole words
        WORDS,

        // Regex model
        REGEX,

        // Match the case and words
        MATCH_CASE_WORDS,

        // Match the case and regex
        MATCH_CASE_REGEX,

        // SQL Model
        SQL
    }

    /**
     * Match value in multi condition
     *
     * @param findStr     insert string
     * @param valueString cell value
     * @param valueColumn cell column
     * @return boolean
     */
    public boolean dealSearchModel(String findStr, String valueString, int valueColumn) {
        boolean matchResult = false;
        if (StrUtil.isEmpty(valueString) || StrUtil.isEmpty(findStr)) {
            return false;
        }
        switch (matchModel) {
            case NONE:
                matchResult = valueString.toLowerCase().contains(findStr) ||
                        valueString.toUpperCase().contains(findStr) ||
                        valueString.contains(findStr.toLowerCase()) ||
                        valueString.contains(findStr.toUpperCase()) ||
                        valueString.toLowerCase().contains(findStr.toLowerCase()) ||
                        valueString.toUpperCase().contains(findStr.toUpperCase());
                if (filterModel == FilterModel.DEFINED_COLUMN) {
                    matchResult = (valueColumn == columnIndex) && matchResult;
                }
                break;
            case REGEX:
                try {
                    matchResult = ReUtil.contains("(?i)" + findStr, valueString);
                    if (filterModel == FilterModel.DEFINED_COLUMN) {
                        matchResult = (valueColumn == columnIndex) && matchResult;
                    }
                } catch (Exception e) {
                    MyStatusTextAction statusTextAction = mainPanel
                            .getStatusTextAction();
                    statusTextAction.setText("Bad pattern");
                    statusTextAction.setColor(UIUtil.getErrorForeground());
                }
                break;
            case WORDS:
                matchResult = valueString.toLowerCase().equals(findStr) ||
                        valueString.toUpperCase().equals(findStr) ||
                        valueString.equals(findStr.toLowerCase()) ||
                        valueString.equals(findStr.toUpperCase()) ||
                        valueString.equalsIgnoreCase(findStr);
                if (filterModel == FilterModel.DEFINED_COLUMN) {
                    matchResult = (valueColumn == columnIndex) && matchResult;
                }
                break;
            case MATCH_CASE:
                matchResult = valueString.contains(findStr);
                if (filterModel == FilterModel.DEFINED_COLUMN) {
                    matchResult = (valueColumn == columnIndex) && matchResult;
                }
                break;
            case MATCH_CASE_REGEX:
                try {
                    findStr = findStr.replace("(?i)", "");
                    matchResult = ReUtil.contains(findStr, valueString.toLowerCase()) ||
                            ReUtil.contains(findStr, valueString.toLowerCase());
                    if (filterModel == FilterModel.DEFINED_COLUMN) {
                        matchResult = (valueColumn == columnIndex) && matchResult;
                    }
                } catch (Exception e) {
                    MyStatusTextAction statusTextAction = mainPanel
                            .getStatusTextAction();
                    statusTextAction.setText("Bad pattern");
                    statusTextAction.setColor(UIUtil.getErrorForeground());
                }
                break;
            case MATCH_CASE_WORDS:
                matchResult = valueString.equals(findStr);
                if (filterModel == FilterModel.DEFINED_COLUMN) {
                    matchResult = (valueColumn == columnIndex) && matchResult;
                }
                break;
            default:
                break;
        }
        return matchResult;
    }
}
