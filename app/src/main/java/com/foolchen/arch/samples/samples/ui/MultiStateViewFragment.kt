package com.foolchen.arch.samples.samples.ui

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.foolchen.arch.app.NoPresenterFragment
import com.foolchen.arch.samples.R
import com.foolchen.arch.samples.view.LoadMoreFooterView
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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  @SuppressLint("StaticFieldLeak")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    mRecyclerView = inflater.inflate(R.layout.fragment_multi_state_view, container,
        false) as IRecyclerView
    mRecyclerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)
    mRecyclerView.layoutManager = LinearLayoutManager(context)

    mRecyclerView.setErrorViewListener {
      Toast.makeText(context, "点击重试...", Toast.LENGTH_SHORT).show()
    }

    val loadMoreView = mRecyclerView.loadMoreFooterView as LoadMoreFooterView
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
          loadMoreView.status = LoadMoreFooterView.Status.IDLE
        }
      }.execute()
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

  private class MultiStateSampleAdapter(var count: Int = 20) : RecyclerView.Adapter<ViewHolder>() {

    fun append(count: Int) {
      val start = this.count
      this.count += count
      notifyItemRangeInserted(start, count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val itemView = TextView(parent.context)
      itemView.layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
      return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      (holder.itemView as TextView).text = "Item $position"
    }
  }

  private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}