package io.fluentqa.qaplugins.excelreader.ui.vcs;

import com.intellij.openapi.Disposable;

import javax.swing.*;

/**
 * @author piercebrands
 */
public abstract class VcsExcelPanel implements Disposable {

    /**
     * View component
     * @return JPanel
     */
    public abstract JPanel getPanel();

    /**
     * Get diff result
     * @return DiffResult
     */
    public abstract int getDiffResultSize();
}
