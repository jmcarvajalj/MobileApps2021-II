package co.edu.unal.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

var isCodeMaker = true
var code = "null"
var codeFound = false
var checkTemp = true
var keyValue: String = "null"


class OnlineCodeGeneratorActivity : AppCompatActivity() {
    lateinit var headTV: TextView
    lateinit var codeEdt: EditText
    lateinit var createCodeBtn: Button
    lateinit var joinCodeBtn: Button
    lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_code_generator)
        headTV = findViewById(R.id.idTVHead)
        codeEdt = findViewById(R.id.idEdtCode)
        createCodeBtn = findViewById(R.id.idBtnCreate)
        joinCodeBtn = findViewById(R.id.idBtnJoin)
        loadingPB = findViewById(R.id.idPBLoading)

        createCodeBtn.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = codeEdt.text.toString()
            createCodeBtn.visibility = View.GONE
            joinCodeBtn.visibility = View.GONE
            headTV.visibility = View.GONE
            codeEdt.visibility = View.GONE
            loadingPB.visibility = View.GONE
            if (code!="null" && code!=""){
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var check = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            if(check==true){
                                createCodeBtn.visibility = View.VISIBLE
                                joinCodeBtn.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                codeEdt.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                            }else{
                                FirebaseDatabase.getInstance().reference.child("codes").push().setValue(code)
                                isValueAvailable(snapshot, code)
                                checkTemp = false
                                Handler().postDelayed({
                                    accepted()
                                    Toast.makeText(this@OnlineCodeGeneratorActivity, "Please don't go back", Toast.LENGTH_SHORT).show()
                                }, 300)
                            }
                        }, 2000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }else{
                createCodeBtn.visibility = View.VISIBLE
                joinCodeBtn.visibility = View.VISIBLE
                headTV.visibility = View.VISIBLE
                codeEdt.visibility = View.VISIBLE
                loadingPB.visibility = View.GONE
                Toast.makeText(this, "Please enter a valid code", Toast.LENGTH_SHORT).show()
            }
        }

        joinCodeBtn.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = codeEdt.text.toString()
            if (code!="null" && code!=""){
                createCodeBtn.visibility = View.GONE
                joinCodeBtn.visibility = View.GONE
                headTV.visibility = View.GONE
                codeEdt.visibility = View.GONE
                loadingPB.visibility = View.VISIBLE
                isCodeMaker = false
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var data: Boolean = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            if(data==true){
                                codeFound = true
                                accepted()
                                createCodeBtn.visibility = View.VISIBLE
                                joinCodeBtn.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                codeEdt.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                            }else{
                                createCodeBtn.visibility = View.VISIBLE
                                joinCodeBtn.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                codeEdt.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                                Toast.makeText(this@OnlineCodeGeneratorActivity, "Invalid code", Toast.LENGTH_SHORT).show()

                            }
                        }, 2000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

            }else{
                Toast.makeText(this, "Please enter a valid code", Toast.LENGTH_SHORT).show()
            }
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

    fun accepted(){
        startActivity(Intent(this, OnlineMultiPlayerGameActivity::class.java))
        createCodeBtn.visibility = View.VISIBLE
        joinCodeBtn.visibility = View.VISIBLE
        codeEdt.visibility = View.VISIBLE
        headTV.visibility = View.VISIBLE
        loadingPB.visibility = View.GONE
    }

    fun isValueAvailable(snapshot: DataSnapshot, code: String): Boolean{
        var data = snapshot.children
        data.forEach{
            var value = it.getValue().toString()
            if (value==code){
                keyValue = it.key.toString()
                return true
            }
        }
        return false
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