package com.addict.easynotes.fragments;

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
    private SwipeRefreshLayout refreshLayout;
    private StickyListHeadersListView stickyList;
    // 每次下拉刷新加载的数据
    private Long rowCount = 10L;


    List<Note> noteList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteList = new NoteDao(getActivity()).searchByUpdateTimeToAndPage(DateUtils.getCurrentDate(), 0L, rowCount);
        Collections.reverse(noteList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes_history, container, false);

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);


        mAdapter = new MyStickyListAdapter(getActivity(), noteList);
        stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.history_list);
        stickyList.setOnItemClickListener(this);
        stickyList.setOnHeaderClickListener(this);
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        stickyList.setAdapter(mAdapter);
        return rootView;
    }

    /**
     * 下拉刷新事件
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                List<Note> newList = new NoteDao(getActivity()).searchByUpdateTimeToAndPage(DateUtils.getCurrentDate(), (long)noteList.size(), rowCount);
                Collections.reverse(newList);
                newList.addAll(noteList);
                noteList = newList;
                mAdapter.refresh(noteList);
            }
        }, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtils.showLong("note update time:" + DateUtils.formatDateToString(noteList.get(position).getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        TextView tv = (TextView) header.findViewById(R.id.header_text);
        tv.getText();
        ToastUtils.showLong("header:" + tv.getText());
    }
}
