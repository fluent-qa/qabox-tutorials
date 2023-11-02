package io.fluentqa.qaplugins.excelreader.core.reader;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.intellij.openapi.diagnostic.Logger;
import io.fluentqa.qaplugins.excelreader.message.ExcelReaderBundle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class XlsReader extends ExcelReader {

    private static final Logger LOG = Logger.getInstance(XlsReader.class);
    private HSSFWorkbook workbook;

    public XlsReader(String filePath) {
        super(filePath);
        initTable();
    }

    @Override
    protected void initTable() {
        getExecute().start(System.currentTimeMillis());
        Map<String, Table<Integer, Integer, Cell>> data = getData();
        File file = new File(getFilePath());
        try {
            workbook = (HSSFWorkbook)WorkbookFactory.create(file);
            setWorkbook(workbook);
            int sheetSize = workbook.getNumberOfSheets();
            for (int i = 0 ; i < sheetSize ; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                if (sheet == null) {
                    LOG.error(ExcelReaderBundle.message("logger.null.sheet.index","XlsReader", i));
                    continue;
                }
                data.put(sheet.getSheetName(), HashBasedTable.create());
                setTableData(sheet.getSheetName(),true);
            }
            workbook.close();
        } catch (IOException e) {
            LOG.error(ExcelReaderBundle.message("logger.init.table.exception","XlsReader", e.getMessage()));
            return;
        }
        getExecute().end(System.currentTimeMillis());
        String msg = ExcelReaderBundle.message("logger.init", getFilePath());
        getExecute().time(msg);
    }

    public void setTableData(String sheetName, boolean force) {
        HSSFSheet sheet = workbook.getSheet(sheetName);
        super.setTableData(sheet, force);
    }
}
