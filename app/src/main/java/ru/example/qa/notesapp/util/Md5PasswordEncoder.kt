package ru.example.qa.notesapp.util

import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class Md5PasswordEncoder @Inject constructor() : PasswordEncoder {

    private val md = MessageDigest.getInstance("MD5")

    override fun hash(password: String): String {
        md.reset()
        md.update(password.toByteArray())
        return BigInteger(1, md.digest()).toString(16).lowercase()
    }

    override fun check(plain: String, hashed: String): Boolean {
        return hash(plain) == hashed
    }
}