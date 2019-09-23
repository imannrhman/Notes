package com.belajar.notes;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListNote extends Fragment {
    RecyclerView recyclerView;
    Context context;
    FloatingActionButton btnFAction;
    RecyclerView.LayoutManager layoutManager;
    List<Notes> listNotes;
    Notes notes;
    int currentId;


    public ListNote() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_note, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        Menu menu = activity.menu;
        if (activity != null) {
            if (menu != null){
                menu.setGroupVisible(0,false);
            }
            activity.showUpButton("Notes");
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        }
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
    private void showPictureDialog(final Notes notes){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Choose Option");
        String[] pictureDialogItems = {
                "Edit",
                "Delete"
        };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment fragment;
                        switch (which) {
                            case 0:
                                fragment = new UbahNote("Ubah",0,notes);
                                setFragment(fragment);
                                break;
                            case 1:
                                DatabaseHelper db = new DatabaseHelper(context);
                                db.delete(notes.getId());
                                setupRecyclerView();
                                break;

                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
         recyclerView = view.findViewById(R.id.rc_item);
        layoutManager = new LinearLayoutManager(context);
        setupRecyclerView();

        btnFAction = view.findViewById(R.id.fab);
        btnFAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UbahNote fragment = new UbahNote("Input",currentId,new Notes());
                setFragment(fragment);
            }
        });


    }

    private void setupRecyclerView() {
        DatabaseHelper db = new DatabaseHelper(context);
        listNotes = db.selectNotesData();
        if (listNotes.size() > 0){
            currentId = listNotes.get(listNotes.size()-1).getId();
        }else{
            currentId = 0 ;
        }
        NotesAdapter adaper = new NotesAdapter(context,listNotes);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaper);
        adaper.notifyDataSetChanged();
        adaper.setOnItemClickCallback(new NotesAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Notes notes) {
                showPictureDialog(notes);
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
