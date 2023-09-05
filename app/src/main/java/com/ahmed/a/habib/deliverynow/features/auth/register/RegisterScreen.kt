package com.ahmed.a.habib.deliverynow.features.auth.register

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.ahmed.a.habib.deliverynow.BaseApp
import com.ahmed.a.habib.deliverynow.R
import com.ahmed.a.habib.deliverynow.features.auth.User
import com.ahmed.a.habib.deliverynow.utils.CircleProgressBar
import com.ahmed.a.habib.deliverynow.utils.CommonPasswordField
import com.ahmed.a.habib.deliverynow.utils.CommonRoundedButton
import com.ahmed.a.habib.deliverynow.utils.CommonTextField
import com.ahmed.a.habib.deliverynow.utils.SharedScreens
import com.ahmed.a.habib.deliverynow.utils.showAlertDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SuppressLint("Range")
@Composable
fun RegisterScreen(
    activity: Activity,
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var mailText by remember { mutableStateOf("") }
    var nameText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    var isLoadingVisible by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.results.onEach {
            when (it) {
                RegisterViewStates.UserIsCashed -> {
                    navController.navigate(
                        route = SharedScreens.UsersDashboard.route,
                        navOptions = NavOptions
                            .Builder()
                            .setPopUpTo(SharedScreens.Register.route, true)
                            .build()
                    )
                }

                is RegisterViewStates.IsRegistered -> {
                    isLoadingVisible = false
                    viewModel.channel.send(RegisterIntents.CashUser(it.user))
                }

                is RegisterViewStates.MsgError -> {
                    isLoadingVisible = false
                    activity.showAlertDialog(activity.getString(R.string.error_happened), it.res)
                }

                is RegisterViewStates.ResError -> {
                    isLoadingVisible = false
                    activity.showAlertDialog(
                        activity.getString(R.string.error_happened),
                        activity.getString(it.res)
                    )
                }

                RegisterViewStates.Loading -> isLoadingVisible = true
            }
        }.launchIn(scope)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(colorResource(id = R.color.navy_blue))
    ) {
        val (createAccTxt, centerColumn, loginTxt) = createRefs()

        Text(text = stringResource(R.string.create_account_),
            style = TextStyle(
                fontWeight = FontWeight.Bold, color = colorResource(id = R.color.white)
            ),
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 100.dp)
                .constrainAs(createAccTxt) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 20.dp, top = 50.dp)
                .constrainAs(centerColumn) {
                    top.linkTo(createAccTxt.bottom)
                    bottom.linkTo(loginTxt.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    verticalBias = 0f
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CommonTextField(
                input = mailText,
                textSize = 16.sp,
                inputColor = R.color.white,
                hint = stringResource(R.string.mail),
                hintColor = R.color.white,
                keyboardType = KeyboardType.Email,
                cursorColor = R.color.white,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                mailText = it
            }

            CommonTextField(
                input = nameText,
                textSize = 16.sp,
                inputColor = R.color.white,
                hint = stringResource(R.string.full_name),
                hintColor = R.color.white,
                keyboardType = KeyboardType.Email,
                cursorColor = R.color.white,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                nameText = it
            }

            CommonPasswordField(
                input = passwordText,
                textSize = 16.sp,
                inputColor = R.color.white,
                hint = stringResource(R.string.password),
                hintColor = R.color.white,
                keyboardType = KeyboardType.Password,
                cursorColor = R.color.white,
                isPassVisible = isPasswordVisible,
                onValueChange = { passwordText = it },
                passwordToggleClicked = { isPasswordVisible = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            CommonRoundedButton(
                name = stringResource(R.string.create),
                mainColor = R.color.dark_yellow,
                textColor = R.color.white,
                modifier = Modifier
                    .padding(top = 60.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
            ) {
                scope.launch {
                    viewModel.channel.send(
                        RegisterIntents.ValidateAndRegister(
                            User(
                                userType = BaseApp.getUserType(),
                                email = mailText,
                                name = nameText,
                                password = passwordText
                            )
                        )
                    )
                }
            }

            if (isLoadingVisible) {
                CircleProgressBar(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp),
                    color = R.color.white,
                )
            }
        }

        Text(
            text = stringResource(R.string.login),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_yellow),
                textDecoration = TextDecoration.Underline
            ),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable {
                    navController.navigate(
                        SharedScreens.Login.route,
                        navOptions = NavOptions
                            .Builder()
                            .setPopUpTo(SharedScreens.Login.route, true)
                            .build()
                    )
                }
                .constrainAs(loginTxt) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}