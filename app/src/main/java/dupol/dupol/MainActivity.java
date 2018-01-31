package dupol.dupol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dupol.dupol.Activity.SlidingUserActivity;
import dupol.dupol.model.UserModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTeksEmail, editTeksPassword;
    private Button buttondaftar;
    private TextView signUp;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");


        if (firebaseAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(getApplicationContext(),SlidingUserActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        buttondaftar = (Button) findViewById(R.id.buttonLogin);

        editTeksEmail = (EditText) findViewById(R.id.editTeksEmail);


        editTeksPassword = (EditText) findViewById(R.id.editTeksPassword);

        signUp = (TextView)findViewById(R.id.SignUp);


        buttondaftar.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    private void daftaruser(){
        String email = editTeksEmail.getText().toString().trim();
        String password = editTeksPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            //password empty
            Toast.makeText(this,"Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //kondisi kalau berhasil login, masuk ke actifity user
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot user : dataSnapshot.getChildren()){
                                UserModel data = user.getValue(UserModel.class);
                                FirebaseUser n = firebaseAuth.getCurrentUser();
                                if(n.getEmail().equals(data.getEmail())){
                                    String status = data.getStatus();
                                    if (status.equals("admin")){
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),HomeAdminActivity.class));
                                    }
                                    else if (status.equals("user")){
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),SlidingUserActivity.class));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



//                    Toast.makeText(MainActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(MainActivity.this, "Login GAGAl", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == buttondaftar){
            daftaruser();
        }

        if(view == signUp){
            startActivity(new Intent(this, Registeractivity.class));
        }

    }
}
