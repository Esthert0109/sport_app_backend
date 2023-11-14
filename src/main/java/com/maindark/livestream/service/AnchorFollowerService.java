package com.maindark.livestream.service;

import com.maindark.livestream.dao.AnchorFollowerDao;
import com.maindark.livestream.domain.AnchorFollower;
import com.maindark.livestream.form.AnchorFollowerForm;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnchorFollowerService {

    @Resource
    AnchorFollowerDao anchorFollowerDao;

    public void createData(AnchorFollowerForm anchorFollowerForm){
        AnchorFollower anchorFollower = new AnchorFollower();
        anchorFollower.setAnchorId(Long.parseLong(anchorFollowerForm.getAnchorId()));
        anchorFollower.setFollowerId(Long.parseLong(anchorFollowerForm.getFollowerId()));
        anchorFollower.setFollowDate(new Date());
        anchorFollowerDao.insertData(anchorFollower);
    }

    public void deleteData(Long anchorId,Long followerId){
        anchorFollowerDao.deleteData(anchorId,followerId);
    }

}
