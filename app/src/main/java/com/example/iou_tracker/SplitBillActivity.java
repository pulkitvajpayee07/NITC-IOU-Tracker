package com.example.iou_tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaDrm;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.opencensus.tags.Tag;

public class SplitBillActivity extends AppCompatActivity {

    ArrayList<String> selectedItems = new ArrayList<>();
    ListView listView;
    Button btSplit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> nameList = new ArrayList();
    EditText amount;
    double amount1;
    String gName;
    CollectionReference billRef = db.collection("Bill");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);

        Bundle bundle = getIntent().getExtras();
        gName = bundle.getString("gName");
        Toast.makeText(SplitBillActivity.this,gName,Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.listPerson);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        amount = findViewById(R.id.editText7);
        btSplit = findViewById(R.id.button12);

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

        btSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = amount.getText().toString();
                if(s.trim().equals("")){
                    Toast.makeText(SplitBillActivity.this,"Yeha aaya ",Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        amount1 = Double.parseDouble(s);
                    } catch (Exception e) {
                        Toast.makeText(SplitBillActivity.this, "Yeha pr aaya toh hai ", Toast.LENGTH_SHORT).show();
                    }

                }
                Toast.makeText(SplitBillActivity.this,"Amount ="+amount.getText().toString(),Toast.LENGTH_SHORT).show();

                splitBill();
            }
        });

    }
    public void splitBill(){
        final HashMap<String , Double> map = new HashMap<>();
        for(int i =0;i<selectedItems.size();i++){
            map.put(selectedItems.get(i),amount1/selectedItems.size());
        }
//        db.collection("Bill").whereEqualTo("billNo",gName)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if(!queryDocumentSnapshots.isEmpty()) {
////                            HashMap<String, Double> map1 = new HashMap<>();
////                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
////                                map1 = (HashMap<String, Double>) snapshot.get("listOfPerson");
////                            }
//
//
//
//
//                            db.collection("Bill").document(gName).update("listOfPerson",map);
//                            db.collection("Bill").document(gName).update("Amount",amount1);
//                            Intent toBack = new Intent(SplitBillActivity.this,HomeActivity.class);
//                            startActivity(toBack);
//                        }
//                        else{
                            Bill bill = new Bill(gName,map,amount1);
                            System.out.println(bill.billNo);
                            billRef.document(gName).set(bill);
                            Intent toBack = new Intent(SplitBillActivity.this,HomeActivity.class);
                            startActivity(toBack);
//                        }
//                    }
//                });


    }
}
