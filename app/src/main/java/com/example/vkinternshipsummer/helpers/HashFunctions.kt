package com.example.vkinternshipsummer.helpers

import java.io.File
import java.io.FileInputStream
import java.security.DigestInputStream
import java.security.MessageDigest

fun generateHashCode(path: String): String? {
    val file = File(path)
    return if (file.isFile) {
        if (file.length() == 0L) {
            generateHashCodeForEmptyFile(file)
        } else {
            generateHashCodeForFile(file)
        }
    } else {
        null
    }
}

private fun generateHashCodeForFile(file: File): String {
    val md = MessageDigest.getInstance("SHA-256")

    FileInputStream(file).use { fis ->
        DigestInputStream(fis, md).use { dis ->
            val buffer = ByteArray(8192)
            var bytesRead = dis.read(buffer)
            while (bytesRead != -1) {
                md.update(buffer, 0, bytesRead)
                bytesRead = dis.read(buffer)
            }
        }
    }

    md.update(file.lastModified().toByte())

    val hashBytes = md.digest()
    return bytesToHexString(hashBytes)
}

private fun generateHashCodeForEmptyFile(file: File): String {
    val filePathBytes = "${file.absolutePath}${file.name}".toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    md.update(filePathBytes)

    md.update(file.lastModified().toByte())

    val hashBytes = md.digest()
    return bytesToHexString(hashBytes)
}

private fun bytesToHexString(bytes: ByteArray): String {
    val hexChars = CharArray(bytes.size * 2)
    for (i in bytes.indices) {
        val v = bytes[i].toInt() and 0xFF
        hexChars[i * 2] = "0123456789ABCDEF"[v.ushr(4)]
        hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
    }
    return String(hexChars)
}