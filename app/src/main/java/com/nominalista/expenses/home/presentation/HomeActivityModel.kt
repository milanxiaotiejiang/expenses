package com.nominalista.expenses.home.presentation

import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import com.nominalista.expenses.Application
import com.nominalista.expenses.R
import com.nominalista.expenses.settings.work.ExpenseExportWorker
import com.nominalista.expenses.settings.work.ExpenseImportWorker
import com.nominalista.expenses.util.reactive.DataEvent
import com.nominalista.expenses.util.reactive.Event
import java.util.*

class HomeActivityModel(
        application: Application
) : AndroidViewModel(application) {

    val selectFileForImport = Event()
    val showExpenseImportFailureDialog = Event()
    val requestExportPermissions = Event()
    val showExpenseExportFailureDialog = Event()
    val navigateToSettings = Event()
    val navigateToSupport = Event()

    val showMessage = DataEvent<Int>()
    val showActivity = DataEvent<Uri>()
    val observeWorkInfo = DataEvent<UUID>()

    private var expenseImportId: UUID? = null
    private var expenseExportId: UUID? = null


    fun importExpensesRequested() {
        selectFileForImport.next()
    }

    fun fileForImportSelected(fileUri: Uri) {
        if (expenseImportId != null) return

        val id = ExpenseImportWorker.enqueue(getApplication<Application>(), fileUri)
        expenseImportId = id
        observeWorkInfo.next(id)
    }

    fun downloadTemplate() {
        showActivity.next(TEMPLATE_XLS_URI)
    }

    fun exportExpensesRequested() {
        requestExportPermissions.next()
    }

    fun exportPermissionsGranted() {
        if (expenseExportId != null) return

        val id = ExpenseExportWorker.enqueue(getApplication<Application>())
        expenseExportId = id
        observeWorkInfo.next(id)
    }

    fun handleWorkInfo(workInfo: WorkInfo) {
        when (workInfo.id) {
            expenseImportId -> handleExpenseImportWorkInfo(workInfo)
            expenseExportId -> handleExpenseExportWorkInfo(workInfo)
        }
    }

    private fun handleExpenseImportWorkInfo(workInfo: WorkInfo) {
        expenseImportId = when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                showMessage.next(R.string.expense_import_success_message)
                null
            }
            WorkInfo.State.FAILED -> {
                showExpenseImportFailureDialog.next()
                null
            }
            else -> return
        }
    }

    private fun handleExpenseExportWorkInfo(workInfo: WorkInfo) {
        expenseExportId = when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                showMessage.next(R.string.expense_export_success_message)
                null
            }
            WorkInfo.State.FAILED -> {
                showExpenseExportFailureDialog.next()
                null
            }
            else -> return
        }
    }

    fun navigateToSettingsRequested() {
        navigateToSettings.next()
    }

    fun navigateToSupportRequested() {
        navigateToSupport.next()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeActivityModel(
                    application
            ) as T
        }
    }

    companion object {
        private val TEMPLATE_XLS_URI =
                Uri.parse("https://raw.githubusercontent.com/nominalista/expenses/master/resources/template.xls")
    }
}