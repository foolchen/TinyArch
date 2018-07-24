package com.foolchen.arch.samples.samples.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foolchen.arch.samples.R
import com.foolchen.arch.samples.base.NoPresenterLogFragment
import com.foolchen.arch.samples.samples.ui.MultiTypeSamplesFragment
import com.foolchen.arch.samples.samples.ui.MultiplePresentersSamplesFragment
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : NoPresenterLogFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    btn_hidden.setOnClickListener {
      startFragment(HiddenChangedFragment::class.java.name)
    }

    btn_vp.setOnClickListener {
      startFragment(
          ViewPagerFragment::class.java.name)
    }

    btn_presenters.setOnClickListener {
      startFragment(MultiplePresentersSamplesFragment::class.java.name)
    }
    btn_multi_items_samples.setOnClickListener {
      startFragment(MultiTypeSamplesFragment::class.java.name)
    }

    btn_multi_state_samples.setOnClickListener {
      startFragment(MultiStateViewFragment::class.java.name)
    }
  }


}
