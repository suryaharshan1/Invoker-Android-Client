package com.windroilla.invoker.api.responseclasses;

/**
 * Created by Surya Harsha Nunnaguppala on 12/11/15.
 */
public class Course {
    public int id;
    public String name;
    public int institute_id;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getInstitute_id() {
        return institute_id;
    }
}
