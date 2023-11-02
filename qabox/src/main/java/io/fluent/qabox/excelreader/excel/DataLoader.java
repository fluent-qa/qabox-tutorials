package io.fluentqa.qaplugins.excelreader.excel;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import io.fluentqa.qaplugins.excelreader.core.reader.ReaderResult;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderTabState;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderView;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderWidget;
import io.fluentqa.qaplugins.excelreader.ui.components.MyTableModel;
import io.fluentqa.qaplugins.excelreader.ui.vcs.DiffTablePanel;
import io.fluentqa.qaplugins.excelreader.util.TableUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author piercebrands
 * @since 2.1.1
 */
public class DataLoader {

    private static final DataLoader INSTANCE = new DataLoader();

    public Project project;

    private DataLoader() {

    }

    public static DataLoader getInstance() {
        return INSTANCE;
    }

    public Project getProject() {
        return project;
    }

    private void load(Project project, ReaderResult result,
                      ExcelReaderMainPanel excelReader, String filePath) {
        this.project = project;
        // reset the filter when load/reload the file
        excelReader.resetFilter();
        excelReader.getOpenInFinderAction().setFilePath(filePath);
        excelReader.getOpenWithNativeAction().setFilePath(filePath);

        // check is xls or xlsx file by sheetMap exists
        if (result.getSheetMap() == null || result.getSheetMap().isEmpty()) {
            excelReader.getTableModel().setDataVector(result.getRows(), result.getHeader());
            excelReader.drawCsv();
        } else {
            excelReader.setSheetMap(result.getSheetMap());
            excelReader.drawXlsOrXlsx();
        }
        excelReader.getFilterAction().setColumns(result.getHeader());
    }

    public void loadSheet(int sheetIndex, JBTable table, VirtualFile virtualFile) {
        ReaderResult result = new DataResolver(virtualFile).resolve(sheetIndex);
        MyTableModel model = (MyTableModel) table.getModel();
        model.setDataVector(result.getRows(), result.getHeader());
        TableUtil.setDefaultColor(table);
    }

    public void load(@NotNull VirtualFile data, @NotNull Project project) {
        // prepare the toolWindow
        ExcelReaderView excelReaderView = ExcelReaderView.getInstance(project);
        ExcelReaderTabState tabState = new ExcelReaderTabState(
                data.getName(),data.getPath());

        // write data to toolWindow
        ReaderResult result = new DataResolver(data).resolve(0);
        ExcelReaderWidget widget = excelReaderView.refreshData(tabState, data);
        this.load(project, result, widget.getMainPanel(), data.getPath());
    }

    public void loadDiff(@NotNull ReaderResult readerResult,
                         @NotNull DiffTablePanel tablePanel){
        tablePanel.fill(readerResult);
    }
}
