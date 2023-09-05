package com.ahmed.a.habib.deliverynow.features.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.ahmed.a.habib.deliverynow.R
import com.ahmed.a.habib.deliverynow.utils.SharedScreens
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("Range")
@Composable
fun SplashScreen(navController: NavHostController) {

    val scope = rememberCoroutineScope()
    val customFont = FontFamily(Font(R.font.cairo_regular_font, FontWeight.Normal))
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading))

    LaunchedEffect(true) {
        scope.launch {
            delay(5000)

            navController.navigate(
                route = SharedScreens.Login.route,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(SharedScreens.Splash.route, true)
                    .build()
            )
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colorResource(id = R.color.white))
    ) {
        val (icon, textBlock, loading) = createRefs()

        Image(
            painter = painterResource(R.drawable.app_logo_1024),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .width(180.dp)
                .height(180.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(modifier = Modifier
            .padding(top = 60.dp)
            .constrainAs(textBlock) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(icon.top)
            }) {

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.dark_grey)
            )

            Text(
                text = stringResource(R.string.rah_ben_edaik),
                fontSize = 30.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp),
                textAlign = TextAlign.Center,
                fontFamily = customFont,
                color = colorResource(id = R.color.dark_yellow)
            )
        }

        LottieAnimation(composition = composition,
            iterations = Integer.MAX_VALUE,
            speed = 2f,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .constrainAs(loading) {
                    top.linkTo(textBlock.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    verticalBias = 0.8f
                }
        )
    }
}