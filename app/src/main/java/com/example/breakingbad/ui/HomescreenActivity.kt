package com.example.breakingbad.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import android.widget.Filter
import android.widget.Filterable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import java.util.*
import kotlin.collections.ArrayList

class HomescreenActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    CompoundButton.OnCheckedChangeListener {
    private var adapter: CharactersRecyclerViewAdapter? = null
    private val characterViewModel: CharacterViewModel by viewModels {
        CharacterViewModel.CharacterViewModelFactory((application as BreakingBadApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        val rvCharacters       = rv_characters
        characterViewModel.allCharacters.observe(this, Observer {
            adapter = CharactersRecyclerViewAdapter(it as ArrayList<BreakingBadCharacter>)
            rvCharacters.adapter = adapter
        })
        val svSearchCharacters = sv_search_characters
        svSearchCharacters.setOnQueryTextListener(this)
        val seasonOneCheckbox = season_1.apply {
            setOnCheckedChangeListener(this@HomescreenActivity)
        }
        val seasonTwoCheckbox = season_2.apply {
            setOnCheckedChangeListener(this@HomescreenActivity)
        }
        val seasonThreeCheckbox = season_3.apply {
            setOnCheckedChangeListener(this@HomescreenActivity)
        }
        val seasonFourCheckbox = season_4.apply {
            setOnCheckedChangeListener(this@HomescreenActivity)
        }
        val seasonFiveCheckbox = season_5.apply {
            setOnCheckedChangeListener(this@HomescreenActivity)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        characterViewModel.allCharacters.observe(this, Observer {
            val seasons = arrayListOf<Int>()
            if (season_1.isChecked) {
                seasons.add(1)
            }
            if (season_2.isChecked) {
                seasons.add(2)
            }
            if (season_3.isChecked) {
                seasons.add(3)
            }
            if (season_4.isChecked) {
                seasons.add(4)
            }
            if (season_5.isChecked) {
                seasons.add(5)
            }
            val filteredSet = hashSetOf<BreakingBadCharacter>()
            it.forEach { character ->
                seasons.forEach { season ->
                    if (character.appearances.contains((season.toString()))) {
                        filteredSet.add(character)
                    }
                }
            }
            val filteredAndSorted = arrayListOf<BreakingBadCharacter>()
            val filtered = filteredSet.toList().sortedBy { character ->
                character.name
            }
            filtered.forEach {
                filteredAndSorted.add(it)
            }
            adapter?.submitList(filteredAndSorted)
            rv_characters.adapter = adapter
        })
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        Log.d("onQueryTextChange", "query: " + query)
        adapter?.filter?.filter(query)
        return true
    }

    inner class CharactersRecyclerViewAdapter(private var bbCharacters: ArrayList<BreakingBadCharacter>):
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
                        breakingBadCharacters = bbCharacters as ArrayList<BreakingBadCharacter>
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
}