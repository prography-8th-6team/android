package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.PostBillingDto
import com.jerny.moiz.domain.repository.TravelRepository
import com.jerny.moiz.presentation.util.FileResult
import javax.inject.Inject

class PutBillingsUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(
        id: Int,
        token: String,
        data: PostBillingDto,
        imgList: List<FileResult>?
    ) =
        repository.putBillings(id, token, data, imgList)
}