// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package io.fluentqa.qaplugins.excelreader.ui;


import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBUI;
import io.fluentqa.qaplugins.excelreader.excel.DataFilter;
import io.fluentqa.qaplugins.excelreader.excel.DataLoader;
import io.fluentqa.qaplugins.excelreader.ui.actions.*;
import io.fluentqa.qaplugins.excelreader.ui.components.*;
import io.fluentqa.qaplugins.excelreader.ui.components.MySearchReplaceComponent;
import io.fluentqa.qaplugins.excelreader.ui.components.MySheetTabChangeListener;
import io.fluentqa.qaplugins.excelreader.ui.components.MyTableModel;
import io.fluentqa.qaplugins.excelreader.util.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author piercebrands
 * @since 2.1.1
 */
public class ExcelReaderMainPanel {

    private static final Logger LOG = Logger.getInstance(ExcelReaderMainPanel.class);

    /* Components */
    private JPanel myToolWindowContent;
    private JBTable dataTable;
    private JBTabbedPane sheetPane;
    private MyCellDialog cellDialog;
    private MySearchReplaceComponent src;
    private MyTableModel tableModel;
    private JBScrollPane defaultScrollPane;
    private MySheetTabChangeListener sheetListener;
    private JBPopupMenu sqlPopupMenu;

    private final Project project;

    private final VirtualFile virtualFile;

    /* Multi Sheets */
    private Map<Integer, String> sheetMap = null;

    /* Actions */
    private MyToggleMatchCaseAction myToggleMatchCaseAction;
    private MyToggleWordsAction myToggleWordsAction;
    private MyToggleRegexAction myToggleRegexAction;
    private MyToggleSqlAction myToggleSqlAction;
    private MyStatusTextAction statusTextAction;
    private MyPrevOccurrenceAction prevAction;
    private MyNextOccurrenceAction nextAction;
    private MyGotoCellAction cellAction;
    private MyOpenInFinderAction openInFinderAction;
    private MyOpenWithNativeAction openWithNativeAction;
    private MyFilterAction filterAction;

    /* Search Filter */
    final private SearchResult searchResult = new SearchResult();
    final private Set<String> sqlPopup = new HashSet<>();

    private MySqlPopupItemListener sqlPopupItemListener;


