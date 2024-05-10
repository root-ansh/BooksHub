package io.github.curioustools.bookshub.uiccomponents

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.curioustools.bookshub.baseutils.MyComposeColors
import io.github.curioustools.bookshub.baseutils.MyComposeColors.Green80
import io.github.curioustools.bookshub.baseutils.MyComposeColors.Transparent
import io.github.curioustools.bookshub.baseutils.toIcon

@Composable
@Preview
fun SelectionButtonsOneByTwo(
    modifier: Modifier = Modifier,
    items: List<String> = listOf("Login", "Forgot Pwd", "Signup"),
    default: String = "",
    onClick: (String) -> Unit = {},
) {
    val selected: MutableState<String> = remember { mutableStateOf(default) }

    val r = 24.dp
    val startShape = RoundedCornerShape(r,0.dp,0.dp,r)
    val endShape = RoundedCornerShape(0.dp,r,r,0.dp)
    val midShape = RectangleShape
    Row(
        modifier = modifier.fillMaxWidth().wrapContentHeight()
    ) {

        items.mapIndexed { index, s ->
            val color = if(s.equals(selected.value,true) ) Green80 else Transparent
            val shape = when(index){
                0 -> startShape
                items.lastIndex  -> endShape
                else ->midShape
            }
            Text(
                text = s,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(IntrinsicSize.Max).weight(1f)
                    .background(color = color, shape)
                    .border(BorderStroke(1.dp,MyComposeColors.Grey),shape)
                    .clickable {
                        selected.value = s
                        onClick.invoke(s)
                    }
                    .padding(12.dp)

            )

        }
    }

}

@Preview
@Composable
fun shimmerBrush(showShimmer: Boolean = true,targetValue:Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "transition")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            )
            , label = "translateAnimation"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent,Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}


fun Modifier.bottomElevation(dims: Dp): Modifier {

    return this.then(Modifier.drawWithContent {
        val paddingPx = dims.toPx()
        clipRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height + paddingPx
        ) {
            this@drawWithContent.drawContent()
        }
    })
}



@Preview
@Composable
fun SimpleRatingBar(
    rating: Float=2.75f,
    total:Int = 5,
    starConfig: Modifier = Modifier,
    rowConfig:Modifier = Modifier,
    fillColor:Color = MyComposeColors.Orange80,
    unFillColor:Color = MyComposeColors.Black
) {
    val colored = rating.toInt()
    val halfColored = Math.round(rating - colored)
    val uncolored = total - colored - halfColored
    Row(modifier = rowConfig) {
        repeat(colored){ Icons.Default.Star.toIcon(starConfig,tint = fillColor)() }
        repeat(halfColored){ Icons.AutoMirrored.Default.StarHalf.toIcon(starConfig,tint = fillColor)() }
        repeat(uncolored){ Icons.Outlined.Star.toIcon(starConfig,tint = unFillColor)() }
    }
}
