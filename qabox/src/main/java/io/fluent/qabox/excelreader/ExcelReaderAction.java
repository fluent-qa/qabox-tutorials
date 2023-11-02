package io.fluentqa.qaplugins.excelreader;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;

import com.intellij.openapi.vfs.VirtualFile;
import io.fluentqa.qaplugins.excelreader.excel.DataLoader;
import io.fluentqa.qaplugins.excelreader.util.AnActionEventUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author piercebrands
 * @since 2.1.1
 */
public class ExcelReaderAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final VirtualFile data = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (project == null || data == null) {
            return;
        }
        DataLoader.getInstance().load(data, project);
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(AnActionEventUtil.available(e));
    }
}
