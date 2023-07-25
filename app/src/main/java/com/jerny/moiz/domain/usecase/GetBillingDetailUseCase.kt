package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.BillingDetailDto
import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetBillingDetailUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String): BillingDetailDto =
        repository.getBillingDetail(id.toString(), token)
}