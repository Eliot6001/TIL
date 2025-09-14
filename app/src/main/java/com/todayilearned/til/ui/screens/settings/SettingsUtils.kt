package com.todayilearned.til.ui.screens.settings

import android.content.Context
import android.graphics.Typeface
import android.net.Uri

import com.todayilearned.til.ui.ViewModel.TilRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.io.File
import java.io.InputStream
import kotlinx.serialization.json.Json


    suspend fun replaceCustomFontFile(input: InputStream, ctx: Context): File = withContext(Dispatchers.IO) {
        val outFile = File(ctx.filesDir, "your_custom.ttf")
        input.use { ins ->
            outFile.outputStream().use { outs ->
                ins.copyTo(outs)
            }
        }
        outFile
    }



    suspend fun exportDataToUri(uri: Uri, ctx: Context, repo: TilRepository) {
        withContext(Dispatchers.IO) {
            val json = repo.exportBackupJson()
            ctx.contentResolver.openOutputStream(uri)?.use { out ->
                out.write(json.toByteArray(Charsets.UTF_8))
                out.flush()
            }
        }
    }

    suspend fun importDataFromStream(stream: InputStream, ctx: Context, repo: TilRepository) {
        withContext(Dispatchers.IO) {
            val text = stream.bufferedReader().use { it.readText() }
            repo.importBackupJson(text)
        }
    }