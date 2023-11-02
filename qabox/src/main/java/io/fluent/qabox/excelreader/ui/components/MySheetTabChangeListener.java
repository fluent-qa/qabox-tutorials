package io.fluentqa.qaplugins.excelreader.ui.components;


import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.table.JBTable;
import io.fluentqa.qaplugins.excelreader.excel.DataLoader;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MySheetTabChangeListener implements ChangeListener {

    private final ExcelReaderMainPanel panel;
    public MySheetTabChangeListener(ExcelReaderMainPanel panel) {
        this.panel = panel;
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        JBTabbedPane sheetPane = (JBTabbedPane) e.getSource();
        changeTab(sheetPane);
    }

    public void changeTab(JBTabbedPane sheetPane) {
        int selectedIndex = sheetPane.getSelectedIndex();
        if (selectedIndex < 0) {
            return;
        }
        JBScrollPane scrollPane = (JBScrollPane) sheetPane.getSelectedComponent();
        JViewport viewport = scrollPane.getViewport();
        JBTable table = (JBTable) viewport.getView();
        panel.refreshActiveTable(table);
        DataLoader.getInstance().loadSheet(selectedIndex, table, panel.getVirtualFile());
    }
}
