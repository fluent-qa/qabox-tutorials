package com.liuzhihang.toolkit.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.psi.*;
import com.liuzhihang.toolkit.config.Settings;
import com.liuzhihang.toolkit.constants.Constants;
import org.jetbrains.annotations.NotNull;

/**
 * @author liuzhihang
 * @date 2021/5/29 13:01
 */
public class SettingsHandlerUtils {

    public static String paramNameHandler(Settings settings, String jsonKey) {

        jsonKey = jsonKey.trim();

        // 是否为关键字
        if (Constants.KEY_WORD_LIST.contains(jsonKey)) {
            return jsonKey + "X";
        }
        // 是否需要驼峰转换
        if (settings.getCamelcase()) {

            if (jsonKey.contains("_") || jsonKey.contains("-") || jsonKey.contains(" ")) {
                String[] split = jsonKey.split("[_\\- ]");

                StringBuilder camelCased = new StringBuilder();
                for (int i = 1; i < split.length; i++) {
                    camelCased.append(split[i].substring(0, 1).toUpperCase()).append(split[i].substring(1));
                }
                return split[0] + camelCased;

            }


        }
        return jsonKey;
    }


    public static void lombokHandler(@NotNull Settings settings, @NotNull PsiElementFactory psiElementFactory, @NotNull PsiClass psiClass) {

        if (!settings.getUseLombok()) {
            return;
        }

        PsiModifierList modifierList = psiClass.getModifierList();

        if (modifierList == null) {
            return;
        }

        if (settings.getLombokData() && psiClass.getAnnotation("lombok.Data") == null) {
            PsiAnnotation annotationFromText = psiElementFactory.createAnnotationFromText("@lombok.Data", psiClass);
            modifierList.addBefore(annotationFromText, modifierList.getFirstChild());
        }
        if (settings.getLombokGetter() && psiClass.getAnnotation("lombok.Getter") == null) {
            PsiAnnotation annotationFromText = psiElementFactory.createAnnotationFromText("@lombok.Getter", psiClass);
            modifierList.addBefore(annotationFromText, modifierList.getFirstChild());
        }
        if (settings.getLombokSetter() && psiClass.getAnnotation("lombok.Setter") == null) {
            PsiAnnotation annotationFromText = psiElementFactory.createAnnotationFromText("@lombok.Setter", psiClass);
            modifierList.addBefore(annotationFromText, modifierList.getFirstChild());
        }
        if (settings.getLombokBuilder() && psiClass.getAnnotation("lombok.Builder") == null) {
            PsiAnnotation annotationFromText = psiElementFactory.createAnnotationFromText("@lombok.Builder", psiClass);
            modifierList.addBefore(annotationFromText, modifierList.getFirstChild());
        }

    }

    public static void commentHandler(@NotNull Settings settings, @NotNull PsiElementFactory psiElementFactory,
                                      @NotNull PsiClass psiClass, @NotNull String commentJson) {

        if (!settings.getGenerateComments()) {
            return;
        }

        PsiField[] fields = psiClass.getFields();

        if (fields.length == 0) {
            return;
        }

        try {
            JsonObject jsonObject = JsonParser.parseString(commentJson).getAsJsonObject();

            String commentText
                    = "/* \n"
                    + "Create from Json string by IDEA plugin —— Toolkit\n\n"
                    + GsonFormatUtil.gsonFormat(new GsonBuilder().serializeNulls().setPrettyPrinting().create(), jsonObject)
                    + "\n*/";

            PsiField psiField = fields[0];

            PsiComment commentFromText = psiElementFactory.createCommentFromText(commentText, psiField);

            psiClass.addBefore(commentFromText, psiField);

        } catch (Exception ignored) {
        }

    }

}
