package dupol.dupol;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dupol.dupol.Activity.SlidingUserActivity;
import dupol.dupol.fragment.ProfilFragment;
import dupol.dupol.model.UserModel;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 23 ;
    private boolean sentToSettings = false;
    private Button buttonlogout, buttonTambah, buttonChoose,buttonTampil;
    private ImageView imageViewchoose;
    private EditText editTextName, editTextAddress, editTexttanggal, editTextNohp;
    private Spinner spinnerGender;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private Uri filePath, PathUrl, fileUri;
    private String filePat = null;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 3;
    private static final int REQUEST_PERMISSION_SETTING = 4;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 5;
    private SharedPreferences permissionStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        storageReference = FirebaseStorage.getInstance().getReference();


        editTextAddress = (EditText) findViewById(R.id.editTeksAlamat);
        editTextName = (EditText) findViewById(R.id.editTextnama);
        editTexttanggal =(EditText)findViewById(R.id.editTeksTanggal);
        editTextNohp =(EditText) findViewById(R.id.editTeksNoHp);
        spinnerGender =(Spinner) findViewById(R.id.spinnerGender);
        imageViewchoose =(ImageView) findViewById(R.id.imageViewChoose);
        buttonTampil =(Button) findViewById(R.id.buttonTakePicture);

         buttonChoose =(Button) findViewById(R.id.buttonChooseFoto);
//         buttonlogout = (Button) findViewById(R.id.buttonlogout);
        buttonTambah =(Button) findViewById(R.id.Savedata);
//        buttonTampil =(Button) findViewById(R.id.buttonTampilData);

        progressDialog = new ProgressDialog(this);

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
//         FirebaseUser user = firebaseAuth.getCurrentUser();

//        buttonlogout.setOnClickListener(this);
        buttonTambah.setOnClickListener(this);
        buttonChoose.setOnClickListener(this);
        buttonTampil.setOnClickListener(this);
//        buttonTampil.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        tampildata();
    }

    @Override
    public void onClick(View view) {
//        if (view== buttonlogout)
//        {
//            firebaseAuth.signOut();
//            finish();
//            startActivity(new Intent(this   ,MainActivity.class));
//        }
        if (view == buttonTambah){

                if(bitmap !=null){
                    upload();
                }
                else {
                    tambahData();

                }
        }
        if (view == buttonChoose){
            ShowFileChooser();
        }
        if (view == buttonTampil)
            askPermission();
             captureImage();

    }

    public void  tampildata(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()){
                    UserModel data =  user.getValue(UserModel.class);
                    FirebaseUser u = firebaseAuth.getCurrentUser();
                    if (u.getEmail().equals(data.getEmail())){
                        editTextName.setText(data.getNama());
                        editTextAddress.setText(data.getAddress());
                        editTexttanggal.setText(data.getTs());
                        editTextNohp.setText(data.getPhone());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void tambahData() {

        String nama = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String gender =spinnerGender.getSelectedItem().toString().trim();
        String tanggal = editTexttanggal.getText().toString().trim();
        String hp = editTextNohp.getText().toString().trim();

//        String urlFoto;
//        urlFoto = PathUrl.toString();
//        UserModel userData = new UserModel(nama, address, gender);
//        databaseReference.child(firebaseUser.getUid()).setValue(userData);

        Map<String,Object> data = new HashMap<String, Object>();
        data.put("userid", firebaseUser.getUid());
        data.put("nama", nama);
        data.put("gender", gender);
        data.put("ts", tanggal);
        data.put("address", address);
        data.put("phone", hp);


//        data.put("Image_url", urlFoto);
        databaseReference.child(firebaseUser.getUid()).updateChildren(data);

        Toast.makeText(getApplicationContext(), "Sukses" , Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this, SlidingUserActivity.class));
    }

    private void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
//            filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
//                imageViewchoose.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewchoose.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                filePat = fileUri.getPath();

                imageViewchoose.setVisibility(View.VISIBLE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                bitmap = BitmapFactory.decodeFile(filePat, options);
                filePath = getImageUri(getApplicationContext(), bitmap);
                imageViewchoose.setImageBitmap(bitmap);

            } else if (resultCode == RESULT_CANCELED) {
                imageViewchoose.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(),
                        "Batal", Toast.LENGTH_SHORT)
                        .show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void askPermission(){
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("ijinkan Menggunakan Memori");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Injinkan menggunakan memori");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();


        } else {
            proceedAfterPermission();
        }
    }


    private void proceedAfterPermission() {
//        Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
    }
    private void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "upload");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + "upload directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void upload(){
        showDialog();
        String uid = firebaseUser.getUid();
        StorageReference riversRef = storageReference.child("user/" + uid + ".jpg");


        riversRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                         progressDialog.dismiss();
                        PathUrl = taskSnapshot.getDownloadUrl();
                        Map<String,Object> data = new HashMap<String, Object>();
                        data.put("image_url", PathUrl);
                        databaseReference.child(firebaseUser.getUid()).updateChildren(data);//        data.put("Image_url", urlFoto);
//                        tambahData();
//                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                            hideDialog();
//                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage(((int) progress) + "% Uploaded..");
                    }
                });







    }

    private void showDialog(){
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }
    private void hideDialog(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }



}
