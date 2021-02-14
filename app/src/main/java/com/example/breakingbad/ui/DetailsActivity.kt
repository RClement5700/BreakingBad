package com.example.breakingbad.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.breakingbad.BreakingBadApplication
import com.example.breakingbad.R
import com.example.breakingbad.character.CharacterViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    private val characterViewModel: CharacterViewModel by viewModels {
        CharacterViewModel.CharacterViewModelFactory((application as BreakingBadApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val charId = intent.extras?.get("char_id") as Int
        characterViewModel.getCharacter(charId).observe(this, Observer {
            Picasso.get().load(it.img).into(iv_character_portrait)
            tv_input_name.text        = it.name
            tv_input_aka.text         = it.nickname
            tv_input_birthday.text    = it.birthday
            tv_input_occupation.text  = it.occupation
            tv_input_status.text      = it.status
            tv_input_appearances.text = it.appearances
        })
    }
}