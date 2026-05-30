package dev.muffar.moneyfikasi.export.utils

import android.content.Context
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedDateTime
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream
import java.io.OutputStreamWriter

class ExportManager(private val context: Context) {

    private val headers = listOf(
        context.getString(R.string.label_date_header),
        context.getString(R.string.label_type_header),
        context.getString(R.string.label_category_header),
        context.getString(R.string.label_wallet_header),
        context.getString(R.string.label_amount_header),
        context.getString(R.string.label_note_header)
    )

    fun exportToCsv(transactions: List<Transaction>, outputStream: OutputStream) {
        val writer = OutputStreamWriter(outputStream)
        writer.write(headers.joinToString(",") + "\n")
        transactions.forEach { transaction ->
            val line = "${transaction.date.formattedDateTime()}," +
                    "${transaction.type.value}," +
                    "${transaction.category.name}," +
                    "${transaction.wallet.name}," +
                    "${transaction.amount}," +
                    "${transaction.note ?: ""}\n"
            writer.write(line)
        }
        writer.flush()
        writer.close()
    }

    fun exportToExcel(transactions: List<Transaction>, outputStream: OutputStream) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet(context.getString(R.string.menu_transactions))

        val headerRow = sheet.createRow(0)
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }

        transactions.forEachIndexed { index, transaction ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(transaction.date.formattedDateTime())
            row.createCell(1).setCellValue(transaction.type.value)
            row.createCell(2).setCellValue(transaction.category.name)
            row.createCell(3).setCellValue(transaction.wallet.name)
            row.createCell(4).setCellValue(transaction.amount)
            row.createCell(5).setCellValue(transaction.note ?: "")
        }

        workbook.write(outputStream)
        workbook.close()
    }
}
