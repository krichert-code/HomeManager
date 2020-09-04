package com.homemanager;

public interface NetworkService {
    void setUrl(String url);
    boolean isError();
    void clearError();
}
