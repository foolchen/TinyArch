package com.foolchen.arch.samples.samples.ui

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.foolchen.arch.app.NoPresenterFragment
import com.foolchen.arch.samples.R
import com.foolchen.arch.samples.view.LoadMoreFooterView
import com.foolchen.arch.utils.dp2px
import com.foolchen.arch.view.recyclerview.IRecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import org.jetbrains.anko.matchParent

/**
 * 多状态布局展示
 * @author chenchong
 * 2018/7/24
 * 上午9:26
 */
class MultiStateViewFragment : NoPresenterFragment() {
  private lateinit var mRecyclerView: IRecyclerView
  private lateinit var mSwipeRefreshLayout: SmartRefreshLayout

  private var mHeaderViewNo = 0
  private var mFooterViewNo = 0

  private val mHeaderViews: ArrayList<View> by lazy {
    ArrayList<View>()
  }
  private val mFooterViews: ArrayList<View> by lazy {
    ArrayList<View>()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  @SuppressLint("StaticFieldLeak")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    mSwipeRefreshLayout = inflater.inflate(R.layout.fragment_multi_state_view, container,
        false) as SmartRefreshLayout
    mSwipeRefreshLayout.setEnableLoadMore(false)

    mRecyclerView = mSwipeRefreshLayout.findViewById(R.id.rv)
    mRecyclerView.layoutManager = LinearLayoutManager(context)

    val loadMoreView = mRecyclerView.loadMoreFooterView as LoadMoreFooterView

    mSwipeRefreshLayout.setOnRefreshListener {
      Log.d("MultiStateViewFragment", "触发下拉刷新")
      object : AsyncTask<Unit, Unit, Unit>() {

        override fun onPreExecute() {
          mRecyclerView.setLoading()
        }

        override fun doInBackground(vararg params: Unit?) {
          SystemClock.sleep(2000)
        }

        override fun onPostExecute(result: Unit?) {
          (mRecyclerView.iAdapter as MultiStateSampleAdapter).set(10)
          mRecyclerView.setNormal()
          mSwipeRefreshLayout.finishRefresh()
          loadMoreView.status = LoadMoreFooterView.Status.IDLE
        }
      }.execute()
    }

    mRecyclerView.setErrorViewListener {
      Toast.makeText(context, "点击重试...", Toast.LENGTH_SHORT).show()
    }

    mRecyclerView.setOnLoadMoreListener {

      object : AsyncTask<Unit, Unit, Unit>() {

        override fun onPreExecute() {
          loadMoreView.status = LoadMoreFooterView.Status.LOADING
        }

        override fun doInBackground(vararg params: Unit?) {
          SystemClock.sleep(2000)
        }

        override fun onPostExecute(result: Unit?) {
          (mRecyclerView.iAdapter as MultiStateSampleAdapter).append(10)
          if (mRecyclerView.iAdapter.itemCount >= 60) {
            loadMoreView.status = LoadMoreFooterView.Status.THE_END
          } else {
            loadMoreView.status = LoadMoreFooterView.Status.IDLE
          }
        }
      }.execute()
    }
    mRecyclerView.iAdapter = MultiStateSampleAdapter()
    return mSwipeRefreshLayout
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    menu.add(1, 2, 3, "ERROR").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(1, 1, 2, "LOADING").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(1, 3, 1, "NORMAL").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(1, 4, 4, "ADD HEADER").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(1, 5, 5, "REMOVE HEADER").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(1, 6, 6, "ADD FOOTER").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(1, 7, 7, "REMOVE FOOTER").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      1 -> {
        mRecyclerView.setLoading()
      }
      2 -> {
        mRecyclerView.setError()
      }
      3 -> {
        mRecyclerView.setNormal()
      }
      4 -> {
        val header = TextView(context)
        header.layoutParams = ViewGroup.LayoutParams(matchParent, 60.dp2px())
        header.gravity = Gravity.CENTER
        header.text = "HeaderView ${mHeaderViewNo++}"
        mHeaderViews.add(header)
        mRecyclerView.addHeaderView(header)
      }
      5 -> {
        val lastIndex = mHeaderViews.lastIndex
        if (lastIndex != -1) {
          val header = mHeaderViews.removeAt(lastIndex)
          mHeaderViewNo--
          mRecyclerView.removeHeaderView(header)
        }
      }
      6 -> {
        val footer = TextView(context)
        footer.layoutParams = ViewGroup.LayoutParams(matchParent, 60.dp2px())
        footer.gravity = Gravity.CENTER
        footer.text = "FooterView ${mFooterViewNo++}"
        mFooterViews.add(footer)
        mRecyclerView.addFooterView(footer)
      }
      7 -> {
        val lastIndex = mFooterViews.lastIndex
        if (lastIndex != -1) {
          val header = mFooterViews.removeAt(lastIndex)
          mFooterViewNo--
          mRecyclerView.removeFooterView(header)
        }
      }
      else -> {

      }
    }
    return true
  }

  private class MultiStateSampleAdapter(
      var count: Int = 20) : RecyclerView.Adapter<ViewHolder>() {
    fun append(count: Int) {
      val start = this.count
      this.count += count
      notifyItemRangeInserted(start, count)
    }

    fun set(count: Int) {
      this.count = count
      notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val itemView = TextView(parent.context)
      itemView.layoutParams = ViewGroup.LayoutParams(matchParent, 60.dp2px())
      return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      (holder.itemView as TextView).text = "Item $position"
    }
  }

  private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}