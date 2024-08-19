package com.holidai.jejuway.ui.screen.itinerary_builder.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.ui.theme.JejuAItineraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonPage(
    modifier: Modifier = Modifier,
    onPersonCountSelected: (Int) -> Unit,
    isTravelingWithKids: Boolean,
    onTravelingWithKidsSelected: (Boolean) -> Unit,
    isTravelingWithPets: Boolean,
    onTravelingWithPetsSelected: (Boolean) -> Unit,
) {
    val radioOptions = remember {
        listOf(
            "I'm going solo",
            "Just the two of us",
            "Three's company",
            "The more the merrier",
        )
    }

    var selectedOption by rememberSaveable { mutableIntStateOf(0) }

    var morePeopleValue: String? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = selectedOption) {
        when (selectedOption) {
            0 -> onPersonCountSelected(1)
            1 -> onPersonCountSelected(2)
            2 -> onPersonCountSelected(3)
            3 -> {
                if (morePeopleValue != null) {
                    onPersonCountSelected(morePeopleValue!!.toInt())
                }
            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .then(modifier),
    ) {
        radioOptions.forEachIndexed { index, s ->
            if (index == 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(horizontal = 16.dp)
                        .clickable { selectedOption = index },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == index,
                        onClick = { selectedOption = index }
                    )
                    Text(
                        text = s,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    val interactionSource = remember { MutableInteractionSource() }
                    BasicTextField(
                        value = morePeopleValue ?: "",
                        onValueChange = { morePeopleValue = it },
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .width(48.dp)
                            .height(36.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        interactionSource = interactionSource,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    ) { innerTextField ->
                        OutlinedTextFieldDefaults.DecorationBox(
                            value = morePeopleValue ?: "",
                            innerTextField = innerTextField,
                            enabled = true,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = interactionSource,
                            colors = OutlinedTextFieldDefaults.colors(),
                            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                                top = 0.dp,
                                bottom = 0.dp,
                            ),
                            container = {
                                OutlinedTextFieldDefaults.ContainerBox(
                                    enabled = true,
                                    isError = false,
                                    interactionSource = interactionSource,
                                    colors = TextFieldDefaults.colors()
                                )
                            },
                        )
                    }
                    Text(
                        text = "people",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
                return@forEachIndexed
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(horizontal = 16.dp)
                    .clickable { selectedOption = index },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == index,
                    onClick = { selectedOption = index }
                )
                Text(
                    text = s,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Traveling with kids?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.padding(start = 12.dp),
                ) {
                    val options = mapOf("Yes" to true, "No" to false)
                    options.keys.forEachIndexed { index, s ->
                        SegmentedButton(
                            selected = isTravelingWithKids == options[s],
                            onClick = { onTravelingWithKidsSelected(options[s]!!) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            label = {
                                Text(text = s)
                            }
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Traveling with pets?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.padding(start = 12.dp),
                ) {
                    val options = mapOf("Yes" to true, "No" to false)
                    options.keys.forEachIndexed { index, s ->
                        SegmentedButton(
                            selected = isTravelingWithPets == options[s],
                            onClick = { onTravelingWithPetsSelected(options[s]!!) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            label = {
                                Text(text = s)
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PersonPagePreview() {
    JejuAItineraryTheme {
        Surface {
            PersonPage(
                onPersonCountSelected = { },
                isTravelingWithKids = false,
                onTravelingWithKidsSelected = { },
                isTravelingWithPets = false,
                onTravelingWithPetsSelected = { }
            )
        }
    }
}