package com.project.appealic.utils

import android.text.TextUtils
import android.util.Log
import android.widget.Toast

class ValidationUtils {
    companion object {
        const val VALID = 0
        const val EMPTY_ERROR = 1
        const val EMAIL_MISMATCH_ERROR = 2
        const val PASSWORD_LENGTH_ERROR = 3
        const val PASSWORD_TYPE_ERROR = 4
        const val PASSWORD_MISMATCH_ERROR = 5
        const val PHONE_MISMATCH_ERROR = 6

        fun isValidEmail(email: String): Int {
            if (TextUtils.isEmpty(email)) {
                return EMPTY_ERROR
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return EMAIL_MISMATCH_ERROR
            }

            return VALID
        }

        fun isValidPassword(password: String): Int {
            if (TextUtils.isEmpty(password)) {
                return EMPTY_ERROR
            } else if (password.length < 8) {
                return PASSWORD_LENGTH_ERROR
            } else if (!password.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d\\S]+\$"))) {
                return PASSWORD_TYPE_ERROR
            }

            return VALID
        }

        fun checkConfirmPassword(firstPassword: String, secondPassword: String): Int {
            if (firstPassword != secondPassword) {
                return PASSWORD_MISMATCH_ERROR
            }

            return VALID
        }

        fun isValidPhoneNumber(phone: String): Int {
            val regexPattern = Regex("^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}\$")

            if (TextUtils.isEmpty(phone)) {
                return EMPTY_ERROR
            } else if (!regexPattern.matches(phone)) {
                return PHONE_MISMATCH_ERROR
            }

            return VALID
        }
    }
}