package it.uniba.di.sms2021.managerapp.segreteria.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import it.uniba.di.sms2021.managerapp.segreteria.admin.CoursesListFragment;
import it.uniba.di.sms2021.managerapp.segreteria.admin.ExamsListFragment;
import it.uniba.di.sms2021.managerapp.segreteria.admin.StudentsListFragment;
import it.uniba.di.sms2021.managerapp.segreteria.admin.TeachersListFragment;

public class TabAdapter extends FragmentPagerAdapter {
    private Context context;
    private final int totalTabs;

    public TabAdapter(Context context, @NonNull FragmentManager fm, int totalTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TeachersListFragment();
            case 1:
                return new StudentsListFragment();
            case 2:
                return new ExamsListFragment();
            default:
                return new CoursesListFragment();
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
