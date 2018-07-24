package com.foolchen.arch.samples.samples.lifecycle

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.foolchen.arch.app.NoPresenterFragment
import com.foolchen.arch.utils.dp2px
import com.foolchen.arch.view.recyclerview.IErrorView
import com.foolchen.arch.view.recyclerview.ILoadingView
import com.foolchen.arch.view.recyclerview.IRecyclerView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

/**
 * 多状态布局展示
 * @author chenchong
 * 2018/7/24
 * 上午9:26
 */
class MultiStateViewFragment : NoPresenterFragment() {
  private lateinit var mRecyclerView: IRecyclerView

  private lateinit var mProgressBar: LoadingView
  private lateinit var mErrorView: ErrorView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    mRecyclerView = IRecyclerView(context)
    mRecyclerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)
    mRecyclerView.layoutManager = LinearLayoutManager(context)

    mProgressBar = LoadingView(inflater.context)
    val layoutParams = FrameLayout.LayoutParams(50.dp2px(), 50.dp2px())
    layoutParams.gravity = Gravity.CENTER
    mProgressBar.layoutParams = layoutParams
    mRecyclerView.setLoadingView(mProgressBar)

    mErrorView = ErrorView(inflater.context)
    val layoutParams1 = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        240.dp2px())
    layoutParams1.gravity = Gravity.CENTER
    mErrorView.layoutParams = layoutParams1
    mErrorView.setBackgroundColor(Color.YELLOW)
    mErrorView.text = "Error"
    mErrorView.gravity = Gravity.CENTER
    mRecyclerView.setErrorView(mErrorView)
    mErrorView.setOnClickListener { println(it) }

    mRecyclerView.iAdapter = MultiStateSampleAdapter()
    return mRecyclerView
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    menu.add(1, 2, 2, "ERROR")
    menu.add(1, 1, 1, "LOADING")
    menu.add(1, 3, 1, "NORMAL")
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      1 -> {
        mRecyclerView.setLoading()
      }
      2 -> {
        mRecyclerView.setError()
      }
      else -> {
        mRecyclerView.setNormal()
      }
    }
    return true
  }

  private class ErrorView(context: Context) : TextView(context), IErrorView {
  }

  private class LoadingView(context: Context) : ProgressBar(context), ILoadingView {
    override fun start() {
    }

    override fun stop() {
    }
  }

  private class MultiStateSampleAdapter : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val itemView = TextView(parent.context)
      itemView.layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
      return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      (holder.itemView as TextView).text = "Item $position"
    }
  }

  private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}