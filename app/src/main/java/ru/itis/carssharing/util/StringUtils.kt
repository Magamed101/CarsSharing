package ru.itis.carssharing.util

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun String.getMd5Hash(): String {
    return try {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(toByteArray())
        val number = BigInteger(1, messageDigest)
        var md5 = number.toString(16)
        while (md5.length < 32) {
            md5 = "0$md5"
        }
        md5
    } catch (e: NoSuchAlgorithmException) {
        this
    }
}