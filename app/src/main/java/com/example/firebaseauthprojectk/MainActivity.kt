package com.example.firebaseauthprojectk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fUser:FirebaseUser

    private lateinit var email:TextView
    private lateinit var password:TextView
    private lateinit var buttonEnter:Button
    private lateinit var registerButton:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        try {
            fUser = FirebaseAuth.getInstance().currentUser!!
        }catch (e:Exception){

        }




        email = findViewById(R.id.textMail)
        password = findViewById(R.id.textPassword)
        buttonEnter = findViewById(R.id.enterButton)
        registerButton = findViewById(R.id.register)

        buttonEnter.setOnClickListener{
            if(email.text == "" || password.text == "")
                return@setOnClickListener
            inputAccount(email.text.toString(), password.text.toString())
        }

        registerButton.setOnClickListener{
            var intent:Intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
        }

    }

    fun inputAccount(email:String, passwod:String)
    {
        auth.signInWithEmailAndPassword(email, passwod)
                .addOnSuccessListener {
                    fUser = FirebaseAuth.getInstance().currentUser!!
                    if(fUser.isEmailVerified)//проверяем подтвердил ли пользователь свой email
                    {
                        //если программа попала в этот if то авторизация выполненна успешно и можно двигаться дальше
                        var intent = Intent(this, SettingAccountActivity::class.java)
                        startActivity(intent)

                    }else{
                        //если программа попала в этот else то пользователь выполнил регистрацию, но не подтвердил email
                        Toast.makeText(this,"Подтвердите email по отправленной вам ссылке", Toast.LENGTH_LONG).show()

                    }
                }.addOnFailureListener {
                    Log.d("TAGA", it.toString())
            }
    }

}
