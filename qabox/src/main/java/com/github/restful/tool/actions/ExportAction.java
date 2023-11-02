package com.github.restful.tool.actions;

import com.github.restful.tool.utils.data.Bundle;
import com.github.restful.tool.view.window.WindowFactory;
import com.github.restful.tool.view.window.frame.Window;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class ExportAction extends DumbAwareAction {

    public ExportAction() {
        getTemplatePresentation().setText(Bundle.getString("action.Export.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.Download);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Window toolWindow = WindowFactory.getToolWindow(e.getProject());
        if (toolWindow == null) {
            return;
        }
        toolWindow.export();
    }
}