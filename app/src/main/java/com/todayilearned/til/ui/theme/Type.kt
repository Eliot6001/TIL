package com.todayilearned.til.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
fun getAppTypography(scale: Float = 1.0f, fontFamily: FontFamily = InterFont) = Typography(
    // Display styles
    displayLarge = TextStyle(
        fontSize = (57 * scale).sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (64 * scale).sp,
        fontFamily = fontFamily
    ),
    displayMedium = TextStyle(
        fontSize = (45 * scale).sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (52 * scale).sp,
        fontFamily = fontFamily
    ),
    displaySmall = TextStyle(
        fontSize = (36 * scale).sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (44 * scale).sp,
        fontFamily = fontFamily
    ),

    // Headline styles
    headlineLarge = TextStyle(
        fontSize = (32 * scale).sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = (40 * scale).sp,
        fontFamily = fontFamily
    ),
    headlineMedium = TextStyle(
        fontSize = (28 * scale).sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = (36 * scale).sp,
        fontFamily = fontFamily
    ),
    headlineSmall = TextStyle(
        fontSize = (24 * scale).sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = (32 * scale).sp,
        fontFamily = fontFamily
    ),

    // Title styles - THESE ARE THE MISSING ONES YOU NEED!
    titleLarge = TextStyle(
        fontSize = (22 * scale).sp,
        fontWeight = FontWeight.Medium,
        lineHeight = (28 * scale).sp,
        fontFamily = fontFamily
    ),
    titleMedium = TextStyle(
        fontSize = (16 * scale).sp,
        fontWeight = FontWeight.Medium,
        lineHeight = (24 * scale).sp,
        fontFamily = fontFamily
    ),
    titleSmall = TextStyle(
        fontSize = (14 * scale).sp,
        fontWeight = FontWeight.Medium,
        lineHeight = (20 * scale).sp,
        fontFamily = fontFamily
    ),

    // Body styles
    bodyLarge = TextStyle(
        fontSize = (16 * scale).sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (24 * scale).sp,
        fontFamily = fontFamily
    ),
    bodyMedium = TextStyle(
        fontSize = (14 * scale).sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (20 * scale).sp,
        fontFamily = fontFamily
    ),
    bodySmall = TextStyle(
        fontSize = (12 * scale).sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (16 * scale).sp,
        fontFamily = fontFamily
    ),

    // Label styles
    labelLarge = TextStyle(
        fontSize = (14 * scale).sp,
        fontWeight = FontWeight.Medium,
        lineHeight = (20 * scale).sp,
        fontFamily = fontFamily
    ),
    labelMedium = TextStyle(
        fontSize = (12 * scale).sp,
        fontWeight = FontWeight.Medium,
        lineHeight = (16 * scale).sp,
        fontFamily = fontFamily
    ),
    labelSmall = TextStyle(
        fontSize = (11 * scale).sp,
        fontWeight = FontWeight.Medium,
        lineHeight = (16 * scale).sp,
        fontFamily = fontFamily
    )
)