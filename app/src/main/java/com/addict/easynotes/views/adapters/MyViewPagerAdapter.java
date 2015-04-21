/*
 * Copyright (c) 2015 Coder.HanXin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
