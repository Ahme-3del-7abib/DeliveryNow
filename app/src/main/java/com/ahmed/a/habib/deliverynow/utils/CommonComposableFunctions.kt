package com.ahmed.a.habib.deliverynow.utils


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmed.a.habib.deliverynow.R

@Composable
fun CircleProgressBar(modifier: Modifier = Modifier, color: Int) {
    CircularProgressIndicator(
        modifier = modifier,
        color = colorResource(id = color),
        strokeWidth = Dp(value = 2F)
    )
}

@Composable
fun CommonRoundedButton(
    modifier: Modifier = Modifier,
    name: String,
    mainColor: Int,
    textColor: Int,
    textSize: TextUnit = 18.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick.invoke() },
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        border = BorderStroke(1.dp, colorResource(id = mainColor)),
        colors = ButtonDefaults.buttonColors(
            contentColor = colorResource(id = textColor),
            containerColor = colorResource(id = mainColor)
        )
    ) {
        Text(
            text = name,
            fontSize = textSize,
            modifier = Modifier
                .padding(vertical = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CommonRoundedButtonWithIcon(
    modifier: Modifier = Modifier,
    name: String,
    mainColor: Int,
    textColor: Int,
    icon: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick.invoke() },
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        border = BorderStroke(1.dp, colorResource(id = mainColor)),
        colors = ButtonDefaults.buttonColors(
            contentColor = colorResource(id = textColor),
            containerColor = colorResource(id = mainColor)
        )
    ) {
        Icon(
            modifier = Modifier
                .width(35.dp)
                .height(35.dp)
                .padding(end = 6.dp),
            painter = painterResource(id = icon),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Text(
            text = name,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonOutLineTextField(
    input: String,
    hint: String,
    inputType: KeyboardType,
    modifier: Modifier = Modifier,
    errorColor: Int,
    textSize: TextUnit,
    mainColor: Int,
    onDone: (input: String) -> Unit
) {
    OutlinedTextField(
        value = input,
        label = { Text(text = hint, fontSize = 14.sp) },
        textStyle = TextStyle(fontSize = textSize),
        onValueChange = { onDone.invoke(it.ifEmpty { "" }) },
        keyboardOptions = KeyboardOptions(keyboardType = inputType),
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = outlinedTextFieldColors(
            textColor = colorResource(id = mainColor),
            cursorColor = colorResource(id = mainColor),
            errorBorderColor = colorResource(id = errorColor),
            focusedBorderColor = colorResource(id = mainColor),
            unfocusedBorderColor = colorResource(id = mainColor)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    input: String,
    inputColor: Int,
    hint: String,
    textSize: TextUnit,
    hintColor: Int,
    keyboardType: KeyboardType,
    cursorColor: Int,
    modifier: Modifier,
    onValueChange: (input: String) -> Unit
) {
    TextField(
        value = input,
        modifier = modifier,
        onValueChange = { onValueChange.invoke(it.ifEmpty { "" }) },
        placeholder = {
            Text(
                hint,
                color = colorResource(id = hintColor),
                style = TextStyle(textDirection = TextDirection.Ltr)
            )
        },
        textStyle = TextStyle(
            color = colorResource(id = inputColor),
            textDirection = TextDirection.Ltr,
            textAlign = TextAlign.Start,
            fontSize = textSize
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            cursorColor = colorResource(id = cursorColor),
            focusedIndicatorColor = colorResource(id = cursorColor),
            unfocusedIndicatorColor = colorResource(id = cursorColor)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonPasswordField(
    input: String,
    inputColor: Int,
    hint: String,
    textSize: TextUnit,
    hintColor: Int,
    keyboardType: KeyboardType,
    cursorColor: Int,
    modifier: Modifier,
    isPassVisible: Boolean,
    passwordToggleClicked: (isVisible: Boolean) -> Unit,
    onValueChange: (input: String) -> Unit
) {
    Box {
        TextField(
            value = input,
            singleLine = true,
            modifier = modifier.align(Alignment.CenterStart),
            visualTransformation = if (isPassVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            onValueChange = { onValueChange.invoke(it.ifEmpty { "" }) },
            placeholder = {
                Text(
                    hint,
                    color = colorResource(id = hintColor),
                    style = TextStyle(textDirection = TextDirection.Ltr)
                )
            },
            textStyle = TextStyle(
                color = colorResource(id = inputColor),
                textDirection = TextDirection.Ltr,
                textAlign = TextAlign.Start,
                fontSize = textSize
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = colorResource(id = cursorColor),
                focusedIndicatorColor = colorResource(id = cursorColor),
                unfocusedIndicatorColor = colorResource(id = cursorColor)
            )
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(40.dp)
                .padding(bottom = 12.dp),
            onClick = { passwordToggleClicked.invoke(!isPassVisible) }
        ) {
            if (isPassVisible) {
                Icon(
                    painter = painterResource(R.drawable.baseline_visibility_24),
                    contentDescription = null,
                    tint = colorResource(id = cursorColor)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.baseline_visibility_off_24),
                    contentDescription = null,
                    tint = colorResource(id = cursorColor)
                )
            }
        }
    }
}