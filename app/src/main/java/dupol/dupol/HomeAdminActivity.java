package dupol.dupol;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import dupol.dupol.adapter.ProfilList;
import dupol.dupol.model.UserModel;

public class HomeAdminActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private List<UserModel> listUser;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerlayoutManager;
    private ProfilList  profilListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        recyclerView = (RecyclerView) findViewById(R.id.rvListUser);

        listUser = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        profilListAdapter = new ProfilList(this, listUser);
        recyclerlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerlayoutManager);

        tampildata();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserModel Del = listUser.get(position);
                Toast.makeText(getApplicationContext(),"Deleted", Toast.LENGTH_SHORT);
                hapusdata(Del);
            }

            @Override
            public void onLongClick(View view, int position) {
                UserModel Del = listUser.get(position);
                hapusdata(Del);
            }
        }));

    }

    private void hapusdata( final UserModel del)
    {
        AlertDialog.Builder alertDiaBuilder = new AlertDialog.Builder(this);
        alertDiaBuilder.setMessage("Hapus Data?");

        alertDiaBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserModel userModel = new UserModel();
                        databaseReference.child(del.getUserid()).setValue(userModel);
                        Toast.makeText(getApplicationContext(),"Deleted", Toast.LENGTH_SHORT);
                    }
                });

        alertDiaBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
    }

    private void tampildata() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUser.clear();
                for (DataSnapshot user : dataSnapshot.getChildren()){
                    UserModel userModel = user.getValue(UserModel.class);
                    listUser.add(userModel);

                    recyclerView.setAdapter(profilListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
