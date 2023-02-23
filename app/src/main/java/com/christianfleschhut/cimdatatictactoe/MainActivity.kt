package com.christianfleschhut.cimdatatictactoe

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.christianfleschhut.cimdatatictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var btnList: List<Button>

    private val player1 = "x"
    private val player2 = "o"

    private var currentPlayer = player1
    private var playerMoves = mutableMapOf<String, MutableSet<Int>>(
        player1 to mutableSetOf(),
        player2 to mutableSetOf()
    )

    private var gameWon = false

    private val playerNames = mutableMapOf(
        player1 to "Player 1",
        player2 to "Player 2",
    )

    private var totalWins = mutableMapOf(
        player1 to 0,
        player2 to 0,
    )

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.displayWinner.visibility = View.INVISIBLE

        btnList = listOf(
            binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6,
            binding.btn7, binding.btn8, binding.btn9,
        )

        btnList.forEach { btn ->
            btn.setOnClickListener {
                markField(btn)
            }
        }

        binding.buttonRestart.setOnClickListener { resetGameState() }
        binding.buttonResetStoredScores.setOnClickListener { resetScore() }
        binding.buttonGoToSettings.setOnClickListener { gotoSettings() }

        sharedPreferences = getSharedPreferences("storage", MODE_PRIVATE)
        editor = sharedPreferences.edit()

//        val storedValue = sharedPreferences.getInt("keyTest", 0)
//        Log.i("SP Shared Preferences", "$storedValue")
//
//        val newValue = storedValue + 1
//        editor.putInt("keyTest", newValue).apply()

        val storedScorePlayer1 = sharedPreferences.getInt("scorePlayer1", 0)
        val storedScorePlayer2 = sharedPreferences.getInt("scorePlayer2", 0)

        totalWins[player1] = storedScorePlayer1
        totalWins[player2] = storedScorePlayer2

        updatePlayerScoreDisplays()


        playerNames[player1] = sharedPreferences.getString("playerName1", playerNames[player1])!!
        playerNames[player2] = sharedPreferences.getString("playerName2", playerNames[player2])!!

        binding.playerNamePlayer1.text = getString(
            R.string.player_name_player1, playerNames[player1])
        binding.playerNamePlayer2.text = getString(
            R.string.player_name_player2, playerNames[player2])
    }

    private fun markField(btn: Button) {
        if (btn.text.isNotEmpty() || gameWon) return

        if (currentPlayer == "x") {
            btn.text = "x"
            playerMoves["x"]?.add(btn.tag.toString().toInt())

            checkWin()
            currentPlayer = "o"

        } else {
            btn.text = "o"
            playerMoves["o"]?.add(btn.tag.toString().toInt())

            checkWin()
            currentPlayer = "x"
        }

        println("Player Moves: $playerMoves")
    }

    private fun checkWin() {
        val winningCombinations = listOf(
            setOf(1, 2, 3), setOf(4, 5, 6), setOf(7, 8, 9),
            setOf(1, 4, 7), setOf(2, 5, 8), setOf(3, 6, 9),
            setOf(3, 5, 7), setOf(1, 5, 9)
        )

        val matched = winningCombinations.any {
            playerMoves["x"]!!.containsAll(it) || playerMoves["o"]!!.containsAll(it)
        }
        println("** Match: $matched ** $currentPlayer **")
//        println(playerMoves["x"]!!.size)
//        println(playerMoves["y"]!!.size)

        if (matched) {
            gameWon = true

            binding.displayWinner.apply {
                text = getString(
                    R.string.display_winner, playerNames[currentPlayer]
                )
                visibility = View.VISIBLE
            }

            updateScoreDisplay(winner = currentPlayer)
        }
    }

    private fun updateScoreDisplay(winner: String) {
        totalWins[winner] = totalWins[winner]!! + 1

        if (winner == "x") {
            binding.winsPlayer1.text = getString(R.string.wins_player1, totalWins[winner])

            val newScorePlayer1 = sharedPreferences.getInt("scorePlayer1", 0) + 1
            editor.putInt("scorePlayer1", newScorePlayer1).apply()
        } else {
            binding.winsPlayer2.text = getString(R.string.wins_player2, totalWins[winner])

            val newScorePlayer2 = sharedPreferences.getInt("scorePlayer2", 0) + 1
            editor.putInt("scorePlayer2", newScorePlayer2).apply()
        }
    }

//    private fun toggleCurrentPlayer() {
//        currentPlayer = if (currentPlayer == "x") "o" else "x"
//    }

    private fun clearPlayingField() {
        btnList.forEach { it.text = "" }
    }

    private fun resetGameState() {
        clearPlayingField()
        binding.displayWinner.visibility = View.INVISIBLE

        playerMoves = mutableMapOf(
            player1 to mutableSetOf(),
            player2 to mutableSetOf()
        )
        gameWon = false
    }

    private fun resetScore() {
        totalWins = mutableMapOf(
            player1 to 0,
            player2 to 0,
        )
        updatePlayerScoreDisplays()

        with (editor) {
            remove("scorePlayer1")
            remove("scorePlayer2")
            apply()
        }
    }

    private fun updatePlayerScoreDisplays() {
        binding.winsPlayer1.text = getString(R.string.wins_player1, totalWins[player1])
        binding.winsPlayer2.text = getString(R.string.wins_player2, totalWins[player2])
    }

    private fun gotoSettings() {
        val intent = Intent(this, MainActivity2::class.java)

        intent.putExtra("playerName1", playerNames[player1])
        intent.putExtra("playerName2", playerNames[player2])

        startActivity(intent)
    }
}