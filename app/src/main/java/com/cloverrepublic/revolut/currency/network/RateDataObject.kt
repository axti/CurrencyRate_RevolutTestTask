package com.cloverrepublic.revolut.currency.network

import com.google.gson.annotations.SerializedName

/**
 * Created by axti on 05.05.2020.
 */
data class RateDataObject(
    @SerializedName("baseCurrency")
    val baseCurrency: String,
    @SerializedName("rates")
    val rates: HashMap<String, Double>
)