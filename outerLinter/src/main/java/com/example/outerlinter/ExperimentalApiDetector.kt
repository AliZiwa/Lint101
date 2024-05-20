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
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UElement

class ExperimentalApiDetector : Detector(), SourceCodeScanner {
    companion object {
        val ISSUE = Issue.create(
            id = "ExperimentalAPI",
            briefDescription = "Experimental API literal usage",
            explanation = "Recording experimental apis",
            category = Category.I18N,
            severity = Severity.ERROR,
            implementation = Implementation(
                ExperimentalApiDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UAnnotation::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitAnnotation(node: UAnnotation) {
                if (node.qualifiedName != "kotlin.ExperimentalStdlibApi") return
                context.report(
                    issue = ISSUE,
                    message =
                    """
                        | 
                        |Marking Experimental api usages.
                        |
                        """.trimMargin(),
                    scope = node,
                    location = context.getNameLocation(node)
                )
            }
        }
    }
}