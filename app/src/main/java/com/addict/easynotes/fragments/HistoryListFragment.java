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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.addict.easynotes.R;
import com.addict.easynotes.activities.NewNoteActivity;
import com.addict.easynotes.dao.NoteDao;
import com.addict.easynotes.models.Note;
import com.addict.easynotes.utils.DateUtils;
import com.addict.easynotes.utils.ToastUtils;
import com.addict.easynotes.views.adapters.MyStickyListAdapter;

import java.util.Collections;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class HistoryListFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener,
        StickyListHeadersListView.OnHeaderClickListener {

    private MyStickyListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private StickyListHeadersListView mStickyList;
    private Long mRowCount = 10L;
    private List<Note> mNoteList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteList = new NoteDao(getActivity()).searchByUpdateTimeToAndPage(DateUtils.getCurrentDate(), 0L, mRowCount);
        Collections.reverse(mNoteList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes_history, container, false);

        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);


        mAdapter = new MyStickyListAdapter(getActivity(), mNoteList);
        mStickyList = (StickyListHeadersListView) rootView.findViewById(R.id.listView_history);
        mStickyList.setOnItemClickListener(this);
        mStickyList.setOnHeaderClickListener(this);
        mStickyList.setDrawingListUnderStickyHeader(true);
        mStickyList.setAreHeadersSticky(true);
        mStickyList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mNoteList = new NoteDao(getActivity()).searchByUpdateTimeToAndPage(DateUtils.getCurrentDate(), 0L, (long) mNoteList.size());
        Collections.reverse(mNoteList);
        mAdapter.refresh(mNoteList);
    }

    /**
     * pull to refresh
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                List<Note> newList = new NoteDao(getActivity()).searchByUpdateTimeToAndPage(DateUtils.getCurrentDate(), (long) mNoteList.size(), mRowCount);
                Collections.reverse(newList);
                newList.addAll(mNoteList);
                mNoteList = newList;
                mAdapter.refresh(mNoteList);
            }
        }, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Note note = mAdapter.getList().get(position);
        Intent intent = new Intent(getActivity(), NewNoteActivity.class);
        intent.putExtra("noteId", note.getNoteId());
        startActivity(intent);
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        TextView tv = (TextView) header.findViewById(R.id.textView_header);
        tv.getText();
        ToastUtils.showLong("header:" + tv.getText());
    }
}
