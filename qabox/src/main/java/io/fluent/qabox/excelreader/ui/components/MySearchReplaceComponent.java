// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package io.fluentqa.qaplugins.excelreader.ui.components;

import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.find.FindBundle;
import com.intellij.find.FindInProjectSettings;
import com.intellij.find.SearchReplaceComponent;
import com.intellij.find.SearchTextArea;
import com.intellij.find.editorHeaderActions.*;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.editor.impl.EditorHeaderComponent;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.*;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.components.panels.Wrapper;
import com.intellij.util.BooleanFunction;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import io.fluentqa.qaplugins.excelreader.ui.ExcelReaderMainPanel;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.FlowLayout.CENTER;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.InputEvent.META_DOWN_MASK;

public class MySearchReplaceComponent extends EditorHeaderComponent implements DataProvider {
    public static final int RIGHT_PANEL_WEST_OFFSET = 13;
    private static final float MAX_LEFT_PANEL_PROP = 0.9F;
    private static final float DEFAULT_PROP = 0.33F;

    private final MyTextComponentWrapper mySearchFieldWrapper;
    private JTextComponent mySearchTextComponent;

    private final ActionToolbarImpl mySearchActionsToolbar;
    private final List<AnAction> myEmbeddedSearchActions = new ArrayList<>();
    private final List<Component> myExtraSearchButtons = new ArrayList<>();

    private final ExcelReaderMainPanel toolWindow;
    private final Project myProject;
    private final JComponent myTargetComponent;
    @Nullable
    private OnePixelSplitter mySplitter;


    private final boolean myUseSearchField;

    @NotNull
    private @NlsContexts.Label String myStatusText = "";

    @NotNull
    public static Builder buildFor(@Nullable Project project, @NotNull JComponent component) {
        return new Builder(project, component);
    }

