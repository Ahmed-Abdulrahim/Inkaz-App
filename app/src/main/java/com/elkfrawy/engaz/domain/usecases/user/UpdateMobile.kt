package com.elkfrawy.engaz.domain.usecases.user

import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.repository.AuthRepository
import com.elkfrawy.engaz.domain.util.Result
import javax.inject.Inject

class UpdateMobile @Inject constructor(val authRepo: AuthRepository) {

    suspend fun execute(id: Long, mobile: Long):Result<Unit> = authRepo.updateMobile(id, mobile)
}