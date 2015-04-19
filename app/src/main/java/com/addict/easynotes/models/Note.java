package com.addict.easynotes.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "note")
public class Note {

    @DatabaseField(generatedId = true)
    private int noteId;

    @DatabaseField(canBeNull = false)
    private String content;

    @DatabaseField(canBeNull = false)
    private Date updateTime;

    public Note() {
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", content='" + content + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
