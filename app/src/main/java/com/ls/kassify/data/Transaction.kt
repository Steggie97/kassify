package com.ls.kassify.data

data class Transaction(
    val date: String,
    val amount: Double,
    val category: String,
    val receiptNo: String,
    val text: String,
)

val transactions: List<Transaction> = listOf(
    Transaction("02.05.2024", -30.50, category = "laufende KFZ-Kosten", receiptNo = "Rg-Nr.12342", text = "Aral - tanken"),
    Transaction("03.05.2024", amount = 45.00, category = "Erlöse 19%", receiptNo = "RE2024/1", text = "Rechnung RE2024/1"),
    Transaction(date = "10.05.2024", amount = -10.78, category = "Büromaterial", receiptNo = "2", text = "Michelbrink - Schreibwaren"),
    Transaction(date = "21.05.2024", amount = 65.00, category = "Erlöse 19%", receiptNo = "RE2024/2", text = "Rechnung RE2024/2"),
    Transaction(date = "22.05.2024", amount = -111500.00, category = "Geldtransit", receiptNo = "", text = "Bankeinzahlung")
)