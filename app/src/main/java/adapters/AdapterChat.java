package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import models.ModelChat;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHoder>{
    private static final int MGS_TYPE_LEFT = 0;
    private static final int MGS_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;
    FirebaseUser fUser;


    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==MGS_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right,viewGroup,false);
            return new MyHoder(view);
        }else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left,viewGroup,false);
            return new MyHoder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHoder myHolder, int i) {
        String message = chatList.get(i).getMessage();
        String timeStamp = chatList.get(i).getTimestamp();
        //String type = chatList.get(i).getTimestamp();
        Calendar  cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String datetime = DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
        myHolder.messageTv.setText(message);
        myHolder.timeTv.setText(datetime);

        try {
            Picasso.get().load(imageUrl).into(myHolder.profileTv);

        }catch (Exception e){
        }
        //click xuat hien hop thoai dilog khi delect
        myHolder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xoa");
                builder.setMessage("Ban muon xoa tin nhan nay chu");
                //xoa
                builder.setPositiveButton("Xoa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(i);
                    }
                });
                //cancel xoa
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();;
            }
        });
//        if(type.equals("text")){
//            myHolder.messageTv.setVisibility(View.VISIBLE);
//            myHolder.messageTv.setVisibility(View.GONE);
//            myHolder.messageTv.setText(message);
//        }else {
//            myHolder.messageTv.setVisibility(View.GONE);
//            myHolder.profileTv.setVisibility(View.VISIBLE);
//            Picasso.get().load(message).into(myHolder.profileTv);
//        }
        //set seen/
        if (i == chatList.size()-1){
            if (chatList.get(i).isSeen()){
                myHolder.isSeenTv.setText("Da Kinh");
            }
            else {
                myHolder.isSeenTv.setText("Da gui");

            }
        }else {
           myHolder.isSeenTv.setVisibility(View.GONE);
        }
    }

    private void deleteMessage(int i) {
        String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String msgTimeStamp = chatList.get(i).getTimestamp();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.child("sender").getValue().equals(myUID)){
                        ds.getRef().removeValue();

//                        HashMap<String, Object> hashMap = new HashMap<>();
//                        hashMap.put("message","Dang xoa tin nhan");
//                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context, "Xoa tin nhan...", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Mac mo gi xoa tin nhan nguoi ta", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fUser.getUid())){
           return MGS_TYPE_RIGHT;
        }else
        {
            return MGS_TYPE_LEFT;
        }
    }

    class MyHoder extends RecyclerView.ViewHolder{
        ImageView profileTv;
        TextView messageTv, timeTv, isSeenTv;
        LinearLayout messageLayout;


        public MyHoder(@NonNull View itemView) {
            super(itemView);
            profileTv = itemView.findViewById(R.id.profile_chat_left);
            messageTv = itemView.findViewById(R.id.message_chat_left);
            timeTv = itemView.findViewById(R.id.time_chat_left);
            isSeenTv = itemView.findViewById(R.id.isSeenTv_chat_left);
            messageLayout = itemView.findViewById(R.id.messageLayout);

        }
    }

}
