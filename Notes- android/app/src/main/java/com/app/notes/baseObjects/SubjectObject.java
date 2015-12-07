package com.app.notes.baseObjects;

import java.io.Serializable;

/**
 * Created by Saurabh on 05-11-2015.
 */
public class SubjectObject implements Serializable {
    String name, _id, code, sem/*, UserRating*/;

   /* public String getUserRating() {
        return UserRating;
    }

    public void setUserRating(String userRating) {
        UserRating = userRating;
    }
*/

    public SubjectObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }
}
