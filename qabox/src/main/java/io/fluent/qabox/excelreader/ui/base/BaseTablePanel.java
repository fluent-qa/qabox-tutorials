package io.fluentqa.qaplugins.excelreader.ui.base;


import com.intellij.openapi.Disposable;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import io.fluentqa.qaplugins.excelreader.ui.components.MyTableModel;
import io.fluentqa.qaplugins.excelreader.util.SwingUtil;
import io.fluentqa.qaplugins.excelreader.util.TableUtil;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author piercebrands
 * @since 2022.2.1
 */
public class BaseTablePanel implements TableModelListener, Disposable {

    private final JBTable dataTable;
    private final MyTableModel tableModel;
    private final JBScrollPane scrollPane;

    public BaseTablePanel() {
        tableModel = new MyTableModel();
        dataTable = new JBTable(tableModel);
        dataTable.putClientProperty("terminateEditOnFocusLost", true);
        tableModel.addTableModelListener(this);
        scrollPane = new JBScrollPane(dataTable);
        TableUtil.setTableProperty(dataTable);
        SwingUtil.setJspStyle(scrollPane);
        TableUtil.resetToDefaultStyle(dataTable);
    }

    public JBTable getDataTable() {
        return dataTable;
    }

    public JBScrollPane getContent() {
        return scrollPane;
    }

    public MyTableModel getTableModel() {
        return tableModel;
    }

    @Override
    public void tableChanged(TableModelEvent e) {

    }

    @Override
    public void dispose() {
        tableModel.setRowCount(0);
        scrollPane.removeAll();
    }
}
