package dupol.dupol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dupol.dupol.model.UserModel;

public class TampilDataActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private TextView textViewName, textViewAddress, textViewGender;

    @Override
    protected void onStart() {
        super.onStart();
        tampildata();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_data);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        storageReference = FirebaseStorage.getInstance().getReference();



        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewFAddress);
        textViewGender =(TextView)findViewById(R.id.textViewFGender);




    }

    private void tampildata() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user :dataSnapshot.getChildren()){
                    UserModel data = user.getValue(UserModel.class);
                    FirebaseUser j = firebaseAuth.getCurrentUser();
//                    databaseReference.child(firebaseUser.getUid()).getDatabase();
                    if(j.getEmail().equals(data.getEmail())){
                        textViewName.setText(data.getNama());
                        textViewAddress.setText(data.getAddress());
                        textViewGender.setText(data.getGender());


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
