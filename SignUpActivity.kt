package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.firebaseauth.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBar()

        binding.btnSignUp.setOnClickListener{
            val email = binding.etEmailSignUp.text.toString().trim()
            val pass = binding.etPasswordSignUp.text.toString().trim()
            val confirmPass = binding.etConfirmPasswordSignUp.text.toString().trim()


            CustomDialog.showLoading(this)
            if (checkValidation(email, pass, confirmPass)){
                registerToServer(email, pass)
            }
        }

        binding.tbSignUp.setNavigationOnClickListener{
            finish()
        }
    }

    private fun registerToServer(email: String, pass: String){
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener{ task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener{
                CustomDialog.hideLoading()
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkValidation(email: String, pass: String, confirmPass: String): Boolean {
        if (email.isEmpty()){
            binding.etEmailSignUp.error = "Please field your email"
            binding.etEmailSignUp.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmailSignUp.error = "Please use valid email"
            binding.etEmailSignUp.requestFocus()
        }else if (pass.isEmpty()){
            binding.etPasswordSignUp.error ="Please field your password"
            binding.etPasswordSignUp.requestFocus()
        }else if (confirmPass.isEmpty()){
            binding.etConfirmPasswordSignUp.error ="Please field your Confirm Password "
            binding.etConfirmPasswordSignUp.requestFocus()
        }else if (pass != confirmPass){
            binding.etPasswordSignUp.error = "Your Password didn't match"
            binding.etConfirmPasswordSignUp.error  = "Your confirm pass didn't match"

            binding.etPasswordSignUp.requestFocus()
            binding.etConfirmPasswordSignUp.requestFocus()
        }else{
            binding.etPasswordSignUp.error = null
            binding.etConfirmPasswordSignUp.error = null
            return true
        }
        CustomDialog.hideLoading()
        return false
    }

    private fun initActionBar() {
        setSupportActionBar(findViewById(R.id.tbSignUp))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}
