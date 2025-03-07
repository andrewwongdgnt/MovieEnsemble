package com.dgnt.movienensemble.core.util.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class SerializerImpl : Serializer {

    private val gson: Gson = GsonBuilder().apply {
        // register any type adapter
    }.create()

    override fun <T> serialize(value: T): String = gson.toJson(value)

    override fun <T> deserialize(value: String, type: Type): T {
        return gson.fromJson(value, type)
    }
}