    private MySearchReplaceComponent(@Nullable Project project,
                                     @NotNull JComponent targetComponent,
                                     @NotNull DefaultActionGroup searchToolbar1Actions,
                                     @NotNull DefaultActionGroup searchToolbar2Actions,
                                     @NotNull ExcelReaderMainPanel myToolWindow,
                                     boolean showOnlySearchPanel,
                                     boolean maximizeLeftPanelOnResize,
                                     boolean useSearchField) {
        myProject = project;
        myTargetComponent = targetComponent;
        myUseSearchField = useSearchField;
        toolWindow = myToolWindow;

        for (AnAction child : searchToolbar2Actions.getChildren(null)) {
            if (child instanceof Embeddable) {
                myEmbeddedSearchActions.add(child);
                ShortcutSet shortcutSet = ActionUtil.getMnemonicAsShortcut(child);
                if (shortcutSet != null) {
                    child.registerCustomShortcutSet(shortcutSet, this);
                }
            }
        }
        for (AnAction action : myEmbeddedSearchActions) {
            searchToolbar2Actions.remove(action);
        }

        mySearchFieldWrapper = new MyTextComponentWrapper() {
            @Override
            public void setContent(JComponent wrapped) {
                super.setContent(wrapped);
                mySearchTextComponent = unwrapTextComponent(wrapped);
            }
        };

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(JBColor.border());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        leftPanel.add(mySearchFieldWrapper, constraints);
        constraints.gridy++;
        leftPanel.setBorder(JBUI.Borders.customLine(JBColor.border(), 0, 0, 0, 1));

        searchToolbar1Actions.addAll(searchToolbar2Actions.getChildren(null));

        mySearchActionsToolbar = createSearchToolbar1(searchToolbar1Actions);
        mySearchActionsToolbar.setForceShowFirstComponent(true);
        JPanel searchPair = new NonOpaquePanel(new BorderLayout());
        searchPair.add(mySearchActionsToolbar, BorderLayout.CENTER);

        JPanel rightPanel = new NonOpaquePanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, true, false));
        rightPanel.add(searchPair);
        float initialProportion = maximizeLeftPanelOnResize ? MAX_LEFT_PANEL_PROP : DEFAULT_PROP;

        if (showOnlySearchPanel) {
            add(leftPanel, BorderLayout.CENTER);
        } else {
            if (maximizeLeftPanelOnResize) {
                mySplitter = new OnePixelSplitter(false, initialProportion, initialProportion, initialProportion);
            } else {
                mySplitter = new OnePixelSplitter(false, initialProportion);
            }
            mySplitter.setFirstComponent(leftPanel);
            mySplitter.setSecondComponent(rightPanel);
            mySplitter.setOpaque(false);
            mySplitter.getDivider().setOpaque(false);
            add(mySplitter, BorderLayout.CENTER);

            if (maximizeLeftPanelOnResize) {
                rightPanel.setLayout(new FlowLayout(CENTER, 0, 0));
                rightPanel.setBorder(JBUI.Borders.emptyLeft(RIGHT_PANEL_WEST_OFFSET));
                rightPanel.setMinimumSize(new Dimension(mySearchActionsToolbar.getActions().size()
                        * ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE.width + RIGHT_PANEL_WEST_OFFSET, 0));
                mySearchActionsToolbar.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        int minWidth = 0;
                        for (Component component : rightPanel.getComponents()) {
                            minWidth += component.getPreferredSize().width;
                        }
                        rightPanel.setMinimumSize(new Dimension(minWidth, 0));
                        mySplitter.updateUI();
                    }
                });
                mySplitter.setDividerPositionStrategy(Splitter.DividerPositionStrategy.KEEP_SECOND_SIZE);
                mySplitter.setLackOfSpaceStrategy(Splitter.LackOfSpaceStrategy.HONOR_THE_SECOND_MIN_SIZE);
                mySplitter.setResizeEnabled(false);
                mySplitter.setHonorComponentsMinimumSize(true);
                mySplitter.setHonorComponentsPreferredSize(false);
            } else {
                rightPanel.setBorder(JBUI.Borders.emptyLeft(6));
                mySplitter.setDividerPositionStrategy(Splitter.DividerPositionStrategy.KEEP_FIRST_SIZE);
                mySplitter.setLackOfSpaceStrategy(Splitter.LackOfSpaceStrategy.HONOR_THE_SECOND_MIN_SIZE);
                mySplitter.setHonorComponentsMinimumSize(true);
                mySplitter.setHonorComponentsPreferredSize(true);
                mySplitter.setAndLoadSplitterProportionKey("FindSplitterProportion");
            }
        }

        update("");

        // A workaround to suppress editor-specific TabAction
        new TransferFocusAction().registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0)), this);
        new TransferFocusBackwardAction()
                .registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK)), this);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();

        addTextToRecent(mySearchTextComponent);
    }

    public void setStatusText(@NotNull @NlsContexts.Label String status) {
        myStatusText = status;
    }

    @Nullable
    @Override
    public Object getData(@NotNull @NonNls String dataId) {
        return null;
    }

    public Project getProject() {
        return myProject;
    }

    @NotNull
    public JTextComponent getSearchTextComponent() {
        return mySearchTextComponent;
    }


    private void updateSearchComponent(@NotNull String textToSet) {
        if (!updateTextComponent(true)) {
            replaceTextInTextComponentEnsuringSelection(textToSet, mySearchTextComponent);
            return;
        }
        mySearchTextComponent.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                toolWindow.search(e.getDocument());
            }
        });

        mySearchTextComponent.registerKeyboardAction(e -> {
                    if (StringUtil.isNotEmpty(mySearchTextComponent.getText())) {
                        IdeFocusManager.getInstance(myProject).requestFocus(myTargetComponent, true);
                        addTextToRecent(mySearchTextComponent);
                    }
                }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, SystemInfo.isMac
                        ? META_DOWN_MASK : CTRL_DOWN_MASK),
                JComponent.WHEN_FOCUSED);
        // make sure Enter is consumed by search text field, even if 'next occurrence' action is disabled
        // this is needed to e.g. avoid triggering a default button in containing dialog (see IDEA-128057)
        mySearchTextComponent.registerKeyboardAction(e -> {

        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), WHEN_FOCUSED);

        // It registers a shortcut set automatically on construction
        new VariantsCompletionAction(mySearchTextComponent);
    }

    private static void replaceTextInTextComponentEnsuringSelection(@NotNull String textToSet, JTextComponent component) {
        String existingText = component.getText();
        if (!existingText.equals(textToSet)) {
            component.setText(textToSet);
            // textToSet should be selected even if we have no selection before (if we have the selection then setText will remain it)
            if (component.getSelectionStart() == component.getSelectionEnd()) {
                component.selectAll();
            }
        }
    }

    public void update(@NotNull String findText) {
        boolean needToResetSearchFocus = mySearchTextComponent != null && mySearchTextComponent.hasFocus();
        updateSearchComponent(findText);
        if (needToResetSearchFocus) {
            mySearchTextComponent.requestFocusInWindow();
        }
        updateBindings();
        updateActions();
        List<Component> focusOrder = new ArrayList<>();
        focusOrder.add(mySearchTextComponent);
        focusOrder.addAll(myExtraSearchButtons);
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(new ListFocusTraversalPolicy(focusOrder));
        revalidate();
        repaint();
    }

    public void updateActions() {
        mySearchActionsToolbar.updateActionsImmediately();
        JComponent textComponent = mySearchFieldWrapper.getTargetComponent();
        if (textComponent instanceof SearchTextArea) {
            ((SearchTextArea) textComponent).updateExtraActions();
        }
        if (textComponent instanceof SearchTextArea) {
            ((SearchTextArea) textComponent).updateExtraActions();
        }
    }

    public void addTextToRecent(@NotNull JTextComponent textField) {
        final String text = textField.getText();
        if (text.length() > 0) {
            if (textField == mySearchTextComponent) {
                if (mySearchFieldWrapper.getTargetComponent() instanceof SearchTextField) {
                    ((SearchTextField) mySearchFieldWrapper.getTargetComponent()).addCurrentTextToHistory();
                }
            }
        }
    }

    private boolean updateTextComponent(boolean search) {
        JTextComponent oldComponent = mySearchTextComponent;
        if (oldComponent != null) {
            return false;
        }
        final MyTextComponentWrapper wrapper = mySearchFieldWrapper;

        @NotNull JTextComponent innerTextComponent;
        @NotNull JComponent outerComponent;

        if (myUseSearchField) {
            outerComponent = new SearchTextField(true, this.toString());
            innerTextComponent = ((SearchTextField) outerComponent).getTextEditor();
            innerTextComponent.setBorder(BorderFactory.createEmptyBorder());
        } else {
            innerTextComponent = new JBTextArea() {
                @Override
                public Dimension getPreferredScrollableViewportSize() {
                    Dimension defaultSize = super.getPreferredScrollableViewportSize();
                    if (mySplitter != null &&
                            mySplitter.getSecondComponent() != null &&
                            Registry.is("ide.find.expand.search.field.on.typing", true)) {
                        Dimension preferredSize = getPreferredSize();
                        Dimension minimumSize = getMinimumSize();
                        int spaceForLeftPanel =
                                mySplitter.getWidth() - mySplitter.getSecondComponent().getPreferredSize().width - mySplitter.getDividerWidth();
                        int allSearchTextAreaIcons = JBUI.scale(180);
                        int w = spaceForLeftPanel - allSearchTextAreaIcons;
                        w = Math.max(w, minimumSize.width);
                        return new Dimension(Math.min(Math.max(defaultSize.width, preferredSize.width), w), defaultSize.height);
                    }
                    return defaultSize;
                }
            };
            ((JBTextArea) innerTextComponent).setRows(1);
            ((JBTextArea) innerTextComponent).setColumns(12);
            innerTextComponent.setMinimumSize(new Dimension(150, 0));
            outerComponent = new SearchTextArea(((JBTextArea) innerTextComponent), search);

            myExtraSearchButtons.clear();
            myExtraSearchButtons
                    .addAll(((SearchTextArea)outerComponent).setExtraActions(myEmbeddedSearchActions.toArray(AnAction.EMPTY_ARRAY)));
        }

        UIUtil.addUndoRedoActions(innerTextComponent);
        wrapper.setContent(outerComponent);

        if (search) {
            innerTextComponent.getAccessibleContext().setAccessibleName(FindBundle.message("find.search.accessible.name"));
        } else {
            innerTextComponent.getAccessibleContext().setAccessibleName(FindBundle.message("find.replace.accessible.name"));
        }
        // Display empty text only when focused
        innerTextComponent.putClientProperty(
                "StatusVisibleFunction", (BooleanFunction<JTextComponent>) (c -> c.getText().isEmpty() && c.isFocusOwner()));

        innerTextComponent.putClientProperty(UIUtil.HIDE_EDITOR_FROM_DATA_CONTEXT_PROPERTY, Boolean.TRUE);
        innerTextComponent.setBackground(UIUtil.getTextFieldBackground());
        JComponent finalTextComponent = innerTextComponent;
        innerTextComponent.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent e) {
                finalTextComponent.repaint();
            }

            @Override
            public void focusLost(final FocusEvent e) {
                finalTextComponent.repaint();
            }
        });
        return true;
    }

    private void updateBindings() {
        updateBindings(mySearchActionsToolbar, mySearchFieldWrapper);
    }

    private void updateBindings(@NotNull ActionToolbarImpl toolbar, @NotNull JComponent shortcutHolder) {
        updateBindings(toolbar.getActions(), shortcutHolder);
    }

    private void updateBindings(@NotNull List<? extends AnAction> actions, @NotNull JComponent shortcutHolder) {
        DataContext context = DataManager.getInstance().getDataContext(this);
        for (AnAction action : actions) {
            ShortcutSet shortcut = null;
            if (action instanceof ContextAwareShortcutProvider) {
                shortcut = ((ContextAwareShortcutProvider) action).getShortcut(context);
            } else if (action instanceof ShortcutProvider) {
                shortcut = ((ShortcutProvider) action).getShortcut();
            }
            if (shortcut != null) {
                action.registerCustomShortcutSet(shortcut, shortcutHolder);
            }
        }
    }


    @NotNull
    private ActionToolbarImpl createSearchToolbar1(@NotNull DefaultActionGroup group) {
        ActionToolbarImpl toolbar = createToolbar(group);
        toolbar.setSecondaryActionsTooltip(FindBundle.message("find.popup.show.filter.popup"));
        toolbar.setSecondaryActionsIcon(AllIcons.General.Filter, true);
        toolbar.setNoGapMode();
        toolbar.setSecondaryButtonPopupStateModifier(e -> {
            Icon icon = e.getPresentation().getIcon();
            if (icon != null) {
                e.getPresentation().setIcon(ExecutionUtil.getLiveIndicator(icon));
            }
        });

        KeyboardShortcut keyboardShortcut = ActionManager.getInstance().getKeyboardShortcut("ShowFilterPopup");
        if (keyboardShortcut != null) {
            toolbar.setSecondaryActionsShortcut(KeymapUtil.getShortcutText(keyboardShortcut));
        }

        return toolbar;
    }

    @NotNull
    private ActionToolbarImpl createToolbar(@NotNull ActionGroup group) {
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance()
                .createActionToolbar(ActionPlaces.EDITOR_TOOLBAR, group, true);
        toolbar.setTargetComponent(this);
        toolbar.setLayoutPolicy(ActionToolbar.AUTO_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        return toolbar;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    public static final class Builder {
        private final Project myProject;
        private final JComponent myTargetComponent;

        private final DefaultActionGroup mySearchActions = DefaultActionGroup.createFlatGroup(() -> "search bar 1");
        private final DefaultActionGroup myExtraSearchActions = DefaultActionGroup.createFlatGroup(() -> "search bar 2");
        private ExcelReaderMainPanel toolWindow;


        private Builder(@Nullable Project project, @NotNull JComponent component) {
            myProject = project;
            myTargetComponent = component;
        }

        @NotNull
        public Builder addToolWindow(@NotNull ExcelReaderMainPanel toolWindow) {
            this.toolWindow = toolWindow;
            return this;
        }

        @NotNull
        public Builder addPrimarySearchActions(AnAction @NotNull ... actions) {
            mySearchActions.addAll(actions);
            return this;
        }

        @NotNull
        public Builder addExtraSearchActions(AnAction @NotNull ... actions) {
            myExtraSearchActions.addAll(actions);
            return this;
        }

        @NotNull
        public MySearchReplaceComponent build() {
            return new MySearchReplaceComponent(myProject,
                    myTargetComponent,
                    mySearchActions,
                    myExtraSearchActions,
                    toolWindow,
                    false,
                    false,
                    false
            );
        }
    }

    private static class MyTextComponentWrapper extends Wrapper {
        @Nullable
        public JTextComponent getTextComponent() {
            JComponent wrapped = getTargetComponent();
            return wrapped != null ? unwrapTextComponent(wrapped) : null;
        }

        @NotNull
        protected static JTextComponent unwrapTextComponent(@NotNull JComponent wrapped) {
            if (wrapped instanceof SearchTextField) {
                return ((SearchTextField) wrapped).getTextEditor();
            }
            if (wrapped instanceof SearchTextArea) {
                return ((SearchTextArea) wrapped).getTextArea();
            }
            throw new AssertionError();
        }
    }

    private class TransferFocusAction extends DumbAwareAction {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            Component focusOwner = IdeFocusManager.getInstance(myProject).getFocusOwner();
            if (UIUtil.isAncestor(MySearchReplaceComponent.this, focusOwner)) {
                focusOwner.transferFocus();
            }
        }
    }

    private class TransferFocusBackwardAction extends DumbAwareAction {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            Component focusOwner = IdeFocusManager.getInstance(myProject).getFocusOwner();
            if (UIUtil.isAncestor(MySearchReplaceComponent.this, focusOwner)) {
                focusOwner.transferFocusBackward();
            }
        }
    }
}
