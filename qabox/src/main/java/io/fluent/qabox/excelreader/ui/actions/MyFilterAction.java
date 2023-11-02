package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.JBPopupMenu;
import io.fluentqa.qaplugins.excelreader.message.ComponentBundle;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;


public class MyFilterAction extends DumbAwareAction {

    public Vector<String> columns;
    private JCheckBoxMenuItem anywhere;
    private List<JCheckBoxMenuItem> columnItems;
    private JPopupMenu popup;
    private final ExcelReaderMainPanel panel;
    private final MyAnywhereListener anywhereListener = new MyAnywhereListener();
    private final MyColumnItemListener columnItemListener = new MyColumnItemListener();

    public MyFilterAction(Icon icon, ExcelReaderMainPanel panel) {
        super(() -> ComponentBundle.message("home.toolbar.filter"), icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Component component = e.getInputEvent().getComponent();
        // first times is null
        if (popup == null) {
            popup = new JBPopupMenu();
            anywhere = new JCheckBoxMenuItem("Anywhere", true);
            anywhere.addItemListener(anywhereListener);
            popup.setAutoscrolls(true);
            popup.add(anywhere);
        }
        if (columns != null && !columns.isEmpty() && columnItems == null) {
            popup.add(new JSeparator());
            columnItems = new ArrayList<>();
            for (String col : columns) {
                JCheckBoxMenuItem tmp = new JCheckBoxMenuItem(col);
                columnItems.add(tmp);
                tmp.addItemListener(columnItemListener);
                popup.add(tmp);
            }
        }
        popup.show(component, component.getWidth(), component.getHeight());
    }

    public Vector<String> getColumns() {
        return columns;
    }

    public void setColumns(Vector<String> columns) {
        this.columns = columns;
        if (columnItems != null) {
            columnItems.clear();
        }
    }

    private void clearSelections() {
        removeColumnItemsListener();
        for (JCheckBoxMenuItem item : columnItems) {
            item.setSelected(false);
        }
        addColumnItemsListener();
    }

    private void selectedAnyWhere(boolean selected) {
        removeAnywhereListener();
        anywhere.setSelected(selected);
        addAnywhereListener();
    }

    private void removeAnywhereListener() {
        anywhere.removeItemListener(anywhereListener);
    }

    private void addAnywhereListener() {
        anywhere.addItemListener(anywhereListener);
    }

    private void removeColumnItemsListener() {
        for (JCheckBoxMenuItem item : columnItems) {
            item.removeItemListener(columnItemListener);
        }
    }

    private void addColumnItemsListener() {
        for (JCheckBoxMenuItem item : columnItems) {
            item.addItemListener(columnItemListener);
        }
    }

    public boolean isAnywhere() {
        return anywhere == null || anywhere.isSelected();
    }

    public String selectedValue() {
        for (JCheckBoxMenuItem item : columnItems) {
            if (item.isSelected()) {
                return item.getText();
            }
        }
        return null;
    }

    private class MyAnywhereListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // clear the columnItems selected status
                clearSelections();

                // set anywhere selected
                selectedAnyWhere(true);
                popup.updateUI();

                // refresh the table
                String text = panel.getSrc().getSearchTextComponent().getText();
                panel.search(text);
            }
        }
    }

    private class MyColumnItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // clear the columnItems selected status
                clearSelections();

                // clear the anywhere selected status
                selectedAnyWhere(false);

                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                item.removeItemListener(columnItemListener);
                item.setSelected(true);
                item.addItemListener(columnItemListener);
                popup.updateUI();

                // refresh the table
                String text = panel.getSrc().getSearchTextComponent().getText();
                panel.search(text);
            }
        }
    }
}
