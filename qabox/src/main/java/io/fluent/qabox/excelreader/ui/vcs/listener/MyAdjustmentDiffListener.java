package io.fluentqa.qaplugins.excelreader.ui.vcs.listener;

import com.intellij.ui.components.JBScrollPane;
import io.fluentqa.qaplugins.excelreader.ui.vcs.DiffTablePanel;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * @author piercebrands
 * @since 2022.2.1
 */
public class MyAdjustmentDiffListener implements AdjustmentListener {

    private final JBScrollPane contentL;
    private final JBScrollPane contentR;

    public MyAdjustmentDiffListener(DiffTablePanel leftTablePanel, DiffTablePanel rightTablePanel) {
        contentL = leftTablePanel.getTablePanel().getContent();
        contentR = rightTablePanel.getTablePanel().getContent();

        contentL.getVerticalScrollBar().addAdjustmentListener(this);
        contentL.getHorizontalScrollBar().addAdjustmentListener(this);

        contentR.getVerticalScrollBar().addAdjustmentListener(this);
        contentR.getHorizontalScrollBar().addAdjustmentListener(this);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getSource() == contentL.getVerticalScrollBar()) {
            contentR.getVerticalScrollBar().setValue(e.getValue());
        } else if (e.getSource() == contentL.getHorizontalScrollBar()) {
            // diffL diffR don't need to scroll Horizontal
            contentR.getHorizontalScrollBar().setValue(e.getValue());
        } else if (e.getSource() == contentR.getVerticalScrollBar()) {
            contentL.getVerticalScrollBar().setValue(e.getValue());
        } else if (e.getSource() == contentR.getHorizontalScrollBar()) {
            // diffL diffR don't need to scroll Horizontal
            contentL.getHorizontalScrollBar().setValue(e.getValue());
        }
    }

}
