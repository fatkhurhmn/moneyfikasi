package dev.muffar.moneyfikasi.about.privacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun PrivacyPolicyScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_privacy_policy),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PolicySection(
                title = stringResource(R.string.privacy_intro_title),
                body = stringResource(R.string.privacy_intro_body)
            )
            PolicySection(
                title = stringResource(R.string.privacy_data_stored_title),
                body = stringResource(R.string.privacy_data_stored_body)
            )
            PolicySection(
                title = stringResource(R.string.privacy_local_storage_title),
                body = stringResource(R.string.privacy_local_storage_body)
            )
            PolicySection(
                title = stringResource(R.string.privacy_backups_exports_title),
                body = stringResource(R.string.privacy_backups_exports_body)
            )
            PolicySection(
                title = stringResource(R.string.privacy_notifications_title),
                body = stringResource(R.string.privacy_notifications_body)
            )
            PolicySection(
                title = stringResource(R.string.privacy_security_title),
                body = stringResource(R.string.privacy_security_body)
            )
            PolicySection(
                title = stringResource(R.string.privacy_third_party_title),
                body = stringResource(R.string.privacy_third_party_body)
            )
            PolicySection(
                title = stringResource(R.string.privacy_user_control_title),
                body = stringResource(R.string.privacy_user_control_body)
            )
            PolicySection(
                title = stringResource(R.string.privacy_changes_title),
                body = stringResource(R.string.privacy_changes_body)
            )
        }
    }
}

@Composable
private fun PolicySection(
    title: String,
    body: String,
) {
    PrimaryCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
