package io.fluentqa.qaplugins.excelreader.core.reader;


import com.google.common.collect.Table;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.PathUtil;
import io.fluentqa.qaplugins.excelreader.core.util.ExcelUtil;
import io.fluentqa.qaplugins.excelreader.core.util.TimeUtil;
import io.fluentqa.qaplugins.excelreader.message.ExcelReaderBundle;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public abstract class ExcelReader {

    protected Workbook workbook;
    private static final Logger LOG = Logger.getInstance(ExcelReader.class);
    private final Map<String, Table<Integer,Integer, Cell>> data = new LinkedHashMap<>();
    private final TimeUtil execute = TimeUtil.getInstance();

    private final String filePath;
    private final String fileName;

    public ExcelReader(String filePath) {
        this.filePath = filePath;
        this.fileName = PathUtil.getFileName(filePath);
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public TimeUtil getExecute() {
        return execute;
    }

    public Map<String, Table<Integer, Integer, Cell>> getData() {
        return data;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * Init the table date
     * @throws IOException IOException
     */
    protected abstract void initTable() throws IOException;

    public void setTableData(Sheet sheet, boolean force) {
        if (sheet == null) {
            LOG.error(ExcelReaderBundle.message("logger.null.sheet"));
            return;
        }
        Map<String, Table<Integer, Integer, Cell>> data = getData();
        Table<Integer, Integer, Cell> firstSheetTable = data.get(sheet.getSheetName());
        if (!firstSheetTable.isEmpty() && !force) {
            return;
        }
        firstSheetTable.clear();
        for (Row row : sheet) {
            for (Cell cell : row) {
                int rowNum = row.getRowNum();
                int column = cell.getColumnIndex();
                firstSheetTable.put(rowNum, column, cell);
            }
        }
    }

    public ReaderResult resolve(int sheetIndex) {
        execute.start(System.currentTimeMillis());
        ReaderResult resolve = resolve(getSheetNames().get(sheetIndex));
        String msg = ExcelReaderBundle.message("logger.load", filePath);
        execute.end(System.currentTimeMillis());
        execute.time(msg);
        return resolve;
    }

    public ReaderResult resolve(String sheetName) {
        FormulaEvaluator evaluator = workbook.getCreationHelper()
                .createFormulaEvaluator();
        Table<Integer, Integer, Cell> table = data.get(sheetName);
        if (table.isEmpty()) {
            return new ReaderResult(new Vector<>(), new Vector<>(),getSheetNames(),fileName,filePath);
        }
        TreeSet<Integer> treeSetRows = new TreeSet<>(table.rowKeySet());
        TreeSet<Integer> treeSetCols = new TreeSet<>(table.columnKeySet());
        Vector<String> header = new Vector<>();
        for (int i  = 0 ; i < treeSetRows.last() ; i++) {
            if (i > 0){
                break;
            }
            for (int j = 0 ; j <= treeSetCols.last() ; j++) {
                Cell cell = table.get(i, j);
                Object cellValue = getCellValue(cell, evaluator);
                header.add(cellValue == null ? "" : cellValue.toString());
            }
        }
        Vector<Vector<Object>> rows = new Vector<>();
        for (int i  = 1 ; i <= treeSetRows.last() ; i++) {
            Vector<Object> vector = new Vector<>();
            for (int j = 0 ; j <= treeSetCols.last() ; j++) {
                Cell cell = table.get(i, j);
                Object cellValue = getCellValue(cell, evaluator);
                vector.add(cellValue);
            }
            rows.add(vector);
        }
        return new ReaderResult(header,rows,getSheetNames(),fileName,filePath);
    }

    public Map<Integer, String> getSheetNames() {
        Set<String> strings = data.keySet();
        String[] nameArray = strings.toArray(new String[0]);
        Map<Integer, String> sheetMap = new LinkedHashMap<>();
        for (int i = 0 ; i < nameArray.length ; i++) {
            sheetMap.put(i,nameArray[i]);
        }
        return sheetMap;
    }

    public Object getCellValue(Cell cell, FormulaEvaluator formulaEvaluator) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (ExcelUtil.isDouble(cell)) {
                    DecimalFormat df = new DecimalFormat("###################.###########");
                    return df.format(cell.getNumericCellValue());
                } else {
                    return DateFormatUtils.format(cell.getDateCellValue(),
                            "yyyy-MM-dd HH:mm:ss");
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return formulaEvaluator.evaluate(cell).formatAsString();
            case BLANK:
            default:
                return "";
        }
    }
}
