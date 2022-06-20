package com.wutanda.napollo.converters.music;

import com.wutanda.napollo.api.v1_0.music.TimelinePostRequest;
import com.wutanda.napollo.api.v1_0.music.common.TimelinePost;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.timelinepost.TimelinePostEntity;

public interface TimelinePostConverter extends Converter<TimelinePostRequest, TimelinePost, TimelinePostEntity> {}
