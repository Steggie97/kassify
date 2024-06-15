package com.ls.kassify.Validation

import java.text.NumberFormat

class ValidateAmount {
    fun execute(amount: Double, cashBalance: Double): ValidationResult {
        if(cashBalance - amount < 0.00) {
            return ValidationResult(
                successful = false,
                errorMessage = "Der Kassenbestand darf nicht negativ sein (${NumberFormat.getCurrencyInstance().format(cashBalance - amount)})."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}