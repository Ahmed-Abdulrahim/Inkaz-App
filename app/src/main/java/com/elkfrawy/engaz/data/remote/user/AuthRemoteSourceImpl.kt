package com.elkfrawy.engaz.data.remote.user
import com.elkfrawy.engaz.domain.util.Result
import android.util.Log
import com.elkfrawy.engaz.data.remote.Api
import com.elkfrawy.engaz.data.util.ClientException
import com.elkfrawy.engaz.data.util.DataNotAvailableException
import com.elkfrawy.engaz.data.util.ServerException
import com.elkfrawy.engaz.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRemoteSourceImpl @Inject constructor(val api: Api) : AuthRemoteSource {


    override suspend fun login(number: Long, password: String):Result<Long> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.login(number, password)
            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it.id)
                } ?: Result.Failure(DataNotAvailableException())
            } else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun addUser(
        name: String,
        number: Long,
        nationalID: String,
        password: String,
        address: String,
        car_name: String,
        car_model: Int,
        car_number: Int,
        car_color: String,
        car_license: String,
    ): Result<Long> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.register(name, number, nationalID, password,
                address, car_name, car_model, car_number, car_color, car_license)

            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            }  else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun show(id: Long): Result<User> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.getUser(id)
            Log.d("Code from retrofit", "code: ${response.code()}")
            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            }  else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun updateEmail(id: Long, email: String): Result<Unit> =  withContext(Dispatchers.IO) {
        try {
            val response = api.updateEmail(id, email)
            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            } else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun updateMobile(id: Long, mobile: Long): Result<Unit> =  withContext(Dispatchers.IO) {
        try {
            val response = api.updateMobile(id, mobile)
            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            } else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun updatePassword(id: Long, password: String): Result<Unit> =  withContext(Dispatchers.IO) {
        try {
            val response = api.updatePassword(id, password)
            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            } else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun updateUserInfo(
        id: Long,
        name: String,
        address: String,
        nationalID: String
    ): Result<Unit> =  withContext(Dispatchers.IO) {
        try {
            val response = api.updateUserInfo(id, name, nationalID, address)
            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            } else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun updateLatLng(id: Long, lat: String, lng: String): Result<Unit> =  withContext(Dispatchers.IO) {
        try {
            val response = api.updateLatLng(id, lat, lng)
            if (response.code() in 200..298) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataNotAvailableException())
            } else if (response.code() in 400..499)
                Result.Failure(ClientException())
            else if (response.code() in 500..599)
                Result.Failure(ServerException())
            else
                Result.Failure(ServerException())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}