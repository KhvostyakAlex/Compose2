package ru.leroymerlin.internal.compose2.ui.screens.cards

import android.annotation.SuppressLint
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
import ru.leroymerlin.internal.compose2.ui.theme.cardCollapsedBackgroundColor
import ru.leroymerlin.internal.compose2.ui.theme.cardExpandedBackgroundColor

@ExperimentalCoroutinesApi
@Composable
fun CardsScreenOld(viewModel: CardsViewModel) {
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
fun ExpandableCardOld(
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
    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "bgColorTransition") {
       // if (expanded) Color.White else cardCollapsedBackgroundColor //другой фон при раскрытии
        Color.White
    }
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "paddingTransition") {
        if (expanded) 12.dp else 12.dp
    }
    val cardElevation by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "elevationTransition") {
        if (expanded) 24.dp else 4.dp
    }

    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = EXPAND_ANIMATION_DURATION,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
       // if (expanded) 8.dp else 8.dp
        8.dp
    }

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Card(
        backgroundColor = cardBgColor,
        contentColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.black
            )
        ),
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column {
            Box() {
                CardTitle(title = card.title/*, onClick = onCardArrowClick*/)
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
fun CardArrowOld(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_less_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees)
            )
        }
    )
}

@Composable
fun CardTitleOld(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Start,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContentOld(
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
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Здесь будут данные",
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "Здесь будут данные",
                textAlign = TextAlign.Center
            )
        }
    }
}
