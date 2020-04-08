package com.example.iou_tracker;

import androidx.appcompat.app.AppCompatActivity;

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

public class SplitBillActivity extends AppCompatActivity {

    ArrayList<String> selectedItems = new ArrayList<>();
    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> nameList = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);

        Bundle bundle = getIntent().getExtras();
        String gName = bundle.getString("gName");
        Toast.makeText(SplitBillActivity.this,gName,Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.listPerson);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        db.collection("Group").whereEqualTo("gName",gName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots,  FirebaseFirestoreException e) {
                nameList.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){

                    nameList= (ArrayList<String>)snapshot.get("names");
                }
                String[] names = new String[nameList.size()];
                for(int j =0;j<nameList.size();j++){
                    names[j] = nameList.get(j);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SplitBillActivity.this,R.layout.row_layout, R.id.txt_lan,names);
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

    }
}
