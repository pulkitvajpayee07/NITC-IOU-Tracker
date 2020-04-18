package com.example.iou_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import java.util.EventListener;

public class Reminder extends AppCompatActivity {

    //Declaring EditText
    private EditText editTextEmail;
    private EditText editTextMessage;
    Button btBack;

    //Send button
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //Initializing the views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        buttonSend = (Button) findViewById(R.id.buttonSend);

        btBack = findViewById(R.id.button18);

        //Adding click listener
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }

        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Reminder.this,HomeActivity.class));
            }
        });

    }

    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String subject = "For Refund Money";
        String message = editTextMessage.getText().toString().trim();
        if(email.isEmpty()){
            editTextEmail.setError("Please Enter Email id");
            editTextEmail.requestFocus();
        }else if(subject.isEmpty()){
            editTextMessage.setError("Please Enter Message");
            editTextMessage.requestFocus();
        }else {
            //Creating SendMail object
            SendMail sm = new SendMail(this, email, subject, message);

            //Executing sendmail to send email
            sm.execute();
        }
    }

//    @Override
//    public void onClick(View v) {
//        sendEmail();
//    }
}

