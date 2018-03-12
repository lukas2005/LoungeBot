package com.github.jreddit.request.retrieval.submissions;

import com.github.jreddit.request.retrieval.param.SubmissionSort;

public class CustomRedditRequest extends SubmissionsOfSubredditRequest {
	String query;

	public CustomRedditRequest(String query, String subreddit, SubmissionSort sort) {
		super(subreddit, sort);
		this.query = query;
		addParameter("q", query+" nsfw:no");
	}

}
