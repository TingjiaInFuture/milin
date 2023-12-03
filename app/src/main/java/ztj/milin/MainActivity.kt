package ztj.milin


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseUser
import ztj.milin.ui.theme.MilinTheme


class MainActivity : ComponentActivity() {

    var user = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Initialize Firebase Auth(deleted)

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        actionBar?.hide()
        createAccount("2287315312@qq.com","11vda89")
        setContent {
//                    val intent = Intent(this, FullscreenActivity::class.java)
//        startActivity(intent)
            MilinTheme {
                // 创建登录状态变量
//                val currentUser = auth.currentUser
//                if (currentUser!=null)
//                    Main(user)
//                else {
//                    // 如果未登录，重定向到登录界面
//                    Login(user){ username ->
//                        user = username}
//                    createAccount("2287315312@qq.com","1fssaA89")

//                }
            Main(user)
            }
        }
    }


    private fun updateUI(user: FirebaseUser?) {

    }
    companion object {
        private const val TAG = "EmailPassword"
    }

fun createAccount(email: String, password: String) {
    // [START create_user_with_email]
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
                updateUI(null)
            }
        }
    // [END create_user_with_email]
}
}