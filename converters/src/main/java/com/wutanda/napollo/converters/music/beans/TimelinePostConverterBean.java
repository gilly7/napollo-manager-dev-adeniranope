package com.wutanda.napollo.converters.music.beans;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wutanda.napollo.api.v1_0.music.TimelinePostRequest;
import com.wutanda.napollo.api.v1_0.music.common.TimelinePost;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.converters.music.TimelinePostConverter;
import com.wutanda.napollo.persistence.timelinepost.TimelinePostEntity;

@Component
public class TimelinePostConverterBean implements TimelinePostConverter {
	@Autowired
	private AccountUserConverter accountUserConverter;

	@Value("${napollo.media.image.url}")
	private String mediaImagePath;

	@Override
	public Optional<TimelinePostEntity> createEntity(TimelinePostRequest request) {
		return Optional.empty();
	}

	@Override
	public Optional<TimelinePost> createResponse(TimelinePostEntity entity) {
		if (entity != null) {
			final TimelinePost timelinePost = new TimelinePost();
			timelinePost.setPost(entity.getPost());
			if (entity.getPostUrl() != null) {
				timelinePost.setPostImage(mediaImagePath.concat(entity.getPostUrl()));
			}
			timelinePost.setOwner(this.accountUserConverter.createResponse(entity.getAccountUser()).orElse(null));
			return Optional.of(timelinePost);
		}
		return Optional.empty();
	}
}
