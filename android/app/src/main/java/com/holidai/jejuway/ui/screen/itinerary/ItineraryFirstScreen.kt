package com.holidai.jejuway.ui.screen.itinerary

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.domain.usecases.JsonToTestingLllm
import com.holidai.jejuway.ui.screen.itinerary.components.AddToLibraryDialog
import com.holidai.jejuway.ui.screen.itinerary.components.ItineraryContent
import com.holidai.jejuway.ui.screen.itinerary_builder.components.ItineraryBuilderLoading
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryFirstScreen(
    modifier: Modifier = Modifier,
    content: String,
    viewModel: ItineraryViewModel = koinViewModel(),
    onNavigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val itinerary by viewModel.itinerary.collectAsState()
    val reviewState by viewModel.reviewState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val lazyListState = rememberLazyListState()

    var isReviewDialogOpen by rememberSaveable { mutableStateOf(false) }

    var isEditPreferencesActive by remember { mutableStateOf(false) }

    // show fab when the screen is scrolled to the top
    val showFab: Boolean by remember(lazyListState) {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
    }

    BackHandler {
        if (isEditPreferencesActive) {
            isEditPreferencesActive = false
            return@BackHandler
        }
        onNavigateUp()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.setContent(content)
        delay(2000)
        try {
        viewModel.setContent(content)
            val jsonToTestingLllmm = JsonToTestingLllm().invoke(content)
            Timber.i("jsonToTestingLllmm: $jsonToTestingLllmm")
            delay(2000)
            viewModel.setItinerary(
                context = context,
                jsonToTestingLllmm
            )
        } catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(context, "Failed to parse itinerary", Toast.LENGTH_SHORT).show()
        }

//        // Dummy
//        viewModel.setItinerary(
//            context = context,
//            DummyDataSource.provideGeneratedItinerary("123")
//        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    navigationIconContentColor = Color.White,
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    titleContentColor = Color.Transparent,
                    actionIconContentColor = Color.White
                ),
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate up"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }
                ),
                exit = fadeOut() + slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }
                ),
            ) {
                FloatingActionButton(
                    onClick = { isEditPreferencesActive = true },
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = innerPadding.calculateBottomPadding()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val selectedImage = remember { mutableStateListOf<Uri?>(null) }
            val photoPicker =
                rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
                    selectedImage.apply {
                        clear()
                        addAll(it)
                        Toast.makeText(
                            context,
                            "Images added to the activity's album",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            itinerary?.let {
                ItineraryContent(
                    schedules = it.schedule,
                    lazyListState = lazyListState,
                    onDismissedToEnd = {
                        photoPicker.launch("image/*")
                    },
                    onDismissedToStart = { schedule ->
                        isReviewDialogOpen = true
                        viewModel._currentScheduleId.value = schedule.id
                    },
                )
            }
        }
    }

    AnimatedVisibility(
        visible = isEditPreferencesActive,
    ) {
        var notes by remember { mutableStateOf("") }

        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data = it.data
                    val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    notes += result?.get(0) ?: ""
                } else {
                    Toast.makeText(context, "Voice input failed", Toast.LENGTH_SHORT).show()
                }
            }


        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            value = notes,
                            onValueChange = {
                                notes = it
                            },
                            placeholder = { Text("Add your preferences here") },
                            maxLines = 5,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Default
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                            intent.putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            intent.putExtra(
                                RecognizerIntent.EXTRA_PROMPT,
                                "Go on then, say something."
                            )
                            launcher.launch(intent)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardVoice, contentDescription = null)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.updateItinerary(context, notes)
                            isEditPreferencesActive = false
                            notes = ""
                        }) {
                        Text(
                            text = "Save",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    if (isLoading) {
        ItineraryBuilderLoading()
    }

    if (isReviewDialogOpen) {
        AddToLibraryDialog(
            onDismissRequest = {
                isReviewDialogOpen = false
                viewModel.onRatingChanged(0f)
                viewModel.onReviewChanged("")
            },
            onConfirmation = {
                viewModel.onSubmitReview(context)
                isReviewDialogOpen = false
            },
            review = reviewState.review,
            onReviewChange = viewModel::onReviewChanged,
            rating = reviewState.rating,
            onRatingChange = viewModel::onRatingChanged
        )
    }
}