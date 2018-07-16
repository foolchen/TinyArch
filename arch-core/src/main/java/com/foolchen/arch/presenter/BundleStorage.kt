package com.foolchen.arch.presenter

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

/**
 * 用于保存bundle对应的key-values
 * @author chenchong
 * 2018/7/16
 * 下午3:16
 */
data class BundleStorage(val key: String, val bundle: Bundle) : Parcelable {

  constructor(source: Parcel) : this(
      source.readString(),
      source.readParcelable<Bundle>(Bundle::class.java.classLoader)
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(key)
    writeParcelable(bundle, 0)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<BundleStorage> = object : Parcelable.Creator<BundleStorage> {
      override fun createFromParcel(source: Parcel): BundleStorage = BundleStorage(source)
      override fun newArray(size: Int): Array<BundleStorage?> = arrayOfNulls(size)
    }
  }
}