package dev.muffar.moneyfikasi.about.terms

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
fun TermsOfServiceScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_terms_of_service),
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
            TermsSection(
                title = stringResource(R.string.terms_acceptance_title),
                body = stringResource(R.string.terms_acceptance_body)
            )
            TermsSection(
                title = stringResource(R.string.terms_use_title),
                body = stringResource(R.string.terms_use_body)
            )
            TermsSection(
                title = stringResource(R.string.terms_financial_title),
                body = stringResource(R.string.terms_financial_body)
            )
            TermsSection(
                title = stringResource(R.string.terms_user_data_title),
                body = stringResource(R.string.terms_user_data_body)
            )
            TermsSection(
                title = stringResource(R.string.terms_backups_exports_title),
                body = stringResource(R.string.terms_backups_exports_body)
            )
            TermsSection(
                title = stringResource(R.string.terms_security_title),
                body = stringResource(R.string.terms_security_body)
            )
            TermsSection(
                title = stringResource(R.string.terms_availability_title),
                body = stringResource(R.string.terms_availability_body)
            )
            TermsSection(
                title = stringResource(R.string.terms_changes_title),
                body = stringResource(R.string.terms_changes_body)
            )
        }
    }
}

@Composable
private fun TermsSection(
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
