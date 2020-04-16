package com.example.iou_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public  class UpdateActivity extends AppCompatActivity  {
    private EditText pass, name, mobile ;
    Button updatebtn;
    private FirebaseFirestore db ;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;
    String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        db = FirebaseFirestore.getInstance();
        pass = findViewById(R.id.editText_password);
        name = findViewById(R.id.editText_name);
        mobile = findViewById(R.id.editText_mobile);
        updatebtn = findViewById(R.id.button_update);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name.getText().toString().trim();
                String newPassword = pass.getText().toString().trim();
                String newMobile = mobile.getText().toString().trim();


                if (newName.isEmpty()) {

                }
                if (newPassword.isEmpty()) {

                }
                if (newMobile.isEmpty()) {

                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                userId = user.getUid();
                if (user != null) {
                    Log.d("OnSuccess", "User profile is updated" + userId);
                    final DocumentReference documentReference = db.collection("Users").document(userId);
                    Map<String, Object> updatemap = new HashMap<>();
                    updatemap.put("FName", newName);
                    updatemap.put("Contact", newMobile);
                    documentReference.update(updatemap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "OnSuccess: User profile is updated");
                                    Toast.makeText(UpdateActivity.this,"your profile is updated ",Toast.LENGTH_SHORT).show();
                                    Intent ToHome = new Intent(UpdateActivity.this, HomeActivity.class);
                                    startActivity(ToHome);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "OnFailure: ", e);
                                }
                            });

                } else {
                    // No user is signed in
                }
            }
        });


                //mFirebaseAuth = FirebaseAuth.getInstance();
                //userId = mFirebaseAuth.getCurrentUser().getUid();



}

}
