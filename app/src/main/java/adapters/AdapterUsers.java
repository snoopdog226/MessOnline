package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messonline.ChatActivity;
import com.example.messonline.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import models.ModelUsers;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder>{
    Context context;
    List<ModelUsers> usersList;


    public AdapterUsers(Context context, List<ModelUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users,viewGroup,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        String hisUID = usersList.get(i).getUid();
        String userImage = usersList.get(i).getImage();
        String userName = usersList.get(i).getName();
        String userCommit = usersList.get(i).getCommit();


        holder.mNameTv.setText(userName);
        holder.mCommitTV.setText(userCommit);
        try{
            Picasso.get().load(userImage).placeholder(R.drawable.ic_home_back).into(holder.mimageView);
        }
        catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid",hisUID);
                context.startActivity(intent);
            }
        });

    }
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mimageView;
        TextView mNameTv,mCommitTV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mimageView = itemView.findViewById(R.id.image_row);
            mNameTv = itemView.findViewById(R.id.name_row);
            mCommitTV = itemView.findViewById(R.id.commit_row);
        }
    }
    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
