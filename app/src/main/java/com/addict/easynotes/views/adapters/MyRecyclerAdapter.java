package com.addict.easynotes.views.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.addict.easynotes.R;
import com.addict.easynotes.models.Note;
import com.addict.easynotes.utils.DateUtils;

import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<Note> mNoteList;

    public MyRecyclerAdapter(List<Note> noteList) {
        mNoteList = noteList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mDateTextView;
        private TextView mContentTextView;

        public ViewHolder(View v) {
            super(v);
        }

        public ViewHolder(View v, TextView dateTextView, TextView contentTextView){
            this(v);

            mDateTextView = dateTextView;
            mContentTextView = contentTextView;
            mView =v;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_today, viewGroup, false);
        TextView dateTextView = (TextView) v.findViewById(R.id.date_textView);
        TextView contentTextView = (TextView) v.findViewById(R.id.content_textView);

        return new ViewHolder(v, dateTextView, contentTextView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.mContentTextView.setText(mNoteList.get(position).getContent());
        viewHolder.mDateTextView.setText(DateUtils.dateOrTimeFormat(mNoteList.get(position).getUpdateTime()));
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public List<Note> getList(){
        return mNoteList;
    }

    public void refresh(List<Note> list){
        mNoteList = list;
        notifyDataSetChanged();
    }
}
