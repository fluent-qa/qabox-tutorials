package io.fluentqa.qaplugins.excelreader.ui.components;

import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyCellDialog extends JDialog implements ActionListener {

    private JBTable table;
    private JButton ok;
    private JButton cancel;
    private JBTextField textField;

    public MyCellDialog(JBTable table) {
        this.table = table;
        this.createComponent();
    }

    private void createComponent() {
        setTitle("Go to Row/Column");
        setModal(true);
        Dimension size = new Dimension(336, 118);
        setPreferredSize(size);
        setSize(size);
        setMinimumSize(size);
        setResizable(false);
        setLocationRelativeTo(WindowManager.getInstance().findVisibleFrame());

        JPanel panel = new JPanel(new BorderLayout());
        JPanel center = new JPanel(new FlowLayout());
        JLabel label = new JLabel("[Row][:column]:");
        textField = new JBTextField(18);
        center.add(label);
        center.add(textField);
        panel.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancel = new JButton("Cancel");
        ok = new JButton("Ok");
        cancel.addActionListener(this);
        ok.addActionListener(this);
        bottom.add(cancel);
        bottom.add(ok);
        panel.add(bottom, BorderLayout.SOUTH);

        setContentPane(panel);
        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            setVisible(false);
        } else if (e.getSource() == ok) {
            setVisible(false);
        }
    }

    public JBTextField getTextField() {
        return textField;
    }

    public void refreshCell() {
        if (table != null) {
            int row = table.getSelectedRow();
            int column = table.getSelectedColumn();
            row = Math.max(0, row);
            column = Math.max(0, column);
            textField.setText((row + 1) + ":" + (column + 1));
        }
    }

    public void setTable(JBTable table) {
        this.table = table;
    }
}
