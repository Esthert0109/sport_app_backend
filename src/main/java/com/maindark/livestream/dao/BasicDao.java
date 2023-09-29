package com.maindark.livestream.dao;

public interface BasicDao<T> {
    public Integer insert(T t);
    public Integer getMaxId();
    public Integer getMaxUpdatedAt();

    public void updateDataById(T t);
}
