package org.bmsk.androidcoroutinehandson.api

import org.bmsk.androidcoroutinehandson.model.Item

data class ImageSearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Item>
)
