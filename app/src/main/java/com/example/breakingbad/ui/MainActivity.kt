package com.example.breakingbad.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import com.example.breakingbad.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewSwitcher.ViewFactory, View.OnClickListener {

    private var isRunning = true
    private var index = 0
    private val interval = 2500L
    private var backgrounds = arrayListOf(
        R.drawable.details_bg, R.drawable.details_bg_ls, R.drawable.portrait_bg_1,
        R.drawable.portrait_bg_2, R.drawable.portrait_bg_3
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_title.setOnClickListener(this)
        image_switcher.setOnClickListener(this)
        btn_select_characters.setOnClickListener(this)
        startAnimatedBackground()
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, HomescreenActivity::class.java)
        startActivity(intent)
    }

    override fun makeView(): View {
        val imageView           = ImageView(this)
        imageView.scaleType     = ImageView.ScaleType.CENTER_INSIDE
        imageView.layoutParams  = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT)
        return imageView
    }

    override fun finish() {
        isRunning = false
        super.finish()
    }

    private fun startAnimatedBackground() {
        val aniIn  = AnimationUtils.loadAnimation(this, R.anim.fadein)
        val aniOut = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        aniIn.duration      = 1000
        aniOut.duration     = 1000
        val imageSwitcher   = image_switcher
        imageSwitcher.inAnimation   = aniIn
        imageSwitcher.outAnimation  = aniOut
        imageSwitcher.setFactory(this)
        imageSwitcher.setImageResource(backgrounds[index])
        val handler = Handler(mainLooper)
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (isRunning) {
                    index++
                    index %= backgrounds.size
                    imageSwitcher.setImageResource(backgrounds[index])
                    handler.postDelayed(this, interval)
                }
            }
        }
        handler.postDelayed(runnable, interval)
    }
}