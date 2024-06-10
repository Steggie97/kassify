package com.ls.kassify.data

data class Transaction(
    var transId: Int = 0,
    var date: String = "01.01.2024",
    var amount: Double = 0.00,
    var category: String = "",
    var receiptNo: String = "",
    var text: String = "",
)
/*
val transactions: List<Transaction> = listOf(
    Transaction(transId = 0, date = "02.05.2024", amount = -30.50, category = "laufende KFZ-Kosten", receiptNo = "Rg-Nr.12342", text = "Aral - tanken"),
    Transaction(transId = 1, date = "03.05.2024", amount = 45.00, category = "Erlöse 19%", receiptNo = "RE2024/1", text = "Rechnung RE2024/1"),
    Transaction(transId = 2, date = "10.05.2024", amount = -10.78, category = "Büromaterial", receiptNo = "2", text = "Michelbrink - Schreibwaren"),
    Transaction(transId = 3, date = "21.05.2024", amount = 65.00, category = "Erlöse 19%", receiptNo = "RE2024/2", text = "Rechnung RE2024/2"),
    Transaction(transId = 4, date = "22.05.2024", amount = -111500.00, category = "Geldtransit", receiptNo = "", text = "Bankeinzahlung")
)

 */