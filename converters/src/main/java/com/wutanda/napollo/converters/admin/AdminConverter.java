package com.wutanda.napollo.converters.admin;

import com.wutanda.napollo.api.v1_0.admin.AdminRequest;
import com.wutanda.napollo.api.v1_0.admin.common.Admin;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.admin.AdminEntity;

public interface AdminConverter extends Converter<AdminRequest, Admin, AdminEntity> {}
