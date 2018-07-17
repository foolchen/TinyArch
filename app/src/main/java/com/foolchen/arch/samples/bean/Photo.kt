package com.foolchen.arch.samples.bean

import com.google.gson.annotations.SerializedName

data class Photo(
    var id: String,
    var created_at: String,
    var updated_at: String,
    var width: Int,
    var height: Int,
    var color: String,
    var description: Any,
    var urls: Urls,
    var links: Links,
    var categories: List<Any>,
    var sponsored: Boolean,
    var likes: Int,
    var liked_by_user: Boolean,
    var current_user_collections: List<Any>,
    var slug: Any,
    var user: User
)

data class User(
    var id: String,
    var updated_at: String,
    var username: String,
    var name: String,
    var first_name: String,
    var last_name: String,
    var twitter_username: Any,
    var portfolio_url: String,
    var bio: String,
    var location: String,
    @SerializedName("links")
    var links: UserLinks,
    var profile_image: ProfileImage,
    var instagram_username: String,
    var total_collections: Int,
    var total_likes: Int,
    var total_photos: Int
)

data class UserLinks(
    var self: String,
    var html: String,
    var photos: String,
    var likes: String,
    var portfolio: String,
    var following: String,
    var followers: String
)

data class ProfileImage(
    var small: String,
    var medium: String,
    var large: String
)

data class Urls(
    var raw: String,
    var full: String,
    var regular: String,
    var small: String,
    var thumb: String
)

data class Links(
    var self: String,
    var html: String,
    var download: String,
    var download_location: String
)