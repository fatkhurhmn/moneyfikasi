package dev.muffar.moneyfikasi.about.main

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Article
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onOpenSourceLicensesClick: () -> Unit,
) {
    val context = LocalContext.current
    val versionName = remember(context) {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        versionNameOf(packageInfo.versionName)
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_about),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            PrimaryCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettingItem(
                        title = stringResource(R.string.label_app_version),
                        subtitle = versionName,
                        icon = Icons.Rounded.Info,
                        onClick = null,
                        trailing = {}
                    )
                    CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    SettingItem(
                        title = stringResource(R.string.title_privacy_policy),
                        subtitle = stringResource(R.string.msg_privacy_policy_description),
                        icon = Icons.AutoMirrored.Rounded.Article,
                        onClick = onPrivacyPolicyClick,
                    )
                    CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    SettingItem(
                        title = stringResource(R.string.title_terms_of_service),
                        subtitle = stringResource(R.string.msg_terms_of_service_description),
                        icon = Icons.Rounded.Description,
                        onClick = onTermsOfServiceClick,
                    )
                    CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    SettingItem(
                        title = stringResource(R.string.title_open_source_licenses),
                        subtitle = stringResource(R.string.msg_open_source_licenses_description),
                        icon = Icons.Rounded.Code,
                        onClick = onOpenSourceLicensesClick,
                    )
                }
            }
        }
    }
}

private fun versionNameOf(versionName: String?): String {
    return versionName?.takeIf { it.isNotBlank() } ?: "-"
}
