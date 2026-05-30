package dev.muffar.moneyfikasi.about.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.about.main.AboutScreen
import dev.muffar.moneyfikasi.about.privacy.PrivacyPolicyScreen
import dev.muffar.moneyfikasi.about.terms.TermsOfServiceScreen
import dev.muffar.moneyfikasi.navigation.Screen

fun NavController.navigateToAbout() {
    navigate(Screen.About.route)
}

fun NavController.navigateToPrivacyPolicy() {
    navigate(Screen.PrivacyPolicy.route)
}

fun NavController.navigateToTermsOfService() {
    navigate(Screen.TermsOfService.route)
}

fun NavGraphBuilder.aboutNavGraph(
    onBackClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
) {
    composable(route = Screen.About.route) {
        AboutScreen(
            onBackClick = onBackClick,
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onTermsOfServiceClick = onTermsOfServiceClick
        )
    }

    composable(route = Screen.PrivacyPolicy.route) {
        PrivacyPolicyScreen(
            onBackClick = onBackClick
        )
    }

    composable(route = Screen.TermsOfService.route) {
        TermsOfServiceScreen(
            onBackClick = onBackClick
        )
    }
}
