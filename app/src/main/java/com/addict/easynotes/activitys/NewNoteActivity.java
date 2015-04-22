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

package com.addict.easynotes.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.addict.easynotes.R;
import com.addict.easynotes.dao.NoteDao;
import com.addict.easynotes.models.Note;
import com.addict.easynotes.utils.ToastUtils;
import com.github.clans.fab.FloatingActionButton;

import java.util.Date;


public class NewNoteActivity extends BaseActivity {

    protected FloatingActionButton mFab;
    private EditText mEditTextContent;
    private TextView mTextViewContent;
    private Toolbar mToolbar;
    private Boolean isEdit = true;
    private Note mNote;
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_save:
                    if (mEditTextContent.getText().toString().isEmpty()) {
                        ToastUtils.showShort(R.string.toast_emptyText);
                        break;
                    }
                    if (mNote.getContent() == null || !mNote.getContent().equals(mEditTextContent.getText().toString())) {
                        saveChanges();
                        break;
                    }
                    if (mNote.getContent().equals(mEditTextContent.getText().toString())) {
                        finish();
                        break;
                    }
                    break;
                case R.id.action_edit:
                    setEditState();
                    msg += "Click edit";
                    break;
                case R.id.action_delete:
                    new AlertDialog.Builder(NewNoteActivity.this)
                            .setTitle(R.string.alert_title_delete)
                            .setMessage(R.string.alert_message_delete)
                            .setPositiveButton(R.string.alert_btn_delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mNote != null) {
                                        new NoteDao(NewNoteActivity.this).deleteById(mNote.getNoteId());
                                    }
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.alert_btn_cancel, null)
                            .create()
                            .show();
                    break;
                case R.id.action_share:
                    msg += "Click share";
                    break;
            }

            if (!msg.equals("")) {
                ToastUtils.showShort(msg);
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        mEditTextContent = (EditText) findViewById(R.id.editText_content);
        mTextViewContent = (TextView) findViewById(R.id.textView_content);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mNote = new Note();
        Intent intent = getIntent();
        Integer noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1) {
            mNote = new NoteDao(this).searchById(noteId);
            mEditTextContent.setText(mNote.getContent());
            mTextViewContent.setText(mNote.getContent());
            mToolbar.setTitle(R.string.view_note);
            mEditTextContent.setVisibility(View.INVISIBLE);
            mTextViewContent.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);
            isEdit = false;
        } else {
            mToolbar.setTitle(R.string.new_note);
            mEditTextContent.setVisibility(View.VISIBLE);
            mTextViewContent.setVisibility(View.INVISIBLE);
            mEditTextContent.requestFocus();
            isEdit = true;
            mFab.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);

        mTextViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditState();
            }
        });

        // Set FloatingActionButton listener
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextContent.getText().toString().isEmpty()) {
                    ToastUtils.showShort(R.string.toast_emptyText);
                    return;
                }
                if (mNote.getContent() == null || !mNote.getContent().equals(mEditTextContent.getText().toString())) {
                    saveChanges();
                    return;
                }
                if (mNote.getContent().equals(mEditTextContent.getText().toString())) {
                    finish();
                }
            }
        });
        // Set Gravity for fab
        setGravityForFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGravityForFab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (isEdit) {
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(false);
        } else {
            menu.findItem(R.id.action_save).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            if (mEditTextContent.getText().toString().isEmpty()) {
                finish();
                return true;
            }
            if (mNote.getContent() == null || !mNote.getContent().equals(mEditTextContent.getText().toString())) {
                saveChanges();
                return true;
            }
            if (mNote.getContent().equals(mEditTextContent.getText().toString())) {
                finish();
                return true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mEditTextContent.getText().toString().isEmpty()) {
                return super.onKeyDown(keyCode, event);
            }
            if (mNote.getContent() == null || !mNote.getContent().equals(mEditTextContent.getText().toString())) {
                saveChanges();
                return super.onKeyDown(keyCode, event);
            }
            if (mNote.getContent().equals(mEditTextContent.getText().toString())) {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void saveChanges() {
        new AlertDialog.Builder(NewNoteActivity.this)
                .setTitle(R.string.alert_title_save)
                .setMessage(R.string.alert_message_save)
                .setPositiveButton(R.string.alert_btn_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNote.setContent(mEditTextContent.getText().toString());
                        mNote.setUpdateTime(new Date(System.currentTimeMillis()));
                        new NoteDao(NewNoteActivity.this).createOrUpdate(mNote);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(R.string.alert_btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void setEditState() {
        mEditTextContent.setSelection(mEditTextContent.getText().toString().length());
        mEditTextContent.setVisibility(View.VISIBLE);
        mTextViewContent.setVisibility(View.INVISIBLE);
        mFab.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(R.string.edit_note);
        isEdit = true;
        NewNoteActivity.this.invalidateOptionsMenu();
    }


    // Set gravity for fab by Settings
    private void setGravityForFab() {
        SharedPreferences sp = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String gravity = sp.getString("gravity", "right");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = (int) getResources().getDimension(R.dimen.spacing_normal);

        switch (gravity) {
            case "right":
                lp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                lp.rightMargin = (int) getResources().getDimension(R.dimen.spacing_normal);
                break;
            case "center":
                lp.gravity = Gravity.BOTTOM | Gravity.CENTER;
                break;
            case "left":
                lp.gravity = Gravity.BOTTOM | Gravity.LEFT;
                lp.leftMargin = (int) getResources().getDimension(R.dimen.spacing_normal);
        }

        mFab.setLayoutParams(lp);
    }
}
