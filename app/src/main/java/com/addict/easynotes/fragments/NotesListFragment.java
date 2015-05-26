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

package com.addict.easynotes.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.addict.easynotes.R;
import com.addict.easynotes.activities.NewNoteActivity;
import com.addict.easynotes.dao.NoteDao;
import com.addict.easynotes.models.Note;
import com.addict.easynotes.utils.DateUtils;
import com.addict.easynotes.views.adapters.DividerItemDecoration;
import com.addict.easynotes.views.adapters.MyRecyclerAdapter;
import com.addict.easynotes.views.adapters.RecyclerItemClickListener;
import com.github.clans.fab.FloatingActionButton;

import java.util.List;


public class NotesListFragment extends BaseFragment {

    protected RecyclerView mRecyclerView;
    protected MyRecyclerAdapter mAdapter;
    protected FloatingActionButton mFab;
    List<Note> mNoteList;
    private int mScrollOffset = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize data
        mNoteList = new NoteDao(getActivity()).searchByUpdateTimeFrom(DateUtils.getCurrentDate());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);

        /******************BEGIN Initialize RecyclerView***************************/
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_notes_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //set divider
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Note note = mAdapter.getList().get(position);
                Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                intent.putExtra("noteId", note.getNoteId());
                startActivity(intent);
            }
        }));

        // Set MyRecyclerAdapter as the adapter for RecyclerView.
        mAdapter = new MyRecyclerAdapter(mNoteList);
        mRecyclerView.setAdapter(mAdapter);
        /******************END Initialize RecyclerView*************************/

        // Set FloatingActionButton listener
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                startActivity(intent);
            }
        });

        // Set Gravity for fab
        setGravityForFab();

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {
                        mFab.hide(true);
                    } else {
                        mFab.show(true);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mNoteList = new NoteDao(getActivity()).searchByUpdateTimeFrom(DateUtils.getCurrentDate());
        mAdapter.refresh(mNoteList);
        setGravityForFab();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    // Set gravity for fab by Settings
    private void setGravityForFab() {
        SharedPreferences sp = getActivity().getSharedPreferences(getActivity().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String gravity = sp.getString("gravity", "right");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = (int) getActivity().getResources().getDimension(R.dimen.spacing_normal);

        switch (gravity) {
            case "right":
                lp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                lp.rightMargin = (int) getActivity().getResources().getDimension(R.dimen.spacing_normal);
                break;
            case "center":
                lp.gravity = Gravity.BOTTOM | Gravity.CENTER;
                break;
            case "left":
                lp.gravity = Gravity.BOTTOM | Gravity.LEFT;
                lp.leftMargin = (int) getActivity().getResources().getDimension(R.dimen.spacing_normal);
        }

        mFab.setLayoutParams(lp);
    }
}
