package com.yan.feracode.spotify.data.api.retrofit.deserializer

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.yan.feracode.spotify.data.api.Constants
import java.lang.reflect.Type

class TracksDeserializer<T> : ListDeserializer<T> {

    @Throws(JsonParseException::class)
    override fun deserialize(je: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<T>? {
        val trackJsonArray = je.asJsonObject.get(Constants.Deserializer.TRACKS)
        return Gson().fromJson<List<T>>(trackJsonArray, typeOfT)
    }
}
