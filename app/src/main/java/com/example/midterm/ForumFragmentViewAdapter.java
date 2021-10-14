package com.example.midterm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForumFragmentViewAdapter extends RecyclerView.Adapter<ForumFragmentViewAdapter.ListViewHolder> {
    ArrayList<DataServices.Comment> CommentList;
    ForumFragmentViewAdapter.ForumFragmentListener fListener;
    String Token;
    DataServices.Account account;

    public ForumFragmentViewAdapter(ArrayList<DataServices.Comment> Data, String token,DataServices.Account currentAccount, ForumFragmentViewAdapter.ForumFragmentListener fListener) {
        this.CommentList = Data;
        this.Token = token;
        this.fListener = fListener;
        this.account = currentAccount;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_details, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(view, fListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.textviewCommentDesc.setText(CommentList.get(position).getText());
        holder.textviewCommentAuthor.setText(CommentList.get(position).getCreatedBy().getName());
        holder.textViewCommenttimestamp.setText( CommentList.get(position).getCreatedAt().toString());

        if(account == CommentList.get(position).getCreatedBy() ){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fListener.deleteComment(String.valueOf(CommentList.get(position).getCommentId()));
            }
        });



    }

    @Override
    public int getItemCount() {
        return CommentList.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        ForumFragmentListener fListener;
        TextView textviewCommentAuthor, textviewCommentDesc, textViewCommenttimestamp;
        ImageView imageViewDelete;

        public ListViewHolder(@NonNull View itemView, ForumFragmentListener forumViewListener) {
            super(itemView);
            this.fListener = forumViewListener;
            textviewCommentAuthor = itemView.findViewById(R.id.textviewCommentAuthor);
            textviewCommentDesc = itemView.findViewById(R.id.textviewCommentDesc);
            textViewCommenttimestamp = itemView.findViewById(R.id.textViewCommenttimestamp);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);

        }
    }

    interface ForumFragmentListener {
        void deleteComment(String commentId);
    }
}
