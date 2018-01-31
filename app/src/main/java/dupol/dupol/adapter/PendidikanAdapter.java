package dupol.dupol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dupol.dupol.model.UserModel;

/**
 * Created by Toshiba C55B on 1/29/2018.
 */

public class PendidikanAdapter extends RecyclerView.Adapter<PendidikanAdapter.MyViewHolder> {
    Context context;
    List<UserModel> listUser;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
