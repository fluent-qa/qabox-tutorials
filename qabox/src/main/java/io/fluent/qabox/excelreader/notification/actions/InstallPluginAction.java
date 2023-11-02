package io.fluentqa.qaplugins.excelreader.notification.actions;

import com.intellij.ide.plugins.PluginManagerConfigurable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;


public class InstallPluginAction extends DumbAwareAction {
  private final String mySearchOption;

  public InstallPluginAction(@NotNull @Nls String text, @NotNull String searchOption) {
    super(text);
    mySearchOption = searchOption;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), PluginManagerConfigurable.class, c -> c.enableSearch(mySearchOption));
  }
}