package com.example.mobileapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapplication.databinding.ActivitySignBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class SignActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignBinding

    private val RC_SIGN_IN = 9001 // 구글 로그인 요청 코드

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signSubmitBtn.setOnClickListener {
            val email = binding.signIdInput.text.toString()
            val password = binding.signPassInput1.text.toString()

            // 여기에 회원가입 로직을 구현하세요
            // 예시: Firebase Authentication의 createUserWithEmailAndPassword 메서드를 사용하여 회원가입을 수행합니다
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 회원가입 성공 시, 다음 화면으로 이동합니다
                        navigateToNextScreen()
                    } else {
                        // 회원가입 실패 시, 오류 메시지를 표시합니다
                        Toast.makeText(
                            this@SignActivity,
                            "회원가입에 실패했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // 구글 회원가입 버튼 클릭 시
        binding.signGoogle.setOnClickListener {
            googleSignUp()
        }

        binding.signTwitter.setOnClickListener {
            // 여기에 Twitter 회원가입 로직을 구현하세요
            // 예시: TwitterAuthClient를 사용하여 Twitter 회원가입 플로우를 시작합니다
            // 성공적인 회원가입 후 다음 화면으로 이동할 수 있습니다
        }

        binding.signFacebook.setOnClickListener {
            // 여기에 Facebook 회원가입 로직을 구현하세요
            // 예시: Facebook Login SDK를 사용하여 Facebook 회원가입 플로우를 시작합니다
            // 성공적인 회원가입 후 다음 화면으로 이동할 수 있습니다
        }
    }

    // 구글 회원가입 플로우 시작
    private fun googleSignUp() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // onActivityResult 메서드 오버라이드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 구글 로그인 요청 코드와 결과 코드가 일치하는 경우
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // 구글 로그인에 성공한 경우
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                // 구글 로그인에 실패한 경우
                Log.d("mobileApp", "GoogleSignIn - ${e.message}")
            }
        }
    }

    // Firebase에 구글 인증 정보로 회원가입 및 로그인
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 회원가입 또는 로그인 성공
                    Log.d("mobileApp", "GoogleSignIn - Successful")
                    finish()
                } else {
                    // 회원가입 또는 로그인 실패
                    Log.d("mobileApp", "GoogleSignIn - Not Successful")
                }
            }
    }

    private fun navigateToNextScreen() {
        // 다음 화면으로 이동합니다 (예: HomeActivity)
        val intent = Intent(this, FirstActivity::class.java)
        startActivity(intent)
        finish() // 선택적으로 SignActivity를 종료할 수 있으며, 사용자가 다시 이전 화면으로 돌아갈 수 없습니다
    }
}
