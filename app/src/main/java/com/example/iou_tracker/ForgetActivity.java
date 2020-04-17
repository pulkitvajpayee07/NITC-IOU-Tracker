package com.example.iou_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {
    EditText etEmail;
    Button btReset;

    FirebaseAuth firebaseAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        etEmail = findViewById(R.id.editText_email);
        btReset = findViewById(R.id.button_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(etEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetActivity.this, "password reset link has been sent to your email", Toast.LENGTH_SHORT).show();
                                    Intent ToLogin = new Intent(ForgetActivity.this, MainActivity.class);
                                    startActivity(ToLogin);
                                } else {
                                    Toast.makeText(ForgetActivity
                                            .this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
}