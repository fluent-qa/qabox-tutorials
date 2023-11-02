package io.fluentqa.qaplugins.excelreader.core.util;

import com.github.obiscr.excelreader.core.reader.ExcelType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author Wuzi
 * @date 2022/6/18
 */
public class FileUtil {

    public static boolean isSameType(@NotNull VirtualFile source,
                                     @NotNull VirtualFile target) {
        boolean sourceVal = source.getName().endsWith(ExcelType.CSV.getValue());
        boolean targetVal = target.getName().endsWith(ExcelType.CSV.getValue());
        if (sourceVal && targetVal) {
            return true;
        }
        return !sourceVal && !targetVal;
    }

    public static boolean isExcel(VirtualFile virtualFile) {
        return isExcel(virtualFile.getName());
    }

    public static boolean isExcel(String fileName) {
        return fileName.endsWith(ExcelType.CSV.getValue()) ||
                fileName.endsWith(ExcelType.XLS.getValue()) ||
                fileName.endsWith(ExcelType.XLSX.getValue());
    }

    public static boolean isCsv(String fileName) {
        return fileName.endsWith(ExcelType.CSV.getValue());
    }

    public static boolean isCsv(VirtualFile file) {
        return isCsv(file.getName());
    }

    public static boolean isXls(VirtualFile file) {
        return isXls(file.getName());
    }

    public static boolean isXls(String fileName) {
        return fileName.endsWith(ExcelType.XLS.getValue());
    }

    public static boolean isXlsx(VirtualFile file) {
        return isXlsx(file.getName());
    }

    public static boolean isXlsx(String fileName) {
        return fileName.endsWith(ExcelType.XLSX.getValue());
    }
}
