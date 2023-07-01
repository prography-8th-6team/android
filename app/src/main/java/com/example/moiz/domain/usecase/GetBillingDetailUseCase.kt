package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.BillingDetailDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetBillingDetailUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String): BillingDetailDto =
        repository.getBillingDetail(id.toString(), token)
}