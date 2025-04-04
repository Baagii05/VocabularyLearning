package com.example.vocabularylearning.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vocabularylearning.R
import com.example.vocabularylearning.VocabularyTopAppBar
import com.example.vocabularylearning.data.WordDisplayMode
import com.example.vocabularylearning.ui.AppViewModelProvider
import com.example.vocabularylearning.ui.navigation.NavigationDestination

object DisplaySettingsDestination : NavigationDestination {
    override val route = "display_settings"
    override val titleRes = R.string.edit_item_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySettingsScreen(
    navigateBack: () -> Unit,
    viewModel: DisplaySettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            VocabularyTopAppBar(
                title = "Display Settings",
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Word Display Options",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            DisplayModeOption(
                text = "Show both Mongolian and foreign words",
                selected = uiState.displayMode == WordDisplayMode.BOTH,
                onClick = { viewModel.updateDisplayMode(WordDisplayMode.BOTH) }
            )

            DisplayModeOption(
                text = "Show only foreign words",
                selected = uiState.displayMode == WordDisplayMode.FOREIGN_ONLY,
                onClick = { viewModel.updateDisplayMode(WordDisplayMode.FOREIGN_ONLY) }
            )

            DisplayModeOption(
                text = "Show only Mongolian words",
                selected = uiState.displayMode == WordDisplayMode.MONGOLIAN_ONLY,
                onClick = { viewModel.updateDisplayMode(WordDisplayMode.MONGOLIAN_ONLY) }
            )
        }
    }
}

@Composable
fun DisplayModeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}