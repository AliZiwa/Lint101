package com.example.linter

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class LinterRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(StringLiteralDetector.ISSUE, ExperimentalApiDetector.ISSUE)
    override val api: Int
        get() = CURRENT_API
    override val vendor: Vendor?
        get() = Vendor(
            vendorName = "Sensei lee",
            identifier = "sensei_advanced_linters"
        )
}