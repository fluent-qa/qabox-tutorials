package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.find.FindBundle;
import com.intellij.find.editorHeaderActions.Embeddable;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.actionSystem.ex.TooltipDescriptionProvider;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;
import org.jetbrains.annotations.NotNull;

/**
 * @author piercebrands
 * @since 2.1.1
 */
public class MyToggleWordsAction extends TableHeaderToggleAction implements Embeddable,
        TooltipDescriptionProvider {

    private boolean selected = false;
    private final ExcelReaderMainPanel panel;

    public MyToggleWordsAction(ExcelReaderMainPanel panel) {
        super(FindBundle.message("find.whole.words"),
                AllIcons.Actions.Words,
                AllIcons.Actions.WordsHovered,
                AllIcons.Actions.WordsSelected);
        this.panel = panel;
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
    public void update(@NotNull AnActionEvent e) {
        boolean sqlSelected = panel.getMyToggleSqlAction().isSelected();
        if (sqlSelected) {
            e.getPresentation().setEnabled(false);
            return;
        }
        boolean regexSelected = panel.getMyToggleRegex().isSelected();
        e.getPresentation().setEnabled(!regexSelected);
        super.update(e);
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        selected = state;
        panel.refreshStatusText();
        panel.getSrc().updateActions();
        String text = panel.getSrc().getSearchTextComponent().getText();
        panel.search(text);
    }
}
