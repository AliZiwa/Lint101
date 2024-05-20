package com.example.outerlinter

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiModifierList
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UFile
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UVariable

class StringLiteralDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            id = "StringLiteral",
            briefDescription = "String literal usage",
            explanation = "We should not use string literals in .kt files",
            category = Category.I18N,
            severity = Severity.ERROR,
            implementation = Implementation(
                StringLiteralDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(ULiteralExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitLiteralExpression(node: ULiteralExpression) {
                if (!node.isString) return

                if (isInComposablePreview(node)) return

                if(nodeViolatesLintRule(node)) {
                    reportIssue(context, node)
                }
            }
        }
    }

    private fun reportIssue(
        context: JavaContext,
        node: ULiteralExpression
    ) {
        context.report(
            issue = ISSUE,
            message ="String literals should be final or live in the strings.xml file for reference.",
            scope = node,
            location = context.getNameLocation(node)
        )
    }

    private fun nodeViolatesLintRule(node: ULiteralExpression): Boolean {
        val parent = node.uastParent
        if (parent is UVariable) {
            val modifiers = parent.modifierList
            if (modifiers != null && violatesConstantRule(modifiers)) return true
        }
        return false
    }

    private fun violatesConstantRule(modifiers: PsiModifierList): Boolean {
        return !modifiers.hasModifierProperty("const")
    }

    private fun isInComposablePreview(node: ULiteralExpression): Boolean {
        val containingFunction = node.getContainingUMethod()
        return containingFunction?.hasPreviewAnnotation() == true
    }

    private fun UElement?.getContainingUMethod(): UMethod? {
        var element = this
        while (element != null && element !is UFile) {
            if (element is UMethod) {
                return element
            }
            element = element.uastParent
        }
        return null
    }

    private fun UMethod.hasPreviewAnnotation(): Boolean {
        return this.annotations.any { it.qualifiedName == "androidx.compose.ui.tooling.preview.Preview" }
    }
}