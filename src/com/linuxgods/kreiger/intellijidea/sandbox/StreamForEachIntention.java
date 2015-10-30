package com.linuxgods.kreiger.intellijidea.sandbox;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

public class StreamForEachIntention extends PsiElementBaseIntentionAction {

    public StreamForEachIntention() {
        super();
        setText("Stream forEach");
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        return element.isWritable() && (matches(element) || matches(getParentOfType(element, PsiLambdaExpression.class)));
    }

    private boolean matches(@Nullable PsiElement element) {
        final PsiMethodCallExpression parent = getParentOfType(element, PsiMethodCallExpression.class);
        if (null == parent) {
            return false;
        }
        PsiMethod method = parent.resolveMethod();
        if (method == null) {
            return false;
        }
        PsiClass containingClass = method.getContainingClass();
        if (containingClass == null) {
            return false;
        }
        if (!"java.util.stream.Stream".equals(containingClass.getQualifiedName())) {
            return false;
        }
        PsiExpressionList argumentList = parent.getArgumentList();
        PsiExpression[] arguments = argumentList.getExpressions();
        if (arguments.length != 1) {
            return false;
        }
        if (!(arguments[0] instanceof PsiLambdaExpression)) {
            return false;
        }
        return true;
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getText();
    }

    @NotNull
    @Override
    public String getText() {
        return super.getText();
    }
}
