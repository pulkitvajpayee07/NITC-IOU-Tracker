package com.example.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class checkBalance extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String gName;
    Button btReminder,btBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);

        TextView groupName = findViewById(R.id.textView6);
        ListView listView = findViewById(R.id.listBalance);
        btReminder = findViewById(R.id.button15);
        btBack = findViewById(R.id.button20);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            gName = bundle.getString("gName");
            groupName.setText(gName);
        }



        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(checkBalance.this,HomeActivity.class));
            }
        });
        db.collection("Bill").whereEqualTo("billNo",gName).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        HashMap<String, Double> map = new HashMap<>();
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                map = (HashMap<String, Double>) snapshot.get("listOfPerson");
                            }


                            ArrayList<String> balance = new ArrayList<>();
                            if (map != null) {
                                for (Map.Entry<String, Double> entry : map.entrySet()) {
                                    balance.add(entry.getKey() + ":     " + entry.getValue());
                                }
                            } else {
                                balance.add("No Bill Split yet.");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, balance);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }

                    }
                });
        btReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toReminder = new Intent(checkBalance.this,Reminder.class);
                startActivity(toReminder);
                finish();
            }
        });
    }
}
