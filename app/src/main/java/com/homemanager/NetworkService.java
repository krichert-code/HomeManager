package com.homemanager;

import android.content.Context;

public interface NetworkService {
    void setUrl(String url);
    boolean isError();
    void clearError();
    Context getContext();
}
