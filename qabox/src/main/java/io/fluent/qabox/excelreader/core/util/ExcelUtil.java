package io.fluentqa.qaplugins.excelreader.core.util;

import com.intellij.openapi.vfs.VirtualFile;
import io.fluentqa.qaplugins.excelreader.core.reader.ExcelType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;


public class ExcelUtil {

    public static ExcelType type(VirtualFile file) {
        if (file.getName().endsWith(ExcelType.XLSX.getValue())) {
            return ExcelType.XLSX;
        } else if (file.getName().endsWith(ExcelType.XLS.getValue())) {
            return ExcelType.XLS;
        } else if (file.getName().endsWith(ExcelType.CSV.getValue())) {
            return ExcelType.CSV;
        } else {
            return ExcelType.UNKNOWN;
        }
    }

    public static boolean isDouble(Cell cell) {
        return !DateUtil.isCellDateFormatted(cell);
    }
}
