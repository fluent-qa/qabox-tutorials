package io.fluentqa.qaplugins.excelreader.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.components.JBPanelWithEmptyText;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author piercebrands
 * @since 2.0.0
 */
public class ExcelReaderToolWindowFactory implements ToolWindowFactory, DumbAware {

    @NonNls
    public static final String TOOL_WINDOW_ID = "ExcelReader";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }
        JBPanelWithEmptyText myMessagePanel = new JBPanelWithEmptyText()
                .withEmptyText(getToolTipText());
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myMessagePanel, "Usage", false);
        toolWindow.getContentManager().addContent(content);
        ExcelReaderView excelReaderView = ExcelReaderView.getInstance(project);
        excelReaderView.initToolWindow((ToolWindowEx) toolWindow);
    }

    public String getToolTipText() {
        return "Select a excel file(csv,xlx,xlsx), Press " +
                " Command Shift D or " + " Ctrl Shift D ";
    }
}
