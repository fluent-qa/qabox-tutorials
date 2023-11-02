package io.fluentqa.qaplugins.excelreader.ui.vcs;

import com.intellij.diff.DiffContext;
import com.intellij.diff.FrameDiffTool;
import com.intellij.diff.contents.*;
import com.intellij.diff.requests.ContentDiffRequest;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ExcelDiffViewer implements FrameDiffTool.DiffViewer {

    private final VcsExcelPanel vscDiffPanel;
    private final JPanel myPanel;
    private final String myHelpID = "vsc.diff.excel";
    public ExcelDiffViewer(@NotNull DiffContext context,
                           @NotNull ContentDiffRequest request) {

        DiffContent leftContent = request.getContents().get(0);
        String leftTitle = request.getContentTitles().get(0);
        String rightTitle = request.getContentTitles().get(1);
        FileContentImpl rightFile = (FileContentImpl)request.getContents().get(1);

        try {
            if (leftContent instanceof EmptyContent || "Base Version".equalsIgnoreCase(leftTitle)) {
                byte[] bytes = readFile(rightFile);
                if (bytes == null) {
                    throw new RuntimeException("Excel vcs diff: file is null");
                }
                File rightTmp = FileUtil.createTempFile(".exceleditor_b_", "." + rightFile.getFile().getExtension(), true);
                FileUtil.writeToFile(rightTmp, bytes);
                VirtualFile rightVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(rightTmp);
                if (rightVirtualFile == null) {
                    throw new RuntimeException("Excel vcs diff: right file is null");
                }
                vscDiffPanel = new VcsExcelViewPanel(rightVirtualFile,rightTitle);
            } else {
                FileContentImpl leftFile = (FileContentImpl)request.getContents().get(0);

                byte[] leftBytes = readFile(leftFile);
                byte[] rightBytes = readFile(rightFile);

                if (leftBytes == null || rightBytes == null) {
                    throw new RuntimeException("Excel vcs diff: file is null");
                }

                File leftTmp = FileUtil.createTempFile(".exceleditor_a_", "." + leftFile.getFile().getExtension(), true);
                FileUtil.writeToFile(leftTmp, leftBytes);

                File rightTmp = FileUtil.createTempFile(".exceleditor_b_", "." + rightFile.getFile().getExtension(), true);
                FileUtil.writeToFile(rightTmp, rightBytes);

                VirtualFile leftVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(leftTmp);
                VirtualFile rightVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(rightTmp);

                if (leftVirtualFile == null || rightVirtualFile == null) {
                    throw new RuntimeException("Excel vcs diff: file is null");
                }
                vscDiffPanel = new VcsExcelDiffPanel(leftVirtualFile, rightVirtualFile,
                        leftTitle,rightTitle);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        myPanel = new JPanel(new BorderLayout());
        myPanel.add(vscDiffPanel.getPanel(), BorderLayout.CENTER);
    }

    private static byte[] readFile(DiffContent content) {
        if (content instanceof EmptyContent) {
            return null; // empty side of "addition/deletion"
        }
        if (content instanceof FileContent) {
            VirtualFile file = ((FileContent)content).getFile();
            try {
                return ReadAction.compute(file::contentsToByteArray);
            }
            catch (IOException e) {
                return null; // smth bad
            }
        }
        if (content instanceof DocumentContent) {
            // text content with no assigned
            String text = ((DocumentContent)content).getDocument().getText();
            return null; // can try to parse as CSV? Or checked in canShow()
        }

        // Have no virtual file.
        // Typically, should not happen, but it might be some exotic.
        // Should have been checked in DiffTool.canShow()
        throw new IllegalStateException();
    }

    @Override
    public @NotNull JComponent getComponent() {
        return myPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return vscDiffPanel.getPanel();
    }

    @Override
    public FrameDiffTool.@NotNull ToolbarComponents init() {
        FrameDiffTool.ToolbarComponents components = new FrameDiffTool.ToolbarComponents();
        int size = vscDiffPanel.getDiffResultSize();
        components.statusPanel = new JLabel(size + " difference");
        return components;
    }

    @Override
    public void dispose() {
        Disposer.dispose(vscDiffPanel);
    }
}