    public ExcelReaderMainPanel(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        this.project = project;
        this.virtualFile = virtualFile;
        initialization();
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public void initialization() {
        this.createUIComponents();
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    public JBTable getDataTable() {
        return dataTable;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSheetMap(Map<Integer, String> sheetMap) {
        this.sheetMap = sheetMap;
    }

    public Set<String> getSqlPopup() {
        return sqlPopup;
    }

    private void createUIComponents() {
        /* Init Table Component */
        tableModel = new MyTableModel();
        dataTable = new JBTable(tableModel);
        dataTable.setCellSelectionEnabled(true);
        defaultScrollPane = new JBScrollPane(dataTable);
        defaultScrollPane.setBorder(new JBEmptyBorder(0));
        SwingUtil.setJspStyle(defaultScrollPane);

        sheetPane = new JBTabbedPane(JBTabbedPane.BOTTOM);
        sheetPane.setTabComponentInsets(JBUI.emptyInsets());
        sheetListener = new MySheetTabChangeListener(this);

        /* Init Search Component */
        cellDialog = new MyCellDialog(dataTable);
        myToolWindowContent = new JPanel(new BorderLayout());
        statusTextAction = new MyStatusTextAction();
        prevAction = new MyPrevOccurrenceAction(dataTable, statusTextAction.getLabel(), searchResult);
        nextAction = new MyNextOccurrenceAction(dataTable, statusTextAction.getLabel(), searchResult);
        cellAction = new MyGotoCellAction(AllIcons.Graph.SnapToGrid, dataTable, cellDialog);
        openInFinderAction = new MyOpenInFinderAction();
        openWithNativeAction = new MyOpenWithNativeAction();
        filterAction = new MyFilterAction(AllIcons.General.Filter, this);
        myToggleMatchCaseAction = new MyToggleMatchCaseAction(this);
        myToggleWordsAction = new MyToggleWordsAction(this);
        myToggleRegexAction = new MyToggleRegexAction(this);
        myToggleSqlAction = new MyToggleSqlAction(this);
        src = MySearchReplaceComponent.buildFor(project,myToolWindowContent)
                .addPrimarySearchActions(createPrimarySearchActions())
                .addExtraSearchActions(myToggleMatchCaseAction, myToggleWordsAction,
                        myToggleRegexAction, myToggleSqlAction)
                .addToolWindow(this)
                .build();
        statusTextAction.setText("0 results");

        sqlPopupItemListener = new MySqlPopupItemListener(sqlPopup, src.getSearchTextComponent());
        /* Add Component to MainPanel */
        myToolWindowContent.add(src, BorderLayout.NORTH);
        myToolWindowContent.add(defaultScrollPane, BorderLayout.CENTER);
        myToolWindowContent.updateUI();
    }

    /**
     * Draw xls/xlsx view, if sheetMaps is not null,
     * means opening the xls/xlsx file.
     *
     * @see DataLoader#load(VirtualFile, Project)
     */
    public void drawXlsOrXlsx() {
        sheetPane.removeAll();
        sheetPane.removeChangeListener(sheetListener);

        for (Map.Entry<Integer, String> entry : sheetMap.entrySet()) {
            JBScrollPane scrollPane = initSheetTable();
            scrollPane.setBorder(new JBEmptyBorder(0));
            sheetPane.addTab(entry.getValue(), scrollPane);
        }
        sheetPane.updateUI();

        // after addTab action, add change listener
        sheetPane.addChangeListener(sheetListener);

        myToolWindowContent.remove(defaultScrollPane);
        myToolWindowContent.add(sheetPane, BorderLayout.CENTER);
        myToolWindowContent.updateUI();

        // default to select first sheet
        sheetPane.setSelectedIndex(0);
        sheetListener.changeTab(sheetPane);
    }

    /**
     * Draw csv view, if sheetMaps is null or empty,
     * means opening the csv file.
     *
     * @see DataLoader#load(VirtualFile, Project)
     */
    public void drawCsv() {
        myToolWindowContent.remove(sheetPane);
        myToolWindowContent.add(defaultScrollPane, BorderLayout.CENTER);
        myToolWindowContent.updateUI();
        TableUtil.resetToDefaultStyle(dataTable);
        refreshActiveTable(dataTable);
    }

    public void refreshActiveTable(JBTable table) {
        getNextAction().setTable(table);
        getPrevAction().setTable(table);
        getCellAction().setTable(table);
        getCellDialog().setTable(table);
    }

    /**
     * Create the sheet tables
     *
     * @return JBScrollPane
     */
    private JBScrollPane initSheetTable() {
        MyTableModel tableModel = new MyTableModel();
        JBTable table = new JBTable();
        table.setModel(tableModel);
        table.setCellSelectionEnabled(true);
        JBScrollPane scrollPane = new JBScrollPane(table);
        SwingUtil.setJspStyle(scrollPane);
        TableUtil.resetToDefaultStyle(table);
        return scrollPane;
    }

    @NotNull
    protected AnAction[] createPrimarySearchActions() {
        return new AnAction[]{
//                myToggleMatchCaseAction,
//                myToggleWordsAction,
//                myToggleRegexAction,
//                new Separator(),
                statusTextAction,
                prevAction,
                nextAction,
                cellAction,
                new Separator(),
                filterAction,
                new Separator(),
                openInFinderAction,
                openWithNativeAction
        };
    }

    public MyTableModel getTableModel() {
        return tableModel;
    }

    public MyStatusTextAction getStatusTextAction() {
        return statusTextAction;
    }

    public MyNextOccurrenceAction getNextAction() {
        return nextAction;
    }

    public MyPrevOccurrenceAction getPrevAction() {
        return prevAction;
    }

    public MyGotoCellAction getCellAction() {
        return cellAction;
    }

    public MyCellDialog getCellDialog() {
        return cellDialog;
    }

    public MyFilterAction getFilterAction() {
        return filterAction;
    }

    public MyToggleWordsAction getMyToggleWordsAction() {
        return myToggleWordsAction;
    }

    public MyToggleRegexAction getMyToggleRegex() {
        return myToggleRegexAction;
    }

    public MyToggleSqlAction getMyToggleSqlAction() {
        return myToggleSqlAction;
    }

    public MySearchReplaceComponent getSrc() {
        return src;
    }

    public MyOpenInFinderAction getOpenInFinderAction() {
        return openInFinderAction;
    }

    public MyOpenWithNativeAction getOpenWithNativeAction() {
        return openWithNativeAction;
    }

    public void search(Document document) {
        String insertValue = "";
        try {
            insertValue = document.getText(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        search(insertValue);
    }

    public void search(String insertValue) {
        // clean the old search result
        searchResult.clear();
        // init filter instance
        DataFilter instance = DataFilter.getInstance();
        instance.setMainPanel(this);
        // check if show popup
        checkIfNeedShowSqlPopup(insertValue);

        if (sheetMap == null || sheetMap.isEmpty()) {
            instance.searchInCsv(insertValue, dataTable);
        } else {
            instance.searchInXlsOrXlsx(insertValue, sheetPane);
        }
    }

    public void checkIfNeedShowSqlPopup(String strQuery) {
        if (!myToggleSqlAction.isSelected() || StringUtil.isEmpty(strQuery)) {
            return;
        }
        String lastChar = strQuery.substring(strQuery.length() - 1);
        if ("/".equals(lastChar)) {
            Vector<String> columns = filterAction.getColumns();
            if (sqlPopupMenu == null) {
                sqlPopupMenu = new JBPopupMenu();
            } else {
                sqlPopupMenu.removeAll();
            }
            for (String s: columns) {
                if (!sqlPopup.contains(s) || !strQuery.contains(s)){
                    if (StringUtil.isEmpty(s)) {
                        continue;
                    }
                    JBMenuItem item = new JBMenuItem(s);
                    item.addActionListener(sqlPopupItemListener);
                    sqlPopupMenu.add(item);
                }
            }
            if (sqlPopupMenu.getComponentCount() == 0) {
                return;
            }
            JTextComponent component = src.getSearchTextComponent();
            int position = component.getCaretPosition();
            Point point;
            try {
                Rectangle2D rectangle2D = component.modelToView2D(position);
                point = new Point((int)rectangle2D.getMaxX(), (int)rectangle2D.getMaxY());
            }
            catch (BadLocationException e) {
                point = component.getCaret().getMagicCaretPosition();
            }
            int x = point == null ? 0 : point.x;
            int y = point == null ? 1 : point.y;
            sqlPopupMenu.show(component,x,y);
        }
    }

    public DataFilter.FilterModel getFilterModel() {
        return filterAction.isAnywhere() ?
                DataFilter.FilterModel.ANYWHERE :
                DataFilter.FilterModel.DEFINED_COLUMN;
    }

    public DataFilter.MatchModel getMatchModel() {
        DataFilter.MatchModel matchModel = DataFilter.MatchModel.NONE;
        if (myToggleSqlAction.isSelected()) {
            matchModel = DataFilter.MatchModel.SQL;
            return matchModel;
        }
        if (myToggleMatchCaseAction.isSelected() && myToggleWordsAction.isSelected()) {
            matchModel = DataFilter.MatchModel.MATCH_CASE_WORDS;
            return matchModel;
        }
        if (myToggleMatchCaseAction.isSelected() && myToggleRegexAction.isSelected()) {
            matchModel = DataFilter.MatchModel.MATCH_CASE_REGEX;
            return matchModel;
        }
        if (myToggleMatchCaseAction.isSelected()) {
            matchModel = DataFilter.MatchModel.MATCH_CASE;
            return matchModel;
        }
        if (myToggleWordsAction.isSelected()) {
            matchModel = DataFilter.MatchModel.WORDS;
            return matchModel;
        }
        if (myToggleRegexAction.isSelected()) {
            matchModel = DataFilter.MatchModel.REGEX;
            return matchModel;
        }
        return matchModel;
    }

    public void resetFilter() {
        getStatusTextAction().setText("0 result");
        getStatusTextAction().setColor(JBColor.BLACK);
        src.getSearchTextComponent().setText("");
        MyPrevNextOccurrenceAction.currentIndex = -1;
        getFilterAction().setColumns(null);
    }

    public void refreshStatusText() {
        String text = "";
        if (myToggleMatchCaseAction.isSelected() && myToggleWordsAction.isSelected()) {
            text = "Match case and words";
        }
        if (myToggleMatchCaseAction.isSelected() && myToggleRegexAction.isSelected()) {
            text = "Match case and regex";
        }
        if (myToggleMatchCaseAction.isSelected()) {
            text = "Match case";
        }
        if (myToggleWordsAction.isSelected()) {
            text = "Words";
        }
        if (myToggleRegexAction.isSelected()) {
            text = "Regex";
        }
        LOG.info("ExcelReader::UseMatchModel:" + text);
    }
}
