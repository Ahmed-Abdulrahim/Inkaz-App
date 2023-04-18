package com.elkfrawy.engaz.domain.usecases.user

import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.repository.AuthRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class Login @Inject constructor(val authRepo: AuthRepository) {

    suspend fun execute(number: Long, password: String): Result<Long> = authRepo.login(number, password)
}