package com.foolchen.arch.samples.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * @author chenchong
 * 2018/6/5
 * 下午3:22
 */
class NestedViewPagerItemFragment : NoPresenterLogFragment() {


  @SuppressLint("SetTextI18n")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val textView = TextView(inflater.context)
    textView.apply {
      layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT)
      gravity = Gravity.CENTER
      text = "${arguments?.getString("tab_name") ?: ""}#NestedFragment"
    }
    return textView
  }

  override fun onLazyInit(savedInstanceState: Bundle?) {
    Log.i("Arch",
        "${arguments?.getString(
            "tab_name")}#Nested#onLazyInit(${if (savedInstanceState == null) "null" else "savedInstanceState"})")
  }

  override fun onSupportVisible() {
    Log.i("Arch", "${arguments?.getString(
        "tab_name")}#Nested#onSupportVisible()")
  }

  override fun onSupportInvisible() {
    Log.i("Arch", "${arguments?.getString(
        "tab_name")}#Nested#onSupportInvisible()")
  }
}