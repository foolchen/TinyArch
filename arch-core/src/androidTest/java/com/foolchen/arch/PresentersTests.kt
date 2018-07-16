package com.foolchen.arch

import android.os.Bundle
import android.support.test.runner.AndroidJUnit4
import com.foolchen.arch.presenter.BasePresenter
import com.foolchen.arch.presenter.PresentersFactory
import nucleus5.factory.PresenterFactory
import nucleus5.presenter.Presenter
import nucleus5.view.ViewWithPresenter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 针对多个Presenter的测试
 * @author chenchong
 * 2018/7/16
 * 下午3:28
 */
@RunWith(AndroidJUnit4::class)
class PresentersTests {
  lateinit var presenter: MockPresenter
  lateinit var view: MockView
  @Before
  fun setup() {
    presenter = MockPresenter()
    Assert.assertNotNull(presenter)
    presenter.create(null)
    view = MockView()
  }

  @Test
  fun testPresenters() {
    val presenter1 = presenter.getPresenter<MockNestedPresenter>(1)
    val presenter2 = presenter.getPresenter<MockNestedPresenter>(2)
    Assert.assertNotNull(presenter1)
    Assert.assertNotNull(presenter2)

    Assert.assertEquals(presenter1.value, "Init value = 1")
    Assert.assertEquals(presenter2.value, "Init value = 2")
  }

  @Test
  fun testViews() {
    val presenter1 = presenter.getPresenter<MockNestedPresenter>(1)
    val presenter2 = presenter.getPresenter<MockNestedPresenter>(2)
    Assert.assertNull(presenter.view)
    Assert.assertNull(presenter1.view)
    Assert.assertNull(presenter2.view)

    presenter.takeView(view)
    Assert.assertNotNull(presenter.view)
    Assert.assertNotNull(presenter1.view)
    Assert.assertNotNull(presenter2.view)

    presenter1.print()
    presenter2.print()
  }

  @Test
  fun testRestore() {
    // 此时尚未存在presenter1,presenter2
    var bundle = Bundle()
    presenter.save(bundle)
    presenter.create(bundle)
    val presenter1 = presenter.getPresenter<MockNestedPresenter>(1)
    val presenter2 = presenter.getPresenter<MockNestedPresenter>(2)

    Assert.assertEquals(presenter1.value, "Init value = 1")
    Assert.assertEquals(presenter2.value, "Init value = 2")

    bundle = Bundle()
    presenter.save(bundle)
    presenter.create(bundle)

    Assert.assertEquals(presenter1.value, "Saved value = (Init value = 1)")
    Assert.assertEquals(presenter2.value, "Saved value = (Init value = 2)")
    presenter.takeView(view)
    presenter1.print()
    presenter2.print()
  }
}

class MockPresenter : BasePresenter<MockView>() {

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)
    setPresentersFactory(object : PresentersFactory<MockView> {
      override fun createPresenter(o: Any): Presenter<MockView> {
        val p: MockNestedPresenter
        when (o as Int) {
          1 -> {
            p = MockNestedPresenter()
            p.value = "Init value = 1"
          }
          2 -> {
            p = MockNestedPresenter()
            p.value = "Init value = 2"
          }
          else -> {
            throw RuntimeException("Unsupported type.")
          }
        }
        return p
      }
    })
  }

}

class MockView : ViewWithPresenter<MockPresenter> {
  private lateinit var factory: PresenterFactory<MockPresenter>

  override fun getPresenter(): MockPresenter {
    return factory.createPresenter()
  }

  override fun getPresenterFactory(): PresenterFactory<MockPresenter> {
    return factory
  }

  override fun setPresenterFactory(presenterFactory: PresenterFactory<MockPresenter>) {
    this.factory = presenterFactory
  }

  fun print(msg: String) {
    println(msg)
  }
}

class MockNestedPresenter : BasePresenter<MockView>() {
  var value: String? = null

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)
    savedState?.let {
      value = it.getString("value")
    }
  }

  override fun onSave(state: Bundle) {
    super.onSave(state)
    state.putString("value", "Saved value = ($value)")
  }

  fun print() {
    view!!.print(value ?: "Value is null.")
  }
}