package edu.dongnao.rental.house.api;

import java.util.Date;

import edu.dongnao.rental.house.domain.HouseDTO;
import edu.dongnao.rental.house.domain.HouseSubscribeDTO;
import edu.dongnao.rental.house.domain.HouseSubscribeStatus;
import edu.dongnao.rental.house.form.DatatableSearch;
import edu.dongnao.rental.house.form.HouseForm;
import edu.dongnao.rental.house.form.MapSearch;
import edu.dongnao.rental.house.form.RentSearch;
import edu.dongnao.rental.lang.ServiceMultiResult;
import edu.dongnao.rental.lang.ServiceResult;

/**
 * 房屋管理服务接口
 * 
 */
public interface IHouseService {
	/**=========================基本操作=========================*/
    /**
     * 新增房源
     * @param houseForm
     * @return
     */
    ServiceResult<HouseDTO> save(HouseForm houseForm);
    /**
     * 修改房源信息
     * @param houseForm
     * @return
     */
    ServiceResult<HouseDTO> update(HouseForm houseForm);
    /**
     * 管理后台查询房源信息
     * @param searchBody
     * @param userId
     * @return
     */
    ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody, Long userId);

    /**
     * 查询完整房源信息
     * @param id
     * @return
     */
    ServiceResult<HouseDTO> findCompleteOne(Long id, Long userId);

    /**
     * 移除图片
     * @param id
     * @return
     */
    ServiceResult<Boolean> removePhoto(Long id);

    /**
     * 更新封面
     * @param coverId
     * @param targetId
     * @return
     */
    ServiceResult<Boolean> updateCover(Long coverId, Long targetId);

    /**
     * 新增标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult<Boolean> addTag(Long houseId, String tag);

    /**
     * 移除标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult<Boolean> removeTag(Long houseId, String tag);

    /**
     * 更新房源状态
     * @param id
     * @param status
     * @return
     */
    ServiceResult<Boolean> updateStatus(Long id, int status);
    
    /**=========================检索浏览操作=========================*/
    /**
     * 查询房源信息集
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);
    
    /**=========================地图浏览操作=========================*/
    /**
     * 全地图查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> wholeMapQuery(MapSearch mapSearch);

    /**
     * 精确范围数据查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> boundMapQuery(MapSearch mapSearch);
    
    /**=========================看房预约操作=========================*/

    /**
     * 加入预约清单
     * @param houseId
     * @return
     */
    ServiceResult<Boolean> addSubscribeOrder(Long houseId, Long userId);

    /**
     * 获取对应状态的预约列表
     */
    ServiceMultiResult<HouseSubscribeDTO> querySubscribeList(HouseSubscribeStatus status, Long userId,  int start, int size);

    /**
     * 预约看房时间
     * @param houseId
     * @param orderTime
     * @param telephone
     * @param desc
     * @return
     */
    ServiceResult<?> subscribe(Long houseId, Long userId,  Date orderTime, String telephone, String desc);

    /**
     * 取消预约
     * @param houseId
     * @return
     */
    ServiceResult<?> cancelSubscribe(Long houseId, Long userId);

    /**
     * 管理员查询预约信息接口
     * @param start
     * @param size
     */
    ServiceMultiResult<HouseSubscribeDTO> findSubscribeList(Long userId, int start, int size);

    /**
     * 完成预约
     */
    ServiceResult<Boolean> finishSubscribe(Long houseId, Long subscribeId, Long userId);
}
