package com.example.messonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//Chuyen doi Activity kem theo du lieu
public class RegisterActivity extends AppCompatActivity {
    EditText mEmailEdt,mPasswordEdt;
    Button mRegisterBtn;
    ProgressDialog pd;
    private FirebaseAuth mauth;
    TextView mHaveAccountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tạo tài khoản");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Anh xa
        mEmailEdt = findViewById(R.id.email_edt);
        mPasswordEdt = findViewById(R.id.password_edt);
        mRegisterBtn = findViewById(R.id.register_btn_register);
        mHaveAccountTV = findViewById(R.id.have_accountTv);

        //firebase
        mauth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setMessage("Đăng đăng ký đợi xí");
        //Click xu ly su kien, truyen vao mot doi tuong
        mHaveAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tao email
                String email = mEmailEdt.getText().toString().trim();
                String password = mPasswordEdt.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailEdt.setError("Email vừa nhập sai định dạng!");
                    mEmailEdt.setFocusable(true);
                }
                else if (password.length()<6){
                    mPasswordEdt.setError("Mật khẩu hơn 6 ký tự!");
                    mPasswordEdt.setFocusable(true);
                }
                else {
                    //tao email, password tu tham so o tren
                    registerUser(email,password);
                }

            }
        });
    }

    private void registerUser(String email, String password) {
        pd.show();
        mauth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            FirebaseUser user = mauth.getCurrentUser();

                            String email = user.getEmail();
                            String uid   = user.getUid();

                            //Doi tuong luu gia tri theo cap theo key/value va truy cap theo key, khong theo tu tu nhu Arraylist
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("onlineStatus","online");
                            hashMap.put("typingTo","noOne");
                            hashMap.put("commit","");
                            hashMap.put("image","");
                            hashMap.put("cover","");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);



                            Toast.makeText(RegisterActivity.this, "Đợi xí đang vào... \n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            //neu ma sai thi no toast tra ve thoi ;v
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Tài khoản đã có người dùng", Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                //Tren ten tai khoang email
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}