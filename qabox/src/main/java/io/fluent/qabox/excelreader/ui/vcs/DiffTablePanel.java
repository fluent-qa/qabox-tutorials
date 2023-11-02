package io.fluentqa.qaplugins.excelreader.ui.vcs;


import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import io.fluentqa.qaplugins.excelreader.core.reader.ReaderResult;
import io.fluentqa.qaplugins.excelreader.ui.base.*;
import io.fluentqa.qaplugins.excelreader.ui.components.*;
import io.fluentqa.qaplugins.excelreader.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author piercebrands
 * @since 2022.2.1
 */
public class DiffTablePanel implements Disposable {

    private final JPanel rootPanel;
    private final BaseTablePanel tablePanel;
    private final JBTextField topFileHash;
    private final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();

    public DiffTablePanel(String contentTitle) {
        tablePanel = new BaseTablePanel();
        tablePanel.getContent().setBorder(JBUI.Borders.empty());

        TableUtil.setDiffTableProperty(tablePanel.getDataTable());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(JBUI.Borders.emptyBottom(1));
        JLabel topFileIcon = new JLabel(AllIcons.Ide.Readonly);
        topFileHash = new JBTextField(contentTitle);
        topFileHash.setEditable(false);
        topPanel.add(topFileIcon, BorderLayout.WEST);
        topPanel.add(topFileHash, BorderLayout.CENTER);

        JPanel tablePanelDecorator = new JPanel(new BorderLayout());
        tablePanelDecorator.add(topPanel, BorderLayout.NORTH);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tablePanel.getContent(), BorderLayout.CENTER);
        tablePanelDecorator.add(panel, BorderLayout.CENTER);
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(tablePanelDecorator, BorderLayout.CENTER);
    }

    public void fill(ReaderResult result) {
        tablePanel.getTableModel().setDataVector(
                result.getRows(), result.getHeader());
        TableUtil.setDiffColor(tablePanel.getDataTable());
    }

    public JBTextField getTopFileHash() {
        return topFileHash;
    }

    public JComponent createComponent() {
        return rootPanel;
    }

    public void update(ReaderResult result) {
        // Refresh dataTable
        MyTableModel tableModel = tablePanel.getTableModel();
        tableModel.setDataVector(result.getRows(), result.getHeader());
        TableUtil.setDiffColor(tablePanel.getDataTable());
    }

    public BaseTablePanel getTablePanel() {
        return tablePanel;
    }

    public DefaultTableCellRenderer getDefaultRenderer() {
        return defaultRenderer;
    }

    @Override
    public void dispose() {
        tablePanel.dispose();
        rootPanel.removeAll();
    }
}
