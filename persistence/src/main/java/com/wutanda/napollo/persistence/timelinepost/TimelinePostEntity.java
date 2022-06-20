package com.wutanda.napollo.persistence.timelinepost;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;
import com.wutanda.napollo.persistence.users.UserEntity;

@Entity
@Table(schema = "timeline", name = "timeline_posts")
public class TimelinePostEntity extends BaseEntity {

	private String post;

	private String postUrl;

	@ManyToOne
	private AccountUserEntity accountUser;

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public AccountUserEntity getAccountUser() {
		return accountUser;
	}

	public void setAccountUser(AccountUserEntity accountUser) {
		this.accountUser = accountUser;
	}

}
