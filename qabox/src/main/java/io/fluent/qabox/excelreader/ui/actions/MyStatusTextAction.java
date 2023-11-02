package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.CustomComponentAction;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class MyStatusTextAction extends DumbAwareAction implements CustomComponentAction {

    private final JLabel label = new JLabel();

    public JLabel getLabel() {
        return label;
    }

    @NotNull
    @Override
    public JComponent createCustomComponent(@NotNull Presentation presentation, @NotNull String place) {
        //noinspection HardCodedStringLiteral
        label.setText("9888 results");
        Dimension size = label.getPreferredSize();
        size.height = Math.max(size.height, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE.height);
        label.setPreferredSize(size);
        label.setText(null);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }

    public void setText(String text) {
        getLabel().setText(text);
    }

    public void setColor(Color color) {
        getLabel().setForeground(color);
    }
}
