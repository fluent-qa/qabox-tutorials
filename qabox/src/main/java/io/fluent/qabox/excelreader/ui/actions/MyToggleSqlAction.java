package io.fluentqa.qaplugins.excelreader.ui.actions;


import com.intellij.find.FindBundle;
import com.intellij.find.editorHeaderActions.Embeddable;
import com.intellij.find.impl.RegExHelpPopup;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.TooltipDescriptionProvider;
import com.intellij.openapi.actionSystem.ex.TooltipLinkProvider;
import io.fluentqa.qaplugins.excelreader.icons.ExcelReaderIcons;
import io.fluentqa.qaplugins.excelreader.message.ComponentBundle;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author piercebrands
 * @since 2.1.1
 */
public class MyToggleSqlAction extends TableHeaderToggleAction implements Embeddable , TooltipLinkProvider,
        TooltipDescriptionProvider {

    private boolean selected = false;
    private final ExcelReaderMainPanel panel;

    public MyToggleSqlAction(ExcelReaderMainPanel panel) {
        super(ComponentBundle.message("home.toolbar.find.sql"),
                ExcelReaderIcons.SQL,
                ExcelReaderIcons.SQL_HOVERED,
                ExcelReaderIcons.SQL_SELECTED);
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

    @Override
    public @Nullable TooltipLink getTooltipLink(@Nullable JComponent owner) {
        return new TooltipLink(ComponentBundle.message("home.toolbar.find.sql.usage"),
                () -> BrowserUtil.browse("https://docs.obiscr.com/article/ER-2021.3.2"));
    }
}
