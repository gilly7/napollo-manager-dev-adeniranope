package com.wutanda.napollo.managers.music.beans;

import com.wutanda.napollo.api.v1_0.music.GenreRequest;
import com.wutanda.napollo.api.v1_0.music.common.Genre;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.music.GenreConverter;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.managers.music.GenreManager;
import com.wutanda.napollo.persistence.music.GenreEntity;
import com.wutanda.napollo.persistence.music.dao.GenreEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class GenreManagerBean implements GenreManager {

  @Autowired private PermissionManager permissionManager;

  @Autowired private GenreEntityDao genreEntityDao;

  @Autowired private LoggerManager loggerManager;

  @Autowired private GenreConverter genreConverter;

  @Override
  @Transactional
  public AbstractResponse create(Authorization authorization, GenreRequest request)
      throws NapolloException {
    this.loggerManager.log(
        GenreManagerBean.class,
        LogLevel.TRACE,
        "Creating a new genre : ".concat(request.toString()));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_GENRE.name()));
    if (this.genreEntityDao.findByName(request.getName()).isPresent()) {
      throw NapolloException.internalError("Genre with name already registered");
    }
    final GenreEntity genreEntity =
        this.genreConverter
            .createEntity(request)
            .orElseThrow(() -> NapolloException.badRequest("Unable to create a genre."));
    this.genreEntityDao.save(genreEntity);
    return AbstractResponse.response("Genre created successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Genre> get(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(
        GenreManagerBean.class, LogLevel.TRACE, "Getting genre information with ".concat(identity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_GENRE.name()));
    final GenreEntity genreEntity =
        this.genreEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Cannot find the genre information"));
    return AbstractResponse.response(
        this.genreConverter.createResponse(genreEntity),
        "Genre information fetched successfully",
        Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Genre>> list(Authorization authorization, Integer page, Integer size)
      throws NapolloException {
    this.loggerManager.log(GenreManagerBean.class, LogLevel.TRACE, "Listing all genres");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_GENRES.name()));
    final Page<GenreEntity> genreEntityPage =
        this.genreEntityDao.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));
    final Page<Genre> genrePage =
        new PageImpl(
            genreEntityPage.stream()
                .map(this.genreConverter::createResponse)
                .collect(Collectors.toList()),
            genreEntityPage.getPageable(),
            genreEntityPage.getTotalElements());
    return AbstractResponse.response(genrePage, "Genre listed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse update(Authorization authorization, String identity, GenreRequest request)
      throws NapolloException {
    this.loggerManager.log(GenreManagerBean.class, LogLevel.TRACE, "Updating genre information");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPDATE_GENRE.name()));
    final GenreEntity genreEntity =
        this.genreEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Cannot find the genre information"));
    if (request.getName() != null) {
      genreEntity.setName(request.getName());
    }
    if (request.getDescription() != null) {
      genreEntity.setDescription(request.getDescription());
    }
    this.genreEntityDao.save(genreEntity);
    return AbstractResponse.response("Genre updated successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse delete(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(GenreManagerBean.class, LogLevel.TRACE, "Deleting genre information");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.REMOVE_GENRE.name()));
    final GenreEntity genreEntity =
        this.genreEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Cannot find the genre information"));
    this.genreEntityDao.delete(genreEntity);
    return AbstractResponse.response("Genre removed successfully", Boolean.TRUE);
  }
}
