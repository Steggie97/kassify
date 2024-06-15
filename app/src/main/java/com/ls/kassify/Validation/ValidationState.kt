package com.ls.kassify.Validation

data class ValidationState (
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,
    val amountError: String? = null
)