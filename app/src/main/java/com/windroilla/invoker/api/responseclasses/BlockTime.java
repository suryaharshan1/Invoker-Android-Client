package com.windroilla.invoker.api.responseclasses;

/**
 * Created by Surya Harsha Nunnaguppala on 13/11/15.
 */
public class BlockTime {
    public int id;
    public String starttime;
    public String endtime;
    public String created_time;
    public int course_id;

    public int getId() {
        return id;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getCreated_time() {
        return created_time;
    }

    public int getCourse_id() {
        return course_id;
    }
}
