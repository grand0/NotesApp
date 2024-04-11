package ru.example.qa.notesapp.data.local.storage

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStorageManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun saveToAppStorage(uri: Uri): String {
        val id = uri.lastPathSegment ?: throw IllegalArgumentException("URI is empty")
        val appFile = File(context.filesDir, id)
        if (!appFile.exists()) {
            context.contentResolver.openInputStream(uri)?.use { input ->
                appFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return id
    }

    fun getFileUri(id: String): Uri {
        return Uri.withAppendedPath(context.filesDir.toUri(), id)
    }
}