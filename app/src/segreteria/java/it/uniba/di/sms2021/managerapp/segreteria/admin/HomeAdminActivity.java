package it.uniba.di.sms2021.managerapp.segreteria.admin;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.Set;

import it.uniba.di.sms2021.managerapp.R;
import it.uniba.di.sms2021.managerapp.segreteria.entities.Segreteria;
import it.uniba.di.sms2021.managerapp.segreteria.service.SettingsAdmin;

public class HomeAdminActivity extends AppCompatActivity {

    public static final int EDIT_ITEM_ID = View.generateViewId();
    public static final int SAVE_ITEM_ID = View.generateViewId();
    public static final int CANCEL_ITEM_ID = View.generateViewId();
    public static final int LOGOUT_ITEM_ID = View.generateViewId();
    private static final String filename = "segreteria.srl";
    private static File loginFile;
    private static Segreteria loggedAdmin;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        File file = new File(getApplicationContext().getExternalFilesDir(null), "IT");
        traduci(file.exists());

        toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Home");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(findViewById(R.id.fragment));
        AppBarConfiguration appBarConfiguration = (new AppBarConfiguration.Builder(Set.of(R.id.homeAdminFragment, R.id.profileAdminFragment))).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        loginFile = new File(getApplicationContext().getExternalFilesDir(null), filename);
        if(loginFile.exists()) {
            readFile();
        }else {
            Intent intent = new Intent(getApplicationContext(), LoginAdminActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void readFile(){
        ObjectInputStream input;

        try {
            input = new ObjectInputStream(new FileInputStream(new File(getExternalFilesDir(null), filename)));
            loggedAdmin = (Segreteria) input.readObject();
            input.close();
            Toast.makeText(getApplicationContext(), String.format("%s %s", getString(R.string.welcome_msg), loggedAdmin.getEmail()),Toast.LENGTH_LONG).show();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Segreteria getLoggedAdmin() {
        return loggedAdmin;
    }

    public static void setLoggedAdmin(Segreteria loggedAdmin) {
        HomeAdminActivity.loggedAdmin = loggedAdmin;
    }

    public static File getLoginFile() {
        return loginFile;
    }

    public void disableBackArrow() {
        toolbar.setNavigationIcon(null);
    }

    public void enableBackArrow() {
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
        toolbar.setNavigationOnClickListener(view -> goBackFragment());
    }

    public void goBackFragment() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsAdmin.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void traduci(Boolean flag) {

        Locale locale;
        if (!flag) {
            File file = new File(getApplicationContext().getExternalFilesDir(null), "IT");
            locale = Locale.ENGLISH;
            file.delete();
            saveFile("EN");
        } else {
            File file = new File(getApplicationContext().getExternalFilesDir(null), "EN");
            locale = Locale.ITALIAN;
            file.delete();
            saveFile("IT");
        }
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }

    public void saveFile(String FILE_NAME) {
        ObjectOutput out;

        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(getBaseContext().getExternalFilesDir(null), FILE_NAME)));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}