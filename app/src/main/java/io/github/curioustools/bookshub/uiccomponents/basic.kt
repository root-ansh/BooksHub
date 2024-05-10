package io.github.curioustools.bookshub.uiccomponents

import androidx.annotation.IntRange
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.github.curioustools.bookshub.R
import io.github.curioustools.bookshub.baseutils.ComposableBlockSaver
import io.github.curioustools.bookshub.baseutils.ComposeUtils
import io.github.curioustools.bookshub.baseutils.MyComposeColors
import io.github.curioustools.bookshub.baseutils.MyComposeColors.White

sealed class IconModel(open val id: Any, open val onClick: ((Any) -> Unit)?) {
    data class CircleUrlModel(val url: String, override val id: Any = Unit, val placeholder:Int? = null, override val onClick: ((Any) -> Unit)? = null) : IconModel(id, onClick)
    data class VectorModel(val res: Int, override val id: Any = Unit, override val onClick: ((Any) -> Unit)? = null) : IconModel(id, onClick)
    data class ButtonIcon(val text:String,override val id: Any = Unit ,override val onClick: ((Any) -> Unit)? = null) :IconModel(id,onClick)
    companion object{
        val mockCircle = CircleUrlModel("https://images.unsplash.com/photo-1579353977828-2a4eab540b9a")
        val mockVector = listOf(VectorModel(R.drawable.ic_search), ButtonIcon("logout"))

    }
}

@Composable
@Preview()
fun BasicIcon(model: IconModel = IconModel.mockVector.first(), size: Dp = 32.dp, bg: Color = MyComposeColors.Transparent,config:Modifier = Modifier,) {


    Surface(shape = CircleShape, color = bg, modifier = config) {

        when (model) {

            is IconModel.CircleUrlModel -> {
                val placeHolderWithPreview =if(LocalInspectionMode.current){
                    model.placeholder?:android.R.drawable.ic_menu_close_clear_cancel
                }else model.placeholder
                val placeHolderPainter = if(placeHolderWithPreview!=null) painterResource(id = placeHolderWithPreview) else null
                AsyncImage(

                    modifier = Modifier.size(size.times(1.2f)).clickable { model.onClick?.invoke(model.id) },
                    model = model.url,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    placeholder =placeHolderPainter
                )
            }

            is IconModel.VectorModel -> {
                Image(
                    painter = painterResource(id = model.res),
                    contentDescription = "",
                    modifier = Modifier.size(size).padding(2.dp).clickable { model.onClick?.invoke(model.id) },
                    contentScale = ContentScale.FillBounds
                )
            }

            is IconModel.ButtonIcon ->{
                Button(onClick = { model.onClick?.invoke(model.id) }) {
                    Text(text = model.text.uppercase())
                }
            }
        }
    }
}


@Composable
@Preview
fun ScreenBar(startIcon:IconModel = IconModel.mockCircle,endIcons:List<IconModel> = IconModel.mockVector,title: String=LoremIpsum(20).values.joinToString("")){
    Surface(modifier = Modifier, shadowElevation = 0.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicIcon(startIcon)
            Text(
                text = title,
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                maxLines = 1,
                style = ComposeUtils.Typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            endIcons.mapIndexed { idx, it ->
                BasicIcon(it)
                if (idx != endIcons.lastIndex) Spacer(modifier = Modifier.size(4.dp))
            }

        }
    }
}



@Composable
@Preview
fun AppIcon(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    size: Dp = 120.dp,
    rounding: Dp = 16.dp,
    elevation: Dp = 8.dp,
    border: BorderStroke = BorderStroke(4.dp, White)
) {
    val shape = RoundedCornerShape(rounding)
    Surface(
        shape = shape,
        color = MyComposeColors.Green,
        shadowElevation = elevation,
        modifier = modifier.border(border, shape)
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
            modifier = imageModifier.size(size),
            contentScale = ContentScale.FillBounds
        )
    }

}

@Composable
@Preview
fun LoadingUI(modifier: Modifier=Modifier){
    Surface(
        shape = CircleShape,
        color = MyComposeColors.White,
        shadowElevation = 8.dp,
        modifier = modifier
    ){
        CircularProgressIndicator(
            modifier = Modifier.padding(4.dp).size(24.dp)
        )

    }

}


