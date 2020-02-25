package com.dongnaoedu.mall.manager.dto.front;

import java.io.Serializable;
import java.util.List;

/**
 * @author allen
 */
public class SearchResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long recordCount;

	private int totalPages;

	private List<SearchItem> itemList;

	public Long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<SearchItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
}
