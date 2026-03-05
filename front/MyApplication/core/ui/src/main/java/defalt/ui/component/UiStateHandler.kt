package defalt.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import defalt.ui.state.UiState

/**
 * Gère les états Loading / Error / Success de façon centralisée.
 * Le contenu Success est délégué via [onSuccess].
 *
 * Usage :
 * ```
 * UiStateHandler(uiState = uiState, onRetry = viewModel::retry) { accounts ->
 *     LazyColumn { ... }
 * }
 * ```
 */
@Composable
fun <T> UiStateHandler(
    uiState: UiState<T>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    loadingColor: Color = Color.Red,
    errorColor: Color = Color.Red,
    onSuccess: @Composable (T) -> Unit,
) {
    when (uiState) {
        is UiState.Loading -> LoadingContent(
            modifier = modifier,
            color = loadingColor,
        )
        is UiState.Error -> ErrorContent(
            message = uiState.message,
            onRetry = onRetry,
            modifier = modifier,
            color = errorColor,
        )
        is UiState.Success -> onSuccess(uiState.data)
    }
}

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = color)
    }
}

@Composable
fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    color: Color = Color.Red,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Réessayer",
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onRetry),
            )
        }
    }
}
