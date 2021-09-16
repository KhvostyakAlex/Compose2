package ru.leroymerlin.internal.phonebook.ui.screens.cards

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
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
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.copyToClipboard
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList
import values.COLLAPSE_ANIMATION_DURATION
import values.EXPAND_ANIMATION_DURATION
import values.FADE_IN_ANIMATION_DURATION
import values.FADE_OUT_ANIMATION_DURATION


@ExperimentalCoroutinesApi
@Composable
fun CardsScreen(viewModel: CardsViewModel) {
   // val cards = viewModel.cards.collectAsState()
    val cards:List<IntraruUserDataList> by viewModel.cards.observeAsState(emptyList())
   // val expandedCardIds = viewModel.expandedCardIdsList.collectAsState()
    val expandedCardIds = viewModel.expandedCardIdsList.observeAsState()
    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightWhite
            )
        )
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

                Row() {
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
    val activity = LocalContext.current as Activity

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
    val activity = LocalContext.current as Activity
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
            // Spacer(modifier = Modifier.heightIn(2.dp))

            Text(
                text = "LDAP - ${card.account}",
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            )
            if (card.workPhone != "" && card.workPhone != "null") {
                Text(
                    text = "№Тел ${card.workPhone}",
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            }

            if (card.mobilePhone != "" && card.mobilePhone != "null") {
                Text(
                    text = "№Тел ${card.mobilePhone}",
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            }
            if (card.workEmail != "null" && card.workEmail.length > 2) {
                val charA = card.workEmail.indexOf("@", 0, false)
                val charB = card.workEmail.indexOf(", isConfirmed", charA, false)
                val workEmail = card.workEmail.substring(8, charB)

                Text(
                    text = "Email - $workEmail",
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            }

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
                        backgroundColor = colorResource(id = R.color.lmNCKD)
                    ) {
                        Icon(Icons.Filled.Phone, "", modifier = Modifier)
                    }

                    FloatingActionButton(
                        onClick = {
                             //   Log.e("copyTobuffer - ",card.firstName.toString())
                            onClickButtonCopy(card, context)

                        },
                        modifier = Modifier.padding(8.dp),
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
    var phoneNum =""

    if(workPhone != "" && workPhone != "null"){
        Log.e("workPhone - ",card.workPhone.toString())

        workPhone = workPhone.replace("+","")
        workPhone =  workPhone.replace("-","")
        workPhone = workPhone.replace(" ","")
        val isDigits = TextUtils.isDigitsOnly(workPhone) //проверка - является ли числом

        if(isDigits){
            val character = workPhone[0]//определяем первый знак

            if(character.toString() == "7"){
                phoneNum = "+$workPhone"
            }else{
                phoneNum = workPhone
            }
            context.copyToClipboard(phoneNum)//нужно ли копировать в буффер?
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNum"))//открываем звонилку
            context.startActivity(intent)
        }

    }else if(card.mobilePhone != "" && card.mobilePhone != "null"){
        Log.e("mobilePhone - ", card.mobilePhone)
        mobilePhone = mobilePhone.replace("+","")
        mobilePhone =  mobilePhone.replace("-","")
        mobilePhone = mobilePhone.replace(" ","")
        val isDigits = TextUtils.isDigitsOnly(mobilePhone) //проверка - является ли числом
        if(isDigits){
            val character = mobilePhone[0]//определяем первый знак
            if(character.toString() == "7"){
                phoneNum = "+$mobilePhone"
            }else{
                phoneNum = mobilePhone
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
    var phoneNum =""

    if(workPhone != "" && workPhone != "null"){
        Log.e("workPhone - ",card.workPhone.toString())

        workPhone = workPhone.replace("+","")
        workPhone =  workPhone.replace("-","")
        workPhone = workPhone.replace(" ","")
        val isDigits = TextUtils.isDigitsOnly(workPhone) //проверка - является ли числом

        if(isDigits){
            val character = workPhone[0]//определяем первый знак

            if(character.toString() == "7"){
                phoneNum = "+$workPhone"
            }else{
                phoneNum = workPhone
            }
            context.copyToClipboard(phoneNum)//нужно ли копировать в буффер?
        }

    }else if(card.mobilePhone != "" && card.mobilePhone != "null"){
        Log.e("mobilePhone - ", card.mobilePhone)
        mobilePhone = mobilePhone.replace("+","")
        mobilePhone =  mobilePhone.replace("-","")
        mobilePhone = mobilePhone.replace(" ","")
        val isDigits = TextUtils.isDigitsOnly(mobilePhone) //проверка - является ли числом
        if(isDigits){
            val character = mobilePhone[0]//определяем первый знак
            if(character.toString() == "7"){
                phoneNum = "+$mobilePhone"
            }else{
                phoneNum = mobilePhone
            }
            context.copyToClipboard(phoneNum)//нужно ли копировать в буффер?
        }
    }
}

