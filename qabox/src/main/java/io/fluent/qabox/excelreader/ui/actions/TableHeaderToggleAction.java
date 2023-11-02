package io.fluentqa.qaplugins.excelreader.ui.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.CheckboxAction;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public abstract class TableHeaderToggleAction extends CheckboxAction implements DumbAware {

    protected boolean selected;

    protected TableHeaderToggleAction(@NotNull String text, @Nullable Icon icon, @Nullable Icon hoveredIcon, @Nullable Icon selectedIcon) {
        super(text);
        getTemplatePresentation().setIcon(icon);
        getTemplatePresentation().setHoveredIcon(hoveredIcon);
        getTemplatePresentation().setSelectedIcon(selectedIcon);
    }

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }

    @NotNull
    @Override
    public JComponent createCustomComponent(@NotNull Presentation presentation, @NotNull String place) {
        JComponent customComponent = super.createCustomComponent(presentation, place);
        customComponent.setFocusable(false);
        customComponent.setOpaque(false);
        return customComponent;
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return selected;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
