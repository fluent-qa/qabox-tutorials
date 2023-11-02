package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.table.JBTable;
import io.fluentqa.qaplugins.excelreader.message.ComponentBundle;
import io.fluentqa.qaplugins.excelreader.ui.components.SearchResult;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class MyNextOccurrenceAction extends MyPrevNextOccurrenceAction {

    public MyNextOccurrenceAction(JBTable table, JLabel label, SearchResult searchResult) {
        super(ComponentBundle.message("home.toolbar.next"),
                AllIcons.Actions.NextOccurence,
                table, label, searchResult);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        findDifferenceCell(true);
    }

    @Override
    public void setTable(JBTable table) {
        super.setTable(table);
    }
}
