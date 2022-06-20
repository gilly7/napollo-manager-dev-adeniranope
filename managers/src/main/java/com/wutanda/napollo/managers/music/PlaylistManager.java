package com.wutanda.napollo.managers.music;

import com.wutanda.napollo.api.v1_0.music.PlaylistRequest;
import com.wutanda.napollo.api.v1_0.music.common.Playlist;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.managers.AbstractManager;
import com.wutanda.napollo.common.transport.AbstractResponse;
import org.springframework.data.domain.Page;

public interface PlaylistManager extends AbstractManager<PlaylistRequest, Playlist> {

  AbstractResponse addMedia(
      Authorization authorization, String playlistIdentity, String mediaIdentity, Boolean state)
      throws NapolloException;

  AbstractResponse<Page<Playlist>> listByStatus(
      Authorization authorization, Boolean state, Integer page, Integer size)
      throws NapolloException;
}
