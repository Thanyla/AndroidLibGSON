package com.example.androidlibgson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private static final String URL = "https://jsonplaceholder.typicode.com/posts/";
    private List<HashMap<String, String>> listPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.listView);
        listPosts = new ArrayList();

        ObtencaoDePosts odp = new ObtencaoDePosts();
        odp.execute();
    }
    private class ObtencaoDePosts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Download JSON" ,Toast.LENGTH_LONG ).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Auxiliar aux = new Auxiliar();
            String jsonStr = aux.conectar(URL);

            try {
                Gson gson = new Gson();
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Post p = new Post();
                    p = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Post.class);

                    HashMap<String, String> list = new HashMap<>();
                    list.put("userId", String.valueOf(p.getUserId()));
                    list.put("id", String.valueOf(p.getId()));
                    list.put("title", p.getTitle());
                    list.put("body", p.getBody());

                    listPosts.add(list);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }//method

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                    listPosts,
                    R.layout.item_lista,
                    new String[]{"id","title","body"},
                    new int[]{R.id.textViewNome, R.id.textViewTitle, R.id.textViewBody});

            lv.setAdapter(adapter);

        }//method
    }//inner class
}
