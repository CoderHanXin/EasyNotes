package com.addict.easynotes.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.addict.easynotes.R;
import com.addict.easynotes.dao.NoteDao;
import com.addict.easynotes.models.Note;
import com.addict.easynotes.utils.ToastUtils;
import com.github.clans.fab.FloatingActionButton;

import java.util.Date;


public class NewNoteActivity extends BaseActivity {

    private EditText newNote;
    private TextView newNoteTextView;
    private Toolbar toolbar;
    private Boolean isEdit = true;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        newNote = (EditText) findViewById(R.id.newNote_editText);
        newNoteTextView = (TextView) findViewById(R.id.newNote_textView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        note = new Note();
        Intent intent = getIntent();
        Integer noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1) {
            note = new NoteDao(this).searchById(noteId);
            newNote.setText(note.getContent());
            newNoteTextView.setText(note.getContent());
            toolbar.setTitle(R.string.view_note);
            newNote.setVisibility(View.INVISIBLE);
            newNoteTextView.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(newNote.getWindowToken(), 0);
            isEdit = false;
        }else {
            toolbar.setTitle(R.string.new_note);
            newNote.setVisibility(View.VISIBLE);
            newNoteTextView.setVisibility(View.INVISIBLE);
            newNote.requestFocus();
            isEdit = true;
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        newNoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditState();
            }
        });

        // Set FloatingActionButton listener
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newNote.getText().toString().isEmpty()){
                    ToastUtils.showShort(R.string.toast_emptyText);
                    return;
                }
                if(note.getContent() == null || !note.getContent().equals(newNote.getText().toString())){
                    saveChanges();
                    return;
                }
                if(note.getContent().equals(newNote.getText().toString())){
                    finish();
                    return;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        if(isEdit){
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(false);
        }else {
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
            if(newNote.getText().toString().isEmpty()){
                finish();
                return true;
            }
            if(note.getContent() == null || !note.getContent().equals(newNote.getText().toString())){
                saveChanges();
                return true;
            }
            if(note.getContent().equals(newNote.getText().toString())){
                finish();
                return true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_save:
                    if(newNote.getText().toString().isEmpty()){
                        ToastUtils.showShort(R.string.toast_emptyText);
                        break;
                    }
                    if(note.getContent() == null || !note.getContent().equals(newNote.getText().toString())){
                        saveChanges();
                        break;
                    }
                    if(note.getContent().equals(newNote.getText().toString())){
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
                                    if(note != null){
                                        new NoteDao(NewNoteActivity.this).deleteById(note.getNoteId());
                                    }
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.alert_btn_cancel,null)
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
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(newNote.getText().toString().isEmpty()){
                return super.onKeyDown(keyCode, event);
            }
            if(note.getContent() == null || !note.getContent().equals(newNote.getText().toString())){
                saveChanges();
                return super.onKeyDown(keyCode, event);
            }
            if(note.getContent().equals(newNote.getText().toString())){
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void saveChanges(){
        new AlertDialog.Builder(NewNoteActivity.this)
                .setTitle(R.string.alert_title_save)
                .setMessage(R.string.alert_message_save)
                .setPositiveButton(R.string.alert_btn_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        note.setContent(newNote.getText().toString());
                        note.setUpdateTime(new Date(System.currentTimeMillis()));
                        new NoteDao(NewNoteActivity.this).createOrUpdate(note);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(R.string.alert_btn_cancel,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void setEditState(){
        newNote.setSelection(newNote.getText().toString().length());
        newNote.setVisibility(View.VISIBLE);
        newNoteTextView.setVisibility(View.INVISIBLE);
        getSupportActionBar().setTitle(R.string.edit_note);
        isEdit = true;
        NewNoteActivity.this.invalidateOptionsMenu();
    }
}
