package com.comma.cw01272.reservation.activity;

import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.comma.cw01272.reservation.R;
import com.google.firebase.storage.FirebaseStorage;

public class AddCompany extends AppCompatActivity implements View.OnClickListener {

    private ImageView com_photo;

    private EditText iptName;
    private EditText iptInfo;
    private EditText iptVaildNum;
    private EditText iptTotal;

    private Button btnUpload;
    private FloatingActionButton btnTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        com_photo = (ImageView) findViewById(R.id.com_photo);

        iptName = (EditText) findViewById(R.id.iptName);
        iptInfo = (EditText) findViewById(R.id.iptInfo);
        iptTotal = (EditText) findViewById(R.id.iptTotal);
        iptVaildNum = (EditText) findViewById(R.id.iptVaildNum);

        btnTakePhoto = (FloatingActionButton) findViewById(R.id.btnTakePhoto);
        btnUpload = (Button) findViewById(R.id.btnUpload);

        btnTakePhoto.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTakePhoto : {

            }break;
            case R.id.btnUpload : {
                FirebaseStorage.getInstance().getReference("com_photo")

            }break;
        }
    }
}
