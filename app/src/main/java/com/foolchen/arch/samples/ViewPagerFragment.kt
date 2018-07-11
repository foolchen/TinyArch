package com.foolchen.arch.samples

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_view_pager.*
import org.jetbrains.anko.bundleOf

/**
 * 包含有TabLayout+ViewPager+Fragment实现的多标签页面
 * @author chenchong
 * 2018/6/5
 * 下午2:57
 */
class ViewPagerFragment : BaseFragment() {

  private lateinit var mItems: ArrayList<String>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mItems = ArrayList()
    for (i in 0..10) {
      mItems.add("Tab ${i + 1}")
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_view_pager, container, false)
  }

  override fun onLazyInit(savedInstanceState: Bundle?) {
    super.onLazyInit(savedInstanceState)
    view_pager.adapter = ViewPagerAdapter(childFragmentManager)
    tab_layout.setupWithViewPager(view_pager, true)
  }

  private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
      val f = ViewPagerItemFragment()
      f.arguments = bundleOf("tab_name" to getPageTitle(position))
      return f
    }

    override fun getCount(): Int {
      return mItems.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
      return mItems[position]
    }
  }
}