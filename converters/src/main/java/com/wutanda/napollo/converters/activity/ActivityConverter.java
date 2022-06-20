package com.wutanda.napollo.converters.activity;

import com.wutanda.napollo.api.v1_0.activity.common.Activity;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.activity.ActivityEntity;

public interface ActivityConverter extends Converter<Void, Activity, ActivityEntity> {}
