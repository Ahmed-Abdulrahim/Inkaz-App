package com.elkfrawy.engaz.domain.usecases.user
import com.elkfrawy.engaz.domain.model.User
import  com.elkfrawy.engaz.domain.util.Result
import com.elkfrawy.engaz.domain.repository.AuthRepository
import javax.inject.Inject

class GetUser @Inject constructor(val authRepo: AuthRepository) {

    suspend fun execute(id: Long):Result<User> = authRepo.show(id)

}