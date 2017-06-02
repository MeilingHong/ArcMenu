package com.example.menu;

import com.example.menu.ArcMenu.ArcMenuListener;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity implements ArcMenuListener {

    private ArcMenu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = (ArcMenu) findViewById(R.id.id_menu);
        menu.setOnArcMenuListener(this);
    }


    public void dealMenuClick(View v) {
        Toast.makeText(this, "这是"+v.getTag(), Toast.LENGTH_SHORT).show();
    }

}
