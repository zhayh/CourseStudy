package edu.niit.android.course.service;

import edu.niit.android.course.entity.UserInfo;

public interface UserInfoService {
    UserInfo get(String username);
    void save(UserInfo userInfo);
    void modify(UserInfo userInfo);
}
