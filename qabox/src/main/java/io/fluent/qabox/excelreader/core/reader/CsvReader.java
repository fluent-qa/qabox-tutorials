package io.fluentqa.qaplugins.excelreader.core.reader;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.fluentqa.qaplugins.excelreader.message.ExcelReaderBundle;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

public class CsvReader extends ExcelReader {

    private final Table<Integer,Integer, String> table = HashBasedTable.create();

    public CsvReader(String filePath) {
        super(filePath);
        initTable();
    }

    @Override
    protected void initTable() {
        getExecute().start(System.currentTimeMillis());
        cn.hutool.core.text.csv.CsvReader reader = CsvUtil.getReader();
        CsvData csvData = reader.read(new File(getFilePath()));
        List<CsvRow> allRows = csvData.getRows();
        if (allRows.size() == 0) {
            return;
        }
        for (int i = 0 ; i < allRows.size(); i++) {
            CsvRow strings = allRows.get(i);
            List<String> rawList = strings.getRawList();
            for (int j = 0 ; j < rawList.size() ; j++) {
                table.put(i, j, rawList.get(j));
            }
        }
        getExecute().end(System.currentTimeMillis());
        String msg = ExcelReaderBundle.message("logger.init", getFilePath());
        getExecute().time(msg);
    }

    @Override
    public ReaderResult resolve(int sheetIndex) {
        getExecute().start(System.currentTimeMillis());
        if (table.isEmpty()) {
            return new ReaderResult(new Vector<>(), new Vector<>(),
                    new HashMap<>(0),getFileName(),getFilePath());
        }
        TreeSet<Integer> treeSetRows = new TreeSet<>(table.rowKeySet());
        TreeSet<Integer> treeSetCols = new TreeSet<>(table.columnKeySet());
        Vector<String> header = new Vector<>();
        for (int i  = 0 ; i < 1 ; i++) {
            for (int j = 0 ; j < treeSetCols.last() ; j++) {
                String cell = table.get(i, j);
                header.add(cell == null ? "" : cell);
            }
        }
        Vector<Vector<Object>> rows = new Vector<>();
        for (int i  = 1 ; i <= treeSetRows.last() ; i++) {
            Vector<Object> vector = new Vector<>();
            for (int j = 0 ; j < treeSetCols.last() ; j++) {
                String cell = table.get(i, j);
                vector.add(cell);
            }
            rows.add(vector);
        }
        getExecute().end(System.currentTimeMillis());
        String msg = ExcelReaderBundle.message("logger.load", getFilePath());
        getExecute().time(msg);
        return new ReaderResult(header,rows,new HashMap<>(0),getFileName(),getFilePath());
    }

}
