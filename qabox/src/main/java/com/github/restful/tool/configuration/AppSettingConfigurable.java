/*
  Copyright (C), 2018-2020, ZhangYuanSheng
  FileName: AppSettingsConfigurable
  Author:   ZhangYuanSheng
  Date:     2020/5/27 18:06
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.github.restful.tool.configuration;

import com.github.restful.tool.utils.data.Bundle;
import com.github.restful.tool.view.window.options.AppSettingWindow;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author ZhangYuanSheng
 * @version 1.0
 */
public class AppSettingConfigurable implements Configurable {

    private AppSettingWindow settingsComponent;

    /**
     * issue: 导入Kotlin插件后编译错误, Nls位置不在org.jetbrains.annotations
     * `@Nls(capitalization = Nls.Capitalization.Title)`
     */
    @Override
    public String getDisplayName() {
        return Bundle.getString("setting.configurable.displayName");
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return settingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsComponent = new AppSettingWindow();
        return settingsComponent.getContent();
    }

    @Override
    public boolean isModified() {
        return AppSetting.getInstance().isModified(settingsComponent.getAppSetting());
    }

    @Override
    public void apply() {
        AppSetting.getInstance().setAppSetting(settingsComponent.getAppSetting());
    }

    @Override
    public void reset() {
        AppSetting settings = AppSetting.getInstance();
        settingsComponent.setAppSetting(settings.getAppSetting());
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
