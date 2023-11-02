package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.find.FindBundle;
import com.intellij.find.editorHeaderActions.Embeddable;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.TooltipDescriptionProvider;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;
import org.jetbrains.annotations.NotNull;

public class MyToggleMatchCaseAction extends TableHeaderToggleAction implements Embeddable,
        TooltipDescriptionProvider {

    private boolean selected = false;
    private final ExcelReaderMainPanel panel;

    public MyToggleMatchCaseAction(ExcelReaderMainPanel panel) {
        super(FindBundle.message("find.case.sensitive"),
                AllIcons.Actions.MatchCase,
                AllIcons.Actions.MatchCaseHovered,
                AllIcons.Actions.MatchCaseSelected);
        this.panel = panel;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(!panel.
                getMyToggleSqlAction().isSelected());
        super.update(e);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return selected;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        selected = state;
        panel.refreshStatusText();
        String text = panel.getSrc().getSearchTextComponent().getText();
        panel.search(text);
    }

}
