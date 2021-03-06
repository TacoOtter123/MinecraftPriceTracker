package com.mistershorr.intentslearning

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {

    companion object {
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // test to see if the username came across

        // to "check the mail", we get the Intent that started this Activity by just using intent
        // "open the mail" by getting the extra from that intent
        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)
        // toast needs  activity it's in, message, length to display
        editText_registration_username.setText(username)

        button_registration_cancel.setOnClickListener {
            finish()
        }

        button_registration_create.setOnClickListener {
            when {
                editText_registration_username.text.toString().length < 3 -> {
                    Toast.makeText(this, "username too short", Toast.LENGTH_SHORT).show()
                }
                editText_registration_password.text.toString() != editText_registration_confirm_password.text.toString() -> {
                    Toast.makeText(this, "passwords don't match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    registerUser()
                }
            }
        }
    }
    fun registerUser() {
        val user = BackendlessUser()
        user.setProperty("email", editText_registration_email.text.toString())
        user.setProperty("name", editText_registration_name.text.toString())
        user.setProperty("username", editText_registration_username.text.toString())
        user.password = editText_registration_password.text.toString()

        Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(registeredUser: BackendlessUser?) {
                val intent = Intent().apply {
                    putExtra(EXTRA_USERNAME, editText_registration_username.text.toString())
                    putExtra(EXTRA_PASSWORD, editText_registration_password.text.toString())
                }
                setResult(RESULT_OK, intent)
                finish()
            }

            override fun handleFault(fault: BackendlessFault) {
                Toast.makeText(this@RegistrationActivity, fault.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
}