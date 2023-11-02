package io.fluentqa.qaplugins.excelreader.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.JBSwingUtilities;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Tool Window Object
 *
 * @author piercebrands
 * @since 2.1.1
 */
public class ExcelReaderWidget extends JPanel {

    private final ExcelReaderMainPanel mainPanel;


    public ExcelReaderWidget(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        mainPanel = new ExcelReaderMainPanel(project, virtualFile);
    }

    public JComponent getComponent() {
        return mainPanel.getContent();
    }

    public ExcelReaderMainPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected Graphics getComponentGraphics(Graphics graphics) {
        return JBSwingUtilities.runGlobalCGTransform(this, super.getComponentGraphics(graphics));
    }

}
