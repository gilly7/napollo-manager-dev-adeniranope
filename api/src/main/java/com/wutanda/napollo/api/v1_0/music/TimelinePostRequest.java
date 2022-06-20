package com.wutanda.napollo.api.v1_0.music;

import org.springframework.web.multipart.MultipartFile;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

public class TimelinePostRequest extends BaseRequest {

	private String post;
	private MultipartFile postMultipartFile;

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public MultipartFile getPostMultipartFile() {
		return postMultipartFile;
	}

	public void setPostMultipartFile(MultipartFile postMultipartFile) {
		this.postMultipartFile = postMultipartFile;
	}

}
