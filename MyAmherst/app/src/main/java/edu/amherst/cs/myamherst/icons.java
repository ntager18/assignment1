package edu.amherst.cs.myamherst;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class icons extends AppCompatActivity {
    private Button breakfast;
    private Button lunch;
    private Button dinner;
    private TextView finalResult;
    private EditText time;
    private String allItems;


    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons);
        time = (EditText) findViewById(R.id.in_time);
        finalResult = (TextView) findViewById(R.id.tv_result);


        breakfast = (Button) findViewById(R.id.breakfast);
        lunch = (Button) findViewById(R.id.lunch);
        dinner = (Button) findViewById(R.id.dinner);

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfDestruct();
            }
        });
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    public void selfDestruct() {
        AsyncTaskRunner myTask = new AsyncTaskRunner();
        myTask.execute("5");
        //Intent intent = new Intent(this, icons.class);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_icons, container, false);

        //Text view only


        //List view
        listview = (ListView) v.findViewById(R.id.listview);

        Document doc  = null;
        try {
            doc = Jsoup.connect("https://www.amherst.edu/campuslife/housing-dining/dining/menu").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element content = doc.getElementById("dining-menu-2017-2-13-Breakfast-menu-listing");
        Elements paras = content.getElementsByTag("p");
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayList<Menu> items = FileUtil.readFromFile(this);

        for (Element para : paras) {
            String paraText = para.text();
            listItems.add(paraText);
        }

        for(int i = 0; i < items.size(); i++){
            Menu mItem = items.get(i);
            listItems.add(mItem.getItems());
        }

       // ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.fragment_menu, listItems);
        //listview.setAdapter(itemsAdapter);
        MenuArrayAdapter adapter = new MenuArrayAdapter(this, items);
        listview.setAdapter(adapter);
        return v;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private class Scraper extends AsyncTaskRunner{

            public ArrayList<ArrayList<ArrayList<String>>> scrapeMenu(String date) throws IOException {

                ArrayList<ArrayList<ArrayList<String>>> menues = new ArrayList<ArrayList<ArrayList<String>>>();
                ArrayList<ArrayList<String>> breakfast = new ArrayList<ArrayList<String>>();
                ArrayList<ArrayList<String>> lunch = new ArrayList<ArrayList<String>>();
                ArrayList<ArrayList<String>> dinner = new ArrayList<ArrayList<String>>();
                menues.add(breakfast);
                menues.add(lunch);
                menues.add(dinner);

                Document doc;
                doc = Jsoup.connect("https://www.amherst.edu/campuslife/housing-dining/dining/menu").get();

                Element day = doc.getElementById("dining-menu-"+date);

                Elements meals = day.getElementsByClass("dining-menu-meal");

                for(int i=0; i<meals.size(); i++){
                    Elements courses = meals.get(i).getElementsByClass("dining-course-name");
                    Elements content = meals.get(i).select("p");

                    for(int j=0; j<courses.size(); j++){
                        if(i==0){
                            breakfast.add(new ArrayList<String>());
                            breakfast.get(j).add(courses.get(j).text());
                            String s = content.get(j).text();

                            int start=0;

                            for(int k = 0; k<s.length(); k++){
                                if(s.substring(k, k+1).equals(";")){
                                    breakfast.get(j).add(s.substring(start,k));
                                    start=k+1;
                                }
                            }
                        }
                        else if(i==1){
                            lunch.add(new ArrayList<String>());
                            lunch.get(j).add(courses.get(j).text());
                            String s = content.get(j).text();

                            int start=0;

                            for(int k = 0; k<s.length(); k++){
                                if(s.substring(k, k+1).equals(";")){
                                    lunch.get(j).add(s.substring(start,k));
                                    start=k+1;
                                }
                            }
                        }
                        else if(i==2){
                            dinner.add(new ArrayList<String>());
                            dinner.get(j).add(courses.get(j).text());
                            String s = content.get(j).text();

                            int start=0;

                            for(int k = 0; k<s.length(); k++){
                                if(s.substring(k, k+1).equals(";")){
                                    dinner.get(j).add(s.substring(start,k));
                                    start=k+1;
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i<menues.size(); i++)
                {
                    System.out.println("IIIII");
                    for (int j = 0; j<menues.get(i).size(); j++)
                    {
                        System.out.println("JJJJ");
                        for (String s: menues.get(i).get(j))
                        {
                            System.out.println(s);
                        }
                    }
                }
                System.out.println(menues.get(0).get(0).get(2));
                return menues;

            }
        }

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress(resp); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(icons.this,
                    "ProgressDialog",
                    "Wait for "+time.getText().toString()+ " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            finalResult.setText(text[0]);

        }
    }


}

