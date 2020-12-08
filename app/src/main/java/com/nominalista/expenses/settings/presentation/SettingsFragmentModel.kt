package com.nominalista.expenses.settings.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nominalista.expenses.Application
import com.nominalista.expenses.R
import com.nominalista.expenses.common.presentation.Theme
import com.nominalista.expenses.data.preference.PreferenceDataSource
import com.nominalista.expenses.util.reactive.DataEvent
import com.nominalista.expenses.util.reactive.Variable
import io.reactivex.disposables.CompositeDisposable

class SettingsFragmentModel(
        application: Application,
        private val preferenceDataSource: PreferenceDataSource
) : AndroidViewModel(application) {
    val itemModels = Variable(emptyList<SettingItemModel>())
    val showMessage = DataEvent<Int>()
    val showActivity = DataEvent<Uri>()
    val showThemeSelectionDialog = DataEvent<Theme>()
    val applyTheme = DataEvent<Theme>()

    private val disposables = CompositeDisposable()

    // Lifecycle start

    init {
        loadItemModels()
    }

    private fun loadItemModels() {
        itemModels.value = createApplicationSection()
    }

    private fun createApplicationSection(): List<SettingItemModel> {
        val context = getApplication<Application>()

        val itemModels = mutableListOf<SettingItemModel>()
        itemModels += createApplicationHeader(context)
        itemModels += createDarkMode(context)

        return itemModels
    }

    private fun createApplicationHeader(context: Context): SettingItemModel =
            SettingsHeaderModel(context.getString(R.string.application))

    private fun createDarkMode(context: Context): SettingItemModel {
        val title = context.getString(R.string.theme)

        val darkMode = preferenceDataSource.getTheme(context)

        val summary = when (darkMode) {
            Theme.LIGHT -> context.getString(R.string.light)
            Theme.DARK -> context.getString(R.string.dark)
            Theme.SYSTEM_DEFAULT -> context.getString(R.string.system_default)
        }

        return SummaryActionSettingItemModel(title, summary).apply {
            click = { showThemeSelectionDialog.next(darkMode) }
        }
    }

    // Lifecycle end

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun themeSelected(theme: Theme) {
        getApplication<Application>().let {
            preferenceDataSource.setTheme(it, theme)
        }

        loadItemModels()

        applyTheme.next(theme)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingsFragmentModel(
                    application,
                    application.preferenceDataSource
            ) as T
        }
    }

}