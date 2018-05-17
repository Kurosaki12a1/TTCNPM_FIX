package com.bku.picshub.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bku.picshub.R;

import java.util.List;

/**
 * Created by Welcome on 5/11/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

    private List<CommentViewHolder> commentList;
    private Context context;

    public CommentAdapter(List<CommentViewHolder> _commentList, Context _context) {
        this.commentList = _commentList;
        this.context = _context;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        CommentViewHolder commentViewHolder = commentList.get(position);
        holder.whenBind(commentViewHolder, context);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void addItem(CommentViewHolder commentView) {
        commentList.add(commentView);
        notifyItemChanged(commentList.size() - 1);
    }

}
