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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dupol.dupol.Activity.SlidingUserActivity;
import dupol.dupol.model.UserModel;

public class Registeractivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextNameregis, editTextPasswordRegis;
    private Button buttonregis;
    private TextView textViewsyarat, textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");


        if (firebaseAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(getApplicationContext(),SlidingUserActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        editTextNameregis = (EditText) findViewById(R.id.editTeksEmail);
        editTextPasswordRegis =(EditText) findViewById(R.id.editTeksPassword);
        textViewsyarat =(TextView) findViewById(R.id.Syarat);
        textViewSignin =(TextView) findViewById(R.id.SignIn);
        buttonregis =(Button) findViewById(R.id.buttonRegis);

        buttonregis.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        textViewsyarat.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonregis){
            Regisuser();
        }
        if ((view == textViewSignin)) {
            startActivity(new Intent(this,MainActivity.class));
        }
        if (view == textViewsyarat){
            startActivity(new Intent(this  ,SyaratActivity.class));
        }
        }

    private void Regisuser() {
        String email = editTextNameregis.getText().toString().trim();
        String password = editTextPasswordRegis.getText().toString().trim();

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

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //kondisi kalau berhasil login, masuk ke actifity user
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    UserModel userLogin = new UserModel(user.getUid(),user.getEmail(),"user");
                    databaseReference.child(user.getUid()).setValue(userLogin);



                    finish();
                    startActivity(new Intent(getApplicationContext(),SlidingUserActivity.class));

//                    Toast.makeText(MainActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(Registeractivity.this, "Login GAGAl", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


