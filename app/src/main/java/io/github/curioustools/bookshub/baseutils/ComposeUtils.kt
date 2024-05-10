package io.github.curioustools.bookshub.baseutils

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.curioustools.bookshub.baseutils.MyComposeColors.Green
import io.github.curioustools.bookshub.baseutils.MyComposeColors.Green40
import io.github.curioustools.bookshub.baseutils.MyComposeColors.Green80
import io.github.curioustools.bookshub.baseutils.MyComposeColors.Green95
import io.github.curioustools.bookshub.baseutils.MyComposeColors.GreenGrey80

object ComposeUtils{

    @Composable
    fun AppTheme(showDynamicColor: Boolean = true, content: @Composable () -> Unit) {
        val darkTheme = isSystemInDarkTheme()
        val systemOSGreaterThanS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        val ctx = LocalContext.current
        val colorScheme = when {
            showDynamicColor && systemOSGreaterThanS && darkTheme -> dynamicDarkColorScheme(ctx)
            showDynamicColor && systemOSGreaterThanS && !darkTheme -> dynamicLightColorScheme(ctx)
            darkTheme -> darkColorScheme(
                primary = Green40,
                secondary = GreenGrey80,
                tertiary = Green80
            )
            else -> lightColorScheme(
                primary = Green,
                secondary = Green,
                tertiary = Green40,
                background = Green95,
                surface = Green95,
                onPrimary = Color.White,
                onSecondary = Color.White,
                onTertiary = Color.White,
                onBackground = Color(0xFF1C1B1F),
                onSurface = Color(0xFF1C1B1F),


            )
        }
        MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
    }


    val Typography = Typography(
        bodyLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
        titleLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp),
        labelSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp)
    )






}


