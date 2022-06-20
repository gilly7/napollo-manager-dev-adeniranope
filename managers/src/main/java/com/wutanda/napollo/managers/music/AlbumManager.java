package com.wutanda.napollo.managers.music;

import com.wutanda.napollo.api.v1_0.music.AlbumRequest;
import com.wutanda.napollo.api.v1_0.music.common.Album;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.managers.AbstractManager;
import com.wutanda.napollo.common.transport.AbstractResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AlbumManager extends AbstractManager<AlbumRequest, Album> {

  AbstractResponse create(
      Authorization authorization, MultipartFile multipartFile, AlbumRequest albumRequest)
      throws NapolloException;

  AbstractResponse update(
      Authorization authorization,
      String albumIdentity,
      MultipartFile multipartFile,
      AlbumRequest albumRequest)
      throws NapolloException;

  AbstractResponse addMedia(Authorization authorization, String albumIdentity, String mediaIdentity)
      throws NapolloException;
}
