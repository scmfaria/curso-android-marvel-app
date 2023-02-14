package com.example.marvelapp.presentation.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.presentation.characters.loadmore.CharactersLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment() {
    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    // criando o adapter fora dos metodos do ciclo de vida, garante que quando eu ir pra uma tela e voltar pra essa
    // o meu adapter nao sera recriado tudo novamente, seu estado permanecerá normalmente
    private lateinit var charactersAdapter: CharactersAdapter

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
        observeInitialLoadState()

        // .collect (faz parte do FLOW - é  que observa ele) -> esse cara é arriscado usar pois ele continua sendo escutado mesmo o app estando em background
        // então ele pode chamar o adapter e dar um crash em tempo de execução
        // para ajustar isso, é necessario colocar essa parte da escuta do FLOW dentro de:
        // viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED)
        // com essa linha, automaticamente quando for pra background ele vai parar de escutar e quando voltar, ele volta a escutar novamente
        // evitando gastar recursos desnecessarios ou crashes
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.charactersPagingData("").collect { pagingData ->
                    charactersAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun initCharactersAdapter() {
        charactersAdapter = CharactersAdapter()
        binding.recyclerCharacter.run {
            scrollToPosition(0)
            setHasFixedSize(true) // aqui eu garanto que todos os itens do meu recycler view tenha o mesmo tamanho, conseguindo assim ser mais performatico
            adapter = charactersAdapter.withLoadStateFooter(
                footer = CharactersLoadStateAdapter(
                    charactersAdapter::retry
                )
            )
        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                binding.flipperCharacters.displayedChild = when(loadState.refresh) {
                    is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOADING
                    }
                    is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }
                    is LoadState.Error -> {
                        setShimmerVisibility(false)
                        binding.includeViewError.buttonRetry.setOnClickListener {
                            // para dar um refresh (reload)
                            charactersAdapter.refresh() // limpa o adapter e faz a requisição novamente na posição 0
                        }
                        FLIPPER_CHILD_ERROR
                    }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewLoading.shimmerCharacters.run {
            isVisible = visibility
            if(visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }
}