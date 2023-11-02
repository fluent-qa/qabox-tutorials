package io.fluentqa.qaplugins.excelreader.ui.vcs;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.OnePixelSplitter;
import io.fluentqa.qaplugins.excelreader.core.reader.ReaderResult;
import io.fluentqa.qaplugins.excelreader.excel.DataLoader;
import io.fluentqa.qaplugins.excelreader.excel.DataResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author piercebrands
 * @since 2022.2.1
 */
public class VcsExcelViewPanel extends VcsExcelPanel implements Disposable {

    private final JPanel rootPanel;
    private final DiffTablePanel rightPanel;

    public VcsExcelViewPanel(@NotNull VirtualFile rightFile,
                             @NotNull String rightContentTitle) {

        ReaderResult rightResolve = new DataResolver(rightFile).resolve(0);

        DiffResult.getInstance().addAllToResult(rightResolve);
        // init mainPanel
        @Nullable OnePixelSplitter mySplitter = new OnePixelSplitter(false, 0.5F);
        mySplitter.setDividerWidth(2);

        rightPanel = new DiffTablePanel(rightContentTitle);
        DataLoader.getInstance().loadDiff(rightResolve, rightPanel);
        mySplitter.setSecondComponent(rightPanel.createComponent());

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
    public JPanel getPanel() {
        return rootPanel;
    }

    @Override
    public int getDiffResultSize() {
        return 0;
    }


    @Override
    public void dispose() {
        rightPanel.dispose();
    }
}
