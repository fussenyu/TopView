package cc.fussen.topview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * Created by Fussen on 2017/4/22.
 */

public class SmartScrollView extends FrameLayout {

    private static final String TAG = "SmartScrollView";

    private ViewGroup mContentView;

    /**
     * 需要固定的view
     */
    private ViewGroup mTopView;

    /**
     * 固定在顶部的View在滚动条里最上端的位置
     */
    private int mTopViewTop;

    public SmartScrollView(Context context) {
        super(context);
        initView();
    }


    public SmartScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SmartScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmartScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

        //布局加载完成之后拿到子布局

        post(new Runnable() {
            @Override
            public void run() {
                //拿到填充内容的组件  就是布局中的LinearLayout
                mContentView = (ViewGroup) getChildAt(0);

                //先干掉原有布局
                removeAllViews();

                //自定义scrollview
                CustomScrollView customScrollView = new CustomScrollView(getContext(), SmartScrollView.this);

                //将拿到的内容区域填充到scrollview里
                customScrollView.addView(mContentView);

                //再将自定义的scrollview添加到SmartScrollView里
                addView(customScrollView);
            }
        });

    }

    /**
     * 设置需要固定view的资源id
     *
     * @param resourceId
     */

    public void setTopView(@IdRes final int resourceId) {


        post(new Runnable() {
            @Override
            public void run() {
                //通过SmartScrollView的子view 也就是 view内部的容器 找到topview
                mTopView = (ViewGroup) mContentView.findViewById(resourceId);

                //拿到mTopView的实际高度
                int measuredHeight =  mTopView.getMeasuredHeight();

                ViewGroup.LayoutParams layoutParams = mTopView.getLayoutParams();

                layoutParams.height = measuredHeight;

                mTopView.setLayoutParams(layoutParams);

                //topView与父布局的距离
                mTopViewTop = mTopView.getTop();
            }
        });


    }


    @SuppressLint("ViewConstructor")
    private static class CustomScrollView extends ScrollView {

        private SmartScrollView mScrollView;

        public CustomScrollView(Context context, SmartScrollView scrollView) {
            super(context);
            mScrollView = scrollView;
        }


        @Override
        protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
            super.onScrollChanged(left, top, oldLeft, oldTop);

            mScrollView.onScroll(top);
        }

    }
    /**
     * 必须要放到post中去 否则 会蹦的  这是Android4.4以下的bug 为的只是防止并发操作view
     * @param scrollY
     */
    private void onScroll(final int scrollY) {

        post(new Runnable() {
            @Override
            public void run() {
                if (mTopView == null) return;

                if (scrollY >= mTopViewTop && mTopView.getParent() == mContentView) {

                    mContentView.removeView(mTopView);
                    addView(mTopView);

                } else if (scrollY < mTopViewTop && mTopView.getParent() == SmartScrollView.this) {
                    removeView(mTopView);
                    //添加到LinearLayout角标为1处
                    mContentView.addView(mTopView,1);
                }
            }
        });


    }
}
