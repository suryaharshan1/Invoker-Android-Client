package com.windroilla.invoker.api.requestclasses;

import java.util.ArrayList;

/**
 * Created by Surya Harsha Nunnaguppala on 13/11/15.
 */
public class RequestSetUserCourseList {
    public String mobile_id;
    public ArrayList<Integer> courses = new ArrayList<Integer>();

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public void addCourseId(int id) {
        courses.add(Integer.valueOf(id));
    }
}
