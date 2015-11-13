package com.windroilla.invoker.api.responseclasses;

import java.util.List;

/**
 * Created by Surya Harsha Nunnaguppala on 13/11/15.
 */
public class BlockTimeList {
    public String access_time;
    public List<BlockTime> blockTimes;

    public String getAccess_time() {
        return access_time;
    }

    public List<BlockTime> getBlockTimes() {
        return blockTimes;
    }
}
