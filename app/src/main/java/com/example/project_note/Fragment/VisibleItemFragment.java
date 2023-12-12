package com.example.project_note.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.project_note.DataBase.Note;
import com.example.project_note.DataBase.NoteDatabase;
import com.example.project_note.R;

public class VisibleItemFragment extends Fragment {
    TextView mTxtTitle;
    TextView mTxtDetail;
    ImageButton mImageButtonBack;

    ImageButton mImageButtonEdit;
    private int id;
    Note note2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visible_item,container,false);

        mTxtDetail = view.findViewById(R.id.txt_detail_visible);
        mTxtTitle = view.findViewById(R.id.txt_title_visible);
        mImageButtonBack = view.findViewById(R.id.imgbut_back);
        mImageButtonEdit = view.findViewById(R.id.imgbut_edit);
        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (getParentFragmentManager() != null) {
                        getParentFragmentManager().popBackStack();
                    }
            }
        });

        mImageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(mTxtTitle.getText().toString(),mTxtDetail.getText().toString());
                openFragmentEdit(note,id);
            }
        });


        getParentFragmentManager().setFragmentResultListener("dataFromEdit", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Note note = (Note) result.getSerializable("note5");
                if(note != null) {
                    mTxtDetail.setText(note.getDetail());
                    mTxtTitle.setText(note.getTitle());
                    id = note.getId();
                }
            }
        });
        getParentFragmentManager().setFragmentResultListener("dataFromHome", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Note note = (Note) result.getSerializable("note");
                if(note != null) {
                    mTxtDetail.setText(note.getDetail());
                    mTxtTitle.setText(note.getTitle());
                    id = note.getId();
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener("dataFromSearch", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Note note = (Note) result.getSerializable("note9");
                if(note != null) {
                    mTxtDetail.setText(note.getDetail());
                    mTxtTitle.setText(note.getTitle());
                    id = note.getId();
                }
            }
        });
        return view;
    }

    private void openFragmentEdit(Note note, int id) {

        EditNoteFragment editNoteFragment = new EditNoteFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("note1",  note);
        bundle.putInt("KeyInt",id);
        getParentFragmentManager().setFragmentResult("dataFromVisible",bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_layout,editNoteFragment );
        fragmentTransaction.addToBackStack(null); // Nếu bạn muốn thêm vào back stack
        fragmentTransaction.commit();
    }





    // xem sau
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
//            Bundle receivedBundle = data.getExtras();
//
//            if (receivedBundle != null) {
//                // Truy cập dữ liệu từ Bundle
//                  note2 = (Note) receivedBundle.getSerializable("note2");
//                 if(note2 != null){
//                     Toast.makeText(getContext(), note2.getTitle() + "", Toast.LENGTH_SHORT).show();
//                     Log.e("AAA","Đã vào đây rồi");
//                 }
//
//            }
//
//        }
//    }




}
