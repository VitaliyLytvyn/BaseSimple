package com.me.basesimple.data.entity

import com.google.gson.annotations.SerializedName
import com.me.basesimple.domain.entity.Repo

data class GitRepo(
    val id: Int,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val url: String
) {
    fun toRepo() = Repo(id, name, fullName, url)
}

