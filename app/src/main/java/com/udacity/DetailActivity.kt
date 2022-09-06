package com.udacity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_main.*

class DetailActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var nameFile:TextView
    private lateinit var nameStatus:TextView
    private  val TAG = "DetailActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        button = findViewById(R.id.detail_button)
        nameFile=findViewById(R.id.tv_filename)
        nameStatus=findViewById(R.id.tv_file_status)
        nameFile.text=intent.getStringExtra("filename")
        nameStatus.text=intent.getStringExtra("status")
        move(nameStatus)
        move(nameFile)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun move(view: TextView) {
        val startColor=Color.BLUE
        val endColor=Color.RED
        val va = ValueAnimator.ofFloat(-20f, 20f)
       var cAnimator = ValueAnimator.ofObject(
            ArgbEvaluator(),
            startColor,
            endColor
        )
        cAnimator.addUpdateListener(AnimatorUpdateListener { valueAnimator ->
            view.setTextColor(
                valueAnimator.animatedValue as Int
            )
        })
        cAnimator.setDuration(1000)
        va.duration = 2000 //in millis
        va.addUpdateListener { animation -> view.translationX = animation.animatedValue  as Float }
        va.repeatCount = 5
        va.reverse()
        cAnimator.start()
        va.start()
    }
}
