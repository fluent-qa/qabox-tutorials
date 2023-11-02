package io.fluentqa.qaplugins.excelreader.ui.actions;

import cn.hutool.core.util.StrUtil;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.SystemInfo;
import io.fluentqa.qaplugins.excelreader.icons.ExcelReaderIcons;
import io.fluentqa.qaplugins.excelreader.message.ComponentBundle;
import io.fluentqa.qaplugins.excelreader.notification.MyNotification;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;


public class MyOpenWithNativeAction extends DumbAwareAction {

    public String filePath;

    public MyOpenWithNativeAction() {
        super(() -> ComponentBundle.message("home.toolbar.open.with.native"),
                ExcelReaderIcons.EXCEL);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (StrUtil.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        String cmd = "";
        if (SystemInfo.isMac) {
            cmd = "open " + filePath;
        } else if (SystemInfo.hasXdgOpen()) {
            cmd = "xdg-open " + filePath;
        } else if (SystemInfo.isWindows) {
            filePath = filePath.replace("/", "\\");
            cmd = "explorer " + filePath;
        }
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ioException) {
            new MyNotification().notify("Can't open file: " + filePath);
        }
    }
}
