package com.humax.toy.rxretrotest.holder;

import android.content.Context;
import android.view.View;

import com.benny.library.autoadapter.viewholder.IViewHolder;

import butterknife.ButterKnife;

/**
 * Created by Tony on 16/11/2
 */

public abstract class BaseViewHolder<T> implements IViewHolder<T>{

    private Context context;
    public View itemView;

    @Override
    public void bind(View view) {
        itemView = view;
        context = view.getContext();
        ButterKnife.bind(this, view);
    }
}
