package io.fluentqa.qaplugins.excelreader.ui.vcs;

import io.fluentqa.qaplugins.excelreader.core.reader.ReaderResult;
import io.fluentqa.qaplugins.excelreader.excel.DataLoader;
import io.fluentqa.qaplugins.excelreader.excel.DataResolver;
import io.fluentqa.qaplugins.excelreader.ui.base.*;
import io.fluentqa.qaplugins.excelreader.ui.components.*;
import io.fluentqa.qaplugins.excelreader.ui.vcs.listener.*;
import io.fluentqa.qaplugins.excelreader.util.*;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author piercebrands
 * @since 2022.2.1
 */
public class VcsExcelDiffPanel extends VcsExcelPanel implements Disposable {

    private final JPanel rootPanel;
    private final DiffTablePanel leftPanel;
    private final DiffTablePanel rightPanel;
    private final MyAdjustmentDiffListener adjustmentListener;
    private final MyListSelectionDiffListener listSelectionListener;
    private final DiffResult diffResult;

    public VcsExcelDiffPanel(@NotNull VirtualFile leftFile,
                             @NotNull VirtualFile rightFile,
                             @NotNull String leftContentTitle,
                             @NotNull String rightContentTitle) {

        ReaderResult leftResolve = new DataResolver(leftFile).resolve(0);
        ReaderResult rightResolve = new DataResolver(rightFile).resolve(0);

        // clear the old result at first
        DiffResult.getInstance().getResults().clear();
        diffResult = DiffResult.getInstance().resolve(leftResolve, rightResolve);

        // init mainPanel
        @Nullable OnePixelSplitter mySplitter = new OnePixelSplitter(false, 0.5F);
        mySplitter.setDividerWidth(2);
        leftPanel = new DiffTablePanel(leftContentTitle);
        DataLoader.getInstance().loadDiff(leftResolve, leftPanel);
        mySplitter.setFirstComponent(leftPanel.createComponent());

        rightPanel = new DiffTablePanel(rightContentTitle);
        DataLoader.getInstance().loadDiff(rightResolve, rightPanel);
        mySplitter.setSecondComponent(rightPanel.createComponent());

        adjustmentListener = new MyAdjustmentDiffListener(leftPanel, rightPanel);
        listSelectionListener = new MyListSelectionDiffListener(leftPanel, rightPanel);

        mySplitter.setOpaque(false);
        mySplitter.getDivider().setOpaque(false);
        mySplitter.setDividerPositionStrategy(Splitter.DividerPositionStrategy.KEEP_FIRST_SIZE);
        mySplitter.setLackOfSpaceStrategy(Splitter.LackOfSpaceStrategy.SIMPLE_RATIO);
        mySplitter.setHonorComponentsMinimumSize(true);
        mySplitter.setHonorComponentsPreferredSize(true);
        mySplitter.setAndLoadSplitterProportionKey("FindSplitterProportion");

        rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(mySplitter, BorderLayout.CENTER);
        // TODO add diff table south
    }

    @Override
    public int getDiffResultSize() {
        return diffResult.getSize();
    }

    @Override
    public JPanel getPanel() {
        return rootPanel;
    }

    @Override
    public void dispose() {
        JBScrollPane contentScrollL = leftPanel.getTablePanel().getContent();
        JBScrollPane contentScrollR = rightPanel.getTablePanel().getContent();

        contentScrollL.getVerticalScrollBar().removeAdjustmentListener(adjustmentListener);
        contentScrollL.getHorizontalScrollBar().removeAdjustmentListener(adjustmentListener);
        leftPanel.dispose();

        contentScrollR.getVerticalScrollBar().removeAdjustmentListener(adjustmentListener);
        contentScrollR.getHorizontalScrollBar().removeAdjustmentListener(adjustmentListener);
        rightPanel.dispose();

        JTable contentTableL = leftPanel.getTablePanel().getDataTable();
        JTable contentTableR = rightPanel.getTablePanel().getDataTable();

        contentTableL.getSelectionModel().removeListSelectionListener(listSelectionListener);
        contentTableR.getSelectionModel().removeListSelectionListener(listSelectionListener);
    }
}
