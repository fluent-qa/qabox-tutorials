package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import io.fluentqa.qaplugins.excelreader.message.ComponentBundle;
import io.fluentqa.qaplugins.excelreader.ui.components.MyCellDialog;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static io.fluentqa.qaplugins.excelreader.util.Constant.GO_TO_CELL_SPLIT_MARK;


public class MyGotoCellAction extends DumbAwareAction {

    public JBTable table;
    public MyCellDialog cellDialog;

    public MyGotoCellAction(Icon icon, JBTable table, MyCellDialog cellDialog) {
        super(() -> ComponentBundle.message("home.toolbar.jump"), icon);
        this.table = table;
        this.cellDialog = cellDialog;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        cellDialog.refreshCell();
        cellDialog.setVisible(true);
        JBTextField textField = cellDialog.getTextField();
        String text = textField.getText();
        if (!text.contains(GO_TO_CELL_SPLIT_MARK)) {
            text += ":1";
        } else if (text.endsWith(GO_TO_CELL_SPLIT_MARK)) {
            text += "1";
        }
        String[] cell = text.split(":");
        int x = Integer.parseInt(cell[0]);
        int y = Integer.parseInt(cell[1]);

        x -= 1;
        y -= 1;

        x = Math.min(x, table.getModel().getRowCount());
        y = Math.min(y, table.getModel().getColumnCount());

        table.setRowSelectionInterval(x, x);
        table.setColumnSelectionInterval(y, y);
        Rectangle cellRect = table.getCellRect(x, y, true);
        table.scrollRectToVisible(cellRect);
    }

    public void setTable(JBTable table) {
        this.table = table;
    }
}
