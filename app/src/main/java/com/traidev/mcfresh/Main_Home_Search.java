package com.traidev.mcfresh;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main_Home_Search extends AppCompatActivity implements View.OnClickListener {

    ImageView removeFrag;
    EditText SearchHomeEdit;
    TextView t1,t2,t3,t4,t5,t6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_search_activity);

        removeFrag = findViewById(R.id.removeFrag);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        t6 = findViewById(R.id.t6);

        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);


        SearchHomeEdit = findViewById(R.id.searchHome);
        removeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_Home_Search.super.onBackPressed();
            }
        });

        SearchHomeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   toSearchHome(SearchHomeEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.t1:
                toSearchHome(t1.getText().toString());
                break;
            case R.id.t2:
                toSearchHome(t2.getText().toString());
                break;
            case R.id.t3:
                toSearchHome(t3.getText().toString());
                break;
            case R.id.t4:
                toSearchHome(t4.getText().toString());
                break;
            case R.id.t5:
                toSearchHome(t5.getText().toString());
                break;
            case R.id.t6:
                toSearchHome(t6.getText().toString());
                break;
            default:
                return;

        }

    }

    void toSearchHome(String text)
    {
        Intent id = new Intent(getApplicationContext(), SearchShopsHome.class);
        id.putExtra("searchData",text);
        id.putExtra("searchType", "homeSearch");
        startActivity(id);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void backtoHome(View view) {
        super.onBackPressed();
    }



}
