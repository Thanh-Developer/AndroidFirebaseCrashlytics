package com.android.firebasecrashlytics

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import java.sql.SQLException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var crashlyticsTest: FirebaseCrashlytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
        crashlyticsTest = FirebaseCrashlytics.getInstance()
        initAnalystics()
        initCrashlytics()
    }

    private fun initCrashlytics() {
        findViewById<Button>(R.id.btnCrash).setOnClickListener {

            try {
                throw RuntimeException("Test Crash have try catch") // Add crashlytics
            } catch (e: Exception) {
                addMigrateFailToCrashlytics(e)
            }

            addMigrateSuccessToCrashlytics()
            throw RuntimeException("Test Crash New") // Force a crash
        }

        findViewById<Button>(R.id.btnCollectIssue).setOnClickListener {
            try {
                throw Exception("Test Crash have try catch") // Add crashlytics
            } catch (e: Exception) {
                Log.d("---->", "Exception: ${e.message}")
                addMigrateSuccessToCrashlytics()
                Firebase.crashlytics.recordException(e)
            }
        }

        findViewById<Button>(R.id.btnSQLExcception1).setOnClickListener {
            try {
                throw SQLException("Test Crash SQL 1") // Add crashlytics
            } catch (e: Exception) {
                addCrash1ToCrashlytics(e)
            }
        }

        findViewById<Button>(R.id.btnSQLExcception2).setOnClickListener {
            try {
                throw SQLException("Test Crash SQL 2") // Add crashlytics
            } catch (e: Exception) {
                addCrash2ToCrashlytics(e)
            }
        }
    }

    private fun initAnalystics() {
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, "1")
            param(FirebaseAnalytics.Param.ITEM_NAME, "test")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
        }
    }

    private fun addCrash1ToCrashlytics(e: Exception) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        val keysAndValues = CustomKeysAndValues.Builder()
            .putString("Jobogic", "fail").build()
        crashlytics.setUserId("JohnTestUserId")
        crashlytics.setCustomKeys(keysAndValues)
        crashlytics.log("addMigrateFailToCrashlytics")
        crashlytics.recordException(e)
    }

    private fun addCrash2ToCrashlytics(e: Exception) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        val keysAndValues = CustomKeysAndValues.Builder()
            .putString("Extended", "fail").build()
        crashlytics.setUserId("JohnTestUserId")
        crashlytics.setCustomKeys(keysAndValues)
        crashlytics.log("addMigrateFailToCrashlytics")
        crashlytics.recordException(e)
    }

    private fun addMigrateSuccessToCrashlytics() {
        val crashlytics = FirebaseCrashlytics.getInstance()
        val keysAndValues = CustomKeysAndValues.Builder()
            .putString("table", "JobAsset")
            .putString("Database", "Joblogic")
            .putString("oldDatabaseVersion", "23")
            .putString("newDatabaseVersion", "24")
            .putString("migrateStatus", "Successful")
            .build()
        crashlytics.setUserId("JohnTestUserId")
        crashlytics.setCustomKeys(keysAndValues)
        crashlytics.log("addMigrateSuccessToCrashlytics")
    }

    private fun addMigrateFailToCrashlytics(exception: Exception) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        val keysAndValues = CustomKeysAndValues.Builder()
            .putString("table", "SiteAsset")
            .putString("Database", "Extended")
            .putString("oldDatabaseVersion", "15")
            .putString("newDatabaseVersion", "16")
            .putString("exception", exception.message ?: "message")
            .putString("Locale", Locale.getDefault().toString())
            .putString("TimeZone", Calendar.getInstance().toString())
            .putString("cause", exception.cause.toString())
            .putString("migrateStatus", "Fail")
            .build()
        crashlytics.setUserId("JohnTestUserId")
        crashlytics.setCustomKeys(keysAndValues)
        crashlytics.log("addMigrateFailToCrashlytics")
    }
}