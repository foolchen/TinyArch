package com.foolchen.arch.samples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_hidden_changed.*
import org.jetbrains.anko.bundleOf

/**
 * @author chenchong
 * 2018/6/4
 * 下午5:44
 */
class HiddenChangedFragment : BaseFragment() {
  private lateinit var mFragment: TextFragment

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_hidden_changed, container, false)
  }

  // TODO: 2018/6/5 chenchong 嵌套的子Fragment（TextFragment）的状态没有正确的还原
  override fun onLazyInit(savedInstanceState: Bundle?) {
    super.onLazyInit(savedInstanceState)
    val cfm = childFragmentManager
    val f = cfm.findFragmentById(R.id.fragment_container_1)
    if (f == null) {
      mFragment = TextFragment()
      mFragment.arguments = bundleOf("text" to "测试文案")
      cfm.beginTransaction().replace(R.id.fragment_container_1, mFragment).commit()
    } else {
      mFragment = f as TextFragment
    }
    btn_change_visibility.setOnClickListener {
      val ft = fragmentManager!!.beginTransaction()
      if (mFragment.isHidden) {
        ft.show(mFragment)
      } else {
        ft.hide(mFragment)
      }
      ft.commit()

      if (mFragment.isSupportVisible())
        btn_change_visibility.setText(R.string.text_hide_fragment)
      else
        btn_change_visibility.setText(R.string.text_show_fragment)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    val saveFragmentInstanceState = childFragmentManager.saveFragmentInstanceState(mFragment)
    outState.putParcelable("fragment_state", saveFragmentInstanceState)
  }
}

