package com.todayilearned.til.ui.theme


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.todayilearned.til.R
import com.todayilearned.til.ui.screens.settings.ConfigStore
import java.io.File

val InterFont = FontFamily(
    Font(R.font.inter_regular)
)

val Noto = FontFamily(
    Font(R.font.notosans_regular)
)

val ComicFont = FontFamily(
    Font(R.font.comic_regular)
)

val IBMsans = FontFamily(
    Font(R.font.ibm_sans)
)

@Composable
fun getDynamicFontFamily(): FontFamily {
    val context = LocalContext.current
    val fontKey by ConfigStore.fontKeyFlow(context).collectAsState(initial = "inter")

    return remember(fontKey) {
        when (fontKey) {
            "comic" -> try {
                ComicFont
            } catch (e: Exception) {
                InterFont
            }
            "IBMsans" -> try {
                IBMsans
            }
            catch (e: Exception){
                ComicFont
            }
            "inter" -> try {
                InterFont
            } catch (e: Exception) {
                ComicFont
            }

            "noto" -> try {
                Noto
            } catch (e: Exception) {
                InterFont
            }

            "custom" -> {
                val customFile = File(context.filesDir, "your_custom.ttf")
                if (customFile.exists()) {
                    try {
                        FontFamily(androidx.compose.ui.text.font.Font(customFile))
                    } catch (e: Exception) {
                        InterFont
                    }
                } else {
                    InterFont
                }
            }

            else -> InterFont
        }
    }
}