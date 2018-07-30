package com.foolchen.arch.app

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.foolchen.arch.presenter.FakePresenter


/**
 * 可侧滑返回的Activity,继承自[ArchActivity]
 *
 * @author wayne
 * 2018/7/16
 * 下午4:33
 */
abstract class SwipeBackNoPresenterActivity : SwipeBackArchActivity<FakePresenter>(), BGASwipeBackHelper.Delegate