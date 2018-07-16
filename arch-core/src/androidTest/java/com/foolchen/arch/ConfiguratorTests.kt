package com.foolchen.arch

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.foolchen.arch.config.ConfigNotInitException
import com.foolchen.arch.config.sApplicationContext
import com.foolchen.arch.config.sInit
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author chenchong
 * 2018/7/12
 * 上午9:51
 */
@RunWith(AndroidJUnit4::class)
class ConfiguratorTests {
  /*@Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getTargetContext()

    assertEquals("com.foolchen.arch.test", appContext.packageName)
  }*/

  @Test
  fun testInit() {
    val appContext = InstrumentationRegistry.getTargetContext()
    val configurator = sInit(appContext)
    var ex: Exception? = null
    var ctx: Context? = null
    try {
      ctx = sApplicationContext()
    } catch (e: Exception) {
      ex = e
    }

    Assert.assertNull(ctx)
    Assert.assertNotNull(ex)
    Assert.assertTrue(ex is ConfigNotInitException)

    ctx = null
    ex = null
    configurator.configure()
    try {
      ctx = sApplicationContext()
    } catch (e: Exception) {
      ex = e
    }
    Assert.assertNotNull(ctx)
    Assert.assertNull(ex)
  }
}
