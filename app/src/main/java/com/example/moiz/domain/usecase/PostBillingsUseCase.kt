package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.TravelDetailDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostBillingsUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String, data: PostBillingDto) =
        repository.postBillings(id, token, data)
}