package com.example.marvelapp.factory.response

import com.example.marvelapp.framework.network.response.CharacterResponse
import com.example.marvelapp.framework.network.response.DataContainerResponse
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.ThumbnailResponse

class DataWrapperResponseFactory {

    fun create() = DataWrapperResponse(
        copyright = "",
        data = DataContainerResponse(
            offset = 0,
            total = 1,
            results = listOf(
                CharacterResponse(
                    id = "id",
                    name = "3-D Man",
                    thumbnail = ThumbnailResponse(
                        path = "http://imageUrl",
                        extension = "jpg"
                    )
                )
            )
        )
    )
}