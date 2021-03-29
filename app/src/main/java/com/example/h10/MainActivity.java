package com.example.h10;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    WebView web;
    EditText webAddress;
    boolean historyBlock = false;

    ArrayList<String> addressList = new ArrayList<String>();
    ListIterator<String> it = addressList.listIterator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.web);
        webAddress = findViewById(R.id.editText);
        web.getSettings().setJavaScriptEnabled(true);

        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                /* Checks if the loaded page is already in page history.
                 * Mostly prevents adding the same URL when the page gets loaded twice.
                 * Which happens pretty often unfortunately.
                 * Defined separately to avoid errors in program functionality. */
                if (it.hasPrevious()){
                    if (url.equals(it.previous())){
                        it.next();
                        return;
                    }
                    it.next();
                }
                /* Adds the first URL when page history is empty. */
                if (addressList.size() == 0){
                    it.add(url);
                    return;
                }
                else {
                    /* History isn't recorded when cycling through page history. */
                    if (historyBlock == false) {

                        /* Maximum size of page history. Removes the oldest page if
                        * there's too many pages.*/
                        while (addressList.size() >= 10) {
                            while (it.hasNext()) {
                                it.next();
                            }
                            it.remove();
                        }
                        while (it.hasPrevious()) {
                            it.previous();
                        }
                        it.add(url);
                    }
                    /* Clears the page history from the current page onward,
                    * if cycling through pages with "prev" and "next" buttons.*/
                    else {
                        if (it.hasPrevious()) {
                            if (url.equals(it.previous())) {
                                it.next();
                                return;
                            }
                        }
                        it.next();

                        if (it.hasNext()) {
                            if (url.equals(it.next())) {
                                it.previous();
                                return;
                            }
                        }
                        it.previous();

                        while(it.hasPrevious()){
                            it.previous();
                            it.remove();
                        }
                        it.add(url);
                        historyBlock = false;


                    }
                }

            }
        });

        webAddress.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               String editAddress = webAddress.getText().toString();
               if (editAddress.equals("index.html")){
                    web.loadUrl("file:///android_asset/index.html");
               } else {
                   String address = "http://<d>";
                   address = address.replace("<d>", editAddress);
                   web.loadUrl(address);
               }

           }
        });

    }

    public void reloadPage(View v){
        String currentAddress = web.getUrl();
        web.loadUrl(currentAddress);
    }

    public void javaScriptShoutOut(View v){
        web.evaluateJavascript("javascript:shoutOut()", null);
    }

    public void javaScriptInitialize(View v){
        web.evaluateJavascript("javascript:initialize()", null);
    }

    public void previousPage(View v){
        if (it.hasNext()){
            web.loadUrl(it.next());
            historyBlock = true;
        }
    }

    public void nextPage(View v){
        if (it.hasPrevious()){
            web.loadUrl(it.previous());
            historyBlock = true;
        }
        else {
            historyBlock = false;
        }
    }

    public void printHistory(View v){
        for (String object: addressList){
            System.out.println(object);
        }
        System.out.println(addressList.size());
    }


}