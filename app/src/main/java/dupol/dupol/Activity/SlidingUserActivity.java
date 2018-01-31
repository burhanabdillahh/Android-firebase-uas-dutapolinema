package dupol.dupol.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dupol.dupol.MainActivity;
import dupol.dupol.R;
import dupol.dupol.adapter.PagerAdapter;
import dupol.dupol.fragment.InfoFragment;
import dupol.dupol.fragment.PrestasiFragment;
import dupol.dupol.fragment.ProfilFragment;

public class SlidingUserActivity extends AppCompatActivity implements  ProfilFragment.OnFragmentInteractionListener, PrestasiFragment.OnFragmentInteractionListener, InfoFragment.OnFragmentInteractionListener, View.OnClickListener{

//    private ViewPager viewPager;
//    private PagerAdapter pagerAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private ImageButton imageButton;
//    private StorageReference storageReference;
//    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_user);

        imageButton =(ImageButton) findViewById(R.id.logout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayoutuser);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profil));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.pendidikan));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.info));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPageruser);
        final PagerAdapter  pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == imageButton){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this   ,MainActivity.class));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
