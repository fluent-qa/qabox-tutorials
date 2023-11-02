package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.find.FindBundle;
import com.intellij.find.editorHeaderActions.Embeddable;
import com.intellij.find.impl.RegExHelpPopup;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Toggleable;
import com.intellij.openapi.actionSystem.ex.TooltipDescriptionProvider;
import com.intellij.openapi.actionSystem.ex.TooltipLinkProvider;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class MyToggleRegexAction extends TableHeaderToggleAction implements Embeddable, TooltipLinkProvider,
        TooltipDescriptionProvider {

    private boolean selected = false;
    private final ExcelReaderMainPanel panel;

    public MyToggleRegexAction(ExcelReaderMainPanel panel) {
        super(FindBundle.message("find.regex"),
                AllIcons.Actions.Regex,
                AllIcons.Actions.RegexHovered,
                AllIcons.Actions.RegexSelected);
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
        boolean wordsSelected = panel.getMyToggleWordsAction().isSelected();
        e.getPresentation().setEnabled(!wordsSelected);
        super.update(e);
    }

    @Override
    public @Nullable TooltipLinkProvider.TooltipLink getTooltipLink(@Nullable JComponent owner) {
        return new TooltipLink(FindBundle.message("find.regex.help.link"), RegExHelpPopup.createRegExLinkRunnable(owner));
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
