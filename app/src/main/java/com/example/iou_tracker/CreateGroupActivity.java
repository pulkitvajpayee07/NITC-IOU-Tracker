package com.example.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {


    ArrayList<String> selectedItems = new ArrayList<>();
    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btShowName;
    EditText gName;
    CollectionReference groupRef = db.collection("Group");

    List<String> nameList = new ArrayList();
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        gName = findViewById(R.id.editText5);
        btShowName = findViewById(R.id.button11);
        listView = findViewById(R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots,  FirebaseFirestoreException e) {
                nameList.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){

                    nameList.add(snapshot.getString("FName"));
                }
                String names[] = new String[nameList.size()];
                for(int j =0;j<nameList.size();j++){
                    names[j] = nameList.get(j);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateGroupActivity.this,R.layout.row_layout, R.id.txt_lan,names);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedItem =((TextView)view).getText().toString();
                        if(selectedItems.contains(selectedItem)){
                            selectedItems.remove(selectedItem);
                        }
                        else
                            selectedItems.add(selectedItem);
                    }
                });
            }
        });

        btShowName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroup();
            }
        });

    }
    public void addGroup(){
        Group group = new Group(gName.getText().toString(),selectedItems.size(),selectedItems);
        groupRef.add(group);
        Intent toBack = new Intent(CreateGroupActivity.this,HomeActivity.class);
        startActivity(toBack);
    }

}
