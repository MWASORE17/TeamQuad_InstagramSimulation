package com.example.chyntia.simulasi_ig.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chyntia.simulasi_ig.R;
import com.example.chyntia.simulasi_ig.view.activity.MainActivity;
import com.example.chyntia.simulasi_ig.view.fragment.user.UserProfileFragment;
import com.example.chyntia.simulasi_ig.view.model.entity.Data_Follow;
import com.example.chyntia.simulasi_ig.view.model.entity.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Chyntia on 6/11/2017.
 */

public class SearchRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Data_Follow> user;
    Context context;
    private boolean isButtonClicked = false;
    LoginDBAdapter loginDBAdapter;
    SessionManager session;
    String userName;

    public SearchRVAdapter(List<Data_Follow> user, Context context) {
        this.user = user;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_user, parent, false);

        loginDBAdapter = new LoginDBAdapter(context);
        loginDBAdapter = loginDBAdapter.open();

        session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();

        userName = user.get(SessionManager.KEY_USERNAME);

        return new SearchRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
        final SearchRVAdapter.ViewHolder _holder = (SearchRVAdapter.ViewHolder) holder;
        final Data_Follow _user = this.user.get(position);
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        _holder.nama.setText(_user.nama);

        _holder.nama.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                UserProfileFragment upf = new UserProfileFragment();
                final Bundle args = new Bundle();
                args.putString("USERNAME", _user.nama);
                upf.setArguments(args);
                ((MainActivity) v.getContext()).addfragment(upf, "UserProfile");

            }
        });

        if(loginDBAdapter.checkProfPic(loginDBAdapter.getID(_user.nama))!=null){
            Picasso
                    .with(context)
                    .load(new File(_user.pp))
                    .resize(dpToPx(20), dpToPx(20))
                    .centerCrop()
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .into(_holder.pp);
        }
        else
            _holder.pp.setImageResource(R.drawable.ic_account_circle_black_24dp);

        _holder.btn.setVisibility(View.GONE);
        //animate(holder);
    }

    private int dpToPx(int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return user.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Data_Follow dataFollow) {
        user.add(position, dataFollow);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data_Follow object
    public void remove(Data_Follow dataFollow) {
        int position = user.indexOf(dataFollow);
        user.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama;
        ImageView pp;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.nama);
            pp = (ImageView) itemView.findViewById(R.id.pp_follow);
            btn = (Button) itemView.findViewById(R.id.btn);
        }
    }
}
