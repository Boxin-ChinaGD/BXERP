package com.bx.erp.view.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bx.erp.R;

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private Context context;
    private View loadMoreFootView;
    private ProgressBar footerViewProgressBar;
    private TextView footerViewTextView;
    private View loadMoreCompletedFootView;
    private int mTotalItemCount;//item总数
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mIsLoading = false;//是否正在加载

    public LoadMoreListView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        loadMoreFootView = LayoutInflater.from(context).inflate(R.layout.loadmore_listview_foot, null);
        footerViewProgressBar = loadMoreFootView.findViewById(R.id.loadmore_progressbar);
        footerViewTextView = loadMoreFootView.findViewById(R.id.loadmore_textview);
        setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        int lastVisibleIndex = view.getLastVisiblePosition();
        if (!mIsLoading && scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == mTotalItemCount - 1) {
            mIsLoading = true;
            addFooterView(loadMoreFootView);
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onloadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void onloadMore();
    }

    public void setLoadCompleted() {
        mIsLoading = false;
        removeFooterView(loadMoreFootView);
    }
}
