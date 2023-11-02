package io.fluentqa.qaplugins.excelreader.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.*;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static io.fluentqa.qaplugins.excelreader.ui.ExcelReaderToolWindowFactory.TOOL_WINDOW_ID;


/**
 * @author piercebrands
 * @since 1.0.1
 */
public class ExcelReaderView {

    private static final Logger LOG = Logger.getInstance(ExcelReaderView.class);

    private ToolWindow myToolWindow;
    private final Project myProject;
    private final DefaultExcelReaderRunnerFactory runnerFactory;

    public ExcelReaderView(@NotNull Project project) {
        myProject = project;
        runnerFactory = ApplicationManager.getApplication()
                .getService(DefaultExcelReaderRunnerFactory.class);
        runnerFactory.create(project);
    }

    public static ExcelReaderView getInstance(@NotNull Project project) {
        return project.getService(ExcelReaderView.class);
    }

    public void initToolWindow(ToolWindowEx toolWindow) {
        if (myToolWindow != null) {
            LOG.error("ExcelReader tool window already initialized");
            return;
        }
        myToolWindow = toolWindow;
        runnerFactory.initToolWindow(toolWindow);
    }

    public ExcelReaderWidget refreshData(@NotNull ExcelReaderTabState tabState, @NotNull VirtualFile virtualFile) {
        ToolWindow toolWindow = getOrInitToolWindow();
        toolWindow.getContentManager().removeAllContents(true);
        @Nullable
        ExcelReaderWidget terminalWidget = new ExcelReaderWidget(myProject, virtualFile);
        Content content = createContent(tabState, terminalWidget);
        toolWindow.getContentManager().addContent(content);
        toolWindow.show();
        return Objects.requireNonNull(terminalWidget);
    }

    public @NotNull ToolWindow getOrInitToolWindow() {
        ToolWindow toolWindow = myToolWindow;
        if (toolWindow == null) {
            toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow(TOOL_WINDOW_ID);
            // to call #initToolWindow
            Objects.requireNonNull(toolWindow).getContentManager();
            LOG.assertTrue(toolWindow == myToolWindow);
        }
        return toolWindow;
    }

    @NotNull
    private Content createContent(@NotNull ExcelReaderTabState tabState,
                                  @NotNull ExcelReaderWidget terminalWidget) {

        String tabName = ObjectUtils.notNull(tabState.myTabName,
                "New File");

        return ContentFactory.SERVICE.getInstance().
                createContent(terminalWidget.getComponent(),
                        tabName, false);
    }
}

