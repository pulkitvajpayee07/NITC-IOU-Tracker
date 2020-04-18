package com.example.iou_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout,btBillSplit,btCreateGroup,btCheckBalance,btPaidTo,btUpdateProfile;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnLogout = findViewById(R.id.button2);
        btBillSplit = findViewById(R.id.button3);
        btCreateGroup = findViewById(R.id.button4);
        btPaidTo = findViewById(R.id.button6);
        btCheckBalance = findViewById(R.id.button5);
        btUpdateProfile = findViewById(R.id.button7);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intToMain = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intToMain);
            }
        });

        btBillSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToSpilt = new Intent(HomeActivity.this, HaveGroupActivity.class);
                startActivity(ToSpilt);
                finish();
            }
        });

        btCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToCreateGroup = new Intent(HomeActivity.this,CreateGroupActivity.class);
                startActivity(ToCreateGroup);
                finish();
            }
        });
        btPaidTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSelecetGroup = new Intent(HomeActivity.this,SelectGroupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Activity","paidTo");
                toSelecetGroup.putExtras(bundle);
                startActivity(toSelecetGroup);
                finish();
            }
        });
        btCheckBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSelectGroup = new Intent(HomeActivity.this,SelectGroupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Activity","checkBalance");
                toSelectGroup.putExtras(bundle);
                startActivity(toSelectGroup);
                finish();
            }
        });
        btUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUpdate = new Intent(HomeActivity.this,Update_Profile.class);
                startActivity(toUpdate);
                finish();
            }
        });
    }
}
