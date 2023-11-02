package io.fluentqa.qaplugins.excelreader.excel;


import com.intellij.openapi.vfs.VirtualFile;
import io.fluentqa.qaplugins.excelreader.core.reader.*;
import io.fluentqa.qaplugins.excelreader.core.util.ExcelUtil;
import io.fluentqa.qaplugins.excelreader.core.util.FileUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author piercebrands
 * @since 2.1.1
 */
public class DataResolver {

    private final VirtualFile data;
    private final String extension;

    public DataResolver(@NotNull VirtualFile virtualFile) {
        this.data = virtualFile;
        this.extension = virtualFile.getExtension();
    }

    public ReaderResult resolve(int sheetIndex) {
        if (!FileUtil.isExcel(this.extension)) {
            return new ReaderResult();
        }
        ExcelType type = ExcelUtil.type(data);
        switch (type) {
            case CSV -> {
                CsvReader csvReader = new CsvReader(data.getPath());
                return csvReader.resolve(0);
            }
            case XLS -> {
                XlsReader xlxReader = new XlsReader(data.getPath());
                return xlxReader.resolve(sheetIndex);
            }
            case XLSX -> {
                XlsxReader xlsxReader = new XlsxReader(data.getPath());
                return xlsxReader.resolve(sheetIndex);
            }
            case UNKNOWN, default -> {
            }
        }
        return new ReaderResult();
    }

}
