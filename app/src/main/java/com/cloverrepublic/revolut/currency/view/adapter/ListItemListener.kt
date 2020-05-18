package com.cloverrepublic.revolut.currency.view.adapter

/**
 * Created by axti on 06.05.2020.
 */
interface ListItemListener {

    /**
     * Handles value changes done by the user.
     *
     * @param amount
     */
    fun onAmountChanged(amount: Double)

    /**
     * Handles selection of currency
     *
     * @param currencyName
     */
    fun onSelectBaseCurrency(currencyName: String)
}