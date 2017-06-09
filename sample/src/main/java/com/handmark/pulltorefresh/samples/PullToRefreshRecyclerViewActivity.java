package com.handmark.pulltorefresh.samples;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PullToRefreshRecyclerViewActivity extends Activity {

    private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh_recycler_view);

        PullToRefreshRecyclerView pullToRefreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.pull_rv);
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<PullToRefreshRecyclerView.RecyclerViewWithExtra>() {
            @Override
            public void onRefresh(PullToRefreshBase<PullToRefreshRecyclerView.RecyclerViewWithExtra> refreshView) {
                mListAdapter.append(Arrays.asList("new Item at " + System.currentTimeMillis(), "new Item at " + System.currentTimeMillis()));
                refreshView.onRefreshComplete();
            }
        });


        PullToRefreshRecyclerView.RecyclerViewWithExtra refreshableView = pullToRefreshRecyclerView.getRefreshableView();
        mListAdapter = new ListAdapter(this);
        refreshableView.setAdapter(mListAdapter);
        refreshableView.setLayoutManager(new LinearLayoutManager(this));

        mListAdapter.append(Arrays.asList(mStrings));

    }

    private static class ListAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private final LayoutInflater mLayoutInflater;

        public ListAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        private List<String> mList = new ArrayList<>();

        public void append(List<String> l) {
            mList.addAll(l);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private static class Holder extends RecyclerView.ViewHolder {

            Holder(View itemView) {
                super(itemView);
            }
        }

    }

}
