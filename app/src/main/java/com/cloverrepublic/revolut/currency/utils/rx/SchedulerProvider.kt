package com.cloverrepublic.revolut.currency.utils.rx

import io.reactivex.Scheduler

/**
 * Created by axti on 05.05.2020.
 */
interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
    fun computation(): Scheduler
}