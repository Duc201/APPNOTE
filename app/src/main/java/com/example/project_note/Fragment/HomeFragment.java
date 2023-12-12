package com.example.project_note.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_note.DataBase.Note;
import com.example.project_note.DataBase.NoteDatabase;
import com.example.project_note.ItemTouchHelperListener;
import com.example.project_note.NoteAdapter;
import com.example.project_note.R;
import com.example.project_note.RecyclerViewItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HomeFragment extends Fragment implements ItemTouchHelperListener {
    LinearLayout mLayoutNotData;
    RecyclerView mRecyclerView;
    FloatingActionButton mFloatingActionButton;
    List<Note> mlistNote;
    RelativeLayout rootView;

//    ImageButton mImageButtonSearch;
    ImageButton mImageButtonIntroduct;
    NoteAdapter noteAdapter;
    Toolbar toolbar;
    SearchView searchView;
    Button mBtnSearch;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        toolbar = view.findViewById(R.id.toolbar_main);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mLayoutNotData = view.findViewById(R.id.linear_non);
        mRecyclerView = view.findViewById(R.id.ryc_notes);
        mFloatingActionButton = view.findViewById(R.id.btn_floating);
        mImageButtonIntroduct = toolbar.findViewById(R.id.imgbut_introduce);
        searchView = toolbar.findViewById(R.id.imgbut_search);
        rootView = view.findViewById(R.id.root_view);
        mBtnSearch = view.findViewById(R.id.btnSearch);
//        mBtnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFragmentSearch();
//            }
//        });

        // Set view nodata or data
        checkInitData();
        // action open Frame Edit
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentEdit();
            }
        });
        // action open Introduct
        mImageButtonIntroduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showCustomDialog();
                openDialogIntroduce(Gravity.CENTER);
            }
        });

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
        // swip delete item
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerView);

        // setSearchView
        setSearchView();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (searchView != null && !searchView.isIconified()) {
                    // Nếu SearchView đang mở, thu nhỏ nó thay vì thoát ứng dụng
                    searchView.setIconified(true);
                } else {
                    // Nếu không, thực hiện hành động mặc định khi nhấn nút Back
                    requireActivity().finish();
                }

            }
        });


        return view;
    }

    private void initial() {
    }

    private void setSearchView() {
//        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Khi người dùng nhấn nút tìm kiếm trên bàn phím
                // Áp dụng bộ lọc dữ liệu vào Adapter để lọc dữ liệu dựa trên truy vấn tìm kiếm
                noteAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Khi giá trị của ô tìm kiếm thay đổi
                // Áp dụng bộ lọc dữ liệu vào Adapter để ngay lập tức lọc dữ liệu dựa trên văn bản mới nhập
                noteAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

//    private void openFragmentSearch() {
//        FragmentManager fragmentManager = getParentFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frag_layout,new SearchFragment());
//        fragmentTransaction.addToBackStack("FragmentSearch");
//        fragmentTransaction.commit();
//    }


    private void checkInitData(){
        if(isexistData()){
            mLayoutNotData.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        else {
            mLayoutNotData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }


    private void openDialogIntroduce(int gravity) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_introduce);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = gravity;
        window.setAttributes(windowAtrributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }
        else {
            dialog.setCancelable(false);
        }

        dialog.show();

    }


    private void openFragmentVisible(Note note) {


        Bundle bundle = new Bundle();
        bundle.putSerializable("note",  note);
        getParentFragmentManager().setFragmentResult("dataFromHome",bundle);

        VisibleItemFragment visibleItemFragment = new VisibleItemFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_layout,visibleItemFragment );
        fragmentTransaction.addToBackStack(null); // Nếu bạn muốn thêm vào back stack
        fragmentTransaction.commit();
    }

    private void openFragmentEdit() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_layout,new EditNoteFragment());
        fragmentTransaction.addToBackStack("FragmentHome");
        fragmentTransaction.commit();
    }
    Boolean isexistData(){
        List<Note> mList = NoteDatabase.getInstance(getContext()).noteDAO().getListNote();
        return mList != null && !mList.isEmpty();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
            if(viewHolder instanceof NoteAdapter.NoteViewHodel){
                // Lấy title của một Note
                final String nameNoteDelete = mlistNote.get(viewHolder.getAdapterPosition()).getTitle();

                final Note noteDelete = mlistNote.get(viewHolder.getAdapterPosition());

                final int indexDelte = viewHolder.getAdapterPosition();

                //remove item
                noteAdapter.removeItem(indexDelte);
                // Note use RXAndroid
                NoteDatabase.getInstance(getContext()).noteDAO().deleteNote(noteDelete);

                checkInitData();
                Snackbar snackbar = Snackbar.make(rootView,nameNoteDelete+" remove!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteAdapter.undoItem(noteDelete,indexDelte);
                        // Note use RXAndroid
                        NoteDatabase.getInstance(getContext()).noteDAO().insertNote(noteDelete);
                        mLayoutNotData.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
    }


}