@Composable
@Preview
fun BasicEditText(
    modifier: Modifier = Modifier,
    initial: String = "",
    title: String = "Email",
    inputType: KeyboardType = KeyboardType.Email,
    onValueChanged: (String) -> Unit = {}
) {
    val value = remember { mutableStateOf(initial) }
    val passwordVisible = remember { mutableStateOf(false) }
    val actons = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.None,
        autoCorrect = false,
        keyboardType = inputType,

    )
    val visualTransformation = when(inputType){
        KeyboardType.Password -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val endIcon = when(inputType) {
        KeyboardType.Password -> {
             ComposableBlockSaver{
                 val image = if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                 val description = if (passwordVisible.value) "Hide password" else "Show password"
                 IconButton(onClick = {passwordVisible.value = passwordVisible.value.not()}){
                     Icon(imageVector  = image, description)
                 }
             }
     }
     else -> {
         ComposableBlockSaver{
             if(value.value.isNotBlank()){
                 IconButton(onClick = { value.value ="" }) {
                     Icon(imageVector = Icons.Filled.Cancel,"clear")
                 }
             }
         }
     }
    }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
        label = { Text(text = title)},
        placeholder = { Text(text = title)},
        value = value.value,
        onValueChange = {
            value.value = it
            onValueChanged.invoke(it)
        },
        keyboardOptions = actons,
        visualTransformation = if (inputType== KeyboardType.Password && !passwordVisible.value) visualTransformation else VisualTransformation.None,
        trailingIcon = {
            endIcon.print()
        },
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
@Preview
fun LoadingIcon(
    url: String = "https://images.unsplash.com/photo-1579353977828-2a4eab540b9a",
    id: Any = Unit,
    width: Dp = 180.dp,
    desc: String = "icon",
    rounding: Dp = 16.dp,
    config: Modifier = Modifier,
    elevation: Dp = 8.dp,
    onClick: ((Any) -> Unit)? = null,
) {
    val shape = RoundedCornerShape(rounding)
    Surface(
        modifier = config,
        shape = shape,
        color = MyComposeColors.Green,
        shadowElevation = elevation,
    ) {
        val showShimmer = remember { mutableStateOf(true) }
        AsyncImage(
            model = url,
            contentDescription = desc,
            modifier = Modifier
                .background(shimmerBrush(showShimmer.value))
                .width(width).height(width.times(1.35f))
                .clickable { onClick?.invoke(id)}
            ,
            onSuccess = {showShimmer.value = false},
            onError = { e ->
                //showShimmer.value = false
                e.result.throwable.printStackTrace()

                      },
            contentScale = ContentScale.FillBounds
        )
    }

}

@Composable
@Preview
fun IconAndTitle(
    url: String = "https://images.unsplash.com/photo-1579353977828-2a4eab540b9a",
    title: String = LoremIpsum(20).values.joinToString(" "),
    id: Any = Unit,
    width: Dp = 150.dp,
    margins:PaddingValues = PaddingValues(0.dp),
    onClick: ((Any) -> Unit)? = null,

) {
    Column(
        modifier = Modifier.padding( margins).wrapContentHeight().wrapContentWidth().clickable { onClick?.invoke(id) }
    ) {
        LoadingIcon(url,width=width, onClick = onClick)
        Text(
            text = title,
            modifier = Modifier.width(width).padding( vertical = 4.dp),
            maxLines = 2,
            style = ComposeUtils.Typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Preview
@Composable
fun HText(
    text:String = "Text",
    config: Modifier=Modifier,
    additionalStyling:TextStyle = basicTextStyle(3,true)

){
    Text(text = text, modifier = config, style = additionalStyling)
}


@Preview
@Composable
fun BText(
    text:String = "Text",
    config: Modifier=Modifier,
    additionalStyling:TextStyle = basicTextStyle(4,false)

){
    Text(text = text, modifier = config, style = additionalStyling)
}


fun basicTextStyle(@IntRange(1,6) level:Int = 3,isHeading:Boolean):TextStyle{

    return TextStyle.Default.copy(
        fontSize = when(level){
            1 -> if(isHeading) 16.sp else 9.sp
            2 -> if(isHeading) 17.sp else 10.sp
            3 -> if(isHeading) 18.sp else 12.sp
            4 -> if(isHeading) 20.sp else 14.sp
            5 -> if(isHeading) 22.sp else 15.sp
            6 -> if(isHeading) 24.sp else 16.sp
            else -> if(isHeading)  18.sp else 12.sp
        },
        fontWeight = if(isHeading) FontWeight.Medium else FontWeight.Normal
    )
}
