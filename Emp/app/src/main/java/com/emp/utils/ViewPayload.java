package com.emp.utils;

import android.content.Context;
import android.view.View;

import com.emp.models.Model;

public class ViewPayload<T extends Model> extends View {
    public T payload;

    public ViewPayload(Context context, T payload) {
        super(context);
        this.payload = payload;
    }

}
