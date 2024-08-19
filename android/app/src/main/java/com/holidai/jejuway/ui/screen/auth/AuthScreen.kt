package com.holidai.jejuway.ui.screen.auth

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.R
import com.holidai.jejuway.ui.components.FullscreenLoading
import com.holidai.jejuway.ui.screen.auth.components.LoginContent
import com.holidai.jejuway.ui.screen.auth.components.RegisterContent
import com.holidai.jejuway.ui.theme.JejuAItineraryTheme
import com.holidai.jejuway.ui.theme.spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
    onNavigateToMainScreen: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var isInLoginSection by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = state.isSuccessful) {
        if (state.isSuccessful) onNavigateToMainScreen()
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.large)
                    .then(modifier),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.25f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground_splash),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.8f)
                        .weight(0.75f),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    AnimatedContent(targetState = isInLoginSection, label = "auth") {
                        if (it) {
                            LoginContent(
                                modifier = Modifier.fillMaxWidth(),
                                emailValue = state.loginEmail,
                                passwordValue = state.loginPassword,
                                onEmailValueChange = viewModel::onLoginEmailChanged,
                                onPasswordValueChange = viewModel::onLoginPasswordChanged,
                                onLoginButtonClick = { viewModel.login(context) },
                                onNavigateToScreen = { isInLoginSection = false }
                            )
                        } else {
                            RegisterContent(
                                modifier = Modifier.fillMaxWidth(),
                                fullNameValue = state.registerFullName,
                                onFullNameValueChange = viewModel::onRegisterFullNameChanged,
                                emailValue = state.registerEmail,
                                passwordValue = state.registerPassword,
                                onEmailValueChange = viewModel::onRegisterEmailChanged,
                                onPasswordValueChange = viewModel::onRegisterPasswordChanged,
                                onRegisterButtonClick = { viewModel.register(context) },
                                onNavigateToScreen = { isInLoginSection = true }
                            )
                        }
                    }
                }
            }
        }
    }
    if (state.isLoading) {
        FullscreenLoading()
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthScreenPreview() {
    JejuAItineraryTheme {
        Surface {
            AuthScreen(
                onNavigateToMainScreen = { }
            )
        }
    }
}