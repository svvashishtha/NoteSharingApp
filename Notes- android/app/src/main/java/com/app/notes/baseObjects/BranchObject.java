package com.app.notes.baseObjects;

/**
 * Created by Saurabh on 05-11-2015.
 */
public class BranchObject {
    String name,_id;

    public BranchObject(String name, String _id) {
        this.name = name;
        this._id = _id;
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
}
