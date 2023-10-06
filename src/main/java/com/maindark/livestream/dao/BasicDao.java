package com.maindark.livestream.dao;

public interface BasicDao<T> {
    Integer insert(T t);
    Integer getMaxId();
    Integer getMaxUpdatedAt();

    void updateDataById(T t);
}
