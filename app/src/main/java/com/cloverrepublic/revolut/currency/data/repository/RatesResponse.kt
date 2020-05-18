package com.cloverrepublic.revolut.currency.data.repository

import com.cloverrepublic.revolut.currency.network.RateDataObject

/**
 * Created by axti on 06.05.2020.
 */
sealed class RatesResponse {
    data class SuccessResponse(val dataObject: RateDataObject): RatesResponse()
    data class FailedResponse(val errorMessage: String?): RatesResponse()
}