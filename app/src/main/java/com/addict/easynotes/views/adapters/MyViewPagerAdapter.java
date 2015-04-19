package com.addict.easynotes.views.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.addict.easynotes.App;
import com.addict.easynotes.R;
import com.addict.easynotes.fragments.HistoryListFragment;
import com.addict.easynotes.fragments.NotesListFragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private final String[] title = {App.getContext().getResources().getString(R.string.page_today),
            App.getContext().getResources().getString(R.string.page_history)};

    NotesListFragment notesListFragment;
    HistoryListFragment historyListFragment;

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                notesListFragment = new NotesListFragment();
                return notesListFragment;
            case 1:
                historyListFragment = new HistoryListFragment();
                return historyListFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
