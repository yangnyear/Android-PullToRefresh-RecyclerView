package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.internal.LoadingLayout;


public class PullToRefreshRecyclerView extends PullToRefreshBase<PullToRefreshRecyclerView.RecyclerViewWithExtra> {


    private LoadingLayout mHeaderLoadingView;


    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerViewWithExtra createRefreshableView(Context context, AttributeSet attrs) {
        return new RecyclerViewWithExtra(context, attrs);
    }


    @Override
    protected void onRefreshing(final boolean doScroll) {
        RecyclerView.Adapter adapter = mRefreshableView.mRecyclerView.getAdapter();
        if (null == adapter) {
            super.onRefreshing(doScroll);
            return;
        }

        super.onRefreshing(false);

        final LoadingLayout origLoadingView, listViewLoadingView;
        final int selection, scrollToY;


        origLoadingView = getHeaderLayout();
        listViewLoadingView = mHeaderLoadingView;
        selection = 0;
        scrollToY = getScrollY() + getHeaderSize();


        if (doScroll) {
            disableLoadingLayoutVisibilityChanges();

            setHeaderScroll(scrollToY);

            smoothScrollTo(0);
        }
    }

    @Override
    protected void handleStyledAttributes(TypedArray a) {
        super.handleStyledAttributes(a);
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

        // Create Loading Views ready for use later
        FrameLayout frame = new FrameLayout(getContext());
        mHeaderLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_START, a);
        mHeaderLoadingView.setVisibility(View.GONE);
        frame.addView(mHeaderLoadingView, lp);
        mRefreshableView.addHeader(frame);
    }


    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        float exactContentHeight = (float) Math.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }


    public class RecyclerViewWithExtra extends LinearLayout {

        private RecyclerView mRecyclerView;

        public RecyclerViewWithExtra(Context context) {
            super(context);
            init(context, null);
        }

        public RecyclerViewWithExtra(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        public RecyclerViewWithExtra(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context, attrs);
        }


        private void init(Context context, AttributeSet attrs) {
            setOrientation(VERTICAL);
            mRecyclerView = new RecyclerView(context, attrs);
            addView(mRecyclerView);
        }

        public void addHeader(View view) {
            removeAllViews();
            addView(view);
            addView(mRecyclerView);
        }

        public void setLayoutManager(RecyclerView.LayoutManager layout) {
            mRecyclerView.setLayoutManager(layout);
        }

        public void setAdapter(RecyclerView.Adapter adapter) {
            mRecyclerView.setAdapter(adapter);
        }

        public int getContentHeight() {
            return mRecyclerView.getAdapter().getItemCount() * 40;
        }


        public int getScale() {
            return 1;
        }


        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshRecyclerView.this, deltaX, scrollX, deltaY, scrollY,
                    getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }

    }


}
