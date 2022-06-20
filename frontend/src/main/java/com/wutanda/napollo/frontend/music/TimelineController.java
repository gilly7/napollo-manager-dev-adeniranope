package com.wutanda.napollo.frontend.music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wutanda.napollo.api.v1_0.music.TimelinePostRequest;
import com.wutanda.napollo.api.v1_0.music.common.TimelinePost;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.music.TimelinepostManager;

@RestController
public class TimelineController {

	@Autowired
	private TimelinepostManager timelinepostManager;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/timeline/post")
	public ResponseEntity<AbstractResponse<TimelinePost>> create(@RequestHeader("Authorization") String authorization,
			@RequestParam("post") String post,
			@RequestParam(value = "photoMultipartFile", required = false) MultipartFile photoMultipartFile)
			throws NapolloException {
		TimelinePostRequest request = new TimelinePostRequest();
		request.setPost(post);
		request.setPostMultipartFile(photoMultipartFile);
		return ResponseEntity
				.ok(this.timelinepostManager.create(Authorization.bearerAuthorization(authorization), request));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/timeline/posts")
	public ResponseEntity<AbstractResponse<Page<TimelinePost>>> list(
			@RequestHeader("Authorization") String authorization, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size) throws NapolloException {
		return ResponseEntity
				.ok(this.timelinepostManager.list(Authorization.bearerAuthorization(authorization), page, size));
	}
}
