package defalt.ui.state

import defalt.utils.NetworkResult
import defalt.utils.toUserFriendlyMessage


fun <T, R> NetworkResult<T>.toUiState(transform: (T) -> R): UiState<R> = when (this) {
    is NetworkResult.Success -> UiState.Success(transform(data))
    is NetworkResult.Error -> UiState.Error(message = message)
    is NetworkResult.Exception -> UiState.Error(message = throwable.toUserFriendlyMessage())
}
