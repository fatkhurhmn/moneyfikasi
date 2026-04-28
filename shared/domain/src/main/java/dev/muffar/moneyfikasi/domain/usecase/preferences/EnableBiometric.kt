package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class EnableBiometric(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(enable: Boolean) {
        preferencesRepository.enableBiometric(enable)
    }
}
