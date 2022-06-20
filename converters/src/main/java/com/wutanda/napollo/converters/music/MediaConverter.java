package com.wutanda.napollo.converters.music;

import com.wutanda.napollo.api.v1_0.music.MediaRequest;
import com.wutanda.napollo.api.v1_0.music.common.Media;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.music.MediaEntity;

public interface MediaConverter extends Converter<MediaRequest, Media, MediaEntity> {}
