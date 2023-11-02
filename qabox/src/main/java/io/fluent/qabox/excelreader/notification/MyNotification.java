package io.fluentqa.qaplugins.excelreader.notification;


import com.intellij.ide.plugins.newui.SearchWords;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import io.fluentqa.qaplugins.excelreader.excel.DataLoader;
import io.fluentqa.qaplugins.excelreader.message.ExcelReaderBundle;
import io.fluentqa.qaplugins.excelreader.notification.actions.DoNotAskAction;
import io.fluentqa.qaplugins.excelreader.notification.actions.InstallPluginAction;
import io.fluentqa.qaplugins.excelreader.notification.actions.TakeMeGoAction;
import org.jetbrains.annotations.NotNull;


public class MyNotification {

    private static final String GROUP_ID = "Plugins Suggestion";

    private final NotificationGroup notificationGroup =
            NotificationGroupManager.getInstance().
                    getNotificationGroup("Plugins Suggestion");


    public void notify(String content) {
        Project project = DataLoader.getInstance().getProject();
        notify(project, content);
    }

    public void notify(Project project, String content) {
        final Notification notification = notificationGroup.createNotification(content, NotificationType.ERROR);
        notification.notify(project);
    }

    public static void notifyOnStartUp(@NotNull Project project) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(GROUP_ID)
                .createNotification(
                        ExcelReaderBundle.message("notification.startup.suggestion.install"), NotificationType.INFORMATION
                )
                .setTitle(ExcelReaderBundle.message("notification.startup.suggestion.install.title"))
                .addAction(new InstallPluginAction(
                        ExcelReaderBundle.message("notification.startup.suggestion.install.do"),
                        SearchWords.ORGANIZATION.getValue() + "\"Observer & Creator\""))
                .addAction(new TakeMeGoAction(TakeMeGoAction::doTakeMeGo))
                .addAction(new DoNotAskAction(project))
                .setImportant(true)
                .notify(project);
    }

}