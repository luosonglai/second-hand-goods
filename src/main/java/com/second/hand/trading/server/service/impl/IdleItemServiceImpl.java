package com.second.hand.trading.server.service.impl;

import com.second.hand.trading.server.dao.FavoriteDao;
import com.second.hand.trading.server.dao.IdleItemDao;
import com.second.hand.trading.server.dao.UserDao;
import com.second.hand.trading.server.model.FavoriteModel;
import com.second.hand.trading.server.model.IdleItemModel;
import com.second.hand.trading.server.model.UserModel;
import com.second.hand.trading.server.service.IdleItemService;
import com.second.hand.trading.server.utils.MyCustomException;
import com.second.hand.trading.server.vo.PageVo;
import org.springframework.jmx.access.InvalidInvocationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IdleItemServiceImpl implements IdleItemService {

    @Resource
    private IdleItemDao idleItemDao;

    @Resource
    private UserDao userDao;

    @Resource
    private FavoriteDao favoriteDao;




    /**
     * 发布闲置
     * @param idleItemModel
     * @return
     */
    public boolean addIdleItem(IdleItemModel idleItemModel) {
        return idleItemDao.insert(idleItemModel) == 1;
    }

    /**
     * 查询闲置信息，同时查出发布者的信息
     * @param id
     * @return
     */
    public IdleItemModel getIdleItem(Long id) {
        IdleItemModel idleItemModel=idleItemDao.selectByPrimaryKey(id);
        if(idleItemModel!=null){
            idleItemModel.setUser(userDao.selectByPrimaryKey(idleItemModel.getUserId()));
        }
        return idleItemModel;
    }

    /**
     * 查询用户发布的所有闲置
     * user_id建索引
     * @param userId
     * @return
     */
    public List<IdleItemModel> getAllIdelItem(Long userId) {
        return idleItemDao.getAllIdleItem(userId);
    }

    /**
     * 搜索，分页
     * 同时查出闲置发布者的信息
     * @param findValue
     * @param page
     * @param nums
     * @return
     */
    public PageVo<IdleItemModel> findIdleItem(String findValue, int page, int nums) {
        List<IdleItemModel> list=idleItemDao.findIdleItem(findValue, (page - 1) * nums, nums);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItemModel i:list){
                idList.add(i.getUserId());
            }
            List<UserModel> userList=userDao.findUserByList(idList);
            Map<Long,UserModel> map=new HashMap<>();
            for(UserModel user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItemModel i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }
        int count=idleItemDao.countIdleItem(findValue);
        return new PageVo<>(list,count);
    }

    /**
     * 分类查询，分页
     * 同时查出闲置发布者的信息，代码结构与上面的类似，可封装优化，或改为join查询
     * @param idleLabel
     * @param page
     * @param nums
     * @return
     */
    public PageVo<IdleItemModel> findIdleItemByLable(int idleLabel, int page, int nums) {
        List<IdleItemModel> list=idleItemDao.findIdleItemByLable(idleLabel, (page - 1) * nums, nums);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItemModel i:list){
                idList.add(i.getUserId());
            }
            List<UserModel> userList=userDao.findUserByList(idList);
            Map<Long,UserModel> map=new HashMap<>();
            for(UserModel user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItemModel i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }
        int count=idleItemDao.countIdleItemByLable(idleLabel);
        return new PageVo<>(list,count);
    }

    /**
     * 更新闲置信息
     * @param idleItemModel
     * @return
     */
    public boolean updateIdleItem(IdleItemModel idleItemModel){
        return idleItemDao.updateByPrimaryKeySelective(idleItemModel)==1;
    }

    public PageVo<IdleItemModel> adminGetIdleList(int status, int page, int nums) {
        List<IdleItemModel> list=idleItemDao.getIdleItemByStatus(status, (page - 1) * nums, nums);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItemModel i:list){
                idList.add(i.getUserId());
            }
            List<UserModel> userList=userDao.findUserByList(idList);
            Map<Long,UserModel> map=new HashMap<>();
            for(UserModel user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItemModel i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }
        int count=idleItemDao.countIdleItemByStatus(status);
        return new PageVo<>(list,count);
    }


    public PageVo<IdleItemModel> getRecommendedBooks(Long userId, int page, int nums) {
        // 假设每页的页码从1开始，将页码转换为列表中的索引
        int startIndex = (page - 1) * nums;

        Set<IdleItemModel> recommendedBooks = new HashSet<>();
        List<FavoriteModel> favorites = favoriteDao.getMyFavorite(userId);

        //获取当前用户的收藏列表，如果没有则推荐全部
        if (favorites == null || favorites.isEmpty()) {
            List<IdleItemModel> allIdleItem = idleItemDao.getAllIdleItem(userId);
            int totalIdleItems = allIdleItem.size();
            if (startIndex < totalIdleItems) {
                int endIndex = Math.min(startIndex + nums, totalIdleItems);
                return new PageVo<>(allIdleItem.subList(startIndex, endIndex), totalIdleItems);
            } else {
                return new PageVo<>(Collections.emptyList(), 0);
            }
        }

        List<Long> idleListByCurrentUserId = favorites.stream()
                .map(FavoriteModel::getIdleId)
                .distinct()
                .collect(Collectors.toList());

        List<IdleItemModel> myItemModels = idleItemDao.findIdleByList(idleListByCurrentUserId);
        List<String> myIdleNames = myItemModels.stream()
                .map(IdleItemModel::getIdleName)
                .collect(Collectors.toList());

        //根据用户ID分组
        Map<Long, Set<Long>> groupByUserId = favorites.stream()
                .collect(Collectors.groupingBy(FavoriteModel::getUserId,
                        Collectors.mapping(FavoriteModel::getIdleId, Collectors.toSet())));

        // 遍历用户历史行为数据，寻找和用户历史行为相似的用户
        for (Map.Entry<Long, Set<Long>> entry : groupByUserId.entrySet()) {
            if (!entry.getKey().equals(userId)) { // 排除当前用户
                Set<Long> similarUserIdleItem = entry.getValue();
                List<IdleItemModel> idleByList = idleItemDao.findIdleByList(new ArrayList<>(similarUserIdleItem));

                List<String> idleNames = idleByList.stream()
                        .map(IdleItemModel::getIdleName)
                        .collect(Collectors.toList());
                // 计算用户间的相似度：简单地使用 Jaccard 相似系数
                double similarity = calculateJaccardSimilarity(myIdleNames, idleNames);
                if (similarity > 0.5) { // 相似度阈值
                    // 将相似用户喜欢的书籍加入推荐列表
                    recommendedBooks.addAll(idleByList);
                }
            }
        }

        // 去除用户已经购买过的书籍
        recommendedBooks.removeAll(myIdleNames);

        List<IdleItemModel> recommendedBooksList = new ArrayList<>(recommendedBooks);
        int totalRecommendedBooks = recommendedBooksList.size();

        // 分页处理
        if (startIndex < totalRecommendedBooks && startIndex >= 0) {
            int endIndex = Math.min(startIndex + nums, totalRecommendedBooks);
            return new PageVo<>(recommendedBooksList.subList(startIndex, endIndex), totalRecommendedBooks);
        } else {
            return new PageVo<>(Collections.emptyList(), 0);
        }
    }

    // 计算 Jaccard 相似系数
    private double calculateJaccardSimilarity(List<String> list1, List<String> list2) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);
        int intersectionSize = 0;
        for (String item : set1) {
            if (set2.contains(item)) {
                intersectionSize++;
            }
        }
        int unionSize = set1.size() + set2.size() - intersectionSize;
        return (double) intersectionSize / unionSize;
    }
}
