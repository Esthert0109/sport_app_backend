package com.maindark.livestream.service;


import com.maindark.livestream.dao.AnchorFollowDao;
import com.maindark.livestream.domain.AnchorFollow;
import com.maindark.livestream.util.RedisKeyUtil;
import com.maindark.livestream.vo.AnchorFollowVo;
import com.maindark.livestream.vo.LiveStreamUserVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class FollowService {

    @Resource
    RedisTemplate redisTemplate;

    /*@Resource
    TCustomerService tCustomerService;*/

    @Resource
    LiveStreamUserService liveStreamUserService;

    @Resource
    AnchorFollowDao anchorFollowDao;

    public void follow(Integer userId,int entityType,int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 某个用户关注的实体
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
                log.info("followeeKey:{}",followeeKey);
                // 某个实体拥有的粉丝
                String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
                log.info("followerKey:{}",followerKey);
                operations.multi();
                operations.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());
                operations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    public void unfollow(Integer userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
                operations.multi();
                operations.opsForZSet().remove(followeeKey,entityId);
                operations.opsForZSet().remove(followerKey,userId);
                return  operations.exec();
            }
        });
    }

    //查询关注的实体数量
    public long findFolloweeCount(Integer userId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    // 查询实体的粉丝数量
    public long findFollowerCount(int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    //查询当前用户是否关注该实体(该用户对这类实体的id有一个清单zset)
    public boolean hasFollowed(Integer userId,int entityType,int entityId){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey,entityId) != null;
    }


    //查询用户关注的人
    /*public List<Map<String,Object>> findFollowee(Integer userId,int offset,int limit){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, EntityTypeEnum.USER.getCode());
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey,offset,offset + limit -1);
        if (targetIds == null) return null;
        List<Map<String,Object>> list = new ArrayList<>();
        for(Integer targetId:targetIds){
            Map<String,Object> map = new HashMap<>();
            TCustomerVo tCustomer = tCustomerService.getById(targetId);
            map.put("user",tCustomer);
            Double score = redisTemplate.opsForZSet().score(followeeKey,targetId);
            map.put("followTime", DateUtil.interceptTime(score.longValue()));
            list.add(map);
        }
        return list;
    }

    // 查询用户的粉丝
    public List<Map<String,Object>> findFollowers(Integer userId,int offset,int limit){
        String followerKey = RedisKeyUtil.getFollowerKey(EntityTypeEnum.USER.getCode(), userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey,offset,offset + limit -1);
        if (targetIds == null) return null;
        List<Map<String,Object>> list = new ArrayList<>();
        for(Integer targetId:targetIds){
            Map<String,Object> map = new HashMap<>();
            TCustomerVo tCustomer = tCustomerService.getById(targetId);
            map.put("user",tCustomer);
            Double score = redisTemplate.opsForZSet().score(followerKey,targetId);
            map.put("followTime",DateUtil.interceptTime(score.longValue()));
            boolean hasFollowed = hasFollowed(userId,EntityTypeEnum.USER.getCode(), targetId);
            map.put("hasFollowed",hasFollowed);
            list.add(map);
        }
        return list;
    }*/

    // create follow and unfollow
    public void createFollow(Long anchorId, Long followerId) {

        // if follow existed before
        if(checkIfFollowed(anchorId, followerId) == true){
            //check the followed status
            AnchorFollow followDetails = anchorFollowDao.getFollowDetailsByAnchorIdFollowerId(anchorId, followerId);
            Boolean followStatus = followDetails.getStatus();
            Long id = followDetails.getId();

            //if following, then unfollow, else follow back
            anchorFollowDao.updateFollowAnchorStatusById(id, !followStatus);

        }else{
        // if follow not existed, create a new follow
            anchorFollowDao.createFollowAnchor(anchorId, followerId);
        }
    }

    // get all following list
    public List<AnchorFollowVo> getFollowingListByFollowerId(Long followerId, Pageable pageable) {

        Integer limit = pageable.getPageSize();
        Long offset = pageable.getOffset();
        List<AnchorFollowVo> followingList = anchorFollowDao.getFollowingListByFollowerId(followerId,limit, offset );

        for(AnchorFollowVo anchor: followingList) {
            LiveStreamUserVo anchorDetail = new LiveStreamUserVo();
            anchorDetail = liveStreamUserService.findById(anchor.getAnchorId());
            anchor.setAnchorDetails(anchorDetail);
        }

        return followingList;
    }

    public List<AnchorFollowVo> getFollowingListByDescOrder(Long followerId, Pageable pageable) {
        Integer limit = pageable.getPageSize();
        Long offset = pageable.getOffset();
        List<AnchorFollowVo> followingList = anchorFollowDao.getFollowingListByDescOrder(followerId,limit, offset );

        for(AnchorFollowVo anchor: followingList) {
            LiveStreamUserVo anchorDetail = new LiveStreamUserVo();
            anchorDetail = liveStreamUserService.findById(anchor.getAnchorId());
            anchor.setAnchorDetails(anchorDetail);
        }

        return followingList;
    }

    public List<AnchorFollowVo> getFollowingListByAscOrder(Long followerId, Pageable pageable) {
        Integer limit = pageable.getPageSize();
        Long offset = pageable.getOffset();
        List<AnchorFollowVo> followingList = anchorFollowDao.getFollowingListByAscOrder(followerId,limit, offset );

        for(AnchorFollowVo anchor: followingList) {
            LiveStreamUserVo anchorDetail = new LiveStreamUserVo();
            anchorDetail = liveStreamUserService.findById(anchor.getAnchorId());
            anchor.setAnchorDetails(anchorDetail);
        }
        return followingList;
    }


    //  check if follow existed
    public Boolean checkIfFollowed(Long anchorId, Long followerId){
        return anchorFollowDao.checkFollowExistByAnchorIdFollowerId(anchorId, followerId);
    }

    public Boolean checkIfFollowedStatus(Long anchorId, Long followerId) {
        return anchorFollowDao.checkFollowedStatus(anchorId, followerId);
    }


}
