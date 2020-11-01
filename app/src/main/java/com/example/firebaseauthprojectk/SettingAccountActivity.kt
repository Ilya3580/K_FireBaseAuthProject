package com.example.firebaseauthprojectk

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_setting_account.*

class SettingAccountActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var textView: TextView

    private lateinit var auth:FirebaseAuth
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_account)

        auth = FirebaseAuth.getInstance()
        user = auth.getCurrentUser()!!

        listView = findViewById(R.id.listView)
        textView = findViewById(R.id.textView)

        var mas = arrayOfNulls<String>(4)
        mas[0] = "Сменить имя пользователя"
        mas[1] = "Сменить фамилию пользователя"
        mas[2] = "Сменить email"
        mas[3] = "Сменить пароль"

        var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mas)
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener{ adapterView: AdapterView<*>, view: View, position: Int, id: Long ->
            if(position == 3)
            {
                setPassword()
                return@setOnItemClickListener
            }
            alertDialog(position)
        }

        updateParametrs(user.displayName as String, user.email as String)

    }

    private fun alertDialog(id:Int)
    {
        var builder = AlertDialog.Builder(this)

        when(id)
        {
            0->builder.setTitle("Введите имя")
            1->builder.setTitle("Введите фамилию")
            2->builder.setTitle("Введите email")
            3->builder.setTitle("Введите пароль")
        }

        var input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { diallog: DialogInterface, which:Int->
            var text = input.getText().toString()
            var builderFireBase = UserProfileChangeRequest.Builder()
            if(id <2)
            {
                //здесь мы изменяем имя или фамилию пользователя
                val namen = user.getDisplayName()
                val nameAndSurname = namen?.split("#")!!.toTypedArray()
                nameAndSurname[id] = text
                builderFireBase.setDisplayName(nameAndSurname[0] + "#" + nameAndSurname[1])
                user.updateProfile(builderFireBase.build())
                updateParametrs(nameAndSurname[0] + "#" + nameAndSurname[1], user.email as String)
            }
            if(id == 2)
            {
                setEmail(text)
            }
        }
        builder.show()
    }
    private fun setEmail(email:String)
    {
        if(email == user.email)
        {
            Toast.makeText(this, "Введет не корректный email", Toast.LENGTH_LONG).show()
            return
        }
        user.updateEmail(email)//меняем email
            .addOnCompleteListener {
                if(it.isSuccessful())//если все пройдет успешно и удастся email поменяется то мы попадем в if иначе в else
                {
                    user.sendEmailVerification()//отправляем письмо на измененный email для подтверждения
                        .addOnCompleteListener {
                            if (it.isSuccessful()) {
                                dialogWindow("Вам отправленно ссылка на email. Для подтверждения перейдите по ней", true)
                            }
                        }
                }else{
                    Toast.makeText(this, "Ошибка подключения", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this, "Ошибка подключения", Toast.LENGTH_LONG).show()
            }

    }
    private fun setPassword()
    {
        var email = user.getEmail()

        if (email != null) {
            auth.sendPasswordResetEmail(email)//отправляем ссылку для смены пароля на email
                .addOnCompleteListener {
                    if (it.isSuccessful) {//проверяем отправилось ли письмо
                        dialogWindow("Вам отправленно письмо на email для сброса пароля", false)
                    }
                }

        }
    }
    private fun dialogWindow(str:String, flag:Boolean)
    {
        var build = AlertDialog.Builder(this);

        build.setMessage(str)
            .setCancelable(false)
            .setPositiveButton("OK"){ dialogInterface: DialogInterface, i: Int ->
                if(flag) {
                    var intent = Intent(getApplicationContext(), MainActivity::class.java)
                    startActivity(intent);
                }

            }


        var alertDialog: AlertDialog = build.create()
        alertDialog.show()

    }

    private fun updateParametrs(namen: String,  email:String)
    {
        var nameAndSurname = namen.split("#")
        textView.text = "${email}\n${nameAndSurname[0]}\n${nameAndSurname[1]}"
    }

}
