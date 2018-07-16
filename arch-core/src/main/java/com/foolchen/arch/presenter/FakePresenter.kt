package com.foolchen.arch.presenter

import nucleus5.presenter.RxPresenter
import nucleus5.view.ViewWithPresenter

sealed class FakePresenter : RxPresenter<FakeView>()

sealed class FakeView : ViewWithPresenter<FakePresenter>