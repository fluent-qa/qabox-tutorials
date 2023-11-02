package io.fluentqa.qaplugins.excelreader.ui.vcs.listener;


import io.fluentqa.qaplugins.excelreader.ui.vcs.DiffTablePanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author piercebrands
 * @since 2022.2.1
 */
public class MyListSelectionDiffListener implements ListSelectionListener {

    private final JTable contentTableL;
    private final JTable contentTableR;

    public MyListSelectionDiffListener(DiffTablePanel leftPanel,
                                       DiffTablePanel rightPanel) {
        contentTableL = leftPanel.getTablePanel().getDataTable();
        contentTableR = rightPanel.getTablePanel().getDataTable();

        contentTableL.getSelectionModel().addListSelectionListener(this);
        contentTableR.getSelectionModel().addListSelectionListener(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int[] selectedRows;
        if (e.getSource() == contentTableL.getSelectionModel()) {
            selectedRows = contentTableL.getSelectedRows();
            refreshSelections(selectedRows, contentTableR);
        } else if (e.getSource() == contentTableR.getSelectionModel()) {
            selectedRows = contentTableR.getSelectedRows();
            refreshSelections(selectedRows, contentTableL);
        }
    }

    private void refreshSelections(int[] selectedRows, JTable table) {
        if (selectedRows == null || selectedRows.length == 0) {
            return;
        }
        table.getSelectionModel().removeListSelectionListener(this);
        table.clearSelection();
        for (int i : selectedRows) {
            if (i < table.getRowCount()) {
                table.addRowSelectionInterval(i, i);
            }
        }
        table.getSelectionModel().addListSelectionListener(this);
    }
}
