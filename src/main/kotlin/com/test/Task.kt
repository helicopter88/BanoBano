package com.test

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

@JsonFormat
class Task(@JsonProperty("id") val id: Long, @JsonProperty("type") val type: String, @JsonProperty("model_path") val modelGit: String, @JsonProperty("student_paths") val studentGits: Array<String>) {

    init {
        println("$id: $type $modelGit ${studentGits.fold(""){ i, s -> i + s} }")
    }

    override fun toString(): String {
        return BanobanoApplication.mapper.writeValueAsString(this)
    }

}

