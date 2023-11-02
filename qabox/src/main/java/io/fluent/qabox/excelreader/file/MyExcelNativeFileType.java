package io.fluentqa.qaplugins.excelreader.file;

import com.intellij.openapi.fileTypes.INativeFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.IdeFrame;
import io.fluentqa.qaplugins.excelreader.excel.DataLoader;
import io.fluentqa.qaplugins.excelreader.icons.ExcelReaderIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public final class MyExcelNativeFileType implements INativeFileType {

    public static final MyExcelNativeFileType INSTANCE = new MyExcelNativeFileType();

    @Override
    public boolean openFileInAssociatedApplication(Project project, @NotNull VirtualFile file) {
        IdeFrame lastFocusedFrame = IdeFocusManager.getGlobalInstance().getLastFocusedFrame();
        if(lastFocusedFrame == null){
            return false;
        }
        DataLoader.getInstance().load(file,project);
        return false;
    }

    @Override
    public boolean useNativeIcon() {
        return false;
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "MyNativeFile";
    }

    @Override
    public @NotNull String getDescription() {
        return "Excel";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "xls;xlsx;csv";
    }

    @Override
    public Icon getIcon() {
        return ExcelReaderIcons.EXCEL;
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public @NonNls @NotNull String getCharset(@NotNull VirtualFile file, byte @NotNull [] content) {
        return CharsetToolkit.UTF8;
    }
}
