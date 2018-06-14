package com.comma.cw01272.reservation.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.comma.cw01272.reservation.R;
import com.comma.cw01272.reservation.request.ComRegistRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCompany extends AppCompatActivity implements View.OnClickListener {

    // 여기는 화면에서 사용되는 UI들을 가져와여
    private ImageView com_photo;    // 회사 이미지 UI
    private EditText iptName;       // 게시물 제목? 입력 UI
    private EditText iptInfo;       // 게시물 내용? 입력 UI
    private EditText iptVaildNum;  // 가능 인원 입력 UI
    private EditText iptTotal;      // 총 인원 입력 UI
    private String[] strImageServerPath = new String[1];

    private Button btnUpload;       // 게시물 업로드 UI

    private FloatingActionButton btnTakePhoto; // 회사 이미지 가져오기 UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        //여기서 부터 해당 UI 화면에서 가져오기
        com_photo = (ImageView) findViewById(R.id.com_photo);

        iptName = (EditText) findViewById(R.id.iptName);
        iptInfo = (EditText) findViewById(R.id.iptInfo);
        iptTotal = (EditText) findViewById(R.id.iptTotal);
        iptVaildNum = (EditText) findViewById(R.id.iptVaildNum);

        btnTakePhoto = (FloatingActionButton) findViewById(R.id.btnTakePhoto);
        btnUpload = (Button) findViewById(R.id.btnUpload);

        // 버튼 클릭 처리 이벤트 이 액티비티로 등록
        btnTakePhoto.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        //이 액티비티는 사진떄문에 권한 처리가 필요해오... 젤 아래에 권한 처리코드가 있어오
        checkPermission();
    }


    // 이 액티비치가 클릭 이벤트로 등록하니 이 메소드를
    // 오버라이딩해서 여기서 클릭 내용 처리해야지
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 사진 가져오기 버튼 눌럿을 때 처리할 내용
            case R.id.btnTakePhoto : {
                // 이제 사용자에게 이 다이어로그 띄워 어떤 사진 가져올지 물어봅시다.
                ShowTakePhotoDialog();
            }break;

            // 게시물 업로드 버튼을 눌럿을 때 처리할 내용
            case R.id.btnUpload : {
                //업로드 프로세스는 그냥 여기다줄줄히 박을 꼐요
                String valName = iptName.getText().toString();
                String valInfo = iptInfo.getText().toString();
                Integer valValidNum = Integer.valueOf(iptVaildNum.getText().toString()) ;
                Integer valTotal = Integer.valueOf(iptTotal.getText().toString());

                String notice = "";
                if(valName.equals("")) notice += "제목 ";
                if(valInfo.equals("")) notice += "내용 ";
                if(valValidNum == null) notice += "가능인원 ";
                if(valTotal == null) notice += "총인원 ";

                if(!notice.equals("")){
                    Toast.makeText(this,notice + "필드를 올바르게 입력해주세요",Toast.LENGTH_LONG).show();
                    break;
                }

                if(strImageServerPath[0] != null){
                    final android.app.AlertDialog prgressDialog =
                            new ProgressDialog.Builder(this)
                                    .setTitle("글 게시중")
                                    .setCancelable(false)
                                    .show();

                    ComRegistRequest comRegistRequest = new ComRegistRequest(valName, valInfo, valValidNum, valTotal, strImageServerPath[0], new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            prgressDialog.dismiss();
                            Log.d("responded",response);
                            String a = response.split("\\{")[1].split("\\}")[0];
                            Log.d("count : ",a);
                            response = "{" + a + "}";
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success){
                                    finish();
                                }
                                else {
                                    Toast.makeText(AddCompany.this,"실패!",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(AddCompany.this,"실패!",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(comRegistRequest);
                } else {
                    Toast.makeText(this,"이미지가 선택되지 않았거나, 업로드를 하지 못하였습니다.",Toast.LENGTH_LONG).show();
                }

            }break;
        }
    }

    // 사진을 어떻게든 가져왔을 때 이 메소드가 호출 되요.
    // 저 세상 밑에 복잡한 사진 처리 코드에서 알아서 처리해서 여기서 넘겨주는거애오
    private void onTakePhoto(Uri takedImageUri){
        //일단 업로드할 이미지가 선택됬다면 부랴 부랴 일단 이미지를 보여줘요
        com_photo.setImageURI(takedImageUri);
        com_photo.setColorFilter(Color.GRAY);
        strImageServerPath[0] = null;

        //게시물 업로드 버튼과 달리 이미지 파일은 미리미리 업로드 해줘요
        final String serversFileName = new Date().getTime() + "-" + ((int) (Math.random() * 1000)); //서버측 이미지 파일 이름을 여기서 정해줘요
        FirebaseStorage.getInstance().getReference("com_image" + serversFileName + ".jpg")
                .putFile(takedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(!task.isSuccessful()){
                    strImageServerPath[0] = null;
                    String message = task.getException().getMessage();
                    Toast.makeText(AddCompany.this, "이미지 업로드 실패하였습니다.\n" + message, Toast.LENGTH_SHORT).show();
                }else{
                    //이미지 업로드 됬고 URL 받아오기
                    FirebaseStorage.getInstance().getReference("com_image" + serversFileName + ".jpg")
                            .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(!task.isSuccessful()){
                                strImageServerPath[0] = null;
                                String message = task.getException().getMessage();
                                Toast.makeText(AddCompany.this, "이미지 업로드 실패하였습니다.\n" + message, Toast.LENGTH_SHORT).show();
                            }else{
                                strImageServerPath[0] = task.getResult().toString();
                                com_photo.setColorFilter(Color.TRANSPARENT);
                                Toast.makeText(AddCompany.this, "이미지 업로드 완료", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //사진 어디서 가져올까요? 물어보는 팝업창 띄워주는 메소드애오
    private void ShowTakePhotoDialog(){
        new AlertDialog.Builder(this) // 팝업창 빌드기.
                .setTitle("사진 가져오기") // 팝업창 제목
                .setMessage("사진을 어디서 가져올까요?") // 팝업창 내용
                .setCancelable(true) // 바깥 터치하면 그냥 바로 사라짐
                .setPositiveButton("갤러리", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 갤러리 버튼 눌렀을 때 처리할 내용
                        getAlbum(); // 이제 저 아래 세상에 처리 코드가 있어오
                    }
                })
                .setNeutralButton("사진 찍기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // 사진찍기 버튼 눌렀을 때 처리할 내용
                        captureCamera(); // 이제 저 아래 세상에 처리 코드가 있어오
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 취소 눌렀을 때 처리할 내용. 딱히 없죠?
                    }
                })
                .create() //이렇게 세팅한 내용으로 팝업창 빌드 해여
                .show(); // 빌드 완료 되면 이제 띄워요!
    }


    /*******************************************************************************************
    * 여기서 부터는 사진 불러오거나, 찍는 코드를 몰아놨어요. 복잡할 수도 있어요.*
     * 사진찍기나, 앨범가져오기, 사진 자르기는 앱 밖에서 처리하는 거라 다른 세상과 통신 할 필요가 있어요
    * */

    // 여기 다른 세상과 통신하기 위한 간한한 구별 신호를 정하는 거애오
    private static final int PERMISSION_REQ_CAMERA = 189;
    private static final int REQ_TAKE_PHOTO = 498;
    private static final int REQ_TAKE_ALBUM = 914;
    private static final int REQ_IMAGE_CROP = 991;

    //imageURI는 카메라로 찍어온 이미지를 잠시 저장할.
    //resultUIR는 크롭까지 준비된 이미지를 잠시 저장할
    private Uri imageURI, resultUIR;

    // 앨범에서 사진을 가져오기 위한 메소드애오
    private void getAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,REQ_TAKE_ALBUM); //위에서 정했던 통신 신호 여기서 쓰네요
    }

    // 카메라로 사진 찍어 가져오기 위한 메소드 애오
    private void captureCamera(){
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            //외장 메모리가 사용 가능시(사용가능)
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null){
                File photoFile = null;
                // 여기서 쓸수 있는 빈 파일 찾아와요.
                try { photoFile = createImageFile(); } catch (IOException e) { e.printStackTrace(); }
                //이미지 파일 만들기 성공했다면
                if(photoFile != null){
                    //다른 앱에 파일 공유하기 위한 프로바이더 생성
                    imageURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI); // 가져온 빈파일에 채워달라고 요청하는 거애오.
                    startActivityForResult(intent, REQ_TAKE_PHOTO); //위에서 정했던 통신 신호 여기서 쓰네요
                }
            }
            else {
                Toast.makeText(this,"저장 공간에 접근이 불가능합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    // 앨범이든, 카메라로 찍었든, 가져온 이미지를 자르기를 위한 메소드 애오
    private void cropImage(Uri targetImage){
        try {
            resultUIR = Uri.fromFile(createImageFile()); // 여기서 쓸수 있는 빈 파일 찾아와요.
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(targetImage,"image/*"); //아저씨 이 사진으로 잘라주세오
//        intent.putExtra("outputX",200);
//        intent.putExtra("outputY",200);
//        intent.putExtra("aspectX",1);
//        intent.putExtra("aspectY",1);
        intent.putExtra("scale",true);
        intent.putExtra("output",resultUIR); // 가져온 빈파일에 채워달라고 요청하는 거애오
        startActivityForResult(intent,REQ_IMAGE_CROP); //위에서 정했던 통신 신호 여기서 쓰네요
    }

    // 앨범은 이미 로컬에 저장되어 있으니 괜찮은데
    // 카메라로 찍은거나, Crop한 이미지는 외부 세상에 전달하기 위해
    // 임시적으로 로컬에 저장할 필요가 있어요.
    // 여기서 임시적으로 저장할수 있는 공간 찾아 빈파일을 만들어 줘요
    // 그럼 그 빈파일을 필요한곳에 줘서 그 빈파일에 내용을 채워 넣을 수 있게 하는 거죠
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures","RESERV_1");
        //해당 디렉토리가 없으면 생성
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        //파일 생성
        imageFile = new File(storageDir,imageFileName);

        return imageFile;
    }

    // 위에서 외부로 요청한 애들은 다 여기서 응답해줘요/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            // 아! 외부에서 사진 찍기 잘 처리해서 넘겨줬다고 하는군요!
            case REQ_TAKE_PHOTO:{
                if(resultCode == RESULT_OK){
                    // 이미 미리 준비해놓은 imageURI 변수에 잘 담아 줬을 꺼얘요!
                    cropImage(imageURI); // 그럼 바로 잘라달라고 다른 애한테 요청해줘요!
                }
            }break;
            // 아! 외부에서 사용자가 선택한 앨범 사진이 왔다고 하는군요!
            case REQ_TAKE_ALBUM:{
                if(resultCode == RESULT_OK){
                    if(data.getData() != null){
                        // 선택된 앨범 사진 파일은 data.getData() 담겨 있어요!
                        cropImage(data.getData()); // 그럼 바로 잘라달라고 다른 애한테 요청해줘요!
                    }
                }
            }break;

            //앗! 이제 이미지 자르기까지 모두 완료했다군요
           case REQ_IMAGE_CROP:{
                if(resultCode == RESULT_OK){
                    // 이미 미리 준비해놓은 resultURI 변수에 알아수 잘 담아줬을꺼예요
                    // 이제 위에서 결과적으로 다 처리된 이미지를 넘겨줍시다.
                    onTakePhoto(resultUIR);
                }
            }break;
        }
    }

    /************************여긴 권한 처리하는 코드가 담겨 있어오***************************/

    private void ShowNoticeRejectPermissionDialog(){
        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
    private void checkPermission(){
        if ( // 필요한 권한 있는지 검사를 해오
                ContextCompat.checkSelfPermission(this, // 이 어플이
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) //외부 저장소 권한을 가지고 있는지 물어봐요
                        != PackageManager.PERMISSION_GRANTED ||// 승인이 안되어 있오요??
                ContextCompat.checkSelfPermission(this, // 이 어플이
                        Manifest.permission.CAMERA) //외부 저장소 권한을 가지고 있는지 물어봐요
                        != PackageManager.PERMISSION_GRANTED // 승인이 안되어 있오요??
                ) {
            // 오 외부 저장소 승인이 안되어 있다네오
            if ( // 저번에 주인이 절대 안주겠다고 했던가요?
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) || //외부 저장소 접근 권한하고

                    (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.CAMERA)) // 카메라 사용 접근 권한하고
                    ) {
                        // 오맨, 그랬데요!!, 그러면 쓸꺼면 알아서 설정가서 세팅하라고 합시다.
                        ShowNoticeRejectPermissionDialog();
            } else {
                // 그런적은 없다고 하네요. 그럼 지금 달라고 합시다.
                ActivityCompat.requestPermissions(this,
                        new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, // 외부 저장소 접근과
                            android.Manifest.permission.CAMERA // 카메라 사용 접근 권한을
                        },
                        PERMISSION_REQ_CAMERA // 그리고 여기로 대답해줘요(신호코드)!! 위에 있어요!!
                );
            }
        }//필요한 권한 다 있다네오!
    }

    //여기다 사용자에게 권한 요청 응답이 여기서 처리를 해요
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            // 우리가 이쪽으로 응답해달라 신호가 왔군요!!
            case PERMISSION_REQ_CAMERA:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] < 0) {
                        Toast.makeText(this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }
}
