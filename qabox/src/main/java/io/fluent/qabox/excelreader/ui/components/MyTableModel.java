package io.fluentqa.qaplugins.excelreader.ui.components;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.UIUtil;
import io.fluentqa.qaplugins.excelreader.ui.actions.MyStatusTextAction;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * @author piercebrands
 * @since 2.0.1
 */
public class MyTableModel extends DefaultTableModel {

    public MyTableModel() {

    }

    @Override
    public void setDataVector(Vector dataVector, Vector columnIdentifiers) {
        super.setDataVector(dataVector, columnIdentifiers);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {

    }

    public int getColumnIndex(String selectedItem) {
        int size = getColumnCount();
        for (int i = 0; i < size; i++) {
            String columnName = getColumnName(i);
            if (selectedItem.equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    public boolean containsRow(int row, String insertValue, Set<String> sqlPopup, MyStatusTextAction textAction) {
        Map<String, String> stringStringMap;
        try {
            stringStringMap = resolveSql(insertValue);
        } catch (Exception e) {
            textAction.setText("Bad pattern");
            textAction.setColor(UIUtil.getErrorForeground());
            return true;
        }
        if (stringStringMap.size() <= 0) {
            return false;
        }

        int matchedCount = 0;
        int formatCount = 0;
        for (String s : sqlPopup) {
            String value = stringStringMap.get(s);
            if (StringUtil.isEmpty(value)) {
                continue;
            }
            formatCount++;
            int column = getColumnIndex(s);
            if(column < 0) {
                return false;
            }
            if(row - 1 > getRowCount()){
                return false;
            }
            Object valueAt = getValueAt(row, column);
            if (valueAt != null && valueAt.toString().contains(value)) {
                matchedCount++;
            }
        }
        return matchedCount == formatCount;
    }

    public static Map<String,String> resolveSql(String insertValue) {
        Map<String,String> result = new HashMap<>();
        if (StringUtil.isNotEmpty(insertValue)) {
            if ("/".equals(insertValue)) {
                return result;
            }
            while (insertValue.length() > 0) {
                if (!insertValue.contains("=")) {
                    return result;
                }
                int p = insertValue.indexOf("/");
                int e = insertValue.indexOf("=");
                String key = insertValue.substring(p + 1, e);
                insertValue = insertValue.substring(p + 1);
                int n = insertValue.indexOf("/");
                String value;
                if (n < 0) {
                    value = insertValue.substring(e);
                    insertValue = "";
                } else {
                    value = insertValue.substring(e, n);
                    insertValue = insertValue.substring(n);
                }
                result.put(key,value);
            }
        }
        return result;
    }
}
