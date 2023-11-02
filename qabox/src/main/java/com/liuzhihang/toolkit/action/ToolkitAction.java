package com.liuzhihang.toolkit.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.liuzhihang.toolkit.ui.ToolkitForm;
import com.liuzhihang.toolkit.utils.CustomPsiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 唤起操作面板
 *
 * @author liuzhihang
 * @date 2021/4/16 00:00
 */
public class ToolkitAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        // 获取当前project对象
        Project project = e.getData(PlatformDataKeys.PROJECT);
        // 获取当前编辑的文件, 可以进而获取 PsiClass, PsiField 对象
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        // 获取虚拟文件
        // VirtualFile actionFolder = event.getData(LangDataKeys.VIRTUAL_FILE);


        if (project == null ) {
            return;
        }

        ToolkitForm.getInstance(project, editor, psiFile).popup();

    }
}
