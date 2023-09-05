package com.ahmed.a.habib.deliverynow.features.auth.forgetPass

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.a.habib.deliverynow.R
import com.ahmed.a.habib.deliverynow.utils.CircleProgressBar
import com.ahmed.a.habib.deliverynow.utils.CommonOutLineTextField
import com.ahmed.a.habib.deliverynow.utils.CommonRoundedButton
import com.ahmed.a.habib.deliverynow.utils.showAlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ForgetPassBottomSheetScreen(
    activity: Activity,
    bottomSheetState: ModalBottomSheetState,
    viewModel: ForgetPassViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    var isLoadingVisible by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.result.onEach {
            when (it) {
                ForgetPassViewStates.Loading -> {
                    isLoadingVisible = true
                }

                is ForgetPassViewStates.MailIsSent -> {
                    isLoadingVisible = false
                    bottomSheetState.hide()
                    activity.showAlertDialog(
                        activity.getString(R.string.app_name),
                        activity.getString(R.string.email_is_sent_successfully)
                    )
                }

                is ForgetPassViewStates.ShowMsgError -> {
                    isLoadingVisible = false
                    activity.showAlertDialog(activity.getString(R.string.error_happened), it.msg)
                }

                is ForgetPassViewStates.ShowResError -> {
                    isLoadingVisible = false
                    activity.showAlertDialog(
                        activity.getString(
                            R.string.error_happened
                        ), activity.getString(it.res)
                    )
                }
            }
        }.launchIn(scope)
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = { ForgetPassScreen(scope, viewModel, isLoadingVisible) },
        modifier = Modifier.fillMaxSize()
    ) {}
}

@SuppressLint("Range")
@Composable
fun ForgetPassScreen(
    scope: CoroutineScope,
    viewModel: ForgetPassViewModel,
    isLoadingVisible: Boolean
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(color = colorResource(id = R.color.white))
    ) {
        var mailTxt by remember { mutableStateOf("") }

        val (forgetPassTxt, nextBtn, divider, mailEd, loadingProgress) = createRefs()

        Text(text = stringResource(R.string.forget_password_),
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 22.sp,
            color = colorResource(id = R.color.navy_blue),
            modifier = Modifier
                .padding(start = 20.dp, top = 30.dp, end = 8.dp)
                .constrainAs(forgetPassTxt) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        CommonRoundedButton(name = stringResource(R.string.send),
            mainColor = R.color.navy_blue,
            textColor = R.color.white,
            textSize = 14.sp,
            modifier = Modifier
                .padding(top = 18.dp, end = 20.dp)
                .widthIn(min = 40.dp)
                .constrainAs(nextBtn) {
                    start.linkTo(forgetPassTxt.end)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    horizontalBias = 1f
                }
        ) {
            scope.launch {
                viewModel.channel.send(ForgetPassIntents.SendMail(mailTxt.trim()))
            }
        }

        Divider(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
                .background(colorResource(id = R.color.light_grey))
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    top.linkTo(nextBtn.bottom)
                    end.linkTo(parent.end)
                }, thickness = 1.dp
        )

        CommonOutLineTextField(
            input = mailTxt,
            hint = stringResource(R.string.enter_mail),
            textSize = 14.sp,
            inputType = KeyboardType.Email,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .constrainAs(mailEd) {
                    start.linkTo(parent.start)
                    top.linkTo(divider.bottom)
                    end.linkTo(parent.end)
                },
            errorColor = R.color.yellow,
            mainColor = R.color.navy_blue
        ) {
            mailTxt = it
        }

        if (isLoadingVisible) {
            CircleProgressBar(
                color = R.color.navy_blue,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .constrainAs(loadingProgress) {
                        top.linkTo(mailEd.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}