package io.fluentqa.qaplugins.excelreader.notification.actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.fluentqa.qaplugins.excelreader.message.ExcelReaderBundle;
import io.fluentqa.qaplugins.excelreader.notification.MyNotificationKeys;
import org.jetbrains.annotations.NotNull;


public class DoNotAskAction extends NotificationAction {

    private final Project project;

    public DoNotAskAction(@NotNull Project project) {
        super(ExcelReaderBundle.message("notification.startup.suggestion.install.no"));
        this.project = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
        PropertiesComponent.getInstance(project).setValue(MyNotificationKeys.EXCEL_EDITOR_INSTALL, false, true);
        notification.expire();
    }
}
