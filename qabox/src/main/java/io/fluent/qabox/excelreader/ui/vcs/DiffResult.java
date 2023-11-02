package io.fluentqa.qaplugins.excelreader.ui.vcs;


import io.fluentqa.qaplugins.excelreader.core.reader.ReaderResult;
import io.fluentqa.qaplugins.excelreader.ui.components.SearchResult;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * @author piercebrands
 *
 */
public class DiffResult {

    private static final DiffResult INSTANCE = new DiffResult();
    private final List<SearchResult.Cell> results = new LinkedList<>();

    private DiffResult() {

    }

    public static DiffResult getInstance() {
        return INSTANCE;
    }

    public List<SearchResult.Cell> getResults() {
        return results;
    }

    public int getSize() {
        return results.size();
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public SearchResult.Cell getIndexCell(int index) {
        if (index > results.size() - 1) {
            return results.get(0);
        }
        return results.get(index);
    }

    public void addAllToResult(ReaderResult readerResult) {
        results.clear();
        Vector<Vector<Object>> rows = readerResult.getRows();
        for (int row = 0 ; row < rows.size() ; row++) {
            Vector<Object> cols = rows.get(row);
            for (int col = 0 ; col < cols.size() ; col++) {
                results.add(new SearchResult.Cell(row, col, SearchResult.Cell.DiffType.INSERTED));
            }
        }
    }

    public DiffResult resolve(@NotNull ReaderResult leftResolve,
                        @NotNull ReaderResult rightResolve) {
        results.clear();
        int length = Math.min(leftResolve.getRows().size(),
                rightResolve.getRows().size());
        int maxLength = Math.max(leftResolve.getRows().size(),
                rightResolve.getRows().size());
        for(int i = 0 ; i < maxLength ; i++) {
            if (i > length - 1) {
                equals(new Vector<>(),rightResolve.getRows().get(i),i);
                continue;
            }
            equals(leftResolve.getRows().get(i),rightResolve.getRows().get(i),i);
        }
        return this;
    }

    public boolean containsRow(int row) {
        for (SearchResult.Cell cell : results) {
            if (cell.getRow() == row) {
                return true;
            }
        }
        return false;
    }

    public SearchResult.Cell containsCell(int row, int column) {
        for (SearchResult.Cell cell : results) {
            if (cell.getRow() == row && cell.getColumn() == column) {
                return cell;
            }
        }
        return null;
    }

    void equals(Vector<Object> var1, Vector<Object> var2, int col){
        int length = Math.min(var1.size(),var2.size());
        int maxLength = Math.max(var1.size(),var2.size());
        for(int i = 0 ; i < maxLength ; i++) {
            if (i > length - 1) {
                if (!equals(null,var2.get(i))) {
                    results.add(new SearchResult.Cell(col, i, SearchResult.Cell.DiffType.INSERTED));
                }
                continue;
            }
            if(!equals(var1.get(i),var2.get(i))){
                results.add(new SearchResult.Cell(col, i, SearchResult.Cell.DiffType.MODIFIED));
            }
        }
    }

    boolean equals(Object var1, Object var2){
        if(var1 == null && var2 == null){
            return true;
        }
        if(var1 == null || var2 == null){
            return false;
        }
        return var1.equals(var2);
    }
}
