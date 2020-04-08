package com.example.iou_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupActivity extends AppCompatActivity {


    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> groupList = new ArrayList();
    String []names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        listView = findViewById(R.id.listG);
        listView.setChoiceMode(ListView.FOCUSABLES_TOUCH_MODE);

         db.collection("Group").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots,  FirebaseFirestoreException e) {
                groupList.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){

                    groupList.add(snapshot.getString("gName"));
                }
                names = new String[groupList.size()];
                for(int j =0;j <groupList.size();j++){
                    names[j] = groupList.get(j);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_selectable_list_item,groupList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent toSplitBill = new Intent(SelectGroupActivity.this,SplitBillActivity.class);

                Bundle bundle = new Bundle();

                bundle.putString("gName",names[position]);
                toSplitBill.putExtras(bundle);

                startActivity(toSplitBill);

            }
        });

    }
}
