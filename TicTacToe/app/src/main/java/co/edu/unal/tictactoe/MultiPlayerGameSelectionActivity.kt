package co.edu.unal.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MultiPlayerGameSelectionActivity : AppCompatActivity() {

    lateinit var onlineBtn: Button
    lateinit var offlineBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_game_selection)
        onlineBtn = findViewById(R.id.idBtnOnline)
        offlineBtn = findViewById(R.id.idBtnOffline)
        onlineBtn.setOnClickListener {
            singleUser = false
            startActivity(Intent(this, OnlineCodeGeneratorActivity::class.java))
        }
        offlineBtn.setOnClickListener {
            singleUser = false
            startActivity(Intent(this, GameplayActivity::class.java))
        }


        button_quit.setOnClickListener {
        val dialog = AlertDialog.Builder(this).setTitle("Quit Game")
            .setMessage("Do you really want to quit?")
            .setIcon(R.mipmap.tictactoeicon)
            .setPositiveButton("Yes") { _, _ ->
                finishAndRemoveTask()
            }
            .setNeutralButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        val message = dialog.findViewById<View>(android.R.id.message) as TextView?
        message?.textSize = 26f
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val aboutDialog = AlertDialog.Builder(this)

        return when (item.itemId) {

            R.id.about_item -> {
                aboutDialog.setTitle("About this game")
                    .setMessage("Tic Tac Toe by:\n\nJose Miguel Carvajal Jimenez\n\nDon't let your opponent beat you!")
                    .setIcon(R.mipmap.tictactoeicon)
                    .setPositiveButton("Close"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}