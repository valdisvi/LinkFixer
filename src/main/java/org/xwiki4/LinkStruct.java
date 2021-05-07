package org.xwiki4;

class LinkStruct implements Comparable<LinkStruct> {

	String parentLink;
	String realLink;
	String url;
	String name;

	public LinkStruct(String parentLink, String realLink, String url, String name) {
		this.parentLink = parentLink;
		this.realLink = realLink;
		this.url = url;
		this.name = name;
	}

	@Override
	public String toString() {
		return parentLink + " " + realLink + " " + url + " " + name;
	}

	@Override
	public int compareTo(LinkStruct other) {
		int status = parentLink.compareTo(other.parentLink);
		if (status == 0) {
			status = realLink.compareTo(other.realLink);
			if (status == 0)
				status = url.compareTo(other.url);
		}
		return status;
	}

}