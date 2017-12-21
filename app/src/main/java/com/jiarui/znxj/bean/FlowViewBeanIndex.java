package com.jiarui.znxj.bean;

public class FlowViewBeanIndex {
	
	String id;
	
	String img;
	
	String title;

	public FlowViewBeanIndex(String id, String img, String title) {
		super();
		this.id = id;
		this.img = img;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "FlowViewBeanIndex [id=" + id + ", img=" + img + ", title="
				+ title + "]";
	}
	
	

}
