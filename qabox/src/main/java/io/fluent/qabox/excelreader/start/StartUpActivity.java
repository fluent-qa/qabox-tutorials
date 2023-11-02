package io.fluentqa.qaplugins.excelreader.start;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.util.text.StringUtil;
import io.fluentqa.qaplugins.excelreader.notification.MyNotification;
import io.fluentqa.qaplugins.excelreader.notification.MyNotificationKeys;
import org.jetbrains.annotations.NotNull;


public class StartUpActivity implements StartupActivity.DumbAware {

    @Override
    public void runActivity(@NotNull Project project) {
        if (project.isDefault()) {
            return;
        }
        String notify = PropertiesComponent.getInstance(project).getValue(
                MyNotificationKeys.EXCEL_EDITOR_INSTALL);
        if (StringUtil.isEmpty(notify)) {
            PropertiesComponent.getInstance(project).setValue(
                    MyNotificationKeys.EXCEL_EDITOR_INSTALL, true);
            MyNotification.notifyOnStartUp(project);
        } else {
            boolean needNotify = PropertiesComponent.getInstance(project).getBoolean(
                    MyNotificationKeys.EXCEL_EDITOR_INSTALL);
            if (needNotify) {
                MyNotification.notifyOnStartUp(project);
            }
        }
    }

}