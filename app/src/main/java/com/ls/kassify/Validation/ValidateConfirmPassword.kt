package com.ls.kassify.Validation

class ValidateConfirmPassword {

    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "Die Passwörter stimmen nicht überein."
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}