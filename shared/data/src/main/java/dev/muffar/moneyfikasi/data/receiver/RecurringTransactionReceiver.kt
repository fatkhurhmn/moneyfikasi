package dev.muffar.moneyfikasi.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dev.muffar.moneyfikasi.data.worker.RecurringTransactionWorker

class RecurringTransactionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val workRequest = OneTimeWorkRequestBuilder<RecurringTransactionWorker>().build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
