package com.example.homemanager;

public interface NetworkService {
    public void setUrl(String url);
    public boolean isError();
    public void clearError();
}
