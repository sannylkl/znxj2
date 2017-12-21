package com.jiarui.znxj.bean;

import java.io.Serializable;

public class NoticeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;

	String title;

	String img;

	String intro;

	String info;

	String add_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public NoticeEntity(String id, String title, String img, String intro,
			String info, String add_time) {
		super();
		this.id = id;
		this.title = title;
		this.img = img;
		this.intro = intro;
		this.info = info;
		this.add_time = add_time;
	}

	@Override
	public String toString() {
		return "NoticeEntity [id=" + id + ", title=" + title + ", img=" + img
				+ ", intro=" + intro + ", info=" + info + ", add_time="
				+ add_time + "]";
	}

}
