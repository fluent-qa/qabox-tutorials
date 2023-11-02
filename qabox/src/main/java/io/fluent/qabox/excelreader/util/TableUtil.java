package io.fluentqa.qaplugins.excelreader.util;

import cn.hutool.core.util.StrUtil;
import com.intellij.openapi.diff.DiffColors;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.UIUtil;
import io.fluentqa.qaplugins.excelreader.excel.DataFilter;
import io.fluentqa.qaplugins.excelreader.ui.components.SearchResult;
import io.fluentqa.qaplugins.excelreader.ui.vcs.DiffResult;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author piercebrands
 * @since 1.0.0
 */
public class TableUtil {

    public static void setSearchColor(JBTable table, String findStr) {
        MySearchTableCellRenderer tcr = new MySearchTableCellRenderer(findStr);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
        }
    }

    public static void resetToDefaultStyle(JBTable table) {
        // set bg for selected item
        table.setSelectionBackground(UIUtil.getTableSelectionBackground(true));
        table.setSelectionForeground(UIUtil.getTableSelectionForeground(true));
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        MyDefaultTableCellRenderer r = new MyDefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.LEFT);
        table.setDefaultRenderer(Object.class, r);
        setDefaultColor(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public static void setDefaultColor(JTable table) {
        MyDefaultTableCellRenderer tcr = new MyDefaultTableCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
        }
    }

    public static void setDiffColor(JTable table) {
        MyDiffTableCellRenderer tcr = new MyDiffTableCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
        }
    }

    public static void setTableProperty(JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(21);
        table.setCellSelectionEnabled(true);
        table.getTableHeader().setBackground(JBColor.background());
        table.getTableHeader().setForeground(JBColor.foreground());
    }

    public static void setDiffTableProperty(JTable table) {
        setTableProperty(table);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public static class MyDefaultTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color tableBackground = UIUtil.getTableBackground(isSelected, true);
            Color tableForeground = UIUtil.getTableForeground(isSelected, true);
            setBackground(tableBackground);
            setForeground(tableForeground);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        @Override
        public void setHorizontalAlignment(int alignment) {
            super.setHorizontalAlignment(alignment);
        }
    }

    public static class MyDiffTableCellRenderer extends DefaultTableCellRenderer {

        private final EditorColorsScheme schemeForCurrentUITheme = EditorColorsManager.getInstance().getSchemeForCurrentUITheme();
        private final TextAttributes diffAttributesModified = schemeForCurrentUITheme.getAttributes(DiffColors.DIFF_MODIFIED);
        private final TextAttributes diffAttributesInserted = schemeForCurrentUITheme.getAttributes(DiffColors.DIFF_INSERTED);
        private final Color MODIFIED_BG = diffAttributesModified.getBackgroundColor();
        private final Color INSERTED_BG = diffAttributesInserted.getBackgroundColor();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            DiffResult instance = DiffResult.getInstance();
            Color tableBackground = UIUtil.getTableBackground(isSelected, true);
            Color tableForeground = UIUtil.getTableForeground(isSelected, true);
            SearchResult.Cell cell = instance.containsCell(row, column);
            if (cell != null){
                if (cell.getDiffType() == SearchResult.Cell.DiffType.INSERTED){
                    setBackground(INSERTED_BG);
                    setForeground(tableForeground);
                } else if (cell.getDiffType() == SearchResult.Cell.DiffType.MODIFIED) {
                    setBackground(MODIFIED_BG);
                    setForeground(tableForeground);
                } else {
                    setBackground(tableBackground);
                    setForeground(tableForeground);
                }
            } else {
                setBackground(tableBackground);
                setForeground(tableForeground);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    public static class MySearchTableCellRenderer extends DefaultTableCellRenderer {
        private final EditorColorsScheme schemeForCurrentUITheme = EditorColorsManager.getInstance().getSchemeForCurrentUITheme();
        private final TextAttributes editorAttributes = schemeForCurrentUITheme.getAttributes(EditorColors.TEXT_SEARCH_RESULT_ATTRIBUTES);
        private final Color SEARCH_BG = editorAttributes.getBackgroundColor();

        private final String findStr;

        public MySearchTableCellRenderer(String findStr) {
            this.findStr = findStr;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color tableBackground = UIUtil.getTableBackground(isSelected, true);
            Color tableForeground = UIUtil.getTableForeground(isSelected, true);
            if (value == null) {
                setForeground(tableForeground);
                setBackground(tableBackground);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
            String valueString = value.toString();
            boolean notEmptyFindStr = StrUtil.isNotEmpty(findStr);
            boolean containsFindStr = DataFilter.getInstance().
                    dealSearchModel(findStr, valueString, column);
            setForeground(tableForeground);
            if (notEmptyFindStr && containsFindStr) {
                setBackground(SEARCH_BG);
            } else {
                setBackground(tableBackground);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

    }
}
