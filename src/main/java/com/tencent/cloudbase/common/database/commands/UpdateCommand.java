package com.tencent.cloudbase.common.database.commands;



import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class UpdateCommand {
    private List<List<Object>> actions;

    public JSONObject toJSON() throws JSONException {
        JSONObject actionMap = new JSONObject();
        actionMap.put("_actions", actions);

        return actionMap;
    }

    public UpdateCommand( List<List<Object>> actions, List<Object> step) {
        this.actions = actions;
        if (step.size() > 0) {
            this.actions.add(step);
        }
    }
}
