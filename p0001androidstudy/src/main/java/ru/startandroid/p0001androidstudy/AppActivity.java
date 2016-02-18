package ru.startandroid.p0001androidstudy;

import android.support.v7.app.AppCompatActivity;

/**
 * @author by Andrey on 2/8/2016.
 */
public class AppActivity extends AppCompatActivity {

    public TestApplication getTestApplication() {
        return (TestApplication) getApplication();
    }
}
