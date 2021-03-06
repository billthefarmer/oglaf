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

package org.billthefarmer.oglaf

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView

import java.text.DateFormat

import java.util.regex.Matcher
import java.util.regex.Pattern

class Oglaf: Activity()
{
    val URL = "https://oglaf.com"
    val TEXT_PLAIN = "text/plain"

    lateinit var webView: WebView

    // Called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        webView = findViewById(R.id.webview)

        // Enable javascript, Oglaf doesn't work unless JavaScript
        // is enabled
        val settings  = webView.getSettings()
        settings.setJavaScriptEnabled(true)

        // Enable zoom
        settings.setBuiltInZoomControls(true)
        settings.setDisplayZoomControls(false)

        // Follow links and set title
        webView.setWebViewClient(object: WebViewClient()
        {
            // onPageFinished
            override fun onPageFinished(view: WebView, url: String)
            {
                // Get page title
                if (view.getTitle() != null)
                setTitle(view.getTitle())

                if (view.canGoBack())
                getActionBar()?.setDisplayHomeAsUpEnabled(true)

                else
                getActionBar()?.setDisplayHomeAsUpEnabled(false)
            }
        })

        if (savedInstanceState != null)
        // Restore state
        webView.restoreState(savedInstanceState)

        else
        // load Oglaf
        webView.loadUrl(URL)
    }

    // On save instance state
    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        // Save state
        webView.saveState(outState)
    }

    // On create option menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        // Inflate the menu; this adds items to the action bar if it
        // is present.
        getMenuInflater().inflate(R.menu.main, menu)
        return true;
    }

    // On options item
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        // Get id

        val id = item.getItemId()
        when (id)
        {
            // Home
            android.R.id.home ->
            // Back navigation
            if (webView.canGoBack())
                webView.goBack()

            else
                finish()

            // Refresh
            R.id.action_refresh ->
            refresh()

            // Share
            R.id.action_share ->
            share()

            // About
            R.id.action_about ->
            about()

        else ->
            return false;
        }

        return true;
    }

    // On back pressed
    override fun onBackPressed()
    {
        // Back navigation
        if (webView.canGoBack())
            webView.goBack()

        else
            finish()
    }

    // refresh
    private fun refresh()
    {
        webView.reload()
    }

    // share
    private fun share()
    {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType(TEXT_PLAIN)
        val title =
            String.format("%s: %s", getString(R.string.appName), getTitle())
        intent.putExtra(Intent.EXTRA_TITLE, title)
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl())
        startActivity(Intent.createChooser(intent, null))
    }

    // about
    private fun about()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.appName)

        val dateFormat = DateFormat.getDateTimeInstance()
        val spannable = SpannableStringBuilder(getText(R.string.version))
        val pattern = Pattern.compile("%s")
        val matcher = pattern.matcher(spannable)
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              BuildConfig.VERSION_NAME)
        matcher.reset(spannable)
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              dateFormat.format(BuildConfig.BUILT))
        builder.setMessage(spannable)

        // Add the button
        builder.setPositiveButton(android.R.string.ok, null)

        // Create the AlertDialog
        val dialog = builder.show()

        // Set movement method
        val text: TextView = dialog.findViewById(android.R.id.message)
        text.setMovementMethod(LinkMovementMethod.getInstance())
    }
}
