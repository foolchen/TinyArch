package com.foolchen.arch.samples.samples.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import com.foolchen.arch.app.ArchFragment
import com.foolchen.arch.samples.R
import com.foolchen.arch.samples.bean.Photo
import com.foolchen.arch.samples.samples.contracts.MultiplePresentersContract
import com.foolchen.arch.samples.samples.presenter.MultiplePresentersPresenter
import com.foolchen.arch.samples.samples.ui.adapter.MultiplePresentersAdapter
import com.foolchen.arch.samples.view.LoadMoreFooterView
import com.foolchen.arch.utils.GONE
import com.foolchen.arch.utils.VISIBLE
import com.foolchen.arch.view.recyclerview.IItemChildClickListener
import com.foolchen.arch.view.recyclerview.IRecyclerView
import kotlinx.android.synthetic.main.fragment_multiple_presenters_samples_fragment.*
import nucleus5.factory.PresenterFactory

/**
 * 演示多个Presenter的Fragment
 * @author chenchong
 * 2018/7/16
 * 下午5:03
 */
// 在手动设置了PresenterFactory的情况下,不需要该注解
// @RequiresPresenter(MultiplePresentersPresenter::class)
class MultiplePresentersSamplesFragment : ArchFragment<MultiplePresentersPresenter>(), MultiplePresentersContract, IItemChildClickListener {

  private var mAdapter: MultiplePresentersAdapter? = null
  private lateinit var mRecyclerView: IRecyclerView
  private lateinit var mProgressBar: ProgressBar
  private lateinit var mLoadMoreView: LoadMoreFooterView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    // 为了防止使用反射来创建presenter,此处手动设置一个工厂
    presenterFactory = PresenterFactory { MultiplePresentersPresenter() }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_multiple_presenters_samples_fragment, container,
        false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    mRecyclerView = rv
    mProgressBar = progress_bar

    mRecyclerView.layoutManager = LinearLayoutManager(context)
    mRecyclerView.GONE()

    mRecyclerView.setOnLoadMoreListener {
      if (mLoadMoreView.canLoadMore()) {
        mLoadMoreView.status = LoadMoreFooterView.Status.LOADING
        presenter.getMorePhotos()
      }
    }
    mLoadMoreView = mRecyclerView.loadMoreFooterView as LoadMoreFooterView

    presenter.getPhotos()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    menu.add(1, 1, 1, "网络")
    menu.add(1, 2, 2, "缓存")
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      1 -> {
        presenter.isForceCache = false
        presenter.getPhotos()
      }
      else -> {
        presenter.isForceCache = true
        presenter.getPhotos()
      }
    }
    return true
  }

  override fun onRefreshSuccess(photos: List<Photo>) {
    mRecyclerView.VISIBLE()
    mProgressBar.GONE()
    mAdapter = MultiplePresentersAdapter(photos)
    mAdapter!!.setOnItemChildClickListener(this)
    mRecyclerView.iAdapter = mAdapter
  }

  override fun onRefreshError(message: String) {
    Toast.makeText(context, "加载错误", Toast.LENGTH_LONG).show()
    mProgressBar.GONE()
  }

  override fun onLoadMoreSuccess(photos: List<Photo>) {
    mAdapter?.append(photos)
    mLoadMoreView.status = LoadMoreFooterView.Status.IDLE
  }

  override fun onLoadMoreError(message: String) {
    mLoadMoreView.status = LoadMoreFooterView.Status.ERROR
  }

  override fun onLoadMoreEnd(isEnd: Boolean, message: String) {
    if (!isEnd) {
      mRecyclerView.setLoadMoreEnabled(true)
    }
    if (isEnd) {
      mLoadMoreView.status = LoadMoreFooterView.Status.THE_END
    } else {
      mLoadMoreView.status = LoadMoreFooterView.Status.IDLE
    }
  }

  override fun onClick(view: View, data: Any, position: Int) {
    Toast.makeText(context, data.toString(), Toast.LENGTH_LONG).show()
  }
}