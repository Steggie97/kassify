package com.ls.kassify.Validation

import java.text.NumberFormat
class ValidateAmount {
    // modular validation for the amount and cashBalance.
    fun execute(amount: Double, isPositive: Boolean, cashBalance: Double): ValidationResult {
        // case negative cashBalance
        if (!isPositive && (cashBalance - amount < 0.00)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Der Kassenbestand darf nicht negativ sein (${
                    NumberFormat.getCurrencyInstance().format(cashBalance - amount)
                })."
            )
        }

        // case transaction-amount == 0.00
        if (amount == 0.00) {
            return ValidationResult(
                successful = false,
                errorMessage = "Der Betrag darf nicht ${
                    NumberFormat.getCurrencyInstance().format(0.00)
                } betragen."
            )
        }

        // validation is successful
        return ValidationResult(
            successful = true
        )
    }
}