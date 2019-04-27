package com.yan.feracode.spotify.data.api.retrofit.deserializer

import com.google.gson.JsonDeserializer

internal interface ListDeserializer<T> : JsonDeserializer<List<T>>
