package com.belajar.notes;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class UbahNote extends Fragment {
    String action;
    Context context;
    Button btnAction;
    int currentId;
    Notes currentNotes;
    EditText edtJudul,edtDeskripsi;

    public UbahNote(String action,int currentId,Notes notes) {
        this.action = action;
        this.currentId = currentId;
        this.currentNotes = notes;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ubah_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        btnAction = view.findViewById(R.id.btn_aksi);
        contentFilter(view);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                return  true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        Menu menu = activity.menu;
        activity.notes = currentNotes;
        if (activity != null) {
            activity.showUpButton( action + " Data");
            if (action.equals("Ubah")){
                menu.setGroupVisible(0,true);

            }else{
                menu.setGroupVisible(0,false);
            }
        }
    }
    private void contentFilter(View view) {
        edtJudul = view.findViewById(R.id.edt_judul);
        edtDeskripsi = view.findViewById(R.id.edt_deskripsi);
        if (action.equals("Input")){
            final DatabaseHelper db = new DatabaseHelper(context);
            final Notes notes = new Notes();
            btnAction.setText("Input");
            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        notes.setId(currentId+1);
                        notes.setJudul(edtJudul.getText().toString());
                        notes.setDeskripsi(edtDeskripsi.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ' ' HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        notes.setDate(currentDateandTime);
                        db.insert(notes);
                        getActivity().getSupportFragmentManager().popBackStackImmediate();

                }
            });

        }   else if (action.equals("Ubah")){
            final DatabaseHelper db = new DatabaseHelper(context);
            final Notes notes = new Notes();
            edtJudul.setText(currentNotes.getJudul());
            edtDeskripsi.setText(currentNotes.getDeskripsi());
            btnAction.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                notes.setId(currentNotes.getId());
                                notes.setJudul(edtJudul.getText().toString());
                                notes.setDeskripsi(edtDeskripsi.getText().toString());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ' ' HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                notes.setDate(currentDateandTime);
                                db.update(notes);
                                setFragment(new ListNote());
                        }
                    });



        }
}



    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment).addToBackStack(null);
        fragmentTransaction.commit();


    }
}
