package com.ls.kassify.Validation

class ValidatePassword {

    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "Das Passwort muss mindestens 8 Zeichen enthalten."
            )
        }

        if (!(password.any { it.isDigit() } && password.any { it.isLowerCase() } && password.any { it.isUpperCase() } && password.any { it.isLetter() })) {
            return ValidationResult(
                successful = false,
                errorMessage = "Das Passwort muss mindestens einen Großbuchstaben, einen Kleinbuchstaben und eine Zahl enthalten."
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}