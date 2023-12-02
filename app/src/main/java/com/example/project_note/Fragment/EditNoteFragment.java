package com.example.project_note.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project_note.DataBase.Note;
import com.example.project_note.DataBase.NoteDatabase;
import com.example.project_note.R;


public class EditNoteFragment extends Fragment {
    ImageButton mImgButtonBack;
    ImageButton mImgButtonSave;
    ImageButton mImgButtonVisible;
    EditText mEdtTitle;
    EditText mEdtDetail;

    int id = -1;
    public static Backpressedlistener backpressedlistener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note,container,false);
        mImgButtonBack = view.findViewById(R.id.img_back);
        mImgButtonSave = view.findViewById(R.id.imgbut_save);
        mImgButtonVisible = view.findViewById(R.id.imgbut_visible);
        mEdtTitle = view.findViewById(R.id.edt_title);
        mEdtDetail = view.findViewById(R.id.edt_detail);

        mImgButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotetoDataBase();
            }
        });
        mImgButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
            getParentFragmentManager().popBackStack();
       }

            }

        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            Note note = (Note) bundle.getSerializable("note1");
            id = bundle.getInt("KeyInt");
            if (note != null) {
                // Hiển thị dữ liệu trong TextViews
                mEdtDetail.setText(note.getDetail());
                mEdtTitle.setText(note.getTitle());
            }
        }


        return view;
    }
    private void goBack() {
        Note note = new Note(mEdtTitle.getText().toString(),mEdtDetail.getText().toString());
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("note2",  note);
        bundle.putInt("KeyInt",id);

        intent.putExtras(bundle);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

// Kết thúc fragment mới
        getFragmentManager().popBackStack();

    }

    private void saveNotetoDataBase() {
        if( id == -1){
            Note note = new Note(mEdtTitle.getText().toString(),mEdtDetail.getText().toString());
            NoteDatabase.getInstance(getContext()).noteDAO().insertNote(note);
            note.setId(NoteDatabase.getInstance(getContext()).noteDAO().getID(mEdtTitle.getText().toString(),mEdtDetail.getText().toString()));
            hideSoftKeyboard();
            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
            goVisibleItemFragment(note);
        }
        else{
            Note note = new Note(mEdtTitle.getText().toString(),mEdtDetail.getText().toString());
            note.setId(id);
            NoteDatabase.getInstance(getContext()).noteDAO().updateNote(note);
            hideSoftKeyboard();
            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();

            goBack();
        }


    }

    private void goVisibleItemFragment(Note note) {
        VisibleItemFragment visibleItemFragment = new VisibleItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("note5",  note);
        visibleItemFragment.setArguments(bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_layout,visibleItemFragment );

        fragmentTransaction.commit();
    }

    private void hideSoftKeyboard() {
            if (requireActivity() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                View view = requireActivity().getCurrentFocus();
                if (view != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }

}
