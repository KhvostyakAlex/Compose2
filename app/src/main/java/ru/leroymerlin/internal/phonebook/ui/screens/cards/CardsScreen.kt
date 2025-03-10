package ru.leroymerlin.internal.phonebook.ui.screens.cards

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.copyToClipboard
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.phonebook.ui.themes.JetPhonebookTheme
import values.COLLAPSE_ANIMATION_DURATION
import values.EXPAND_ANIMATION_DURATION
import values.FADE_IN_ANIMATION_DURATION
import values.FADE_OUT_ANIMATION_DURATION


@ExperimentalCoroutinesApi
@Composable
fun CardsScreen(viewModel: CardsViewModel) {
    val cards:List<IntraruUserDataList> by viewModel.cards.observeAsState(emptyList())
    val expandedCardIds = viewModel.expandedCardIdsList.observeAsState()
    Scaffold(
        backgroundColor = JetPhonebookTheme.colors.secondaryBackground


    ) {
        LazyColumn {
            itemsIndexed(cards) { _, card ->
                ExpandableCard(
                    card = card,
                    onCardArrowClick = { viewModel.onCardArrowClicked(card.account.toInt()) },
                    expanded = expandedCardIds.value!!.contains(card.account.toInt()),
                )
            }
        }
    }
}

@Composable
fun EmptyCard(title: String){
    Card(
       // backgroundColor = JetHabitTheme.colors.secondaryBackground,
        //contentColor = Color(ContextCompat.getColor(LocalContext.current, R.color.black)),
        backgroundColor = JetPhonebookTheme.colors.primaryBackground,
        contentColor = JetPhonebookTheme.colors.primaryText,
        elevation = 8.dp,
       // shape = RoundedCornerShape(8.dp),
        shape = JetPhonebookTheme.shapes.cornersStyle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Column {
                Row {
                    Text(title, modifier = Modifier.padding(start = 8.dp, top = 8.dp,bottom = 12.dp))
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
        //backgroundColor = Color.White,
        backgroundColor = JetPhonebookTheme.colors.primaryBackground,
        //contentColor = Color(ContextCompat.getColor(LocalContext.current, R.color.black)),
        contentColor = JetPhonebookTheme.colors.primaryText,
        elevation = 8.dp,
        shape = JetPhonebookTheme.shapes.cornersStyle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Column {
            Box (modifier = Modifier.clickable(onClick = onCardArrowClick)){
                Row() {
                    Column(Modifier.weight(1f)) {
                        CardTitle(title = "${card.lastName} ${card.firstName}")
                    }
                    Column(Modifier.weight(1f)) {
                        CardOrgUnitName(title = card.orgUnitName)
                    }
                    Column {
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
    Column {
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
fun CardTitle(title: String) {
       Text(
           text = title,
           modifier = Modifier.padding(8.dp),
           textAlign = TextAlign.Start,
           style = JetPhonebookTheme.typography.toolbar
           )
}
@Composable
fun CardOrgUnitName(title: String/*, onClick: () -> Unit*/) {
    Text(
        text = title,
        modifier = Modifier.padding(8.dp),
        textAlign = TextAlign.Start,
        fontSize = JetPhonebookTheme.typography.caption.fontSize,
        style = JetPhonebookTheme.typography.caption
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    card: IntraruUserDataList,
) {
    val context = LocalContext.current
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
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp, bottom = 8.dp)

        ) {
            Text(
                text = "LDAP - ${card.account}",
                textAlign = TextAlign.Start,
                fontSize = JetPhonebookTheme.typography.caption.fontSize,
                modifier = Modifier.padding(bottom = 2.dp),
                style = JetPhonebookTheme.typography.body
            )
            if (card.workPhone != "" && card.workPhone != "null") {
                Text(
                    text = "№Тел ${card.workPhone}",
                    textAlign = TextAlign.Start,
                    fontSize = JetPhonebookTheme.typography.caption.fontSize,
                    modifier = Modifier.padding(top=2.dp, bottom = 2.dp),
                    style = JetPhonebookTheme.typography.body
                )
            }

            if (card.mobilePhone != "" && card.mobilePhone != "null") {
                Text(
                    text = "№Тел ${card.mobilePhone}",
                    textAlign = TextAlign.Start,
                    fontSize = JetPhonebookTheme.typography.caption.fontSize,
                    modifier = Modifier.padding(top=2.dp, bottom = 2.dp),
                    style = JetPhonebookTheme.typography.body
                )
            }
            if (card.workEmail != "null" && card.workEmail.length > 2) {
                val charA = card.workEmail.indexOf("@", 0, false)
                val charB = card.workEmail.indexOf(", isConfirmed", charA, false)
                val workEmail = card.workEmail.substring(8, charB)

                Text(
                    text = "Email - $workEmail",
                    textAlign = TextAlign.Start,
                    fontSize = JetPhonebookTheme.typography.caption.fontSize,
                    modifier = Modifier.padding(top=2.dp, bottom = 2.dp),
                    style = JetPhonebookTheme.typography.body
                )
            }

            Text(
                text = "Должность - ${card.jobTitle}",
                textAlign = TextAlign.Start,
                fontSize = JetPhonebookTheme.typography.caption.fontSize,
                modifier = Modifier.padding(top=2.dp, bottom = 2.dp),
                style = JetPhonebookTheme.typography.body
            )
            Text(
                text = card.orgUnitName,
                textAlign = TextAlign.Start,
                fontSize = JetPhonebookTheme.typography.caption.fontSize,
                modifier = Modifier.padding(top=2.dp, bottom = 2.dp),
                style = JetPhonebookTheme.typography.body
            )

            if (card.workPhone != "" && card.workPhone != "null" ||
                card.mobilePhone != "" && card.mobilePhone != "null") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    //buttonPhone
                    FloatingActionButton(
                        onClick = {
                                 onClickButtonPhone(card, context = context)

                        },
                        modifier = Modifier.padding(8.dp),
                        contentColor=Color.White,
                        backgroundColor = colorResource(id = R.color.lmNCKD)
                    ) {
                        Icon(Icons.Filled.Phone, "", modifier = Modifier)
                    }

                    FloatingActionButton(
                        onClick = { onClickButtonCopy(card, context) },
                        modifier = Modifier.padding(8.dp),
                        contentColor=Color.White,
                        backgroundColor = colorResource(id = R.color.colorCopy)
                    ) {
                        Icon(Icons.Filled.ContentCopy, "", modifier = Modifier)
                    }
                }
            }
        }
    }
}

fun onClickButtonPhone(card: IntraruUserDataList, context: Context){
    var workPhone = card.workPhone
    var mobilePhone = card.mobilePhone
    var phoneNum: String

    if(workPhone != "" && workPhone != "null"){
        workPhone = workPhone.replace("+","")
        workPhone =  workPhone.replace("-","")
        workPhone = workPhone.replace(" ","")
        val isDigits = TextUtils.isDigitsOnly(workPhone) //проверка - является ли числом

        if(isDigits){
            val character = workPhone[0]//определяем первый знак

            phoneNum = if(character.toString() == "7"){
                "+$workPhone"
            }else{
                workPhone
            }
            context.copyToClipboard(phoneNum)//нужно ли копировать в буффер?
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNum"))//открываем звонилку
            context.startActivity(intent)
        }

    }else if(card.mobilePhone != "" && card.mobilePhone != "null"){
        mobilePhone = mobilePhone.replace("+","")
        mobilePhone =  mobilePhone.replace("-","")
        mobilePhone = mobilePhone.replace(" ","")
        val isDigits = TextUtils.isDigitsOnly(mobilePhone) //проверка - является ли числом
        if(isDigits){
            val character = mobilePhone[0]//определяем первый знак
            phoneNum = if(character.toString() == "7"){
                "+$mobilePhone"
            }else{
                mobilePhone
            }
            context.copyToClipboard(phoneNum)//нужно ли копировать в буффер?
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNum"))//открываем звонилку
            context.startActivity(intent)
        }
    }
}

fun onClickButtonCopy(card: IntraruUserDataList, context: Context){
    var workPhone = card.workPhone
    var mobilePhone = card.mobilePhone
    var phoneNum:String

    if(workPhone != "" && workPhone != "null"){
        workPhone = workPhone.replace("+","")
        workPhone =  workPhone.replace("-","")
        workPhone = workPhone.replace(" ","")
        val isDigits = TextUtils.isDigitsOnly(workPhone) //проверка - является ли числом

        if(isDigits){
            val character = workPhone[0]//определяем первый знак

            phoneNum = if(character.toString() == "7"){
                "+$workPhone"
            }else{
                workPhone
            }
            context.copyToClipboard(phoneNum)//нужно ли копировать в буффер?
        }

    }else if(card.mobilePhone != "" && card.mobilePhone != "null"){
        mobilePhone = mobilePhone.replace("+","")
        mobilePhone =  mobilePhone.replace("-","")
        mobilePhone = mobilePhone.replace(" ","")
        val isDigits = TextUtils.isDigitsOnly(mobilePhone) //проверка - является ли числом
        if(isDigits){
            val character = mobilePhone[0]//определяем первый знак
            phoneNum = if(character.toString() == "7"){
                "+$mobilePhone"
            }else{
                mobilePhone
            }
            context.copyToClipboard(phoneNum)//нужно ли копировать в буффер?
        }
    }
}

