package com.example.avitotech.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.avitotech.domain.repository.SecureStorage
import javax.inject.Inject
import com.example.avitotech.BuildConfig

class SecureStorageImpl @Inject constructor(
    private val context: Context
) : SecureStorage {

    private val sharedPreferences: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun getAccessKey(): String = BuildConfig.YANDEX_ACCESS_KEY
    override fun getSecretKey(): String = BuildConfig.YANDEX_SECRET_KEY

    override fun saveAccessKey(accessKey: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_KEY, accessKey).apply()
    }

    override fun saveSecretKey(secretKey: String) {
        sharedPreferences.edit().putString(KEY_SECRET_KEY, secretKey).apply()
    }

    companion object {
        private const val KEY_ACCESS_KEY = "aws_access_key"
        private const val KEY_SECRET_KEY = "aws_secret_key"
    }
}