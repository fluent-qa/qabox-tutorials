package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.ui.table.JBTable;
import io.fluentqa.qaplugins.excelreader.ui.components.SearchResult;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class MyPrevNextOccurrenceAction extends DumbAwareAction {

    public JBTable table;
    public SearchResult searchResult;
    public JLabel label;
    public static int currentIndex = -1;

    public MyPrevNextOccurrenceAction(String text, Icon icon, JBTable table,
                                      JLabel label,
                                      SearchResult searchResult) {
        super(() -> text, icon);
        this.table = table;
        this.label = label;
        this.searchResult = searchResult;
    }

    public void findDifferenceCell(boolean isNext) {
        if (searchResult == null || searchResult.isEmpty() || table == null) {
            return;
        }
        int maxIndex = searchResult.getSize();
        if (maxIndex == 0) {
            return;
        }
        if (isNext) {
            currentIndex = Math.min(currentIndex + 1, maxIndex);
            if (currentIndex == maxIndex) {
                currentIndex = maxIndex - 1;
            }
        } else {
            currentIndex = Math.max(currentIndex - 1, 0);
        }

        SearchResult.Cell cell = searchResult.getIndexCell(currentIndex);
        if (cell == null) {
            return;
        }

        table.setRowSelectionInterval(cell.getRow(), cell.getRow());
        table.setColumnSelectionInterval(cell.getColumn(), cell.getColumn());
        Rectangle cellRect = table.getCellRect(cell.getRow(), cell.getColumn(), true);
        table.scrollRectToVisible(cellRect);
        label.setText((currentIndex + 1) + "/" + maxIndex);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }

    public void setTable(JBTable table) {
        this.table = table;
    }
}
