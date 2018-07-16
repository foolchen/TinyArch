package com.foolchen.arch.app

import com.foolchen.arch.presenter.FakePresenter

/**
 * 不需要子类应用MVP的Activity
 *
 * @author wayne
 * 2018/7/16
 * 下午4:33
 */
abstract class NoPresenterActivity : ArchActivity<FakePresenter>()