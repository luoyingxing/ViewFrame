# ViewFrame
当下有很多关于轮播图的控件，由于发现它们的种种弊端，最终我自撸了一个，主要有以下优点：

 1. 实现自动无限循环切换；
 2. 当图片数量小于2时，手动切换时，不会出现空白页面； 
 3. 支持点击事件；
 4. 支持通过xml配置相关参数；
 5. **最重要的一点，支持泛型数据源，通过注解配置和反射机制实现轮播效果**
 
## 示例 ##
![轮播图演示](http://img.blog.csdn.net/20171013175322774?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHVveWluZ3hpbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 依赖 ##
本文的图片加载依赖fresco框架，需要在build.grade文件配置以下依赖：
```
    compile 'com.facebook.fresco:fresco:0.8.1'
```
使用时需要在项目工程的Application中配置：
```
	@Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
```

## 使用 ##
一、新建MainActivity
在activity_main.xml 里面引入SlideView；
```
<com.lyx.frame.view.SlideView
            android:id="@+id/sv_main"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            slide:dotFocus="@drawable/icon_home_slide_focus"
            slide:dotNormal="@drawable/icon_home_slide_normal"
            slide:heightScale="1"
            slide:intervalTime="3"
            slide:placeHolderImage="@mipmap/image_empty_fresco"
            slide:widthScale="2" />
```
说明一下，其中的一些配置参数可以通过xml配置，也可以动态配置，都配有相应的方法。

```
slide:dotFocus="@drawable/icon_home_slide_focus"  //底部小圆圈（选中时）
slide:dotNormal="@drawable/icon_home_slide_normal"  //底部小圆圈（未选中时）
slide:widthScale="2"   //轮播图的相对宽度 
slide:heightScale="1"   //轮播图的相对高度
slide:intervalTime="3"   //轮播图的切换时间间隔
slide:placeHolderImage="@mipmap/image_empty_fresco"   //轮播图的占位符
```

在MainActivity中设置数据源和监听事件：

```
public class MainActivity extends AppCompatActivity
	 private SlideView mSlideView;
	 
	 @Override
     protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);
	      
		  mSlideView = findViewById(R.id.sv_main);
		  mSlideView.init(getDefaultSlideList());	
		  mSlideView.setOnItemClickListener(new SlideView.OnItemClickListener<SlideImage>() {

            @Override
            public void onItemClick(SlideImage result, int position) {
                 Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

这其中传入的泛型类型是SlideImage.class，**切记：需要要在SlideImage.class中增加注解，用来标注图片地址的字段：**
```
public class SlideImage {

    @SlideView.ImageUrl
    private String image;  //此处的@SlideView.ImageUrl注解是SlideView的一个内部类
    
    private String text;
}
```


配置到此结束，是不是很简单呢，而通过泛型配置数据源，方面了很多。