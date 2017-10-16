package com.lyx.frame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lyx.frame.annotation.Id;
import com.lyx.frame.annotation.IdParser;
import com.lyx.frame.entity.SlideInfo;
import com.lyx.frame.view.SlideView;

public class MainActivity extends AppCompatActivity {
    @Id(id = R.id.sv_home)
    private SlideView mSlideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IdParser.inject(this);

        mSlideView.init(SlideInfo.getDefaultList());
        mSlideView.setOnItemClickListener(new SlideView.OnItemClickListener<SlideInfo>() {
            @Override
            public void onItemClick(SlideInfo info, int position) {
                Toast.makeText(MainActivity.this, info.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}