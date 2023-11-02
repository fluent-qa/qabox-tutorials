package io.fluentqa.qaplugins.excelreader.ui.vcs;

import com.intellij.diff.DiffContext;
import com.intellij.diff.FrameDiffTool;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.contents.EmptyContent;
import com.intellij.diff.contents.FileContent;
import com.intellij.diff.requests.ContentDiffRequest;
import com.intellij.diff.requests.DiffRequest;
import io.fluentqa.qaplugins.excelreader.file.MyExcelNativeFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author piercebrands
 */
public class ExcelDiffTool implements FrameDiffTool {
    @Override
    public @NotNull DiffViewer createComponent(@NotNull DiffContext diffContext, @NotNull DiffRequest diffRequest) {
        return createViewer(diffContext, (ContentDiffRequest) diffRequest);
    }

    @NotNull
    public static FrameDiffTool.DiffViewer createViewer(@NotNull DiffContext context,
                                                        @NotNull ContentDiffRequest request) {
        return new ExcelDiffViewer(context, request);
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getName() {
        return "Excel diff viewer";
    }

    private static boolean canShowContent(@NotNull DiffContent content) {
        if (content instanceof EmptyContent) {
            return true;
        }
        return content instanceof FileContent &&
                content.getContentType() instanceof MyExcelNativeFileType &&
                ((FileContent) content).getFile().isValid();
    }

    @Override
    public boolean canShow(@NotNull DiffContext diffContext, @NotNull DiffRequest diffRequest) {
        if (!(diffRequest instanceof ContentDiffRequest)) {
            return false;
        }
        List<DiffContent> contents = ((ContentDiffRequest)diffRequest).getContents();
        if (contents.size() != 2) {
            return false;
        }

        return canShowContent(contents.get(0)) && canShowContent(contents.get(1));
    }
}
