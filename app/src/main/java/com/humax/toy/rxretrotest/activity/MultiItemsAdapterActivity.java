package com.humax.toy.rxretrotest.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.benny.library.autoadapter.SimpleAdapterItemAccessor;
import com.benny.library.autoadapter.viewcreator.ViewCreatorCollection;
import com.benny.library.autoadapter.viewholder.DataGetter;
import com.humax.toy.rxretrotest.R;
import com.humax.toy.rxretrotest.holder.BaseViewHolder;
import com.humax.toy.rxretrotest.module.ResultInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tony on 16/11/3
 */

public class MultiItemsAdapterActivity extends Activity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Context context;
    private ChatItemAccessor itemAccessor = new ChatItemAccessor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_items);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager
                .VERTICAL,true));
        ViewCreatorCollection collection = new ViewCreatorCollection.Builder<ResultInstance>()
                .loadingResId(R.layout.adapter_item_loading)
                .addFilter((resultInstance, integer, integer2) -> null,R.layout.adapter_item, ChatViewHolder::new)
                .build();
    }

    private class ChatItemAccessor extends SimpleAdapterItemAccessor<String> {

        private List<String> newData = new ArrayList<>();

        public void appendMessage(String message){
                newData.add(message);
                notifyDataSetChanged();
        }

        public void appendMessages(List<String> messages){
            newData.addAll(messages);
            notifyDataSetChanged();
            recyclerView.scrollToPosition(0);
        }
    }

    private class ChatViewHolder extends BaseViewHolder<ResultInstance>{

        @Override
        public void onDataChange(DataGetter<ResultInstance> getter, int position) {

        }
    }
}
