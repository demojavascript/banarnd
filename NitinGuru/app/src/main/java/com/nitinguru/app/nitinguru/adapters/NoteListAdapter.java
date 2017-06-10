package com.nitinguru.app.nitinguru.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.models.DBNotes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 16-08-2016.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {
    List<DBNotes> notes = new ArrayList<DBNotes>();
    Context ctx;
    RecyclerView resourceIdd;
    public NoteListAdapter(List<DBNotes> notes, Context ctx, RecyclerView resourceIdd){
        this.notes = notes;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
    }
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notes_view, parent, false);
        NoteViewHolder contactViewHolder = new NoteViewHolder(view, ctx, notes, resourceIdd);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        DBNotes note = notes.get(position);
        holder.tv_name.setText(note.getTitle());
        Spanned htmlAsSpanned = Html.fromHtml(note.getDesc());
        holder.tv_notedesc.setText(htmlAsSpanned);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_notedesc;
        Context ctx;
        CardView card_view_notelist;
        List<DBNotes> notes = new ArrayList<DBNotes>();
        public NoteViewHolder(View view, Context ctx, List<DBNotes> notes, RecyclerView resourceIdd){
            super(view);
            this.ctx = ctx;
            this.notes = notes;
            card_view_notelist = (CardView)view.findViewById(R.id.card_view_notelist);
            tv_name = (TextView) view.findViewById(R.id.tv_notename);
            tv_notedesc = (TextView) view.findViewById(R.id.tv_notedesc);
            card_view_notelist.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
