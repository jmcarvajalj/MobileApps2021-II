package co.edu.unal.tictactoe

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlin.system.exitProcess


var isMyMove = isCodeMaker
class OnlineMultiPlayerGameActivity : AppCompatActivity() {
    lateinit var player1TV: TextView
    lateinit var player2TV: TextView
    lateinit var box1Btn: Button
    lateinit var box2Btn: Button
    lateinit var box3Btn: Button
    lateinit var box4Btn: Button
    lateinit var box5Btn: Button
    lateinit var box6Btn: Button
    lateinit var box7Btn: Button
    lateinit var box8Btn: Button
    lateinit var box9Btn: Button
    lateinit var resetBtn: Button
    lateinit var turnTV: TextView
    var player1count = 0
    var player2count = 0
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var emptyCells = ArrayList<Int>()
    var activeUser = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_multi_player_game)
        player1TV = findViewById(R.id.idTVPlayer1)
        player2TV = findViewById(R.id.idTVPlayer2)
        box1Btn = findViewById(R.id.idBtnBox1)
        box2Btn = findViewById(R.id.idBtnBox2)
        box3Btn = findViewById(R.id.idBtnBox3)
        box4Btn = findViewById(R.id.idBtnBox4)
        box5Btn = findViewById(R.id.idBtnBox5)
        box6Btn = findViewById(R.id.idBtnBox6)
        box7Btn = findViewById(R.id.idBtnBox7)
        box8Btn = findViewById(R.id.idBtnBox8)
        box9Btn = findViewById(R.id.idBtnBox9)
        resetBtn = findViewById(R.id.idBtnReset)
        turnTV = findViewById(R.id.idTVTurn)
        resetBtn.setOnClickListener {
            reset()
        }
        FirebaseDatabase.getInstance().reference.child("data").child(code).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var data = snapshot.value
                if(isMyMove==true){
                    isMyMove = false
                    moveOnline(data.toString(), isMyMove)
                }else{
                    isMyMove = true
                    moveOnline(data.toString(), isMyMove)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                reset()
                Toast.makeText(this@OnlineMultiPlayerGameActivity, "Game Reset", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun moveOnline(data: String, move:Boolean){
        val audio1 = MediaPlayer.create(this, R.raw.sound1)
        if(move){
            var buttonSelected: Button?
            buttonSelected = when(data.toInt()){
                1-> box1Btn
                2-> box2Btn
                3-> box3Btn
                4-> box4Btn
                5-> box5Btn
                6-> box6Btn
                7-> box7Btn
                8-> box8Btn
                9-> box9Btn
                else -> {
                    box1Btn
                }
            }
            buttonSelected.text = "O"
            turnTV.text = "Turn : Player 1"
            buttonSelected.setTextColor(Color.parseColor("#D22BB804"))
            player2.add(data.toInt())
            emptyCells.add(data.toInt())
            audio1.start()
            Handler().postDelayed(Runnable { audio1.release() }, 200)
            buttonSelected.isEnabled = false
            checkWinner()
        }
    }

    fun checkWinner(): Int{
        val audio = MediaPlayer.create(this, R.raw.sound2)
        if(player1.contains(1) && player1.contains(2) && player1.contains(3) ||
           player1.contains(1) && player1.contains(4) && player1.contains(7) ||
           player1.contains(3) && player1.contains(6) && player1.contains(9) ||
           player1.contains(7) && player1.contains(8) && player1.contains(9) ||
           player1.contains(4) && player1.contains(5) && player1.contains(6) ||
           player1.contains(1) && player1.contains(5) && player1.contains(9) ||
           player1.contains(3) && player1.contains(5) && player1.contains(7) ||
           player1.contains(2) && player1.contains(5) && player1.contains(8)){
            player1count += 1
            buttonDisable()
            audio.start()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 1 Wins"+"\n\n"+"Do you want to play again?")
            build.setPositiveButton("Yes"){dialog, which->
                reset()
                audio.release()
            }
            build.setNegativeButton("Exit"){dialog, which->
                audio.release()
                removeCode()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1
        }else if(player2.contains(1) && player2.contains(2) && player2.contains(3) ||
                 player2.contains(1) && player2.contains(4) && player2.contains(7) ||
                 player2.contains(3) && player2.contains(6) && player2.contains(9) ||
                 player2.contains(7) && player2.contains(8) && player2.contains(9) ||
                 player2.contains(4) && player2.contains(5) && player2.contains(6) ||
                 player2.contains(1) && player2.contains(5) && player2.contains(9) ||
                 player2.contains(3) && player2.contains(5) && player2.contains(7) ||
                 player2.contains(2) && player2.contains(5) && player2.contains(8)){
            player2count += 1
            audio.start()
            buttonDisable()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 2 Wins"+"\n\n"+"Do you want to play again?")
            build.setPositiveButton("Yes"){dialog, which->
                reset()
                audio.release()
            }
            build.setNegativeButton("Exit"){dialog, which->
                audio.release()
                removeCode()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1
        }else if(emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3) &&
                 emptyCells.contains(4) && emptyCells.contains(5) && emptyCells.contains(6) &&
                 emptyCells.contains(7) && emptyCells.contains(8) && emptyCells.contains(9)){
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Game Draw"+"\n\n"+"Do you want to play again?")
            build.setPositiveButton("Yes"){dialog, which->
                audio.release()
                reset()
            }
            build.setNegativeButton("Exit"){dialog, which->
                audio.release()
                removeCode()
                exitProcess(1)
            }
            build.show()
            return 1
        }
        return 0
    }

    fun playNow(buttonSelected: Button, currCell: Int){
        val audio = MediaPlayer.create(this, R.raw.sound1)
        buttonSelected.text = "X"
        emptyCells.remove(currCell)
        turnTV.text = "Turn : Player 2"
        buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
        player1.add(currCell)
        emptyCells.add(currCell)
        audio.start()
        buttonSelected.isEnabled = false
        Handler().postDelayed(Runnable { audio.release() }, 200)
        checkWinner()
    }

    fun reset(){
        player1.clear()
        player2.clear()
        emptyCells.clear()
        activeUser = 1
        for(i in 1..9){
            var buttonSelected: Button?
            buttonSelected = when(i){
                1-> box1Btn
                2-> box2Btn
                3-> box3Btn
                4-> box4Btn
                5-> box5Btn
                6-> box6Btn
                7-> box7Btn
                8-> box8Btn
                9-> box9Btn
                else -> {box1Btn}
            }
            buttonSelected.isEnabled = true
            buttonSelected.text = ""
            player1TV.text = "Player 1: $player1count"
            player2TV.text = "Player 2: $player2count"
            isMyMove = isCodeMaker
            if(isCodeMaker){
                FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
            }
        }
    }

    fun buttonDisable(){
        for(i in 1..9){
            val buttonSelected = when(i){
                1-> box1Btn
                2-> box2Btn
                3-> box3Btn
                4-> box4Btn
                5-> box5Btn
                6-> box6Btn
                7-> box7Btn
                8-> box8Btn
                9-> box9Btn
                else -> {box1Btn}
            }
            if(buttonSelected.isEnabled == true){
                buttonSelected.isEnabled = false
            }
        }
    }

    fun removeCode(){
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("codes").child(keyValue).removeValue()
        }
    }

    fun disableReset(){
        resetBtn.isEnabled = false
        Handler().postDelayed(Runnable { resetBtn.isEnabled = true }, 2000)
    }

    fun updateDatabase(cellId: Int){
        FirebaseDatabase.getInstance().reference.child("data").child(code).push().setValue(cellId)
    }

    override fun onBackPressed() {
        removeCode()
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
        }
        exitProcess(0)
    }

    fun buttonClick(view: View) {
        if(isMyMove){
            val but = view as Button
            var cellOnline = 0
            when(but.id){
                R.id.idBtnBox1->cellOnline = 1
                R.id.idBtnBox2->cellOnline = 2
                R.id.idBtnBox3->cellOnline = 3
                R.id.idBtnBox4->cellOnline = 4
                R.id.idBtnBox5->cellOnline = 5
                R.id.idBtnBox6->cellOnline = 6
                R.id.idBtnBox7->cellOnline = 7
                R.id.idBtnBox8->cellOnline = 8
                R.id.idBtnBox9->cellOnline = 9
                else->{cellOnline = 0}
            }
            playerTurn = false
            Handler().postDelayed(Runnable { playerTurn = true }, 600)
            playNow(but, cellOnline)
            updateDatabase(cellOnline)
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