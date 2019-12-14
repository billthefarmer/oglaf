package org.billthefarmer.oglaf;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class Oglaf extends Activity
{
    private boolean dark = true;
    private ImageView imageView;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (dark)
            setTheme(R.style.AppDarkTheme);

        setContentView(R.layout.main);

        imageView = findViewById(R.id.oglaf);
    }

    // On create option menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it
        // is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // On options item

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Get id

        int id = item.getItemId();
        switch (id)
        {
        }

        return true;
    }
}
