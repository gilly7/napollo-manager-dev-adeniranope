package com.wutanda.napollo.managers.music;

import org.springframework.data.domain.Page;

import com.wutanda.napollo.api.v1_0.music.TimelinePostRequest;
import com.wutanda.napollo.api.v1_0.music.common.TimelinePost;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.managers.AbstractManager;
import com.wutanda.napollo.common.transport.AbstractResponse;

public interface TimelinepostManager extends AbstractManager<TimelinePostRequest, TimelinePost> {

}
