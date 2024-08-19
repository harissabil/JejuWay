package com.holidai.jejuway.ui.screen.auth.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.ui.theme.JejuAItineraryTheme

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    emailValue: String,
    passwordValue: String,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
    onNavigateToScreen: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            NormalTextComponent(value = "Hey there,")
            HeadingTextComponent(value = "Welcome Back")
        }
        Spacer(modifier = Modifier.height(25.dp))
        Column {
            MyTextFieldComponent(
                labelValue = "Email",
                icon = Icons.Outlined.Email,
                textValue = emailValue,
                onValueChange = onEmailValueChange,
                isEmailField = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            PasswordTextFieldComponent(
                labelValue = "Password",
                icon = Icons.Outlined.Lock,
                textValue = passwordValue,
                onValueChange = onPasswordValueChange
            )
            Spacer(modifier = Modifier.weight(1f))
            BottomComponent(
                textQuery = "Don't have an account? ",
                textClickable = "Register",
                action = "Login",
                onButtonClick = onLoginButtonClick,
                onNavigateToScreen = onNavigateToScreen
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginContentPreview() {
    JejuAItineraryTheme {
        Surface {
            LoginContent(
                emailValue = "email",
                passwordValue = "password",
                onEmailValueChange = {},
                onPasswordValueChange = {},
                onLoginButtonClick = {},
                onNavigateToScreen = {}
            )
        }
    }
}