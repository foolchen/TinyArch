package com.foolchen.arch.samples.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.foolchen.arch.samples.R
import org.jetbrains.anko.bundleOf

/**
 * @author chenchong
 * 2018/6/4
 * 下午5:25
 */
class FragmentContainerActivity : AppCompatActivity() {
  private val FRGAMENT_TAG = "fragment_tag"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fragment_container)

    val extras = intent.extras

    val fm = supportFragmentManager
    var f = fm.findFragmentById(R.id.fragment_container)
    if (f == null) {
      val clazzName: String = extras.getString("clazz_name")
      f = Fragment.instantiate(this, clazzName, extras.getBundle("extras"))
      val ft = fm.beginTransaction()
      ft.replace(R.id.fragment_container, f, FRGAMENT_TAG)
      ft.commit()
    }
  }
}

fun Fragment.startFragment(clazzName: String, args: Bundle? = null) {
  this.context?.apply {
    val bundle = bundleOf(
        "clazz_name" to clazzName,
        "extras" to args
    )
    val intent = Intent(this@apply, FragmentContainerActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }
}