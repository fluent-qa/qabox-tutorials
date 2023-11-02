package io.fluentqa.qaplugins.excelreader.core.reader;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ReaderResult {

    private String title;
    private String filePath;
    private Vector<String> header = new Vector<>();
    private Vector<Vector<Object>> rows = new Vector<>();
    private Map<Integer, String> sheetMap = new HashMap<>();

    public ReaderResult() {

    }

    public ReaderResult(Vector<String> header, Vector<Vector<Object>> rows,
                        Map<Integer, String> sheetMap, String title, String filePath) {
        this.rows = rows;
        this.header = header;
        this.sheetMap = sheetMap;
        this.title = title;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    public Vector<String> getHeader() {
        return header;
    }

    public Vector<Vector<Object>> getRows() {
        return rows;
    }

    public Map<Integer, String> getSheetMap() {
        return sheetMap;
    }

    public String getFirstSheetName()  {
        if (sheetMap.size() == 0) {
            return null;
        }
        for (Map.Entry<Integer, String> entry : sheetMap.entrySet()) {
            if (entry.getValue() != null) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int getSheetIndex(String sheetName)  {
        if (sheetMap.size() == 0) {
            return -1;
        }
        for (Map.Entry<Integer, String> entry : sheetMap.entrySet()) {
            if (entry.getValue().equals(sheetName)) {
                return entry.getKey();
            }
        }
        return -1;
    }

}
