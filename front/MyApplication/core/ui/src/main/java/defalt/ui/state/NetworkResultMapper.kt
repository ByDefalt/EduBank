package defalt.ui.state

import defalt.utils.NetworkResult
import defalt.utils.toUserFriendlyMessage

/**
 * Convertit un [NetworkResult] en [UiState] en mappant automatiquement
 * les erreurs HTTP et les exceptions en messages lisibles pour l'utilisateur.
 *
 * Le [transform] permet de convertir le type du succès.
 * Ex : `result.toUiState { it }` pour conserver la valeur, `result.toUiState { Unit }` pour l'ignorer.
 */
fun <T, R> NetworkResult<T>.toUiState(transform: (T) -> R): UiState<R> = when (this) {
    is NetworkResult.Success -> UiState.Success(transform(data))
    is NetworkResult.Error -> UiState.Error(message = message)
    is NetworkResult.Exception -> UiState.Error(message = throwable.toUserFriendlyMessage())
}
