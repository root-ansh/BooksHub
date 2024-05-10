package io.github.curioustools.bookshub.baseutils

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.core.graphics.toColorInt
import java.util.Date
import java.util.Locale
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt

fun lipsum(count:Int):String{
    return LoremIpsum(count).values.joinToString()
}


fun Date.toCustomDateString() = time.toCustomDateString()

fun Long.toCustomDateString(): String {
    val currentTime = System.currentTimeMillis()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val currentCalendar = Calendar.getInstance()

    val format = SimpleDateFormat("dd MMM, hh:mm aa", Locale.getDefault())

    return when {
        calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) - 1 -> {
            "Last Year"
        }
        calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) + 1 -> {
            "Upcoming"
        }
        Math.abs(this - currentTime) <= 3600000 -> {
            "Just Now"
        }
        calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH) -> {
            "Today, " + SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(calendar.time)
        }

        else -> {
            format.format(calendar.time)
        }
    }
}



@SuppressLint("ModifierFactoryUnreferencedReceiver")
inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}


fun ImageVector.toIcon(modifier: Modifier = Modifier, desc: String = "icon",tint:Color?= Color.Black): ComposableBlock {
    return { Icon(imageVector = this, contentDescription = desc, modifier = modifier, tint = tint?:LocalContentColor.current) }

}

typealias ComposableBlock = @Composable () -> Unit



class ComposableBlockSaver(private  val block: @Composable ()->Unit){

    @Composable
    fun print(){
        block()
    }
}


tailrec fun Context.getComposeParentActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getComposeParentActivity()
    else -> null
}

fun Double.roundOff(decimalPlaces: Int=2): String {
    val multiplier = 10.0.pow(decimalPlaces.toDouble())
    val roundedValue = round(this * multiplier) / multiplier
    return roundedValue.toString()
}

fun Float.roundOff(decimalPlaces: Int = 3, ignoreEmptyZeroes: Boolean = true): String { //3.000233, decimal places: 3
    val multiplier = 10.0.pow(decimalPlaces.toDouble()) // 1000
    val roundedValue = round(this * multiplier) / multiplier // r(3.000233*1000)/1000 = r(3000.233)/1000 = 3000/1000 = 3.000
    val stringValue = roundedValue.toString()
    val mantissa = stringValue.substringAfter('.')
    var mantissaIsOnlyZeroes = true
    mantissa.forEach { if (it != '0') mantissaIsOnlyZeroes = false }  //mantissaIsOnlyZeroes = false
    return if (ignoreEmptyZeroes && mantissaIsOnlyZeroes) {
        roundedValue.roundToInt().toString() // 3
    } else stringValue // 3.000
}


fun getIconUrl(ids:String):String{
    val id = ids.toIntOrNull()
    val op =   "https://picsum.photos/id/${id?:100}/340"
    println(">>> getIconUrl called with $ids and returns $op")
    return op
}
fun getIdFromUrl(url:String):String{
     val id =  url.substringAfter("id/").substringBefore("/")
    println(">>> getIdFromUrl called with url $url & returns $id")
    return id
}