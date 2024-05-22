package donets.danylo.android.utasapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes

import donets.danylo.android.utasapp.utilits.showToast
import java.io.InputStreamReader


class sqlDbFuns(context: Context)  :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val SCOPES = listOf(SheetsScopes.SPREADSHEETS)
    private lateinit var sheetsService: Sheets

        override fun onCreate(db: SQLiteDatabase) {
            val query = "CREATE TABLE $TABLE_NAME " +
                    "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_MARSCHAL_NAME TEXT, " +
                    "$COLUMN_MARSCHAL_PHONE TEXT, " +
                    "$COLUMN_MARSCHAL_RATING TEXT);"
            db.execSQL(query)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        fun addMarschal(name: Any, phone: Any, rating: Any) {
            val db: SQLiteDatabase = this.writableDatabase
            val cv = ContentValues()
            cv.put(COLUMN_MARSCHAL_NAME, name.toString())
            cv.put(COLUMN_MARSCHAL_PHONE, phone.toString())
            cv.put(COLUMN_MARSCHAL_RATING, rating.toString()
            )
            val result = db.insert(TABLE_NAME, null, cv)
            if (result == -1L) {
                showToast("Помилка")
            } else {
                showToast("Успішно додано")
            }
        }

    fun readAllData(): Cursor? {
        val query = "SELECT * FROM $TABLE_NAME"
        val db: SQLiteDatabase = this.readableDatabase
        return db.rawQuery(query, null)
    }

    fun searchData(text: String): Cursor? {
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_MARSCHAL_NAME LIKE '%$text%'"
        val db: SQLiteDatabase = this.readableDatabase
        return db.rawQuery(query, null)
    }


    fun updateData(row_id: Int, name: Any, phone: Any, rating: Any) {
        val db: SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_MARSCHAL_NAME, name.toString())
        cv.put(COLUMN_MARSCHAL_PHONE, phone.toString())
        cv.put(COLUMN_MARSCHAL_RATING, rating.toString())
        val result = db.update(TABLE_NAME, cv, "$COLUMN_ID=?", arrayOf(row_id.toString()))
        if (result == -1) {
            showToast("Error updating")
        } else {
            showToast("Updated successfully")
        }
    }

    fun deleteOneRow(row_id: Int) {
        val db: SQLiteDatabase = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(row_id.toString()))
        if (result == -1) {
            showToast("Error deleting")
        } else {
            showToast("Deleted")
        }
    }


        fun deleteAllData() {
            val db: SQLiteDatabase = this.writableDatabase
            db.execSQL("DELETE FROM $TABLE_NAME")
        }
 /*   fun getSheetsService(): Sheets {
        if (!::sheetsService.isInitialized) {
            val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
            val jsonFactory = JacksonFactory.getDefaultInstance()
            val inputStream = sqlDbFuns::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
            val clientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(inputStream))
            val flow = GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, SCOPES
            ).build()
            val receiver = LocalServerReceiver.Builder().setPort(8888).build()
            val credential = AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
            sheetsService = Sheets.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
        }
        return sheetsService
    }*/




   // @SuppressLint("Range")
   /* fun syncWithSheets() {
        val dbData = readAllData()
        val sheetData = getSheetsService().spreadsheets().values()
            .get(SPREADSHEET_ID, RANGE)
            .execute()
            .getValues()

        // Assuming sheetData is a list of lists where each inner list represents a row
        // and the first element of each row is the ID.
        val sheetDataMap = sheetData.associateBy { it.first() } // Create a map for faster lookup

        dbData?.use { cursor ->
            while (cursor.moveToNext()) {
                val dbId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val dbName = cursor.getString(cursor.getColumnIndex(COLUMN_MARSCHAL_NAME))
                val dbPhone = cursor.getString(cursor.getColumnIndex(COLUMN_MARSCHAL_PHONE))
                val dbRating = cursor.getString(cursor.getColumnIndex(COLUMN_MARSCHAL_RATING))

                // Find the corresponding row in the sheet data
                val sheetRow = sheetDataMap[dbId.toString()]
                if (sheetRow != null) {
                    // If the data in the database and the sheet do not match, update the database
                    if (sheetRow[1] != dbName || sheetRow[2] != dbPhone || sheetRow[3] != dbRating) {
                        updateData(dbId, sheetRow[1], sheetRow[2], sheetRow[3])
                    }
                } else {
                    // If the row does not exist in the sheet, delete it from the database
                    deleteOneRow(dbId)
                }
            }
        }

        // Now check for any rows in the sheet that do not exist in the database and add them
        val dbIds = mutableListOf<String>()
        dbData?.use { cursor ->
            while (cursor.moveToNext()) {
                dbIds.add(cursor.getString(cursor.getColumnIndex(COLUMN_ID)))
            }
        }

        sheetData.forEach { sheetRow ->
            val sheetId = sheetRow.first()
            if (sheetId !in dbIds) {
                // Add the row from the sheet to the database
                addMarschal(sheetRow[1], sheetRow[2], sheetRow[3])
            }
        }
    }*/





    }