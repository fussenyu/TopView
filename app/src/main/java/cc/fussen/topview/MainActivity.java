package cc.fussen.topview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartScrollView smartScrollView = (SmartScrollView) findViewById(R.id.scrollview);

        smartScrollView.setTopView(R.id.topview);
    }
}
