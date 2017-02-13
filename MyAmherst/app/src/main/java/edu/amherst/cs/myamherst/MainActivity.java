package edu.amherst.cs.myamherst;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;





    public class MainActivity extends AppCompatActivity {

        private Button button;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            button = (Button) findViewById(R.id.button_id);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selfDestruct();
                }
            });
        }

        public void selfDestruct() {

            Intent intent = new Intent(this, icons.class);
            startActivity(intent);

        }


    }