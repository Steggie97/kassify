package com.ls.kassify.data

import java.time.LocalDate

data class Transaction(
    var transId: Int = 0,
    var date: LocalDate = LocalDate.now(),
    var isPositiveAmount: Boolean = true,
    var amount: Double = 0.00,
    var category: String = "",
    var receiptNo: String = "",
    var text: String = "",
)