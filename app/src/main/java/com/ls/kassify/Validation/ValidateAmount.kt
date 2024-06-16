package com.ls.kassify.Validation

import java.text.NumberFormat

class ValidateAmount {
    fun execute(amount: Double, isPositive: Boolean, cashBalance: Double): ValidationResult {
        if (!isPositive && (cashBalance - amount < 0.00)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Der Kassenbestand darf nicht negativ sein (${
                    NumberFormat.getCurrencyInstance().format(cashBalance - amount)
                })."
            )
        }

        if (amount == 0.00) {
            return ValidationResult(
                successful = false,
                errorMessage = "Der Betrag darf nicht ${
                    NumberFormat.getCurrencyInstance().format(0.00)
                } betragen."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}