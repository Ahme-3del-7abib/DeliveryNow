package com.ahmed.a.habib.deliverynow.features.auth.login

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
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
import com.ahmed.a.habib.deliverynow.R
import com.ahmed.a.habib.deliverynow.features.auth.forgetPass.ForgetPassBottomSheetScreen
import com.ahmed.a.habib.deliverynow.utils.CircleProgressBar
import com.ahmed.a.habib.deliverynow.utils.CommonPasswordField
import com.ahmed.a.habib.deliverynow.utils.CommonRoundedButton
import com.ahmed.a.habib.deliverynow.utils.CommonRoundedButtonWithIcon
import com.ahmed.a.habib.deliverynow.utils.CommonTextField
import com.ahmed.a.habib.deliverynow.utils.SharedScreens
import com.ahmed.a.habib.deliverynow.utils.showAlertDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("Range")
@Composable
fun LoginScreen(
    activity: Activity,
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    var mailText by remember { mutableStateOf("") }
    var passText by remember { mutableStateOf("") }
    var isLoadingVisible by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val forgetPassBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val callback =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            scope.launch {
                viewModel.channel.send(LoginIntents.SignInWithGoogle(it))
            }
        }

    LaunchedEffect(true) {
        scrollState.animateScrollTo(100)

        viewModel.results.onEach {
            when (it) {
                LoginViewStates.UserIsCashed -> {
                    navController.navigate(
                        route = SharedScreens.UsersDashboard.route,
                        navOptions = NavOptions
                            .Builder()
                            .setPopUpTo(SharedScreens.Login.route, true)
                            .build()
                    )
                }

                is LoginViewStates.IsLoggedIn -> {
                    isLoadingVisible = false
                    viewModel.channel.send(LoginIntents.GetUserFromFireStoreAndCashIt(it.user))
                }

                is LoginViewStates.MsgError -> {
                    isLoadingVisible = false
                    activity.showAlertDialog(activity.getString(R.string.error_happened), it.msg)
                }

                is LoginViewStates.ResError -> {
                    isLoadingVisible = false
                    activity.showAlertDialog(
                        activity.getString(R.string.error_happened),
                        activity.getString(it.res)
                    )
                }

                LoginViewStates.Loading -> {
                    isLoadingVisible = true
                }
            }
        }.launchIn(scope)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.dark_yellow))
    ) {
        val (welcomeTxt, subTxt, centerColumn, createAccTxt) = createRefs()

        Text(text = stringResource(R.string.welcome_back),
            style = TextStyle(
                fontWeight = FontWeight.Bold, color = colorResource(id = R.color.white)
            ),
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 100.dp)
                .constrainAs(welcomeTxt) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(text = stringResource(R.string.login_to_continue),
            style = TextStyle(
                fontWeight = FontWeight.Normal, color = colorResource(id = R.color.white)
            ),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(subTxt) {
                    top.linkTo(welcomeTxt.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 20.dp)
                .constrainAs(centerColumn) {
                    bottom.linkTo(createAccTxt.top)
                    top.linkTo(subTxt.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    verticalBias = 0f
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CommonTextField(
                input = mailText,
                textSize = 16.sp,
                inputColor = R.color.navy_blue,
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

            CommonPasswordField(
                input = passText,
                textSize = 16.sp,
                inputColor = R.color.navy_blue,
                hint = stringResource(R.string.password),
                hintColor = R.color.white,
                keyboardType = KeyboardType.Password,
                cursorColor = R.color.white,
                isPassVisible = isPasswordVisible,
                onValueChange = { passText = it },
                passwordToggleClicked = { isPasswordVisible = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )

            Text(
                text = stringResource(R.string.forget_password),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.navy_blue),
                    textDecoration = TextDecoration.Underline
                ),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable { scope.launch { forgetPassBottomSheetState.show() } }
            )

            CommonRoundedButton(
                name = stringResource(R.string.sign_in),
                mainColor = R.color.navy_blue,
                textColor = R.color.white,
                modifier = Modifier
                    .padding(top = 40.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
            ) {
                scope.launch {
                    viewModel.channel.send(LoginIntents.ValidateAndLogin(mailText, passText))
                }
            }

            Text(
                text = stringResource(R.string.or),
                style = TextStyle(
                    fontWeight = FontWeight.Normal, color = colorResource(id = R.color.white)
                ),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            CommonRoundedButtonWithIcon(
                name = stringResource(R.string.sign_in_with_google),
                mainColor = R.color.white,
                textColor = R.color.dark_grey,
                icon = R.drawable.ic_google,
                modifier = Modifier.padding(horizontal = 30.dp)
            ) {
                scope.launch {
                    viewModel.channel.send(LoginIntents.GetGoogleAccount(activity, callback))
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
            text = stringResource(R.string.create_account),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.navy_blue),
                textDecoration = TextDecoration.Underline
            ),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable {
                    scope.launch {
                        navController.navigate(SharedScreens.Register.route)
                    }
                }
                .constrainAs(createAccTxt) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )

        ForgetPassBottomSheetScreen(
            activity = activity,
            bottomSheetState = forgetPassBottomSheetState
        )
    }
}
