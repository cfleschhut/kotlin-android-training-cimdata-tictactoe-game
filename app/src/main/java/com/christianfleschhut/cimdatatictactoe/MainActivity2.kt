package com.christianfleschhut.cimdatatictactoe

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.christianfleschhut.cimdatatictactoe.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    private lateinit var newPlayerName1: String
    private lateinit var newPlayerName2: String

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("storage", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.buttonGoToMain.setOnClickListener { gotoMain() }

        readIntentData()

        binding.apply {
            inputPlayerName1.setText(newPlayerName1)
            inputPlayerName2.setText(newPlayerName2)

            btnSubmitPlayerName1.setOnClickListener {
                newPlayerName1 = inputPlayerName1.text.toString()
                editor.putString("playerName1", newPlayerName1).apply()
            }

            btnSubmitPlayerName2.setOnClickListener {
                newPlayerName2 = inputPlayerName2.text.toString()
                editor.putString("playerName2", newPlayerName2).apply()
            }
        }
    }

    private fun readIntentData() {
        newPlayerName1 = intent.getStringExtra("playerName1").toString()
        newPlayerName2 = intent.getStringExtra("playerName2").toString()
    }

    private fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}