package com.foolchen.arch.samples.samples.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foolchen.arch.app.NoPresenterFragment
import com.foolchen.arch.view.recyclerview.MultiTypesAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 多类型数据展示
 * @author chenchong
 * 2018/7/20
 * 上午11:46
 */
class MultiTypeSamplesFragment : NoPresenterFragment() {

  private val json1 = """[{"texts":["单列文本"],"type":1},{"texts":["双列文本","双列文本"],"type":2},{"texts":["三列文本","三列文本","三列文本"],"type":3},{"texts":["单列文本"],"type":1},{"texts":["双列文本","双列文本"],"type":2},{"texts":["三列文本","三列文本","三列文本"],"type":3},{"texts":["单列文本"],"type":1},{"texts":["双列文本","双列文本"],"type":2},{"texts":["三列文本","三列文本","三列文本"],"type":3}]"""
  private val json2 = """[{"urls":["单图"],"type":1},{"urls":["双图","双图"],"type":2},{"urls":["三图","三图","三图"],"type":3},{"urls":["单图"],"type":1},{"urls":["双图","双图"],"type":2},{"urls":["三图","三图","三图"],"type":3},{"urls":["单图"],"type":1},{"urls":["双图","双图"],"type":2},{"urls":["三图","三图","三图"],"type":3}]"""

  private lateinit var mRecyclerView: RecyclerView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    mRecyclerView = RecyclerView(inflater.context)
    mRecyclerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)
    mRecyclerView.layoutManager = LinearLayoutManager(inflater.context)
    return mRecyclerView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val data = ArrayList<Any>()

    val gson = Gson()
    data.addAll(
        gson.fromJson<List<Texts>>(json1, object : TypeToken<ArrayList<Texts>>() {}.type))
    data.addAll(
        gson.fromJson<List<Images>>(json2, object : TypeToken<ArrayList<Images>>() {}.type))
    val adapter = MultiTypesAdapterImpl(data)
    adapter.setConverter(MultiTypeConverter())
    adapter.registerMultiType(MultiTextAdapter())
    adapter.registerMultiType(MultiImagesAdapter())
    mRecyclerView.adapter = adapter
  }

  private class MultiTypesAdapterImpl(
      val data: List<Any>) : MultiTypesAdapter<RecyclerView.ViewHolder>() {


    override fun getItem(position: Int): Any {
      return data[position]
    }

    override fun getItemCount(): Int {
      return data.size
    }
  }
}