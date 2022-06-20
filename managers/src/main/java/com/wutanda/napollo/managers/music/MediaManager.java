package com.wutanda.napollo.managers.music;

import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.music.MediaRequest;
import com.wutanda.napollo.api.v1_0.music.MediaSearchRequest;
import com.wutanda.napollo.api.v1_0.music.PlayRequest;
import com.wutanda.napollo.api.v1_0.music.common.Media;
import com.wutanda.napollo.api.v1_0.music.common.Play;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.managers.AbstractManager;
import com.wutanda.napollo.common.transport.AbstractResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MediaManager extends AbstractManager<MediaRequest, Media> {

  AbstractResponse<Media> create(Authorization authorization, MediaRequest request)
      throws NapolloException;

  AbstractResponse like(Authorization authorization, String mediaIdentity, Boolean likeState)
      throws NapolloException;

  AbstractResponse play(Authorization authorization, String mediaIdentity, PlayRequest playRequest)
      throws NapolloException;

  AbstractResponse<Page<AccountUser>> accountUserMediaLikes(
      final Authorization authorization, String mediaIdentity) throws NapolloException;

  AbstractResponse<Page<Media>> accountUserMedia(
      final Authorization authorization, Integer page, Integer size) throws NapolloException;

  AbstractResponse<Page<Media>> accountUserMedia(
      final Authorization authorization, String accountUserIdentity, Integer page, Integer size)
      throws NapolloException;

  AbstractResponse<Page<Media>> trendingMedia(
      final Authorization authorization,
      String city,
      String state,
      String country,
      Integer page,
      Integer size)
      throws NapolloException;

  AbstractResponse<Page<Play>> accountUserPlayHistory(
      final Authorization authorization, Integer page, Integer size) throws NapolloException;

  AbstractResponse<Page<AccountUser>> trendingAccountUser(
      final Authorization authorization,
      String city,
      String state,
      String country,
      Integer page,
      Integer size)
      throws NapolloException;

  AbstractResponse markAsDiscovered(final Authorization authorization, final String mediaIdentity)
      throws NapolloException;

  AbstractResponse<Page<Media>> discoveredMedia(
      final Authorization authorization, Integer page, Integer size) throws NapolloException;

  AbstractResponse likeDiscoveredMedia(
      final Authorization authorization, final String mediaIdentity, Boolean likeStatus)
      throws NapolloException;

  AbstractResponse<List<Media>> likedDiscoveredMedia(final Authorization authorization)
      throws NapolloException;

  AbstractResponse<Page<Media>> search(
      final Authorization authorization, final MediaSearchRequest mediaSearchRequest)
      throws NapolloException;
}
