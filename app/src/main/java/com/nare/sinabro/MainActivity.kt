package com.nare.sinabro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nare.sinabro.ui.theme.Default_Light3
import com.nare.sinabro.ui.theme.Orange
import com.nare.sinabro.ui.theme.OrangeT
import com.nare.sinabro.ui.theme.Puple
import com.nare.sinabro.ui.theme.SinabroTheme
import com.nare.sinabro.ui.theme.The_Light_Dark
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), ModelHelper.Luna{
    lateinit var gen:ModelHelper

    val text = mutableStateOf("여기에 이야기가 될 첫 마디를 적어주세요. 새로운 이야기가 만들어 집니다.")
    val Op_TF = mutableStateOf(true)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gen = ModelHelper(
            this, this.assets
        )
        setContent {
            SinabroTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val text = remember{
//                        mutableStateOf("여기에 이야기가 될 첫 마디를 적어주세요. 새로운 이야기가 만들어 집니다.")
//                    }
//
//                    val Op_TF = remember{
//                        mutableStateOf(true)
//                    }
                    val animatedValue = remember { Animatable(0f) }
                    LaunchedEffect(Op_TF.value) {
                        if(Op_TF.value)animatedValue.animateTo(0f, animationSpec= tween(durationMillis = 800))
                        else animatedValue.animateTo(1f, animationSpec= tween(durationMillis = 800))
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier
                            .padding(top = 60.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .size(240.dp, 40.dp)
                            .background(Default_Light3)) {
                            Row (
                                modifier = Modifier
                                    .fillMaxSize()
                                ,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ){
                                Text(text = "Story", color = Color.White)
                                Text(text = "E-mail", color = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .offset((110.dp.value * animatedValue.value).dp, 0.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .size(130.dp, 40.dp)
                                    .background(Orange)
                            ) {
//                                Row (
//                                    modifier = Modifier
//                                        .offset((110.dp.value * (-animatedValue.value)).dp, 0.dp)
//                                        .size(240.dp, 40.dp)
//                                    ,
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    horizontalArrangement = Arrangement.SpaceAround
//                                ){
//                                    Text(text = "Story", modifier = Modifier.size(130.dp, 40.dp).background(
//                                        Color.Cyan), textAlign = TextAlign.Center)
//                                    Text(text = "E-mail", modifier = Modifier.size(130.dp, 40.dp).background(
//                                        Color.Red), textAlign = TextAlign.Center)
//                                }
                            }

                        }

                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                            NareTextField(
                                modifier = Modifier
//                    .background(MaterialTheme.colorScheme.background)
                                    .padding(top = 20.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .size(360.dp, 240.dp)
                                    .border(
                                        width = 2.dp,
                                        color = Orange,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                ,
                                str = text.value,
                                edit = {text.value = it},
                                tf = Op_TF.value
                            )
                        }
                        Button(onClick = {
                            Op_TF.value = false
                            GlobalScope.launch {
                                gen.gen(text.value)
                            }

                        }, modifier = Modifier
                            .padding(top = 30.dp)
                            .size(150.dp, 50.dp),
                            enabled = Op_TF.value,
                            colors = ButtonDefaults.buttonColors(containerColor = OrangeT)) {
                            Icon(Icons.Default.Create, contentDescription = "Create")
                        }
                    }
                }
            }
        }
    }

    override fun onResult(results: String) {
        this.text.value = results
        this.Op_TF.value = true
    }
}

@Composable
fun NareTextField(
    modifier: Modifier,
    str:String,
    edit:(String) -> Unit,
    tf:Boolean
)
{

    val focusRequester = remember {
        FocusRequester()
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val focusManager = LocalFocusManager.current
    val wide = rememberSaveable { mutableStateOf(64f) }
    val animatedValue = remember { Animatable(64f) }
    LaunchedEffect(tf) {
        animatedValue.animateTo(360f, animationSpec= tween(durationMillis = 800))
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = Orange,
        backgroundColor = Orange
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = str,
            onValueChange = {edit(it)},
            modifier = modifier,
            cursorBrush = SolidColor(Puple),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            enabled = tf,
            decorationBox = {
                Row (
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxSize()
                        .padding(15.dp)
                ){
                    it()
                }
            }

        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Surface(
        color = The_Light_Dark
    ) {
        val text = remember{
            mutableStateOf("hello world")
        }

        val Op_TF = remember{
            mutableStateOf(true)
        }
        val animatedValue = remember { Animatable(64f) }
        LaunchedEffect(Op_TF.value) {
            animatedValue.animateTo(360f, animationSpec= tween(durationMillis = 800))
        }


    }
}