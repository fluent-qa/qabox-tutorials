package io.fluentqa.qaplugins.excelreader.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import org.jetbrains.annotations.NotNull;

/**
 * @author piercebrands
 * @since 1.0.1
 */
public class DefaultExcelReaderRunnerFactory {

    public void create(@NotNull Project project) {

    }

    public void initToolWindow(@NotNull ToolWindowEx toolWindow) {
        toolWindow.setAutoHide(false);
        toolWindow.setToHideOnEmptyContent(true);
    }
}
