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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList
import values.COLLAPSE_ANIMATION_DURATION
import values.EXPAND_ANIMATION_DURATION
import values.FADE_IN_ANIMATION_DURATION
import values.FADE_OUT_ANIMATION_DURATION


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
                    onCardArrowClick = { viewModel.onCardArrowClicked(card.account.toInt()) },
                    expanded = expandedCardIds.value.contains(card.account.toInt()),
                )
            }
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    card: IntraruUserDataList,
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
        contentColor = Color(ContextCompat.getColor(LocalContext.current, R.color.black)),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Column ( // horizontalAlignment = Alignment.CenterHorizontally
                ){
            Box (modifier = Modifier
                .clickable(onClick = onCardArrowClick)
            ){
                Row() {
                    Column(Modifier
                        .weight(1f)
                    ) {
                        CardTitle(title = "${card.firstName} ${card.lastName}")
                    }
                    Column(Modifier
                        .weight(1f)
                    ) {
                        CardOrgUnitName(title = "${card.orgUnitName}")
                    }
                    Column() {
                        CardArrow(degrees = arrowRotationDegree, onClick = onCardArrowClick)
                    }
                }
            }
            ExpandableContent(visible = expanded, card = card)
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    Column() {
        IconButton(
            onClick = onClick,
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
           modifier = Modifier.padding(8.dp),
           textAlign = TextAlign.Start,
           )
}
@Composable
fun CardOrgUnitName(title: String/*, onClick: () -> Unit*/) {
    Text(
        text = title,
        modifier = Modifier.padding(8.dp),
        textAlign = TextAlign.Start,
        fontSize = 12.sp
    )

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    card: IntraruUserDataList,
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
        Column(modifier = Modifier
            .padding(start = 16.dp, end = 8.dp, bottom = 8.dp)

        ) {
           // Spacer(modifier = Modifier.heightIn(2.dp))

                Text(
                    text = "LDAP - ${card.account}",
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
                Text(
                    text = "№Тел ${card.workPhone}",
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            Text(
                text = "№Тел ${card.mobilePhone}",
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            )

                Text(
                    text = card.workEmail,
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            Text(
                text = "Должность - ${card.jobTitle}",
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            )
            Text(
                text = card.orgUnitName,
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            )
        }
    }
}
