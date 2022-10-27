package com.example.marvelapp.framework.network.response

import com.google.gson.annotations.SerializedName
import com.sarafaria.core.domain.model.Character

data class CharacterResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("thumbnail")
    val thumbnail: ThumbnailResponse
)

// extensions
fun CharacterResponse.toCharacterModel(): Character {
    return Character(
        name = this.name,
        imageUrl = "${this.thumbnail.path}.${this.thumbnail.extension}"
            .replace("http", "https")
    )
}
