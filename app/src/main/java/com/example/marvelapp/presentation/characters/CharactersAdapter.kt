package com.example.marvelapp.presentation.characters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sarafaria.core.domain.model.Character

class CharactersAdapter : ListAdapter<Character, CharactersViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        // para cada item da lista, faz o bind com os dados do viewHolder
        // getItem() é um metodo ja pronto que o ListAdapter tem
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Character>() {
            // verifica se o item novo é o item que ja está carregado (oldItem)
            override fun areItemsTheSame(
                oldItem: Character,
                newItem: Character): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Character,
                newItem: Character): Boolean {
                // comparando instancias
                return oldItem == newItem
            }
        }
    }
}