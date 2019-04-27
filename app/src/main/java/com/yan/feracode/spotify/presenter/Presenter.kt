package com.yan.feracode.spotify.presenter

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal abstract class Presenter<T : Presenter.View> {

    private val compositeDisposable = CompositeDisposable()

    var view: T? = null

    fun initialize() {

    }

    open fun terminate() {
        dispose()
    }

    fun addDisposableObserver(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun dispose() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    interface View {

        fun context(): Context
    }
}