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
import com.addict.easynotes.activitys.NewNoteActivity;
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
    private int mScrollOffset = 4;
    List<Note> noteList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize data
        noteList = new NoteDao(getActivity()).searchByUpdateTimeFrom(DateUtils.getCurrentDate());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);

        /******************BEGIN Initialize RecyclerView***************************/
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.notes_list_recycler);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
//        setRecyclerViewLayoutManager();

        //set divider
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Note note = mAdapter.getList().get(position);
                Intent intent = new Intent(getActivity(),NewNoteActivity.class);
                intent.putExtra("noteId", note.getNoteId());
                startActivity(intent);
            }
        }));

        // Set MyRecyclerAdapter as the adapter for RecyclerView.
        mAdapter = new MyRecyclerAdapter(noteList);
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
    public void onResume(){
        super.onResume();

        noteList = new NoteDao(getActivity()).searchByUpdateTimeFrom(DateUtils.getCurrentDate());
        mAdapter.refresh(noteList);
        setGravityForFab();
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;


        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

//        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    // Set gravity for fab by Settings
    private void setGravityForFab(){
        SharedPreferences sp = getActivity().getSharedPreferences(getActivity().getPackageName()+"_preferences", Context.MODE_PRIVATE);
        String gravity = sp.getString("gravity","right");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin=(int) getActivity().getResources().getDimension(R.dimen.spacing_small);

        if(gravity.equals("right")){
            lp.gravity = Gravity.BOTTOM|Gravity.RIGHT;
            lp.rightMargin = (int) getActivity().getResources().getDimension(R.dimen.spacing_small);
        }else if(gravity.equals("center")){
            lp.gravity = Gravity.BOTTOM|Gravity.CENTER;
        }else if(gravity.equals("left")){
            lp.gravity = Gravity.BOTTOM|Gravity.LEFT;
            lp.leftMargin = (int) getActivity().getResources().getDimension(R.dimen.spacing_small);
        }

        mFab.setLayoutParams(lp);
    }
}
