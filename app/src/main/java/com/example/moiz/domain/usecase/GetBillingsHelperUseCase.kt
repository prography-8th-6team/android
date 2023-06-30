package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.BillingHelperDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetBillingsHelperUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        id: Int,
        token: String,
    ): BillingHelperDto? = repository.getBillingsHelper(id, token).results
}