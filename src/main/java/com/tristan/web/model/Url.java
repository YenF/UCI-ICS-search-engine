package com.tristan.web.model;

public class Url implements Comparable<Url>{
	private String url;
	private Integer urlId;
	private Double score;
	public Url(String url, Integer urlId, Double score) {
		this.url = url;
		this.urlId = urlId;
		this.score = score;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getUrlId() {
		return urlId;
	}
	public void setUrlId(Integer urlId) {
		this.urlId = urlId;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	@Override
	public int compareTo(Url o) {
		// TODO Auto-generated method stub
		if (score == o.getScore()) {
			return 0;
		}
		if (score > o.getScore()) {
			return 1;
		}
		return -1;
	}
	
}
