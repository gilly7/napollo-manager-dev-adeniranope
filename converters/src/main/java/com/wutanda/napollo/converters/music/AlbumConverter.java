package com.wutanda.napollo.converters.music;

import com.wutanda.napollo.api.v1_0.music.AlbumRequest;
import com.wutanda.napollo.api.v1_0.music.common.Album;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.music.AlbumEntity;

public interface AlbumConverter extends Converter<AlbumRequest, Album, AlbumEntity> {}
