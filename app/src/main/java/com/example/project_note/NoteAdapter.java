package com.example.project_note;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_note.DataBase.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHodel> implements Filterable {



    public interface IClickListener{
        void onClickItem(Note note);

    }

    IClickListener mIClickListener;
    List<Note> mlistNote;
    List<Note> mListNoteOld;
    Context mContext;



    public NoteAdapter( List<Note> mlistNote, IClickListener iClickListener) {

        this.mlistNote = mlistNote;
        this.mIClickListener= iClickListener;
        this.mListNoteOld = mlistNote;
    }

    @NonNull
    @Override
    public NoteViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes,parent,false);
        return new NoteViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHodel holder, int position) {
        Note note = mlistNote.get(position);

        holder.mTxtTitle.setText(note.getTitle().toString());

        // Creat ColorItem
        int colorIndex = position % colorList.size();
        int colorResId = colorList.get(colorIndex);
        int selectedColor = ContextCompat.getColor(holder.itemView.getContext(), colorResId);

        holder.mLayoutForceGround.setBackgroundColor(selectedColor);
        holder.mLayoutForceGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickItem(note);
            }
        });
    }

    private List<Integer> colorList = Arrays.asList(
            R.color.DeepPink,
            R.color.LightPink,
            R.color.LightGreen,
            R.color.Yellow,
            R.color.LightBlue,
            R.color.MediumPurple
    );




    @Override
    public int getItemCount() {
        if(mlistNote != null){
            return mlistNote.size();
        }
        return 0;
    }

    public class NoteViewHodel extends RecyclerView.ViewHolder{

        private TextView mTxtTitle;
        private CardView mCardView;
        public LinearLayout mLayoutForceGround;

        public NoteViewHodel(@NonNull View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.edt_title_item);
            mCardView = itemView.findViewById(R.id.cardview_note);
            mLayoutForceGround = itemView.findViewById(R.id.layout_forceground);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mIClickListener!=null){
//                        mIClickListener.onClickItem();
//                    }
//                }
//            });
        }
    }
    public void removeItem(int index){
        mlistNote.remove(index);
        notifyItemRemoved(index);
    }
    public void undoItem(Note note , int index){
        mlistNote.add(index,note);
        notifyItemInserted(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    mlistNote = mListNoteOld;
                }else {
                    List<Note> list = new ArrayList<>();
                    for(Note note : mListNoteOld){
                        if(note.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(note);
                        }
                    }
                    mlistNote = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mlistNote;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            mlistNote = (List<Note>) results.values;
            notifyDataSetChanged();;
            }
        };
    }

}
