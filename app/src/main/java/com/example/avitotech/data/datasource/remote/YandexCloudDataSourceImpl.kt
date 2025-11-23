package com.example.avitotech.data.datasource.remote

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.avitotech.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.Date
import javax.inject.Inject

class YandexCloudDataSourceImpl @Inject constructor(
    private val credentialsProvider: YandexCredentialsProvider
) : YandexCloudDataSource {

    private val s3: AmazonS3Client by lazy {
        val credentials = BasicAWSCredentials(
            credentialsProvider.getCredentials().awsAccessKeyId,
            credentialsProvider.getCredentials().awsSecretKey
        )
        AmazonS3Client(credentials).apply {
            endpoint = "https://storage.yandexcloud.net"
        }
    }

    override suspend fun uploadFile(
        inputStream: InputStream,
        remotePath: String,
        contentType: String,
        contentLength: Long
    ): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val metadata = ObjectMetadata().apply {
                    setContentLength(contentLength)
                    setContentType(contentType)
                }

                val putRequest = PutObjectRequest(
                    credentialsProvider.getBucketName(),
                    remotePath,
                    inputStream,
                    metadata
                )

                s3.putObject(putRequest)
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error("Upload failed: ${e.message}")
            }
        }
    }

    override suspend fun getPresignedUrl(remotePath: String): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = GeneratePresignedUrlRequest(
                    credentialsProvider.getBucketName(),
                    remotePath
                ).apply {
                    method = com.amazonaws.HttpMethod.GET
                    expiration = Date(System.currentTimeMillis() + 3600000) // 1 hour
                }

                val url = s3.generatePresignedUrl(request).toString()
                Resource.Success(url)
            } catch (e: Exception) {
                Resource.Error("URL generation failed: ${e.message}")
            }
        }
    }

    override suspend fun downloadFile(remotePath: String): Resource<InputStream> {
        return withContext(Dispatchers.IO) {
            try {
                val request = GetObjectRequest(
                    credentialsProvider.getBucketName(),
                    remotePath
                )
                val s3Object = s3.getObject(request)
                Resource.Success(s3Object.objectContent)
            } catch (e: Exception) {
                Resource.Error("Download failed: ${e.message}")
            }
        }
    }
}