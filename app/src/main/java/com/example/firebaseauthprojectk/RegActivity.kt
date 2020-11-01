package com.example.firebaseauthprojectk

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import java.lang.Exception

@Suppress("CAST_NEVER_SUCCEEDS")
class RegActivity : AppCompatActivity(){

    private lateinit var buttonEnter:Button
    private lateinit var buttonRotate:Button
    private lateinit var name:EditText
    private lateinit var surname:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText

    private lateinit var auth:FirebaseAuth
    private lateinit var user:FirebaseUser
    private lateinit var userParametrs: UserParametrs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        buttonEnter = findViewById(R.id.rightButton)
        buttonRotate = findViewById(R.id.leftButton)

        name = findViewById(R.id.name)
        surname = findViewById(R.id.surname)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        auth = FirebaseAuth.getInstance()
        try {
            user = auth.currentUser!!

        }catch (e:Exception){}


        buttonRotate.setOnClickListener {
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonEnter.setOnClickListener {
            if(name.text.toString() == "" || surname.text.toString() == ""
                || email.text.toString() == "" || password.text.toString() == ""
                || !email.text.toString().contains("@"))
            {
                Toast.makeText(this, "Не все параметры заданы", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            userParametrs = UserParametrs(name.text.toString(), surname.text.toString(), email.text.toString(), password.text.toString())
            regUser()

        }

    }

    private fun regUser(){
        auth.createUserWithEmailAndPassword(userParametrs.email, userParametrs.password)//регистрируем пользователя
            .addOnSuccessListener {
                user = auth.currentUser!!
                user.sendEmailVerification()
                    .addOnSuccessListener {
                        var builder:UserProfileChangeRequest.Builder = UserProfileChangeRequest.Builder()
                        builder.setDisplayName(userParametrs.name + "#" + userParametrs.surname)
                        user.updateProfile(builder.build())
                        dialogWindow("Вам отправленно ссылка на email. Для подтверждения перейдите по ней")
                    }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun dialogWindow(str:String)
    {
        var build:AlertDialog.Builder = AlertDialog.Builder(this)
        build.setMessage(str)
            .setCancelable(false)
            .setPositiveButton("OK"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                    var intent:Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
            }
        var alertDialog:AlertDialog = build.create()
        alertDialog.show()
    }


}