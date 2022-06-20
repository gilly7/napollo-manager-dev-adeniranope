package com.wutanda.napollo.api.v1_0.registration.service;


import com.wutanda.napollo.api.v1_0.registration.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}