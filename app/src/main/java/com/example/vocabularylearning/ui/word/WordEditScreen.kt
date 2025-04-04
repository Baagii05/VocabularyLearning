package com.example.vocabularylearning.ui.word

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vocabularylearning.R
import com.example.vocabularylearning.ui.AppViewModelProvider
import com.example.vocabularylearning.VocabularyTopAppBar
import com.example.vocabularylearning.ui.navigation.NavigationDestination
import com.example.vocabularylearning.ui.theme.VocabularyLearningTheme
import kotlinx.coroutines.launch

object WordEditDestination : NavigationDestination {
    override val route = "word_edit"
    override val titleRes = R.string.edit_item_title
    const val wordIdArg = "wordId"
    val routeWithArgs = "$route/{$wordIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WordEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            VocabularyTopAppBar(
                title = stringResource(WordEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        WordEntryBody(
            wordUiState = viewModel.wordUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateWord()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemEditScreenPreview() {
    VocabularyLearningTheme {
        WordEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}
