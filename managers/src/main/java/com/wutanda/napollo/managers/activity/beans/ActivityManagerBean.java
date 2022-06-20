package com.wutanda.napollo.managers.activity.beans;

import com.wutanda.napollo.api.v1_0.activity.common.Activity;
import com.wutanda.napollo.common.activity.ActivityRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.converters.activity.ActivityConverter;
import com.wutanda.napollo.managers.activity.ActivityManager;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserEntityDao;
import com.wutanda.napollo.persistence.activity.ActivityEntity;
import com.wutanda.napollo.persistence.activity.dao.ActivityEntityDao;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.persistence.users.dao.UserEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class ActivityManagerBean implements ActivityManager {

  @Autowired private ActivityEntityDao activityEntityDao;

  @Autowired private PermissionManager permissionManager;

  @Autowired private AccountUserEntityDao accountUserEntityDao;

  @Autowired private UserEntityDao userEntityDao;

  @Autowired private ActivityConverter activityConverter;

  @Override
  public void postActivity(Authorization authorization, final ActivityRequest activityRequest)
      throws NapolloException {
    final ActivityEntity activityEntity = new ActivityEntity();
    activityEntity.setActivityType(activityRequest.getActivityType().name());
    activityEntity.setActivityOwnerId(activityRequest.getActivitySourceOwnerId());
    activityEntity.setActivityDestinationOwnerId(activityRequest.getActivityDestinationOwnerId());
    activityEntity.setCreatedUser(permissionManager.getAuthorizedUser(authorization).getId());
    activityEntity.setActivityUser(userEntityDao.getOne(activityRequest.getActivityUserId()));
    activityEntityDao.save(activityEntity);
  }

  @Override
  public Page<Activity> getActivities(final Authorization authorization, Integer page, Integer size)
      throws NapolloException {
    permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_ACTIVITIES.name()));
    final UserEntity authorizedUserEntity = permissionManager.getAuthorizedUser(authorization);
    final Page<ActivityEntity> activityEntityPage =
        activityEntityDao.findByCreatedUser(
            authorizedUserEntity.getId(), PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"timestamp")));
    final Page<Activity> activityPage =
        new PageImpl(
            activityEntityPage.stream()
                .map(activityConverter::createResponse)
                .collect(Collectors.toList()),
            activityEntityPage.getPageable(),
            activityEntityPage.getTotalElements());
    return activityPage;
  }
}
