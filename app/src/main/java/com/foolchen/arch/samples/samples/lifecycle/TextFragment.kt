package com.foolchen.arch.samples.samples.lifecycle

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.foolchen.arch.samples.base.NoPresenterLogFragment

/**
 * @author chenchong
 * 2018/6/4
 * 下午6:00
 */
class TextFragment : NoPresenterLogFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val textView = TextView(container!!.context)
    textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)
    textView.gravity = Gravity.CENTER
    textView.text = arguments?.getString("text")
    textView.setBackgroundColor(Color.RED)
    return textView
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
  }
}