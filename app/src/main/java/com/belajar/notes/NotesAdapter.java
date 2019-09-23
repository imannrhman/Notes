package com.belajar.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    Context context;
    List<Notes> listNotes;
    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(Notes notes);
    }

    public NotesAdapter(Context context, List<Notes> listNotes) {
        this.context = context;
        this.listNotes = listNotes;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes,parent,false);
        ViewHolder notesViewHolder = new ViewHolder(view);
        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesAdapter.ViewHolder holder, int position) {
        final  Notes notes = listNotes.get(position);
        holder.tvJudul.setText(notes.getJudul());
        holder.tvDeskripsi.setText(notes.getDeskripsi());
        holder.tvTanggal.setText(notes.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(listNotes.get(holder.getPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal;
        TextView tvJudul;
        TextView tvDeskripsi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
        }
    }
}
