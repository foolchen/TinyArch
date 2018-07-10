package com.foolchen.arch.view

class OptionalView<V>(val view: V?) {

  override fun toString(): String {
    return "OptionalView{" + "view=" + view + '}'.toString()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    val that = other as OptionalView<*>?

    return if (view != null) view == that!!.view else that!!.view == null
  }

  override fun hashCode(): Int {
    return view?.hashCode() ?: 0
  }
}
