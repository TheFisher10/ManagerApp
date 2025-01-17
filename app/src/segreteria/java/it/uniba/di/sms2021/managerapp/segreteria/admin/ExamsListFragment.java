package it.uniba.di.sms2021.managerapp.segreteria.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import it.uniba.di.sms2021.managerapp.R;
import it.uniba.di.sms2021.managerapp.entities.Esame;
import it.uniba.di.sms2021.managerapp.segreteria.editItem.EditExamActivity;
import it.uniba.di.sms2021.managerapp.segreteria.service.SettingsAdmin;
import it.uniba.di.sms2021.managerapp.service.ExamListAdapter;

public class ExamsListFragment extends Fragment {

    private View viewExamsList;
    private ListView examListView;
    private ExamListAdapter adapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Esame> esami;

    public ExamsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewExamsList = inflater.inflate(R.layout.fragment_exams_list, container, false);

        return viewExamsList;
    }

    private synchronized void getExams(ArrayList<Esame> esami) {
        db.collection("esami").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Esame esame = new Esame(document.getString("id"),
                            document.getString("nome"),
                            document.getString("descrizione"),
                            document.getString("cDs"),
                            (ArrayList<String>) document.get("idDocenti"));
                    esami.add(esame);
                }

                examListView = viewExamsList.findViewById(R.id.examListView);

                //pass results to listViewAdapter class
                adapter = new ExamListAdapter(requireActivity().getApplicationContext(), esami);

                //bind the adapter to the listView
                examListView.setAdapter(adapter);

                examListView.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(requireActivity().getApplicationContext(), EditExamActivity.class);
                    intent.putExtra("esame",esami.get(position));
                    startActivity(intent);
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)) {
                    adapter.filter("");
                    examListView.clearTextFilter();
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(requireActivity().getApplicationContext(), SettingsAdmin.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        esami = new ArrayList<Esame>();
        getExams(esami);
        super.onResume();
    }
}