package com.foolchen.arch.samples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : BaseFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    btn_hidden.setOnClickListener {
      startFragment(HiddenChangedFragment::class.java.name)
    }

    btn_vp.setOnClickListener { startFragment(ViewPagerFragment::class.java.name) }
  }


}
