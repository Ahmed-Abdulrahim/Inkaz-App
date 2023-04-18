package com.elkfrawy.engaz.domain.usecases.user

import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.repository.AuthRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class UpdateEmail @Inject constructor(val authRepo: AuthRepository) {
    suspend fun execute(id: Long, email: String):Result<Unit> = authRepo.updateEmail(id, email)
}