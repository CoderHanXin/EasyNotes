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

package com.addict.easynotes.dao;


import android.content.Context;

import com.addict.easynotes.models.Note;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteDao {

    private Context context;
    private RuntimeExceptionDao<Note, Integer> noteRuntimeDao;
    private DatabaseHelper helper;

    public NoteDao(Context context) {
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
        this.noteRuntimeDao = helper.getRuntimeDao(Note.class);
    }

    public void createOrUpdate(Note note) {
        noteRuntimeDao.createOrUpdate(note);
    }

    public void create(Note note) {
        noteRuntimeDao.create(note);
    }

    public Note searchById(Integer noteId) {
        Note note = null;
        try {
            List<Note> notes = noteRuntimeDao.queryBuilder().where().eq("noteId", noteId).query();
            if (notes.size() > 0) {
                note = notes.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return note;
    }

    public List<Note> searchAll() {
        List<Note> notes = noteRuntimeDao.queryForAll();
        return notes;
    }

    public List<Note> searchByUpdateTimeFrom(Date from) {
        List<Note> notes = new ArrayList<>();
        try {
            notes = noteRuntimeDao.queryBuilder().orderBy("updateTime", false).where().ge("updateTime", from).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    public List<Note> searchByUpdateTimeToAndPage(Date to, Long start, Long count) {
        List<Note> notes = new ArrayList<>();
        try {
            notes = noteRuntimeDao.queryBuilder().orderBy("updateTime", false).offset(start).limit(count).where().lt("updateTime", to).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    public int deleteById(Integer noteId) {
        int result = 0;
        try {
            DeleteBuilder<Note, Integer> deleteBuilder = noteRuntimeDao.deleteBuilder();
            deleteBuilder.where().eq("noteId", noteId);
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
