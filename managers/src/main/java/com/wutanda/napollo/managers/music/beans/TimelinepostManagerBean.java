package com.wutanda.napollo.managers.music.beans;

import java.nio.file.Paths;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.wutanda.napollo.persistence.users.UserEntity;

import com.google.common.io.Files;
import com.wutanda.napollo.api.v1_0.music.TimelinePostRequest;
import com.wutanda.napollo.api.v1_0.music.common.TimelinePost;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.music.TimelinePostConverter;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.managers.music.TimelinepostManager;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserEntityDao;
import com.wutanda.napollo.persistence.timelinepost.TimelinePostEntity;
import com.wutanda.napollo.persistence.timelinepost.dao.TimelinePostEntityDao;
import com.wutanda.napollo.validators.common.ImageTypeValidator;
import org.springframework.data.domain.PageRequest;
import java.util.Arrays;

import java.util.stream.Collectors;

@Component
public class TimelinepostManagerBean implements TimelinepostManager {

	@Autowired
	private TimelinePostEntityDao timeliePostEntityDao;

	@Autowired
	private PermissionManager permissionManager;

	@Autowired
	private AccountUserEntityDao accountUserEntityDao;

	@Autowired
	private ImageTypeValidator imageTypeValidator;

	@Autowired
	private TimelinePostConverter timelinePostConverter;

	@Autowired
	private LoggerManager loggerManager;

	@Value("${napollo.media.image.path}")
	private String mediaImagePath;

	@Override
	@Transactional
	public AbstractResponse<TimelinePost> create(Authorization authorization, TimelinePostRequest request)
			throws NapolloException {
		
	    this.permissionManager.validatePermission(
	            authorization, Arrays.asList(PermissionRegistry.CREATE_POSTS.name()));
	    
		TimelinePostEntity entity = new TimelinePostEntity();
		entity.setPost(request.getPost());

		entity.setAccountUser(this.accountUserEntityDao
				.findByUser_EmailAddress(this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
				.orElseThrow(() -> NapolloException.internalError("Unable to create post")));

		// upload
		if (request.getPostMultipartFile() != null && !request.getPostMultipartFile().isEmpty()) {
			imageTypeValidator.validate(request.getPostMultipartFile());
			try {
				final String mediaName = RandomStringUtils.randomAlphanumeric(64);
				final String fileExtensionName = Files
						.getFileExtension(request.getPostMultipartFile().getOriginalFilename());
				request.getPostMultipartFile().transferTo(
						Paths.get(mediaImagePath.concat("/").concat(mediaName).concat(".").concat(fileExtensionName)));
				entity.setPostUrl(mediaName.concat(".").concat(fileExtensionName));
				this.timeliePostEntityDao.save(entity);
			} catch (Exception ioException) {
				this.loggerManager.log(MediaManagerBean.class, LogLevel.ERROR, ioException.getMessage(), ioException);
				throw NapolloException.internalError("Unable to process the media file");
			}
		}
		return AbstractResponse.response("timeline post created successfully", Boolean.TRUE);
	}

	@Override
	public AbstractResponse<Page<TimelinePost>> list(Authorization authorization, Integer page, Integer size)
			throws NapolloException {
		 this.permissionManager.validatePermission(
			        authorization, Arrays.asList(PermissionRegistry.GET_POSTS.name()));
		final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
		Page<TimelinePostEntity> timelinePostEntity = null;
		timelinePostEntity = this.timeliePostEntityDao
				.findByAccountUser_User_Id(authorizedUserEntity.getId(), PageRequest.of(page, size))
				.orElseThrow(() -> NapolloException.internalError("No post found for this user"));
		final Page<TimelinePost> postPage = new PageImpl(timelinePostEntity.stream()
				.map(this.timelinePostConverter::createResponse).collect(Collectors.toList()),
				timelinePostEntity.getPageable(), timelinePostEntity.getTotalElements());

		return AbstractResponse.response(postPage, "Timeline posts listed successfully", Boolean.TRUE);
	}

	@Override
	public AbstractResponse<TimelinePost> get(Authorization authorization, String identity) throws NapolloException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractResponse update(Authorization authorization, String identity, TimelinePostRequest request)
			throws NapolloException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractResponse delete(Authorization authorization, String identity) throws NapolloException {
		// TODO Auto-generated method stub
		return null;
	}

}
