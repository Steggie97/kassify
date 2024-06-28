package com.ls.kassify.data

import java.time.LocalDate

data class Transaction(
    var transNo: Int = 0,
    var date: LocalDate = LocalDate.now(),
    var isPositiveAmount: Boolean = true,
    var amount: Double = 0.00,
    var accountNo: Int  = 1600,
    var category: String = "keine Zuordnung",
    var vat: String = "keine",
    var receiptNo: String = "",
    var text: String = "",
)