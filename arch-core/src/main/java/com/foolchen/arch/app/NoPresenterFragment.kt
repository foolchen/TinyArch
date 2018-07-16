package com.foolchen.arch.app

import com.foolchen.arch.presenter.FakePresenter

/**
 * 不需要子类应用MVP的Fragment
 * @author wayne
 * 2018/7/16
 * 下午4:39
 */
abstract class NoPresenterFragment : ArchFragment<FakePresenter>()