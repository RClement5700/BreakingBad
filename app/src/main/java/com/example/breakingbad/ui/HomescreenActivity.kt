package com.example.breakingbad.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingbad.BreakingBadApplication
import com.example.breakingbad.R
import com.example.breakingbad.character.BreakingBadCharacter
import com.example.breakingbad.character.CharacterViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.view.iv_character_portrait
import kotlinx.android.synthetic.main.activity_details.view.tv_input_name
import kotlinx.android.synthetic.main.activity_homescreen.*
import kotlinx.android.synthetic.main.item_view_character.view.*

/*
    TODO:
        •The user should be able to search for a character by name
        •The user should be able to filter characters by season appearance
        •need to write unit tests
 */

class HomescreenActivity : AppCompatActivity() {
    private val characterViewModel: CharacterViewModel by viewModels {
        CharacterViewModel.CharacterViewModelFactory((application as BreakingBadApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        val adapter      = CharactersRecyclerViewAdapter()
        val rvCharacters = rv_characters
        characterViewModel.allCharacters.observe(this, Observer { //it = movies
            it.let {
                adapter.submitList(it)
            }
        })
        rvCharacters.adapter = adapter
    }

    private inner class CharactersRecyclerViewAdapter:
        RecyclerView.Adapter<CharactersRecyclerViewAdapter.CharactersRecyclerViewHolder>() {

        private var breakingBadCharacters: List<BreakingBadCharacter> = arrayListOf()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersRecyclerViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view_character,
                parent, false)
            return CharactersRecyclerViewHolder(v)
        }
        override fun onBindViewHolder(holder: CharactersRecyclerViewHolder, position: Int) {
            val character: BreakingBadCharacter    = breakingBadCharacters[position]
            holder.itemView.tv_input_nickname.text = character.nickname
            holder.itemView.tv_input_name.text     = character.name
            Picasso.get().load(character.img).into(holder.itemView.iv_character_portrait)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
                intent.putExtra("char_id", character.id)
                holder.itemView.context.startActivity(intent)
            }
        }
        override fun getItemCount(): Int {
            return breakingBadCharacters.size
        }
        fun submitList(breakingBadCharacters: List<BreakingBadCharacter>) {
            this.breakingBadCharacters = breakingBadCharacters
            notifyDataSetChanged()
        }
        inner class CharactersRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}