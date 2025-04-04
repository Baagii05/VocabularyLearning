package com.example.vocabularylearning.ui.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vocabularylearning.R
import com.example.vocabularylearning.VocabularyTopAppBar
import com.example.vocabularylearning.data.Word
import com.example.vocabularylearning.data.WordDisplayMode
import com.example.vocabularylearning.ui.AppViewModelProvider
import com.example.vocabularylearning.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToWordEntry: () -> Unit,
    navigateToWordEdit: (Int) -> Unit,
    navigateToDisplaySettings: () -> Unit,
    windowSize: WindowWidthSizeClass,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            VocabularyTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToWordEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.word_entry_title)
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            wordList = homeUiState.wordList,
            displayMode = homeUiState.displayMode,
            onItemClick = navigateToWordEdit,
            onDeleteWord = viewModel::deleteWord,
            onSettingsClick = navigateToDisplaySettings,
            windowSize = windowSize,
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
fun HomeBody(
    wordList: List<Word>,
    displayMode: WordDisplayMode,
    onItemClick: (Int) -> Unit,
    onDeleteWord: (Word) -> Unit,
    onSettingsClick: () -> Unit,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    var currentWordIndex by remember { mutableStateOf(0) }
    var showHiddenContent by remember { mutableStateOf(false) }


    val isLandscape = windowSize != WindowWidthSizeClass.Compact

    if (wordList.isEmpty()) {
        Column(
            modifier = modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No vocabulary words available",
                style = MaterialTheme.typography.titleLarge
            )
        }
        return
    }

    val currentWord = wordList[currentWordIndex]

    if (isLandscape) {

        Row(
            modifier = modifier.padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { onItemClick(currentWord.id) },
                                onTap = {
                                    if (displayMode != WordDisplayMode.BOTH) {
                                        showHiddenContent = !showHiddenContent
                                    } else {
                                        onItemClick(currentWord.id)
                                    }
                                }
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if (displayMode == WordDisplayMode.BOTH ||
                            displayMode == WordDisplayMode.MONGOLIAN_ONLY ||
                            (displayMode == WordDisplayMode.FOREIGN_ONLY && showHiddenContent)
                        ) {
                            Text(
                                text = currentWord.mongolian,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            if (displayMode == WordDisplayMode.BOTH) {
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                        if (displayMode == WordDisplayMode.BOTH ||
                            displayMode == WordDisplayMode.FOREIGN_ONLY ||
                            (displayMode == WordDisplayMode.MONGOLIAN_ONLY && showHiddenContent)
                        ) {
                            if (displayMode != WordDisplayMode.BOTH) {
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            Text(
                                text = currentWord.english,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        if ((displayMode == WordDisplayMode.FOREIGN_ONLY && !showHiddenContent) ||
                            (displayMode == WordDisplayMode.MONGOLIAN_ONLY && !showHiddenContent)
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Tap to reveal",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(onClick = onSettingsClick) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    Text(text = "Display Mode", modifier = Modifier.padding(start = 8.dp))
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (currentWordIndex > 0) {
                                currentWordIndex--
                            } else {
                                currentWordIndex = wordList.size - 1
                            }
                            showHiddenContent = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Previous word"
                        )
                        Text(text = "Previous", modifier = Modifier.padding(start = 4.dp))
                    }

                    Button(
                        onClick = {
                            if (currentWordIndex < wordList.size - 1) {
                                currentWordIndex++
                            } else {
                                currentWordIndex = 0
                            }
                            showHiddenContent = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Next", modifier = Modifier.padding(end = 4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next word"
                        )
                    }
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { deleteConfirmationRequired = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete word",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(text = "Delete")
                    }

                    Button(
                        onClick = { onItemClick(currentWord.id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Edit")
                    }
                }


                Text(
                    text = "${currentWordIndex + 1} / ${wordList.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } else {

        Column(
            modifier = modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            ) {
                Button(
                    onClick = onSettingsClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                    Text(text = "Display Mode", modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { onItemClick(currentWord.id) },
                            onTap = {
                                if (displayMode != WordDisplayMode.BOTH) {
                                    showHiddenContent = !showHiddenContent
                                } else {
                                    onItemClick(currentWord.id)
                                }
                            }
                        )
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (displayMode == WordDisplayMode.BOTH ||
                        displayMode == WordDisplayMode.MONGOLIAN_ONLY ||
                        (displayMode == WordDisplayMode.FOREIGN_ONLY && showHiddenContent)
                    ) {
                        Text(
                            text = currentWord.mongolian,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        if (displayMode == WordDisplayMode.BOTH) {
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }

                    if (displayMode == WordDisplayMode.BOTH ||
                        displayMode == WordDisplayMode.FOREIGN_ONLY ||
                        (displayMode == WordDisplayMode.MONGOLIAN_ONLY && showHiddenContent)
                    ) {
                        Spacer(modifier = Modifier.height(60.dp))
                        Text(
                            text = currentWord.english,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    if ((displayMode == WordDisplayMode.FOREIGN_ONLY && !showHiddenContent) ||
                        (displayMode == WordDisplayMode.MONGOLIAN_ONLY && !showHiddenContent)
                    ) {
                        Spacer(modifier = Modifier.height(60.dp))
                        Text(
                            text = "Tap to reveal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        if (currentWordIndex > 0) {
                            currentWordIndex--
                        } else {
                            currentWordIndex = wordList.size - 1
                        }
                        showHiddenContent = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous word"
                    )
                    Text(text = "Previous", modifier = Modifier.padding(start = 8.dp))
                }

                Button(
                    onClick = {
                        if (currentWordIndex < wordList.size - 1) {
                            currentWordIndex++
                        } else {
                            currentWordIndex = 0
                        }
                        showHiddenContent = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Next", modifier = Modifier.padding(end = 8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next word"
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { deleteConfirmationRequired = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete word",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Delete")
                }

                Button(
                    onClick = { onItemClick(currentWord.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Edit")
                }
            }


            Text(
                text = "${currentWordIndex + 1} / ${wordList.size}",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }


    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDeleteWord(wordList[currentWordIndex])

                if (currentWordIndex >= wordList.size - 1 && wordList.size > 1) {
                    currentWordIndex--
                }
            },
            onDeleteCancel = { deleteConfirmationRequired = false },
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}
@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        }
    )
}