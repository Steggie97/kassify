package com.ls.kassify.Validation

import android.util.Patterns

class ValidateEmail {

    fun execute(email:String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Das E-Mailfeld darf nicht leer sein."
            )
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Bitte geben Sie eine gültige E-Mail-Adresse ein."
            )
        }

        return ValidationResult(
            successful = true
        )
    }

}