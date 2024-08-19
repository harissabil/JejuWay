package com.holidai.jejuway.ui.screen.itinerary_builder

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.holidai.jejuway.ui.screen.itinerary_builder.components.DatePage
import com.holidai.jejuway.ui.screen.itinerary_builder.components.ItineraryBuilderLoading
import com.holidai.jejuway.ui.screen.itinerary_builder.components.NotesPage
import com.holidai.jejuway.ui.screen.itinerary_builder.components.PersonPage
import com.holidai.jejuway.ui.screen.itinerary_builder.components.ThemePage
import com.holidai.jejuway.ui.theme.spacing
import com.holidai.jejuway.utils.toDateYyyyMmDd
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryBuilderScreen(
    modifier: Modifier = Modifier,
    viewModel: ItineraryBuilderViewModel = koinViewModel(),
    onNavigateToItineraryFirst: (content: String) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val dateRangePickerState = rememberDateRangePickerState()

    val themeLazyGridState = rememberLazyGridState()
    val themeSelected = rememberSaveable { mutableSetOf<JejuTheme>() }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                viewModel.onNotesChanged(state.notes + result?.get(0) + " ")
            } else {
                Toast.makeText(context, "Voice input failed", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(key1 = state.isSuccessful) {
        if (!state.isSuccessful) {
            return@LaunchedEffect
        }

        if (state.content == null) {
            Toast.makeText(context, "Failed to generate itinerary.", Toast.LENGTH_SHORT).show()
        } else {
            onNavigateToItineraryFirst(state.content!!)
        }
    }

    Scaffold { innerPadding ->
        ItineraryBuilderContent(
            modifier = modifier.padding(innerPadding),
            state = state,
            dateRangePickerState = dateRangePickerState,
            themeLazyGridState = themeLazyGridState,
            onThemeSelected = { theme ->
                if (themeSelected.contains(theme)) {
                    themeSelected.remove(theme)
                } else {
                    themeSelected.add(theme)
                }
            },
            onTravelingWithKidsSelected = viewModel::onTravelingWithKidsChanged,
            onTravelingWithPetsSelected = viewModel::onTravelingWithPetsChanged,
            onPersonCountSelected = viewModel::onPersonCountChanged,
            onNotesChanged = viewModel::onNotesChanged,
            onVoiceInput = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Go on then, say something.")
                launcher.launch(intent)
            },
            onNavigateToItinerary = {
                Toast.makeText(
                    context,
                    "Generating the best and personalized itinerary for you...",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.onGeneratingItinerary(
                    startDate = dateRangePickerState.selectedStartDateMillis?.toDateYyyyMmDd()
                        ?: "",
                    endDate = dateRangePickerState.selectedEndDateMillis?.toDateYyyyMmDd() ?: "",
                    themes = themeSelected.joinToString(", ") { it.value }
                )
            }
        )
    }

    if (state.isGenerating) {
        ItineraryBuilderLoading()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryBuilderContent(
    modifier: Modifier = Modifier,
    state: ItineraryBuilderState,
    dateRangePickerState: DateRangePickerState,
    themeLazyGridState: LazyGridState,
    onThemeSelected: (JejuTheme) -> Unit,
    onTravelingWithKidsSelected: (Boolean) -> Unit,
    onTravelingWithPetsSelected: (Boolean) -> Unit,
    onPersonCountSelected: (Int) -> Unit,
    onNotesChanged: (String) -> Unit,
    onVoiceInput: () -> Unit,
    onNavigateToItinerary: () -> Unit,
) {
    val pages = rememberSaveable { listOf("Date", "Person", "Theme", "Notes") }
    var currentPage by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        Text(
            modifier = Modifier
                .animateContentSize()
                .padding(horizontal = MaterialTheme.spacing.large),
            text = "How Do You Want To Plan Your Trip? Let Us Know!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.large)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            pages.forEachIndexed { index, s ->
                AssistChip(
                    onClick = { currentPage = index },
                    label = { Text(text = s) },
                    colors = AssistChipDefaults.assistChipColors().copy(
                        containerColor = if (index == currentPage)
                            MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                        labelColor = if (index == currentPage)
                            MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        AnimatedContent(
            modifier = Modifier.weight(1f), targetState = currentPage, label = "ItineraryBuilder"
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                when (page) {
                    0 -> DatePage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.medium),
                        state = dateRangePickerState
                    )

                    1 -> PersonPage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                        isTravelingWithKids = state.isTravelingWithKids,
                        isTravelingWithPets = state.isTravelingWithPets,
                        onTravelingWithKidsSelected = onTravelingWithKidsSelected,
                        onTravelingWithPetsSelected = onTravelingWithPetsSelected,
                        onPersonCountSelected = onPersonCountSelected
                    )

                    2 -> ThemePage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                        state = themeLazyGridState,
                        onThemeSelected = onThemeSelected,
                    )

                    3 -> NotesPage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                        notes = state.notes,
                        onNotesChanged = onNotesChanged,
                        onVoiceInput = onVoiceInput
                    )
                }
            }
        }

        Button(
            onClick = {
                if (currentPage < pages.size - 1) {
                    currentPage += 1
                } else {
                    onNavigateToItinerary()
                }
            }) {
            Text(
                text = if (currentPage < pages.size - 1) "Next" else "Finish",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}