package ch.ethz.inf.mytodo.io;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import ch.ethz.inf.mytodo.model.Category;
import ch.ethz.inf.mytodo.model.ToDo;

/**
 * Created by m on 24/11/15.
 */
public class IO {

    public ArrayList LoadData(Context context, Category category) {
        int i;
        ArrayList dataset = new ArrayList();


        String str = readFromFile(context, category);

        try {
            JSONObject jObject = new JSONObject(str);

        JSONArray jArray;

        jArray = jObject.getJSONArray("ToDoList");

        for (i = 0; i < jArray.length(); i++) {
            dataset.add(new ToDo(jArray.getJSONObject(i)));

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataset;

    }

    public void WriteData(ArrayList dataset, Context context, Category category){
        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();

        try{
            for (Object obj : dataset) {
                ToDo todo = (ToDo) obj;
                jArray.put(todo.toJSON());

            }
        jObject.put("ToDoList", jArray);

        writeToFile(jObject.toString(),context, category);
        } catch (JSONException e) {
        e.printStackTrace();
        }

    }

    private void writeToFile(String data, Context context, Category category) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(File(category), Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile(Context context, Category category) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(File(category));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    private String File(Category category) {
        switch (category) {
            case Personal:
                return "personal.txt";

            case ETH:
                return "eth.txt";

            case TVShows:
                return "tvshows.txt";

            case Books:
                return "books.txt";

            default:
                return "";
        }
    }

}
