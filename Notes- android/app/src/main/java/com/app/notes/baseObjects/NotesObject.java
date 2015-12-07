package com.app.notes.baseObjects;

import java.io.Serializable;

/**
 * Created by Saurabh on 11-11-2015.
 */
public class NotesObject implements Serializable {
    String _id, course_id, sem_id, teacher;
    float rating;

    public String getId() {
        return _id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getSem_id() {
        return sem_id;
    }

    public void setSem_id(String sem_id) {
        this.sem_id = sem_id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
