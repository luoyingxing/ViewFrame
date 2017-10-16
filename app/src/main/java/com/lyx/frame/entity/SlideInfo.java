package com.lyx.frame.entity;

import com.lyx.frame.view.SlideView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SlideInfo
 * <p>
 * author:  luoyingxing
 * date: 2017/10/16.
 */

public class SlideInfo implements Serializable {
    private String id;
    @SlideView.ImageUrl
    private String image;
    private String title;

    public SlideInfo() {
    }

    public SlideInfo(String id, String image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    public static List<SlideInfo> getDefaultList() {
        List<SlideInfo> list = new ArrayList<>();
        list.add(new SlideInfo("1", "http://img5.imgtn.bdimg.com/it/u=3746587378,3636100835&fm=27&gp=0.jpg", "数字科技"));
        list.add(new SlideInfo("2", "http://img5.imgtn.bdimg.com/it/u=4058681919,1944079704&fm=200&gp=0.jpg", "智能商务"));
        list.add(new SlideInfo("3", "http://img4.imgtn.bdimg.com/it/u=2974164286,4162358356&fm=200&gp=0.jpg", "手机智能"));
        list.add(new SlideInfo("4", "http://img.damuzzz.com/file/upload/201405/27/16-41-27-39-1.jpg", "科技前沿"));
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}