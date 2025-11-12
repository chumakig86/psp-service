package org.example.pspservice.exception

import org.example.pspservice.model.PaymentResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): Map<String, String> {
        return ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage.orEmpty() }
    }

    @ExceptionHandler(InvalidCardException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: InvalidCardException): PaymentResponse {
        return PaymentResponse("", "Failed", ex.message)
    }
}