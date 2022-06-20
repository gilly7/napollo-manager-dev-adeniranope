package com.wutanda.napollo.converters.music;

import com.wutanda.napollo.api.v1_0.music.GenreRequest;
import com.wutanda.napollo.api.v1_0.music.common.Genre;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.music.GenreEntity;

public interface GenreConverter extends Converter<GenreRequest, Genre, GenreEntity> {}
