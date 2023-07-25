package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.BillingHelperDto
import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetBillingsHelperUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        id: Int,
        token: String,
    ): BillingHelperDto? = repository.getBillingsHelper(id, token).results
}