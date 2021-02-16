package com.example.breakingbad.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingbad.R
import com.example.breakingbad.character.BreakingBadCharacter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_character.view.*

class CharactersRecyclerViewAdapter(private var bbCharacters: ArrayList<BreakingBadCharacter>):
    RecyclerView.Adapter<CharactersRecyclerViewAdapter.CharactersRecyclerViewHolder>(), Filterable {
    private var breakingBadCharacters = arrayListOf<BreakingBadCharacter>()

    init {
        breakingBadCharacters = bbCharacters
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersRecyclerViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_character,
            parent, false
        )
        return CharactersRecyclerViewHolder(v)
    }
    override fun onBindViewHolder(holder: CharactersRecyclerViewHolder, position: Int) {
        val character: BreakingBadCharacter = breakingBadCharacters[position]

        holder.itemView.tv_input_nickname.text = character.nickname
        holder.itemView.tv_char_name.text     = character.name
        Picasso.get().load(character.img).into(holder.itemView.iv_char_icon)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("char_id", character.id)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return breakingBadCharacters.size
    }
    fun submitList(breakingBadCharacters: ArrayList<BreakingBadCharacter>) {
        this.breakingBadCharacters = breakingBadCharacters
        notifyDataSetChanged()
    }

    inner class CharactersRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    breakingBadCharacters = bbCharacters
                } else {
                    val resultList = ArrayList<BreakingBadCharacter>()
                    for (row in bbCharacters) {
                        if (row.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    breakingBadCharacters = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = breakingBadCharacters
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                breakingBadCharacters = results?.values as ArrayList<BreakingBadCharacter>
                notifyDataSetChanged()
            }
        }
    }
}