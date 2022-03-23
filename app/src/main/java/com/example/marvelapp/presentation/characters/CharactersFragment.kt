package com.example.marvelapp.presentation.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.sarafaria.core.domain.model.Character
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment() {
    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    // criando o adapter fora dos metodos do ciclo de vida, garante que quando eu ir pra uma tela e voltar pra essa
    // o meu adapter nao sera recriado tudo novamente, seu estado permanecerá normalmente
    private val charactersAdapter = CharactersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCharactersBinding.inflate(
        inflater, container, false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCharactersAdapter()

        charactersAdapter.submitList(
            listOf(
                Character("3D-Man", ""),
                Character("3D-Man", ""),
                Character("3D-Man", ""),
            )
        )
    }

    private fun initCharactersAdapter() {
        binding.recyclerCharacter.run {
            setHasFixedSize(true) // aqui eu garanto que todos os itens do meu recycler view tenha o mesmo tamanho, conseguindo assim ser mais performatico
            adapter = charactersAdapter
        }
    }
}