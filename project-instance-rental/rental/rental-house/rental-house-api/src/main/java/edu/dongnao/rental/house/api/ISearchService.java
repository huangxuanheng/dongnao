package edu.dongnao.rental.house.api;

import java.util.List;

import edu.dongnao.rental.house.domain.HouseBucketDTO;
import edu.dongnao.rental.house.domain.HouseHighlightDTO;
import edu.dongnao.rental.house.form.MapSearch;
import edu.dongnao.rental.house.form.RentSearch;
import edu.dongnao.rental.lang.ServiceMultiResult;
import edu.dongnao.rental.lang.ServiceResult;

/**
 * 全文检索服务接口
 * 
 */
public interface ISearchService {
    /**
     * 索引目标房源
     * @param houseId
     */
    void index(Long houseId);

    /**
     * 移除房源索引
     * @param houseId
     */
    void remove(Long houseId);

    /**
     * 检索搜索引擎中的数据ID
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<Long> query(RentSearch rentSearch);
    
    /**
     * 检索高亮显示
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<HouseHighlightDTO> highlightQuery(RentSearch rentSearch);

    /**
     * 获取补全建议关键词
     * @param prefix 需要进行补全的关键字前缀
     * @return 从搜索引擎中匹配到的补全的关键词条
     */
    ServiceResult<List<String>> suggest(String prefix);
    
    /**
     * 聚合特定小区的房间数
     */
    ServiceResult<Long> aggregateDistrictHouse(String cityEnName, String regionEnName, String district);
    
    /**
     * 聚合统计城市数据
     * @param cityEnName
     * @return
     */
    ServiceMultiResult<HouseBucketDTO> mapAggregate(String cityEnName);

    /**
     * 地图城市级别查询
     * @return
     */
    ServiceMultiResult<Long> mapQuery(String cityEnName, String orderBy,
                                      String orderDirection, int start, int size);
    /**
     * 地图精确范围数据查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<Long> mapQuery(MapSearch mapSearch);

}
