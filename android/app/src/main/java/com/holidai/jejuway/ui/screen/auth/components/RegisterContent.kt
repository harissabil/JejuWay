package com.holidai.jejuway.ui.screen.auth.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.ui.theme.JejuAItineraryTheme

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    fullNameValue: String,
    onFullNameValueChange: (String) -> Unit,
    emailValue: String,
    passwordValue: String,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onRegisterButtonClick: () -> Unit,
    onNavigateToScreen: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        NormalTextComponent(value = "Hello there,")
        HeadingTextComponent(value = "Create an Account")
        Spacer(modifier = Modifier.height(25.dp))

        Column {
            MyTextFieldComponent(
                labelValue = "Full Name",
                icon = Icons.Outlined.Person,
                textValue = fullNameValue,
                onValueChange = onFullNameValueChange
            )
            Spacer(modifier = Modifier.height(10.dp))
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
            CheckboxComponent()
            Spacer(modifier = Modifier.weight(1f))
            BottomComponent(
                textQuery = "Already have an account? ",
                textClickable = "Login",
                action = "Register",
                onButtonClick = onRegisterButtonClick,
                onNavigateToScreen = onNavigateToScreen
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegisterContentPreview() {
    JejuAItineraryTheme {
        Surface {
            RegisterContent(
                fullNameValue = "Full Name",
                onFullNameValueChange = {},
                emailValue = "email",
                passwordValue = "password",
                onEmailValueChange = {},
                onPasswordValueChange = {},
                onRegisterButtonClick = {},
                onNavigateToScreen = {}
            )
        }
    }
}