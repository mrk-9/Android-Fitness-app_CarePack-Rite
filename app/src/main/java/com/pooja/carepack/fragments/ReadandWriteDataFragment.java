package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pooja.carepack.R;

/**
 * Created by Yudiz on 24/12/15.
 */
public class ReadandWriteDataFragment extends BaseFragment
{
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frg_read_write_data,container,false);initUI();
        return view;
    }

    private void initUI() {

    }
}
