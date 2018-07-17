package com.foolchen.arch.samples.network

import com.foolchen.arch.samples.bean.Photo
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Unsplash相关接口
 *
 * @author chenchong
 * 2018/7/17
 * 下午4:31
 */
interface UnsplashService {
  @GET("photos")
  fun getPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int = 10, @Query(
      "order_by") orderBy: String = "popular"): Observable<List<Photo>>

  @GET("photos")
  fun call(@Query("page") page: Int, @Query("per_page") perPage: Int = 10, @Query(
      "order_by") orderBy: String = "popular"): Call<List<Photo>>

}