package com.foolchen.arch.view.layout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
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
  private val DEBUG = true
  private val TAG = "SwipeToRefreshLayout"

  private val HEADER_MODE_TRANLSATE = 1
  private val HEADER_MODE_SCALE = 2

  protected lateinit var mHeaderView: View
  protected lateinit var mContentView: View

  protected val mInflater by lazy { LayoutInflater.from(context) }

  private var isContentViewFound = false
  private var isHeaderViewHeightAssigned = false
  private var mHeaderViewHeight = 0

  private lateinit var mHeaderViewLayoutParams: ViewGroup.LayoutParams

  private var mMotionX = 0F
  private var mMotionY = 0F
  private val mScaleTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
  private var mDragging = false

  private var mContentViewImmediatePosition = 0 // ContentView在拖动过程中,顶部的位置
  private var mHeaderViewImmediateHeight = 0 // HeaderView在拖动过程中的高度
  private var mHeaderViewImmediatePosition = 0 // HeaderView在拖动过程中,顶部的位置

  private var mHeaderMode = HEADER_MODE_TRANLSATE

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
      mHeaderView.layout(0, 0, r, mHeaderViewHeight)
      mContentView.layout(0, mHeaderViewHeight, r, b)
    } else {
      mHeaderView.layout(0, mHeaderViewImmediatePosition, r,
          mHeaderViewImmediatePosition + mHeaderViewHeight)
      mHeaderViewLayoutParams.height = mHeaderViewImmediateHeight
      mHeaderView.layoutParams = mHeaderViewLayoutParams
      mContentView.layout(0, mContentViewImmediatePosition, r, b)

      /*val width = r - l // 自身宽度
      val height = b - t // 自身高度
*/

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

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    var shouldIntercept = super.dispatchTouchEvent(ev)
    val action = ev.action and MotionEvent.ACTION_MASK
    when (action) {
      MotionEvent.ACTION_DOWN -> {
        mMotionX = ev.x
        mMotionY = ev.y
      }
      MotionEvent.ACTION_MOVE -> {
        val dx = ev.x - mMotionX
        val dy = ev.y - mMotionY
        mMotionX = ev.x
        mMotionY = ev.y
        if (!mDragging) {
          if (Math.abs(dy) > mScaleTouchSlop && Math.abs(dy) > Math.abs(dx)) {
            // 允许y轴方向拖动
            shouldIntercept = true
            mDragging = true
          }
        } else {
          if (dy > 0) {
            // 此时为向下拖动
            handleDraggingDown(dx, dy, ev)
          } else if (dy < 0) {
            // 此时为向上拖动
            handleDraggingUp(dx, dy, ev)
          }
          if (DEBUG) {
            Log.d(TAG,
                "Header immediate height = $mHeaderViewImmediateHeight , mContentView position = $mContentViewImmediatePosition")
          }
        }

      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
      }
    }

    return shouldIntercept
  }

  private fun handleDraggingDown(dx: Float, dy: Float, ev: MotionEvent) {
    if (!mContentView.canScrollVertically(-1)) {
      mHeaderViewImmediateHeight += dy.toInt()
      mContentViewImmediatePosition += dy.toInt()
    }
  }

  private fun handleDraggingUp(dx: Float, dy: Float, ev: MotionEvent) {
    mHeaderViewImmediateHeight += dy.toInt()
    mContentViewImmediatePosition += dy.toInt()
    if (mHeaderViewImmediateHeight < 0) mHeaderViewImmediateHeight = 0
    if (mContentViewImmediatePosition < 0) mContentViewImmediatePosition = 0
  }

  private fun canDragDown(): Boolean {
    return mContentView.canScrollVertically(1)
  }

  private fun canDragUp(): Boolean {
    return mContentView.canScrollVertically(-1)
  }
}