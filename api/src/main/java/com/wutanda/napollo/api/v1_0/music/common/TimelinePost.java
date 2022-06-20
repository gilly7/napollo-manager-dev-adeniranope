package com.wutanda.napollo.api.v1_0.music.common;

import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;

public class TimelinePost {

	private String post;
	private String postImage;
	private AccountUser owner;

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getPostImage() {
		return postImage;
	}

	public void setPostImage(String postImage) {
		this.postImage = postImage;
	}

	public AccountUser getOwner() {
		return owner;
	}

	public void setOwner(AccountUser owner) {
		this.owner = owner;
	}

}
