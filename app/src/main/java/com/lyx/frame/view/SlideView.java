package com.lyx.frame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.frame.R;
import com.lyx.frame.animator.TransFormFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SlideView
 * <p/>
 * author:  luoyingxing
 * date: 2017/10/16.
 */
public class SlideView<T> extends FrameLayout {
    private Context mContext;
    private ViewPager mViewPager;
    /**
     * 轮播图数量
     */
    private int mImageCount;
    /**
     * 轮播图切换时间间隔（单位：秒）
     */
    private int mIntervalTime;
    /**
     * 数据源
     */
    private List<T> mSource;
    /**
     * 当前滚动页
     */
    private int mCurrentItem = 0;
    /**
     * 执行轮播任务
     */
    private ScheduledExecutorService mScheduled;
    /**
     * 相对宽度
     */
    private int mSlideWidthScale = 2;
    /**
     * 相对高度
     */
    private int mSlideHeightScale = 1;
    /**
     * 底部小圆圈未选中时的图标
     */
    private int mDotNormalId;
    /**
     * 底部小圆圈选中时的图标
     */
    private int mDotFocusId;
    /**
     * 轮播图的占位图
     */
    private int mPlaceHolderImage;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.setCurrentItem(mCurrentItem);
        }
    };

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.SlideView, 0, 0);
        mSlideWidthScale = array.getInteger(R.styleable.SlideView_widthScale, 2);
        mSlideHeightScale = array.getInteger(R.styleable.SlideView_heightScale, 1);
        mIntervalTime = array.getInteger(R.styleable.SlideView_intervalTime, 3);

        mDotNormalId = array.getResourceId(R.styleable.SlideView_dotNormal, 0);
        mDotFocusId = array.getResourceId(R.styleable.SlideView_dotFocus, 0);

        mPlaceHolderImage = array.getResourceId(R.styleable.SlideView_placeHolderImage, 0);

        array.recycle();
    }

    /**
     * 默认宽高比 2/1
     *
     * @param widthScale  相对宽度
     * @param heightScale 相对高度
     */
    public SlideView setSlideScale(int widthScale, int heightScale) {
        mSlideWidthScale = widthScale;
        mSlideHeightScale = heightScale;
        return this;
    }

    /**
     * 设置轮播时间间隔
     *
     * @param intervalTime 时间间隔（单位：秒）
     * @return SlideView
     */
    public SlideView setIntervalTime(int intervalTime) {
        mIntervalTime = intervalTime;
        return this;
    }

    /**
     * 设置底部小圆圈未选中时的图标
     *
     * @param normalIcon icon
     * @return SlideView
     */
    public SlideView setDotNormalIcon(@IdRes int normalIcon) {
        mDotNormalId = normalIcon;
        return this;
    }

    /**
     * 设置底部小圆圈选中时的图标
     *
     * @param focusIcon icon
     * @return SlideView
     */
    public SlideView setDotFocusIcon(@IdRes int focusIcon) {
        mDotFocusId = focusIcon;
        return this;
    }

    /**
     * 设置轮播图片的占位符
     *
     * @param image image
     * @return SlideView
     */
    public SlideView setPlaceHolderImage(@IdRes int image) {
        mPlaceHolderImage = image;
        return this;
    }

    /**
     * 初始化数据源
     *
     * @param dataSource 数据源
     */
    public void init(List<T> dataSource) {
        if (null == dataSource || 0 == dataSource.size()) {
            return;
        }

        mSource = dataSource;
        mImageCount = mSource.size();

        initUI();
    }

    /**
     * 初始化视图中的各个组件
     */
    private void initUI() {
        startPlay();
        removeAllViews();
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        int width = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = (width / mSlideWidthScale) * mSlideHeightScale;
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        mViewPager = new ViewPager(mContext);
        mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        LinearLayout linearLayout = new LinearLayout(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        linearLayout.setPadding(0, 12, 0, 12);
        linearLayout.setLayoutParams(layoutParams);

        List<SimpleDraweeView> imageViewList = new ArrayList<>();
        List<View> dotViewList = new ArrayList<>();

        for (int i = 0; i < mImageCount; i++) {
            SimpleDraweeView view = new SimpleDraweeView(mContext);
            view.setTag(getImageUrl(mSource.get(i)));
            view.setOnClickListener(new OnItemListener(mSource.get(i), i));
            imageViewList.add(view);
            ImageView dotView = new ImageView(mContext);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(24, 24);
            param.leftMargin = 4;
            param.rightMargin = 4;
            linearLayout.addView(dotView, param);
            dotViewList.add(dotView);
        }

        //当图片数量为2时，将数量翻倍，避免滑动出现白屏
        if (2 == mImageCount) {
            for (int i = 0; i < mImageCount; i++) {
                SimpleDraweeView view = new SimpleDraweeView(mContext);
                view.setTag(getImageUrl(mSource.get(i)));
                view.setOnClickListener(new OnItemListener(mSource.get(i), i));
                imageViewList.add(view);
            }
        }

        //当图片数量为1时，将数量翻3倍，避免滑动出现白屏
        if (1 == mImageCount) {
            for (int i = 0; i < mImageCount * 2; i++) {
                SimpleDraweeView view = new SimpleDraweeView(mContext);
                view.setTag(getImageUrl(mSource.get(0)));
                view.setOnClickListener(new OnItemListener(mSource.get(0), 0));
                imageViewList.add(view);
            }
        }

        mViewPager.setFocusable(true);
        mViewPager.setPageTransformer(true, TransFormFactory.getTransFormer(5));
        mViewPager.setAdapter(new SlideAdapter(imageViewList));
        mViewPager.addOnPageChangeListener(new SlidePageChangeListener(dotViewList));
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);

        relativeLayout.addView(mViewPager);
        relativeLayout.addView(linearLayout);
        addView(relativeLayout);

        startPlay();
    }

    /**
     * 开启轮播任务
     */
    private void startPlay() {
        if (null == mScheduled) {
            mScheduled = Executors.newSingleThreadScheduledExecutor();
        }
        mScheduled.scheduleAtFixedRate(new SlideShowTask(), 1, mIntervalTime, TimeUnit.SECONDS);
    }

    /**
     * 结束轮播任务
     */
    private void stopPlay() {
        if (null != mScheduled) {
            mScheduled.shutdown();
            mScheduled = null;
        }
    }

    private class SlideAdapter extends PagerAdapter {
        List<SimpleDraweeView> list;

        public SlideAdapter(List<SimpleDraweeView> imageList) {
            list = imageList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //TODO Warning：not call removeView here, just do nothing
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mImageCount == 1) {
                position %= mImageCount * 3;
            } else if (mImageCount == 2) {
                position %= mImageCount * 2;
            } else {
                position %= mImageCount;
            }

            if (position < 0) {
                position = mImageCount + position;
            }

            SimpleDraweeView imageView = list.get(position);
            setImageLoader(imageView);

            //TODO remove the parent,or throw IllegalStateException
            ViewParent viewParent = imageView.getParent();
            if (null != viewParent) {
                ((ViewGroup) viewParent).removeView(imageView);
            }

            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 初始化imageView相关配置参数
     *
     * @param imageView SimpleDraweeView
     */
    private void setImageLoader(SimpleDraweeView imageView) {
        if (null == imageView.getTag()) {
            return;
        }
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(imageView.getTag().toString()))
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(true)
                .setOldController(imageView.getController())
                .build();

        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setFadeDuration(800)
                .setFailureImage(getResources().getDrawable(mPlaceHolderImage))
                .setPlaceholderImage(getResources().getDrawable(mPlaceHolderImage))
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .build();

        imageView.setHierarchy(hierarchy);
        imageView.setController(controller);
    }

    private class SlidePageChangeListener implements ViewPager.OnPageChangeListener {
        List<View> list;

        public SlidePageChangeListener(List<View> dotViewList) {
            list = dotViewList;
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentItem = position;
            position %= mImageCount;

            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
                    (list.get(position)).setBackgroundResource(mDotFocusId);
                } else {
                    (list.get(i)).setBackgroundResource(mDotNormalId);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
    }

    /**
     * 轮播图切换Runnable
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (mViewPager) {
                mCurrentItem++;
                mHandler.obtainMessage().sendToTarget();
            }
        }
    }

    /**
     * 释放资源
     */
    private void releaseResources() {
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlay();
        releaseResources();
    }

    private OnItemClickListener<T> mOnItemClickListener;

    /**
     * 设置轮播图的点击事件
     *
     * @param onItemClickListener OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T result, int position);
    }

    private class OnItemListener implements OnClickListener{
        private T info;
        private int position;

        public OnItemListener( T info,int position) {
            this.info = info;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != mOnItemClickListener){
                mOnItemClickListener.onItemClick(info, position);
            }
        }
    }

    private String getImageUrl(T obj) {
        if (obj instanceof String) {
            //如果是String类型，直接返回
            return (String) obj;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ImageUrl.class)) {
                try {
                    field.setAccessible(true);
                    return (String) field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 该注解用于标注泛型类实体中图片的地址字段
     */
    @Target(value = {ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ImageUrl {
    }

    private Type getType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType genericSuperclassType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = genericSuperclassType.getActualTypeArguments();
        return actualTypeArguments[0];
    }
}