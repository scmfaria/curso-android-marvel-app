package com.example.testing.model

import com.sarafaria.core.domain.model.Character

class CharacterFactory {

    fun create(hero: Hero) = when(hero) {
        Hero.ThreeDMan -> Character(
            "3-D Man",
            "https://imageUrl.jpg"
        )
        Hero.ABomb -> Character(
            "A-Bomb",
            "https://imageUrl"
        )
    }

    sealed class Hero {
        object ThreeDMan : Hero()
        object ABomb : Hero()
    }
}