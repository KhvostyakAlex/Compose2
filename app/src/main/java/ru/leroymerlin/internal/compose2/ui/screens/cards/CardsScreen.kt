package ru.leroymerlin.internal.compose2.ui.screens.cards

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.leroymerlin.internal.compose2.R


@ExperimentalCoroutinesApi
@Composable
fun CardsScreen(viewModel: CardsViewModel) {
    val cards = viewModel.cards.collectAsState()
    val expandedCardIds = viewModel.expandedCardIdsList.collectAsState()
    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightWhite
            )
        )
    ) {
        LazyColumn {
            itemsIndexed(cards.value) { _, card ->
                ExpandableCard(
                    card = card,
                    onCardArrowClick = { viewModel.onCardArrowClicked(card.id) },
                    expanded = expandedCardIds.value.contains(card.id),
                )
            }
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    card: ExpandableCardModel,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Card(
        backgroundColor = Color.White,
        contentColor = Color(
            ContextCompat.getColor(LocalContext.current, R.color.black)
        ),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
    ) {
        Column (
           // horizontalAlignment = Alignment.CenterHorizontally
                ){
            Box (modifier = Modifier
                .clickable(onClick = onCardArrowClick)

            ){
                CardTitle(title = card.title
                 //   onClick = onCardArrowClick
                )
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClick
                )

            }
            ExpandableContent(visible = expanded)
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    Column(
        ) {


        IconButton(
            onClick = onClick,
            modifier = Modifier
                ,
                    //.absolutePadding(left = 310.dp),

                    content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_expand_less_24),
                    contentDescription = "Expandable Arrow",
                    modifier = Modifier
                        .rotate(degrees)


                )
            }
        )
    }
}

@Composable
fun CardTitle(title: String/*, onClick: () -> Unit*/) {



       Text(
           text = title,
           modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp),


           //.clickable(onClick = onClick),
           textAlign = TextAlign.Start,

           )

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
) {
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.heightIn(4.dp))
            Row() {
                Text(
                    text = "Здесь будут данные",
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Здесь будут данные",
                    textAlign = TextAlign.End
                )
            }
            Text(
                text = "Здесь будут данные",
                textAlign = TextAlign.Center
            )
        }
    }
}
