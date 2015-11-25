package ch.ethz.inf.mytodo.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 23/11/15.
 */
public class ToDo {
    public ToDo(String title, String dueDate, Boolean done) {
        this.title = title;
        this.dueDate = dueDate;
        this.done = done;
    }

    public ToDo(JSONObject todo) throws JSONException {
        this.title = todo.get("title").toString();
        this.dueDate =  todo.get("date").toString();
        this.done = Boolean.valueOf(todo.get("done").toString());
    }

    private String title;
    private String dueDate;
    private Boolean done;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String  getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("title", title);
        obj.put("date", dueDate);
        obj.put("done", done.toString());

       return obj;
    }
}
