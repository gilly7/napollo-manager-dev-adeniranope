package com.wutanda.napollo.converters.music;

import com.wutanda.napollo.api.v1_0.music.PlaylistRequest;
import com.wutanda.napollo.api.v1_0.music.common.Playlist;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.music.PlaylistEntity;

public interface PlaylistConverter extends Converter<PlaylistRequest, Playlist, PlaylistEntity> {}
