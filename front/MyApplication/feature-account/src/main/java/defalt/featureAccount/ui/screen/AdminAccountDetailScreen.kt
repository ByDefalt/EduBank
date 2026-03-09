package defalt.featureAccount.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountStateEnum
import defalt.domain.entity.account.PersonalInformation
import defalt.featureAccount.usecase.AccountWithInfo
import defalt.featureAccount.viewModel.AdminAccountDetailViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoLabelValue
import defalt.ui.component.ArkeoOutlinedButton
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminAccountDetailScreen(
    id: String,
    onBack: () -> Unit = {},
    viewModel: AdminAccountDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actionState by viewModel.actionState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.load(id) }

    Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        ArkeoTopBar(title = "DÉTAIL COMPTE", onBack = onBack)

        UiStateHandler(
            uiState = uiState,
            onRetry = { viewModel.load(id) },
            loadingColor = CustomColor.ArkeoRed,
            errorColor = CustomColor.ArkeoRed,
        ) { data ->
            val isLoading = actionState is UiState.Loading
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // UC6 : Infos
                InfoCard(data)
                // UC14 : Formulaire MAJ infos personnelles
                EditPersonalInfoCard(
                    info = data.personalInfo,
                    isLoading = isLoading,
                    onSave = { updated -> viewModel.updateInfo(id, updated) },
                )
                // UC10 : Changer état
                ActionsCard(
                    isActive = data.account.state == AccountStateEnum.ACTIVE,
                    isLoading = isLoading,
                    onActivate = { viewModel.activate(id) },
                    onDeactivate = { viewModel.deactivate(id) },
                )
            }
        }
    }
}

@Composable
private fun InfoCard(data: AccountWithInfo) {
    ArkeoCard(title = "INFORMATIONS") {
        ArkeoLabelValue("ID", data.account.id ?: "-")
        ArkeoLabelValue("Prénom", data.personalInfo.firstname ?: "-")
        ArkeoLabelValue("Nom", data.personalInfo.lastname ?: "-")
        ArkeoLabelValue("Email", data.personalInfo.email ?: "-")
        ArkeoLabelValue("Téléphone", data.personalInfo.phoneNumber ?: "-")
        ArkeoLabelValue("Adresse", data.personalInfo.address ?: "-")
        ArkeoLabelValue("État", data.account.state?.value ?: "-")
    }
}

@Composable
private fun EditPersonalInfoCard(
    info: PersonalInformation,
    isLoading: Boolean,
    onSave: (PersonalInformation) -> Unit,
) {
    var firstname by remember(info) { mutableStateOf(info.firstname ?: "") }
    var lastname by remember(info) { mutableStateOf(info.lastname ?: "") }
    var email by remember(info) { mutableStateOf(info.email ?: "") }
    var phone by remember(info) { mutableStateOf(info.phoneNumber ?: "") }
    var address by remember(info) { mutableStateOf(info.address ?: "") }

    ArkeoCard(title = "MODIFIER INFORMATIONS PERSONNELLES") {
        OutlinedTextField(value = firstname, onValueChange = { firstname = it }, label = { Text("Prénom") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = lastname, onValueChange = { lastname = it }, label = { Text("Nom") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Téléphone") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Adresse") }, modifier = Modifier.fillMaxWidth())
        ArkeoButton(
            text = if (isLoading) "Enregistrement…" else "ENREGISTRER",
            onClick = {
                if (!isLoading) onSave(info.copy(firstname = firstname, lastname = lastname, email = email, phoneNumber = phone, address = address))
            },
        )
    }
}

@Composable
private fun ActionsCard(isActive: Boolean, isLoading: Boolean, onActivate: () -> Unit, onDeactivate: () -> Unit) {
    ArkeoCard(title = "ACTIONS") {
        if (!isActive) {
            ArkeoButton(
                text = if (isLoading) "En cours…" else "ACTIVER LE COMPTE",
                onClick = { if (!isLoading) onActivate() },
            )
        }
        if (isActive) {
            ArkeoOutlinedButton(
                text = if (isLoading) "En cours…" else "DÉSACTIVER LE COMPTE",
                onClick = { if (!isLoading) onDeactivate() },
                enabled = !isLoading,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    InfoCard(
        data = AccountWithInfo(
            account = Account(id = "acc-0001", state = AccountStateEnum.ACTIVE),
            personalInfo = PersonalInformation(firstname = "Alice", lastname = "Dupont", email = "alice@mail.fr"),
        ),
    )
}
