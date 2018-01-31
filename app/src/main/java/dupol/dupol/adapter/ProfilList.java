package dupol.dupol.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dupol.dupol.R;
import dupol.dupol.model.UserModel;

/**
 * Created by Toshiba C55B on 1/29/2018.
 */

public class ProfilList extends RecyclerView.Adapter<ProfilList.MyViewHolder>{
    Context context;
    List<UserModel> listUser;

    public ProfilList (Context context, List<UserModel> profilList)
    {
        this.context = context;
        this.listUser = profilList;
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View    itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layoutadmin,parent,false);

        return new MyViewHolder(itemView);
        }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserModel userModel = listUser.get(position);

        holder.namaUser.setText(userModel.getNama());
        holder.Gender.setText(userModel.getGender());




        }

@Override
public int getItemCount() {
        return listUser.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView namaUser,Gender;
    public ImageView imageView;
    public MyViewHolder(View itemView) {
        super(itemView);
        namaUser =(TextView) itemView.findViewById(R.id.textViewNamaL);
        Gender =(TextView) itemView.findViewById(R.id.textViewGenderL);
        imageView = (ImageView) itemView.findViewById(R.id.imageViewProfilList);

    }
}
}

