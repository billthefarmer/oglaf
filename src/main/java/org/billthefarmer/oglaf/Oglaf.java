//  Oglaf - NS(generally)FW
//
//  Copyright (C) 2019	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.billthefarmer.oglaf;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.text.DateFormat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Oglaf extends Activity
{
    public static final String URL = "https://oglaf.com";
    public static final String TEXT_PLAIN = "text/plain";

    private WebView webView;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        webView = findViewById(R.id.webview);

        if (webView != null)
        {
            // Enable javascript, Oglaf doesn't work unless JavaScript
            // is enabled
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);

            // Enable zoom
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);

            // Follow links and set title
            webView.setWebViewClient(new WebViewClient()
            {
                // onPageFinished
                @Override
                public void onPageFinished(WebView view, String url)
                {
                    // Get page title
                    if (view.getTitle() != null)
                        setTitle(view.getTitle());

                    if (view.canGoBack())
                        getActionBar().setDisplayHomeAsUpEnabled(true);

                    else
                        getActionBar().setDisplayHomeAsUpEnabled(false);
                }
            });

            if (savedInstanceState != null)
                // Restore state
                webView.restoreState(savedInstanceState);

            else
                // load Oglaf
                webView.loadUrl(URL);
        }
    }

    // On save instance state
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if (webView != null)
            // Save state
            webView.saveState(outState);
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
            // Home
        case android.R.id.home:
            // Back navigation
            if (webView != null && webView.canGoBack())
                webView.goBack();

            else
                finish();
            break;

            // Share
        case R.id.action_share:
            share();
            break;

            // About
        case R.id.action_about:
            about();
            break;

        default:
            return false;
        }

        return true;
    }

    // On back pressed
    @Override
    public void onBackPressed()
    {
        // Back navigation
        if (webView != null && webView.canGoBack())
            webView.goBack();

        else
            finish();
    }

    // share
    public void share()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(TEXT_PLAIN);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.appName) +
                        ": " + webView.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        startActivity(Intent.createChooser(intent, null));
    }

    // about
    private void about()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about);

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        SpannableStringBuilder spannable =
            new SpannableStringBuilder(getText(R.string.version));
        Pattern pattern = Pattern.compile("%s");
        Matcher matcher = pattern.matcher(spannable);
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              BuildConfig.VERSION_NAME);
        matcher.reset(spannable);
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              dateFormat.format(BuildConfig.BUILT));
        builder.setMessage(spannable);

        // Add the button
        builder.setPositiveButton(android.R.string.ok, null);

        // Create the AlertDialog
        Dialog dialog = builder.show();

        // Set movement method
        TextView text = dialog.findViewById(android.R.id.message);
        if (text != null)
            text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
