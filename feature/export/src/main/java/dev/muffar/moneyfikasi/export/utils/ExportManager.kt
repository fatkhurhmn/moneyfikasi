package dev.muffar.moneyfikasi.export.utils

import android.content.Context
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedDateTime
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

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
        ZipOutputStream(outputStream).use { zip ->
            zip.putTextEntry("[Content_Types].xml", contentTypesXml)
            zip.putTextEntry("_rels/.rels", rootRelationsXml)
            zip.putTextEntry("docProps/app.xml", appPropertiesXml)
            zip.putTextEntry("docProps/core.xml", corePropertiesXml)
            zip.putTextEntry("xl/workbook.xml", workbookXml)
            zip.putTextEntry("xl/_rels/workbook.xml.rels", workbookRelationsXml)
            zip.putTextEntry("xl/worksheets/sheet1.xml", worksheetXml(transactions))
        }
    }

    private fun worksheetXml(transactions: List<Transaction>): String = buildString {
        append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        append("""<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">""")
        append("""<sheetData>""")
        appendRow(1, headers.map { CellValue.Text(it) })
        transactions.forEachIndexed { index, transaction ->
            appendRow(
                index + 2,
                listOf(
                    CellValue.Text(transaction.date.formattedDateTime()),
                    CellValue.Text(transaction.type.value),
                    CellValue.Text(transaction.category.name),
                    CellValue.Text(transaction.wallet.name),
                    CellValue.Number(transaction.amount),
                    CellValue.Text(transaction.note.orEmpty())
                )
            )
        }
        append("""</sheetData></worksheet>""")
    }

    private fun StringBuilder.appendRow(rowIndex: Int, values: List<CellValue>) {
        append("""<row r="$rowIndex">""")
        values.forEachIndexed { columnIndex, value ->
            val cellReference = "${columnName(columnIndex)}$rowIndex"
            when (value) {
                is CellValue.Number -> {
                    append("""<c r="$cellReference"><v>${value.value}</v></c>""")
                }

                is CellValue.Text -> {
                    append("""<c r="$cellReference" t="inlineStr"><is><t>""")
                    append(value.value.escapeXml())
                    append("""</t></is></c>""")
                }
            }
        }
        append("</row>")
    }

    private fun columnName(index: Int): String {
        var column = index
        val name = StringBuilder()
        do {
            name.insert(0, 'A' + column % 26)
            column = column / 26 - 1
        } while (column >= 0)
        return name.toString()
    }

    private fun String.escapeXml(): String = buildString {
        this@escapeXml.forEach { char ->
            when (char) {
                '&' -> append("&amp;")
                '<' -> append("&lt;")
                '>' -> append("&gt;")
                '"' -> append("&quot;")
                '\'' -> append("&apos;")
                else -> append(char)
            }
        }
    }

    private fun ZipOutputStream.putTextEntry(name: String, content: String) {
        putNextEntry(ZipEntry(name))
        write(content.toByteArray(Charsets.UTF_8))
        closeEntry()
    }

    private sealed interface CellValue {
        data class Text(val value: String) : CellValue
        data class Number(val value: Double) : CellValue
    }

    private val contentTypesXml = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
            <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
            <Default Extension="xml" ContentType="application/xml"/>
            <Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>
            <Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>
            <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
            <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
        </Types>
    """.trimIndent()

    private val rootRelationsXml = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
            <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
            <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>
            <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>
        </Relationships>
    """.trimIndent()

    private val workbookXml = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
            <sheets>
                <sheet name="${context.getString(R.string.menu_transactions).escapeXml()}" sheetId="1" r:id="rId1"/>
            </sheets>
        </workbook>
    """.trimIndent()

    private val workbookRelationsXml = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
            <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
        </Relationships>
    """.trimIndent()

    private val appPropertiesXml = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties">
            <Application>Moneyfikasi</Application>
        </Properties>
    """.trimIndent()

    private val corePropertiesXml = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" xmlns:dc="http://purl.org/dc/elements/1.1/">
            <dc:creator>Moneyfikasi</dc:creator>
        </cp:coreProperties>
    """.trimIndent()
}
