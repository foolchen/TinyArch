package com.foolchen.arch.app

import nucleus5.presenter.Presenter
import nucleus5.view.NucleusAppCompatActivity

/**
 * 所有业务Activity的基类,可以应用MVP模式
 *
 * @author wayne
 * 2018/7/16
 * 下午4:33
 */
abstract class ArchActivity<P : Presenter<*>> : NucleusAppCompatActivity<P>()