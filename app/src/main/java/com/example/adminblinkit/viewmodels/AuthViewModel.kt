package com.example.adminblinkit.viewmodels

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adminblinkit.models.Admin
import com.example.adminblinkit.models.Users
import com.example.adminblinkit.utils.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel() : ViewModel() {

    private val auth : FirebaseAuth=FirebaseAuth.getInstance()

    private var _verficationId = MutableStateFlow<String?>(null)
    private var _otpSent = MutableStateFlow(false)
    var otpSend = _otpSent

    private var _isSignInSuccessfully = MutableStateFlow(false)
    val isSignInSuccessfully = _isSignInSuccessfully

    private var _isPasswordReset = MutableLiveData(false)
    var isPasswordReset = _isPasswordReset

    private var _isCurrentUser = MutableStateFlow(false)
    var isCurrentUser = _isCurrentUser

    init {
        if (Utils.getAuthInstance().currentUser != null){
            isCurrentUser.value = true
        }
//        Utils.getAuthInstance().currentUser?.let {
//            isCurrentUser.value = true
//        }

    }

    // For Phone Authentication===========================
    fun sendOTP(userNumber: String, activity: Activity) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
                // signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(ContentValues.TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(ContentValues.TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                _verficationId.value = verificationId
                _otpSent.value = true
            }
        }


        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber(userNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    fun signInWithPhoneAuthCredential(otp: String, userNumber: String, users: Admin) {
        val credential = PhoneAuthProvider.getCredential(_verficationId.value.toString(), otp)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            val token = task.result
            users.adminToken = token

            Utils.getAuthInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        users.uid = Utils.currentUser()
                        FirebaseDatabase.getInstance().getReference("Admins")
                            .child(Utils.currentUser()!!).child("AdminInfo").setValue(users)
                        _isSignInSuccessfully.value = true

                    }
                }
        }
    }


    // For Email Authentication===========================



    suspend fun createUserWithEmail(email: String, password: String, users: Users) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            users.adminToken=it.result

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        users.uid=Utils.currentUser()
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseDatabase.getInstance().getReference("Admins")
                            .child(Utils.currentUser()!!).child("AdminInfo").setValue(users)
                        _isSignInSuccessfully.value = true
                        Log.d("GGG", "createUserWithEmail:${users.uid}")

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", it.exception)

                    }
                }
        }
    }


    fun signInWithEmail(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    _isSignInSuccessfully.value = true
                    Log.d(ContentValues.TAG, "signInWithEmail:success")


                } else {
                    _isSignInSuccessfully.value = false
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)

                }
            }


    }


    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    // You can handle success or perform additional actions here
                    _isPasswordReset.value = true
                } else {
                    // Password reset email sending failed
                    // You can handle failure or display an error message to the user
                }
            }
    }

}





