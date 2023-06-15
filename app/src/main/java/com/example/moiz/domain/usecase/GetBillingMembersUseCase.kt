package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.BillingMembersDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetBillingMembersUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String): List<BillingMembersDto> =
        repository.getBillingMembers(id, token)
}