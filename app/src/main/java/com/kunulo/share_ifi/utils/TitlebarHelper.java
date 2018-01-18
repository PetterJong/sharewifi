package com.kunulo.share_ifi.utils;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunulo.share_ifi.R;


/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TitlebarHelper {
    public ImageView ivLeft;
    public Button btLeft;
    public TextView tvLeft;
    public TextView tvTitle;
    public Button btRight;

    public TitlebarHelper(View v){
        ivLeft = (ImageView) v.findViewById(R.id.iv_left);
        btLeft = (Button) v.findViewById(R.id.bt_left);
        tvLeft = (TextView) v.findViewById(R.id.tv_left);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        btRight = (Button) v.findViewById(R.id.bt_right);
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int titleRes) {
        tvTitle.setText(titleRes);
    }

    public void setBackAndTitle(String title){
        ivLeft.setImageResource(R.mipmap.video_back);
        tvTitle.setText(title);
    }
    public void setBackAndTitle(int titleRes){
        ivLeft.setImageResource(R.mipmap.video_back);
        tvTitle.setText(titleRes);
    }
}
