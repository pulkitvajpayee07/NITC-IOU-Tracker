package com.example.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

public class Delete_Group extends AppCompatActivity {

    String gName, userId, userName, createdBy="";
    private FirebaseFirestore db ;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__group);

        db = FirebaseFirestore.getInstance();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            gName = bundle.getString("gName");
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        db.collection("Users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    userName = (String)documentSnapshot.get("FName");
                }
                db.collection("Group").document(gName).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot != null){
                            createdBy = (String)documentSnapshot.get("createdBy");

                            if(createdBy != null && userName.compareTo(createdBy) == 0){

                                AlertDialog.Builder builder = new AlertDialog.Builder(Delete_Group.this);
                                builder.setTitle("Delete Group");
                                builder.setMessage("Are you sure to Delete?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.collection("Bill").document(gName).delete();
                                        db.collection("Group").document(gName).delete();
                                        Toast.makeText(getApplicationContext(), "Group Deleted successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Delete_Group.this,HomeActivity.class));
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "You've changed your mind to delete Group", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Delete_Group.this,HomeActivity.class));
                                    }
                                });

                                builder.show();
                                flag = 1;
                            }
                            else{
                                Toast.makeText(Delete_Group.this,"You cannot Delete this Group",Toast.LENGTH_LONG).show();

                            }
                        }
                        if(flag == 0){
                            Toast.makeText(Delete_Group.this,"You cannot Delete this Group",Toast.LENGTH_LONG).show();

                        }

                    }

                });
            }
        });

    }
}
