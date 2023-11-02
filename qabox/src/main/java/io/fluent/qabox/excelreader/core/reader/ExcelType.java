package io.fluentqa.qaplugins.excelreader.core.reader;

public enum ExcelType {

    /**
     * The other file
     */
    UNKNOWN("UNKNOWN"),
    /**
     * The csv file
     */
    CSV("csv"),

    /**
     * The xlx file
     */
    XLS("xls"),

    /**
     * The xlsx file
     */
    XLSX("xlsx");

    private final String value;

    ExcelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
