package defalt.featureAccount.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.featureAccount.viewModel.RegisterViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoErrorText
import defalt.ui.component.ArkeoInput
import defalt.ui.component.safeClick
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun RegisterScreen(
    onBackToHome: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Affiche le dialog dès que la création est un succès
    if (uiState is UiState.Success) {
        val accountId = (uiState as UiState.Success).data.id ?: "—"
        AccountCreatedDialog(
            accountId = accountId,
            onConfirm = onBackToHome,
        )
    }

    RegisterContent(
        onRegister = { email, password, firstname, lastname, address, phoneNumber ->
            viewModel.register(email, password, firstname, lastname, address, phoneNumber)
        },
        errorMessage = (uiState as? UiState.Error)?.message,
        isLoading = uiState is UiState.Loading,
        onBackToHome = onBackToHome,
    )
}

// ── Dialog numéro de compte ──────────────────────────────────────────────────
@Composable
private fun AccountCreatedDialog(
    accountId: String,
    onConfirm: () -> Unit,
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }

    // Reset du feedback après un court délai
    LaunchedEffect(copied) {
        if (copied) {
            delay(1800)
            copied = false
        }
    }

    AlertDialog(
        onDismissRequest = {},
        containerColor = Color.White,
        title = {
            Text(
                text = "Compte créé avec succès",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CustomColor.TextPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = CustomColor.BackgroundGray),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "⚠️ Notez bien votre numéro de compte",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CustomColor.ArkeoRed,
                            textAlign = TextAlign.Center,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = CustomColor.TextPrimary,
                                        ),
                                    ) {
                                        append(accountId)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                            )

                            IconButton(onClick = {
                                clipboard.setText(AnnotatedString(accountId))
                                copied = true
                                // Petit feedback système aussi
                                Toast.makeText(context, "Numéro copié", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    contentDescription = "Copier le numéro",
                                )
                            }
                        }

                        if (copied) {
                            Text(
                                text = "Copié !",
                                fontSize = 12.sp,
                                color = CustomColor.ArkeoRed,
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            ArkeoButton(
                text = "OK",
                onClick = onConfirm,
            )
        },
    )
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun RegisterContent(
    onRegister: (String, String, String, String, String, String) -> Unit = { _, _, _, _, _, _ -> },
    onBackToHome: () -> Unit = {},
    errorMessage: String? = null,
    isLoading: Boolean = false,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = CustomColor.ArkeoWhite),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "OUVERTURE DE COMPTE",
                    color = CustomColor.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            }
        }

        // Carte Formulaire Inscription
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "VOS COORDONNÉES",
                    color = CustomColor.ArkeoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                ArkeoInput(
                    email,
                    { email = it },
                    "E-mail",
                    keyboardType = KeyboardType.Email,
                    icon = Icons.Outlined.Person,
                )
                ArkeoInput(
                    password,
                    { password = it },
                    "Mot de passe",
                    icon = Icons.Outlined.Lock,
                    isPassword = true,
                )
                ArkeoInput(
                    firstname,
                    { firstname = it },
                    "Prénom",
                    icon = Icons.Outlined.Person,
                )
                ArkeoInput(
                    lastname,
                    { lastname = it },
                    "Nom",
                    icon = Icons.Outlined.Person,
                )
                ArkeoInput(
                    address,
                    { address = it },
                    "Adresse",
                    icon = Icons.Outlined.Place,
                )
                ArkeoInput(
                    phoneNumber,
                    { phoneNumber = it },
                    "Numéro de téléphone",
                    keyboardType = KeyboardType.Phone,
                    icon = Icons.Outlined.Phone,
                )

                Spacer(modifier = Modifier.height(8.dp))
                if (errorMessage != null) {
                    ArkeoErrorText(message = errorMessage)
                }
                ArkeoButton(
                    text = "VALIDER LA DEMANDE",
                    onClick = { onRegister(email, password, firstname, lastname, address, phoneNumber) },
                    enabled = !isLoading,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = safeClick(onBackToHome),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 24.dp),
        ) {
            Text("Annuler et retour", color = CustomColor.ArkeoRed)
        }
    }
}

@Suppress("VisualLintBounds")
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterContent(onBackToHome = {})
}

@Preview(showBackground = true)
@Composable
fun AccountCreatedDialogPreview() {
    AccountCreatedDialog(
        accountId = "ACC-20260309-00042",
        onConfirm = {},
    )
}
