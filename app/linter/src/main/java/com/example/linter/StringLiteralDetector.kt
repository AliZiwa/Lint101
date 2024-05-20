package com.example.linter

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner

class StringLiteralDetector : Detector(), SourceCodeScanner {
    companion object  {
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

    override fun getApplicableMethodNames(): List<String>? {
        return super.getApplicableMethodNames()
    }
}