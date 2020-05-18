package com.cloverrepublic.revolut.currency.utils.ext

import java.util.*

/**
 * Created by axti on 06.05.2020.
 */
fun Double.format(digits: Int) = "%.${digits}f".format(Locale.US,this)