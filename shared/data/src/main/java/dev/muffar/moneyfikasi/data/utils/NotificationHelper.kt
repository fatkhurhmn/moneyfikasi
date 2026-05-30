package dev.muffar.moneyfikasi.data.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

class NotificationHelper(private val context: Context) {
    fun showRecurringNotification(
        name: String,
        amount: String,
        transactionId: UUID,
        recurringId: UUID? = null,
        type: TransactionType = TransactionType.EXPENSE,
        isEnded: Boolean = false
    ) {
        val channelId = "recurring_transactions"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            context.getString(R.string.title_recurring),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "moneyfikasi://transaction_detail/$transactionId/false".toUri(),
            context,
            Class.forName("dev.muffar.moneyfikasi.MainActivity")
        )

        val pendingIntent = PendingIntent.getActivity(
            context,
            transactionId.hashCode(),
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.title_recurring_transaction_processed))
            .setContentText(
                context.getString(
                    R.string.msg_recurring_transaction_processed,
                    amount,
                    name
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(name.hashCode(), builder.build())

        if (isEnded && recurringId != null) {
            val recurringDeepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                "moneyfikasi://add_edit_recurring_transaction/${type.name.lowercase()}?recurring_transaction_id=$recurringId".toUri(),
                context,
                Class.forName("dev.muffar.moneyfikasi.MainActivity")
            )

            val recurringPendingIntent = PendingIntent.getActivity(
                context,
                recurringId.hashCode(),
                recurringDeepLinkIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val endedBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.title_recurring_transaction_ended))
                .setContentText(
                    context.getString(
                        R.string.msg_recurring_transaction_ended,
                        name
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(recurringPendingIntent)
                .setAutoCancel(true)

            notificationManager.notify(name.hashCode() + 1, endedBuilder.build())
        }
    }
}
