package com.foolchen.tinyfragmentmvp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_view_pager_item.*
import org.jetbrains.anko.bundleOf

/**
 * @author chenchong
 * 2018/6/5
 * 下午3:04
 */
class ViewPagerItemFragment : BaseFragment() {


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_view_pager_item, container, false)
  }

  override fun onLazyInit(savedInstanceState: Bundle?) {
    Log.i("Arch",
        "${arguments?.getString(
            "tab_name")}#onLazyInit(${if (savedInstanceState == null) "null" else "savedInstanceState"})")

    tv_item.text = arguments?.getString("tab_name")

    var f = childFragmentManager.findFragmentById(R.id.fragment_container)
    if (f == null) {
      f = NestedViewPagerItemFragment()
      f.arguments = bundleOf("tab_name" to tv_item.text)
      childFragmentManager.beginTransaction().replace(R.id.fragment_container, f).commit()
    }
  }


  override fun onSupportVisible() {
    Log.i("Arch", "${arguments?.getString(
        "tab_name")}#onSupportVisible()")
  }

  override fun onSupportInvisible() {
    Log.i("Arch", "${arguments?.getString(
        "tab_name")}#onSupportInvisible()")
  }
}