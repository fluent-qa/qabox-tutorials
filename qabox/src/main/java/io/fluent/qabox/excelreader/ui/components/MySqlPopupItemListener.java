package io.fluentqa.qaplugins.excelreader.ui.components;

import com.intellij.openapi.ui.JBMenuItem;

import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * @author piercebrands
 */
public class MySqlPopupItemListener implements ActionListener {

    private final Set<String> sqlPopup;
    private final JTextComponent component;

    public MySqlPopupItemListener(Set<String> sqlPopup, JTextComponent component) {
        this.sqlPopup = sqlPopup;
        this.component = component;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JBMenuItem item = (JBMenuItem) e.getSource();
        // add to stash set
        sqlPopup.add(item.getText());
        String text = component.getText();
        text += item.getText() + "=";
        component.setText(text);
    }
}