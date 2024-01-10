package com.example.project_note.ViewModal

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_note.DataBase.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

class AuthViewModel : ViewModel() {

    private val _userResponse = MutableLiveData<User>()
    public val userResponse: LiveData<User> get() = _userResponse
    private val _checkreplacepass = MutableLiveData<Boolean>()
    public val checkreplacepass: LiveData<Boolean> get() = _checkreplacepass

    private var mProgressDialog: ProgressDialog? = null

    public fun updateUri(uri : Uri){
        _userResponse.value?.image=uri.toString()

    }

fun getInformationUser(context: Context){
    mProgressDialog = ProgressDialog(context)
    mProgressDialog!!.show()

    val user = Firebase.auth.currentUser
    user?.let {
        val name = it.displayName
        val email = it.email
        val photoUrl = it.photoUrl
        _userResponse.value= User(email.toString(),"", name.toString(),photoUrl.toString())
    }
    mProgressDialog!!.dismiss()

}
    fun updateInformationUser(context: Context,name:String){

        _userResponse.value?.name = name
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = _userResponse.value?.name
            photoUri = Uri.parse(_userResponse.value?.image)
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                Toast.makeText(context, "Update thành công", Toast.LENGTH_SHORT).show()
            }
    }
    fun checkValidEmail(context: Context, email: String): Boolean {
        if (email.isBlank()) {
            Toast.makeText(context, "Email is not blank", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, " Email không hợp lệ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun checkValidPassword(context: Context, pass: String): Boolean {
        if (pass.length < 8) {
            Toast.makeText(context, " Mật khẩu có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pass.isBlank()) {
            Toast.makeText(context, "Pass is not blank", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun checkValidRePassword(context: Context, oldpass: String, repass: String): Boolean {
        if (!oldpass.equals(repass)) {
            Toast.makeText(context, " Pass và Comfirm Pass không trùng khớp", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

        fun loginRequest(context: Context, email: String, pass: String){
            mProgressDialog = ProgressDialog(context)
            mProgressDialog!!.show()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val verification = FirebaseAuth.getInstance().currentUser?.isEmailVerified
                        if (verification == true) {
                            _userResponse.value = User(email, pass)
                            mProgressDialog!!.dismiss()
                        } else
                            showAlertDialogVerify(context, email)
                    } else {
                        Toast.makeText(context, "Login Fall: $it", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    fun register(context: Context, email: String, pass: String){
        Firebase.auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Firebase.auth.currentUser?.sendEmailVerification()
                    ?.addOnCompleteListener {
                        showAlertDialogVerify(context,email)
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Failed: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun sendPassordReset(context: Context,email: String){
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _checkreplacepass.value = true
                }
                else{
                    _checkreplacepass.value = false
                }
            }
    }
    private fun showAlertDialogVerify(context: Context,email: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("THÔNG BÁO")
            .setMessage("Vui lòng truy cập email: " + email + " để xác thực tài khoản . Xin cảm ơn!")
            .setPositiveButton("Đồng ý"){dialog,which->
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }
    }