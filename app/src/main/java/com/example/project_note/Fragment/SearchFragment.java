package com.example.project_note.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_note.DataBase.Note;
import com.example.project_note.DataBase.NoteDatabase;
import com.example.project_note.NoteAdapter;
import com.example.project_note.R;

import java.util.List;

public class SearchFragment extends Fragment {
    RecyclerView mRecyclerView;
    NoteAdapter noteAdapter;
    List<Note> mlistNote;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        mRecyclerView = view.findViewById(R.id.rcy_search);
        mlistNote = NoteDatabase.getInstance(getContext()).noteDAO().getListNote();
        noteAdapter = new NoteAdapter(mlistNote, new NoteAdapter.IClickListener() {
            @Override
            public void onClickItem(Note note) {
                openFragmentVisible(note);
            }

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(noteAdapter);




         searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                noteAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Áp dụng bộ lọc dữ liệu vào RecyclerView
                noteAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return view;

    }
    private void openFragmentVisible(Note note) {
        VisibleItemFragment visibleItemFragment = new VisibleItemFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("note",  note);
        visibleItemFragment.setArguments(bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_layout,visibleItemFragment );
//        fragmentTransaction.addToBackStack(null); // Nếu bạn muốn thêm vào back stack
        fragmentTransaction.commit();
    }
}
