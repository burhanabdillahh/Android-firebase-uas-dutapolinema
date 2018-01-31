package dupol.dupol.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import dupol.dupol.MainActivity;
import dupol.dupol.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TambahProfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TambahProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TambahProfilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText editTextNama, editTextTanggal, editTextAlamat, editTextNohp;
    private Spinner spinnerGender;
    private Button buttoneditProfil, buttonchoosefoto;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TambahProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TambahProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TambahProfilFragment newInstance(String param1, String param2) {
        TambahProfilFragment fragment = new TambahProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         final View viewR = inflater.inflate(R.layout.fragment_tambah_profil, container, false);

        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        storageReference = FirebaseStorage.getInstance().getReference();

        editTextNama = (EditText) viewR.findViewById(R.id.editTeksNamaF);
        spinnerGender = (Spinner) viewR.findViewById(R.id.spinnerGenderF);
        editTextTanggal = (EditText) viewR.findViewById(R.id.editTeksTanggalF);
        editTextAlamat = (EditText) viewR.findViewById(R.id.editTeksAddressF);
        editTextNohp = (EditText) viewR.findViewById(R.id.editTeksNohpF);
        buttoneditProfil =(Button) viewR.findViewById(R.id.buttonEditProfilF);
        buttonchoosefoto =(Button) viewR.findViewById(R.id.buttonchooseF);

        buttoneditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahdata();
            }
        });

         return viewR;
    }

    private void tambahdata() {

        String nama = editTextNama.getText().toString().trim();
        String gender =spinnerGender.getSelectedItem().toString().trim();
        String tanggal = editTextAlamat.getText().toString().trim();
        String address = editTextAlamat.getText().toString().trim();
        String hp =editTextNohp.getText().toString().trim();
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
        data.put("phone",hp);


//        data.put("Image_url", urlFoto);
        databaseReference.child(firebaseUser.getUid()).updateChildren(data);
        Intent intent = new Intent(getActivity(),ProfilFragment.class);
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
