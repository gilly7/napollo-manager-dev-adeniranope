package com.wutanda.napollo.converters.music;

import com.wutanda.napollo.api.v1_0.music.common.Play;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.music.PlayHistoryEntity;

public interface PlayHistoryConverter extends Converter<Void, Play, PlayHistoryEntity> {}
