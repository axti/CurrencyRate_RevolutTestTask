package com.cloverrepublic.revolut.currency.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloverrepublic.revolut.currency.R
import com.cloverrepublic.revolut.currency.utils.rx.SchedulerProvider
import com.cloverrepublic.revolut.currency.view.adapter.ListDiffUtilCallback
import com.cloverrepublic.revolut.currency.view.adapter.ListItemListener
import com.cloverrepublic.revolut.currency.view.adapter.RatesAdapter
import com.cloverrepublic.revolut.currency.view.adapter.RxDiffUtil
import com.cloverrepublic.revolut.currency.viewmodel.MainViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(R.layout.activity_main), ListItemListener {
    private val disposables = CompositeDisposable()
    private val viewModel: MainViewModel by viewModel()
    private val listItemsAdapter = RatesAdapter(this)
    private lateinit var itemsLayoutManager: LinearLayoutManager
    private var needScroll = false
    private val schedulerProvider: SchedulerProvider by inject()
    private val transformer = RxDiffUtil.calculateDiff(ListDiffUtilCallback.Companion::create)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        disposables.add(
            viewModel.getLatestCurrencyList()
                .observeOn(schedulerProvider.io())
                .compose(transformer)
                .observeOn(schedulerProvider.ui())
                .doAfterNext {
                    if (needScroll) {
                        recyclerView.scrollToPosition(0)
                        needScroll = false
                    }
                }
                .subscribe(listItemsAdapter)
        )
        disposables.add(viewModel.shouldScrollToTop()
            .observeOn(schedulerProvider.ui())
            .subscribe { needScroll = true }
        )
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    override fun onSelectBaseCurrency(currencyName: String) {
        viewModel.setBaseCurrency(currencyName)
    }

    override fun onAmountChanged(amount: Double) {
        Log.d("activity", "onAmountChanged: $amount")
        viewModel.setBaseMultiplier(amount)
    }

    private fun setupRecyclerView() {
        itemsLayoutManager = LinearLayoutManager(this)
        with(recyclerView) {
            setHasFixedSize(true)
            adapter = listItemsAdapter
            layoutManager = itemsLayoutManager
        }
    }
}
