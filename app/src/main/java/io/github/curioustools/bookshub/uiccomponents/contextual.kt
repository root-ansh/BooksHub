package io.github.curioustools.bookshub.uiccomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.curioustools.bookshub.R
import io.github.curioustools.bookshub.baseutils.ComposeUtils
import io.github.curioustools.bookshub.baseutils.MyComposeColors
import io.github.curioustools.bookshub.baseutils.MyComposeColors.White
import io.github.curioustools.bookshub.baseutils.toIcon
import io.github.curioustools.bookshub.uimodels.Review

@Preview
@Composable
fun ReviewComponent(review: Review = Review.mock,config:Modifier = Modifier,showDelete:Boolean = true, showBook:Boolean=true, onDelete:(Review)->Unit = {}, ){
    Card(
        modifier = config.fillMaxWidth().wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if(showBook){
                    LoadingIcon(review.bookImageUrl, width = 60.dp, elevation = 0.dp)
                }
                Text(
                    text = review.title,
                    modifier = Modifier.weight(1f).padding( vertical = 4.dp, horizontal = 8.dp),
                    maxLines = 3,
                    style = ComposeUtils.Typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )

                if(showDelete){
                    Icons.Default.Cancel.toIcon(modifier = Modifier.padding(horizontal = 8.dp).size(48.dp).clickable { onDelete.invoke(review) })()
                }
            }
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "“",
                    modifier = Modifier.wrapContentHeight().padding(horizontal = 12.dp),
                    maxLines = 3,
                    style = ComposeUtils.Typography.bodyLarge.copy(color = MyComposeColors.DarkGrey, fontSize = 64.sp, ),
                    overflow = TextOverflow.Ellipsis
                )
                Column(Modifier.weight(1f)) {
                    SimpleRatingBar(rating = review.rating.toFloatOrNull()?:0f)
                    Text(
                        text = review.desc,
                        modifier = Modifier.fillMaxWidth().padding( vertical = 4.dp),
                        style = ComposeUtils.Typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "”",
                    modifier = Modifier.padding().height(48.dp).align(Alignment.Bottom),
                    style = ComposeUtils.Typography.bodyLarge.copy(color = MyComposeColors.DarkGrey, fontSize = 64.sp),
                )
            }


        }

    }
}