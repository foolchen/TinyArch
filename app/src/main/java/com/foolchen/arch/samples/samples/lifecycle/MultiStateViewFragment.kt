package com.foolchen.arch.samples.samples.lifecycle

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.foolchen.arch.app.NoPresenterFragment
import com.foolchen.arch.samples.R
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

  private lateinit var mLoadingView: ILoadingView
  private lateinit var mErrorView: IErrorView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    mRecyclerView = inflater.inflate(R.layout.fragment_multi_state_view, container,
        false) as IRecyclerView
    mRecyclerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)
    mRecyclerView.layoutManager = LinearLayoutManager(context)

    /*mLoadingView = LoadingView(inflater.context)
    val layoutParams = FrameLayout.LayoutParams(50.dp2px(), 50.dp2px())
    layoutParams.gravity = Gravity.CENTER
    mLoadingView.layoutParams = layoutParams
    mRecyclerView.setLoadingView(mLoadingView)

    mErrorView = ErrorView(inflater.context)
    val layoutParams1 = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        240.dp2px())
    layoutParams1.gravity = Gravity.CENTER
    mErrorView.layoutParams = layoutParams1
    mErrorView.setBackgroundColor(Color.YELLOW)
    mErrorView.text = "Error"
    mErrorView.gravity = Gravity.CENTER
    mRecyclerView.setErrorView(mErrorView)
    mRecyclerView.setErrorViewListener {
      mRecyclerView.setLoading()
    }*/
    mRecyclerView.setErrorViewListener {
      Toast.makeText(context, "点击重试...", Toast.LENGTH_SHORT).show()
    }

    mRecyclerView.iAdapter = MultiStateSampleAdapter()
    return mRecyclerView
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    menu.add(1, 2, 3, "ERROR").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(1, 1, 2, "LOADING").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(1, 3, 1, "NORMAL").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
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

  /*private class ErrorView(context: Context) : TextView(context), IErrorView

  private class LoadingView(context: Context) : ProgressBar(context), ILoadingView {
    override fun start() {
      Log.d("LoadingView", "LoadingView:onStart()")
    }

    override fun stop() {
      Log.d("LoadingView", "LoadingView:stop()")
    }
  }*/

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