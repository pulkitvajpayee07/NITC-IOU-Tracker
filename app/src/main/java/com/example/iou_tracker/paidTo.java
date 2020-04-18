package com.example.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class paidTo extends AppCompatActivity {

    String gName="";
    Button btCheckBalance,btPaid;
    EditText name,amount;
    int flag=0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference billRef = db.collection("Bill");
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_to);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            gName = bundle.getString("gName");
        btCheckBalance = findViewById(R.id.button14);
        btPaid = findViewById(R.id.button13);
        btCheckBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toCheckBalance = new Intent(paidTo.this,checkBalance.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("gName",gName);
                toCheckBalance.putExtras(bundle1);
                startActivity(toCheckBalance);
            }
        });
        btPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = findViewById(R.id.editText6);
                amount = findViewById(R.id.editText8);
                paid();
            }
        });
    }
    void paid(){
        flag = 0;
        String email="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            email = user.getEmail();
        }
        db.collection("Users").whereEqualTo("Email",email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if(!queryDocumentSnapshots.isEmpty())
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                            userName = (String) snapshot.get("FName");
                        }
                        db.collection("Bill").whereEqualTo("billNo",gName)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (!queryDocumentSnapshots.isEmpty() && flag ==0) {
                                            flag = 1;
                                            HashMap<String, Double> map1 = new HashMap<>();
                                            Double amount2=0.0,amount1;
                                            if(amount.getText().toString().isEmpty()){
                                                amount.setError("Please enter Amount");
                                                amount.requestFocus();
                                            }
                                            else if( name.getText().toString().trim().isEmpty())
                                            {
                                                name.setError("Please enter Name");
                                                name.requestFocus();
                                            }else {
                                                amount1 = Double.parseDouble(amount.getText().toString());
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                        map1 = (HashMap<String, Double>) snapshot.get("listOfPerson");
                                                        amount2 = (Double) snapshot.get("amount");
                                                    }
                                                }
                                                Set<String> key = new HashSet<>();
                                                key = map1.keySet();

                                                for (String ele : key) {
                                                    Double d = map1.get(ele);
                                                    Double d1 = 0.0;
                                                    if (ele.compareTo(name.getText().toString()) == 0) {
                                                        if (d != null) {
                                                            d1 = d + amount1;
                                                            map1.replace(ele, d1);
                                                        }
                                                    } else if (ele.compareTo(userName) == 0) {
                                                        if (d != null) {
                                                            d1 = d - amount1;
                                                            map1.replace(ele, d1);
                                                        }
                                                    }
                                                }
                                                Bill bill = new Bill(gName, map1, amount2 - amount1);
                                                billRef.document(gName).set(bill);
                                                Intent toBack = new Intent(paidTo.this, HomeActivity.class);
                                                startActivity(toBack);
                                                finish();
                                            }
                                        }
                                    }
                                });
                    }
            });
    }
}
