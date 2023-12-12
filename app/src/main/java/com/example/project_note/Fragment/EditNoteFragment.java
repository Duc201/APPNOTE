package com.example.project_note.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
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

    Note note1;
    int id = -1;
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

                openDialogSave(Gravity.CENTER);
            }
        });
        mImgButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("note5",  note1);
                getParentFragmentManager().setFragmentResult("dataFromEdit",bundle);

                openDialogReturn(Gravity.CENTER);

            }

        });

        getParentFragmentManager().setFragmentResultListener("dataFromVisible", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                 note1 = (Note) result.getSerializable("note1");
                mEdtTitle.setText(note1.getTitle());
                mEdtDetail.setText(note1.getDetail());
                id = result.getInt("KeyInt");
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("note5",  note1);
                getParentFragmentManager().setFragmentResult("dataFromEdit",bundle);
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    openDialogReturn(Gravity.CENTER);
                } else {
                    // Không có fragment trong back stack, xử lý theo ý muốn
                    requireActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    private void openDialogReturn(int gravity) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_discardsave);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Đặt background mờ lại
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
        Button btnKeep = dialog.findViewById(R.id.btn_keep);
        Button btnDiscard = dialog.findViewById(R.id.btn_discard);

        btnKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(getParentFragmentManager() != null){
                   getParentFragmentManager().popBackStack();
                   dialog.dismiss();
               }
            }
        });
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void saveNotetoDataBase() {
        if( id == -1){
            Note note = new Note(mEdtTitle.getText().toString(),mEdtDetail.getText().toString());
            NoteDatabase.getInstance(getContext()).noteDAO().insertNote(note);
            note.setId(NoteDatabase.getInstance(getContext()).noteDAO().getID(mEdtTitle.getText().toString(),mEdtDetail.getText().toString()));
            hideSoftKeyboard();
            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
            returnVisibleItemFragment(note);
        }
        else{
            Note note = new Note(mEdtTitle.getText().toString(),mEdtDetail.getText().toString());
            note.setId(id);
            NoteDatabase.getInstance(getContext()).noteDAO().updateNote(note);
            hideSoftKeyboard();
            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            returnVisibleItemFragment(note);
        }

    }
    private void openDialogSave(int gravity) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_save);

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
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnDiscard = dialog.findViewById(R.id.btn_discard);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        saveNotetoDataBase();
                dialog.dismiss();
            }
        });
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void returnVisibleItemFragment(Note note) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("note5",  note);
        getParentFragmentManager().setFragmentResult("dataFromEdit",bundle);
        if (getParentFragmentManager() != null) {
            getParentFragmentManager().popBackStack();
        }
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
