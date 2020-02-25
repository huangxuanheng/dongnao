package com.dongnaoedu.mall.search.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dongnaoedu.mall.manager.dto.front.SearchItem;

public interface ItemMapper {

	List<SearchItem> getItemList();

	SearchItem getItemById(@Param("id") Long id);

}