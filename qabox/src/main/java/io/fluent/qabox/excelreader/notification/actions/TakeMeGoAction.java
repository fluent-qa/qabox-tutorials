package io.fluentqa.qaplugins.excelreader.notification.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.fluentqa.qaplugins.excelreader.message.ExcelReaderBundle;
import org.jetbrains.annotations.NotNull;


public class TakeMeGoAction extends NotificationAction {

    private final Runnable actionPerformed;
    public TakeMeGoAction(Runnable actionPerformed) {
        super(ExcelReaderBundle.message("notification.startup.suggestion.install.go"));
        this.actionPerformed = actionPerformed;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
        actionPerformed.run();
    }

    public static void doTakeMeGo() {
        String helpUrl = "https://plugins.jetbrains.com/plugin/18663-exceleditor";
        BrowserUtil.browse(helpUrl);
    }
}
