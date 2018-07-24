package com.foolchen.arch.view.layout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foolchen.arch.R
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

/**
 * 实现了下拉刷新功能的布局
 *
 * @author chenchong
 * 2018/7/24
 * 下午2:24
 */

open class SwipeToRefreshLayout : ViewGroup {

  protected lateinit var mHeaderView: View
  protected lateinit var mContentView: View

  protected val mInflater by lazy { LayoutInflater.from(context) }

  private var isContentViewFound = false
  private var mHeaderViewHeight = 0

  private lateinit var mHeaderViewLayoutParams: ViewGroup.LayoutParams

  constructor(context: Context) : super(context) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init(context, attrs)
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr) {
    init(context, attrs)
  }

  private fun init(context: Context, attrs: AttributeSet? = null) {
    val ta = context.obtainStyledAttributes(attrs, R.styleable.SwipeToRefreshLayout)

    val headerViewLayoutId = ta.getResourceId(R.styleable.SwipeToRefreshLayout_strl_header_view, -1)
    val contentViewLayoutId = ta.getResourceId(R.styleable.SwipeToRefreshLayout_strl_content_view,
        -1)

    mHeaderView = if (headerViewLayoutId == -1) generateHeaderView(context) else mInflater.inflate(
        headerViewLayoutId, this, false)

    if (mHeaderView !is ISwipeToRefreshHeader) {
      throw IllegalArgumentException("HeaderView必须实现ISwipeToRefreshHeader接口")
    }

    if (contentViewLayoutId != -1) {
      mContentView = findViewById(contentViewLayoutId)
      isContentViewFound = true
    }

    ta.recycle()
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    if (!isContentViewFound) {
      // 未找到内容View
      // 则此处尝试取默认的内容View
      if (childCount < 1) {
        throw IllegalArgumentException(
            "请使用strl_content_view指定一个内容布局,或者将内容布局添加到SwipeToRefreshLayout中,以便于SwipeToRefreshLayout来识别内容布局")
      }

      // 取布局中的第一个View作为内容View
      mContentView = getChildAt(0)
      isContentViewFound = true
    }

    // 将HeaderView插入到布局的第一个位置
    mHeaderViewLayoutParams = mHeaderView.layoutParams ?: LayoutParams(matchParent, wrapContent)
    addView(mHeaderView, 0, mHeaderViewLayoutParams)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec)
    mHeaderViewHeight = mHeaderView.measuredHeight
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    // 在编辑模式下预览HeaderView
    // 在运行模式下,默认隐藏HeaderView
    if (isInEditMode) {
      mHeaderView.layout(0, 0, r, b)
      mContentView.layout(0, mHeaderViewHeight, r, b)
    } else {
      mHeaderViewLayoutParams.height = 0
      mContentView.layout(0, 0, r, b)
    }
  }

  /**
   * 生成一个默认的HeaderView,可以在子类中覆写来实现不同的HeaderView
   *
   * 如果已经使用strl_header_view定义了HeaderView,则该方法无效
   */
  protected fun generateHeaderView(context: Context): View {
    return DefaultHeaderView(context)
  }
}