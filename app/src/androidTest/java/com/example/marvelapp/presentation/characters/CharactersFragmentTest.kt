package com.example.marvelapp.presentation.characters

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelapp.launchFragmentInHiltContainer
import com.example.marvelapp.R
import com.example.marvelapp.extension.asJsonString
import com.example.marvelapp.framework.di.BaseUrlModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(BaseUrlModule::class)
@HiltAndroidTest
class CharactersFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        server = MockWebServer().apply {
            start(8080)
        }
        launchFragmentInHiltContainer<CharactersFragment>()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun shouldShowCharacters_whenViewIsCreated() {
        server.enqueue(MockResponse().setBody("characters_p1.json".asJsonString()))

        onView(
            withId(R.id.recycler_character)
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldLoadMoreCharacters_whenNewPageIsRequested() {
        server.enqueue(MockResponse().setBody("characters_p1.json".asJsonString()))
        server.enqueue(MockResponse().setBody("characters_p2.json".asJsonString()))

        onView(
            withId(R.id.recycler_character)
        ).perform( // perform = executa uma ação no ID que está acima
            RecyclerViewActions.scrollToPosition<CharactersViewHolder>(20)
        )

        // é necessario fazer uma ação na tela para fazer o scroll na tela, senao ele nao vai para a segunda pagina sozinho


        // Assert
        onView(
            withText("Amora")
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldShowErrorView_whenReceivesAnErrorFromApi() {
        // Arrange
        server.enqueue(MockResponse().setResponseCode(404))

        // verifica se caso der erro 404 será exibido um texto de mensagem de erro
        onView(
            withId(R.id.text_initial_loading_error)
        ).check(
            matches(isDisplayed())
        )
    }
}