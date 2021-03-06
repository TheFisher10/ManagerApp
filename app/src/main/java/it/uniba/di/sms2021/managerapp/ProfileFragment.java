package it.uniba.di.sms2021.managerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.security.auth.callback.Callback;

import entities.Studente;

public class ProfileFragment extends Fragment {

    private GuestActivity callback;
    private FirebaseDatabase db;
    private DatabaseReference userRef;

    private static final String TAG = "SimpleToolbarTest";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Log.d(TAG, "Profilo");

        Intent intent = getActivity().getIntent();
        String matricola = intent.getStringExtra("matricola");

        TextView fullname = getActivity().findViewById(R.id.full_name);
        TextView email = getActivity().findViewById(R.id.profile_email);

        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("Studenti");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d(TAG, "Arrivoooooo");
                    if(ds.child("Matricola").equals("1")) {
                        fullname.setText(ds.child("Nome" + " " + "Cognome").getValue(String.class));
                        email.setText(ds.child("Email").getValue(String.class));
                        Log.d(TAG, "Risultatooooooo");
                        Log.d(TAG, fullname.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(getActivity().getApplicationContext(), item.getTitle()+" Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        // Add Edit Menu Item
        int editId = StudentActivity.EDIT_ITEM_ID;
        if (menu.findItem(editId) == null) {
            // If it not exists then add the menu item to menu
            MenuItem edit = menu.add(
                    Menu.NONE,
                    editId,
                    1,
                    getString(R.string.edit)
            );

            // Set an icon for the new menu item
            edit.setIcon(R.drawable.ic_edit);

            // Set the show as action flags for new menu item
            edit.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            // Set a click listener for the new menu item
            edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(getActivity().getApplicationContext(), item.getTitle()+" Clicked", Toast.LENGTH_SHORT).show();
                    NavDirections action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment();
                    Navigation.findNavController(getActivity(), R.id.fragment).navigate(action);
                    return true;
                }
            });
        }

        // Add Logout Menu Item
        int logoutId = StudentActivity.LOGOUT_ITEM_ID;
        if (menu.findItem(logoutId) == null) {
            // If it not exists then add the menu item to menu
            MenuItem logout = menu.add(
                    Menu.NONE,
                    logoutId,
                    2,
                    getString(R.string.logout)
            );

            // Set an icon for the new menu item
            logout.setIcon(R.drawable.ic_logout);

            // Set the show as action flags for new menu item
            logout.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            // Set a click listener for the new menu item
            logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(getActivity().getApplicationContext(), item.getTitle()+" Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), GuestActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
            });
        }

        super.onPrepareOptionsMenu(menu);
    }
}