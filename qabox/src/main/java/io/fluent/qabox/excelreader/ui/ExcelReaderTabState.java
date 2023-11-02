package io.fluentqa.qaplugins.excelreader.ui;

import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.Nls;

/**
 * @author piercebrands
 * @since 1.0.1
 */
public class ExcelReaderTabState {

    @Attribute("tabName")
    public @Nls String myTabName;

    @Attribute("filePath")
    public @Nls String filePath;

    public ExcelReaderTabState(@Nls String myTabName, @Nls String filePath) {
        this.filePath = filePath;
        this.myTabName = myTabName;
    }
}
