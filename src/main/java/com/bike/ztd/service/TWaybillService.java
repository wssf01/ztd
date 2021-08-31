package com.bike.ztd.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bike.ztd.entity.TWaybill;
import com.baomidou.mybatisplus.service.IService;
import com.bike.ztd.qo.WaybillAddQO;
import com.bike.ztd.qo.WaybillCompleteQO;
import com.bike.ztd.qo.WaybillInfoAddQO;
import com.bike.ztd.vo.MyWaybillListVO;
import com.bike.ztd.vo.WaybillDetailsVO;
import com.bike.ztd.vo.WaybillListVO;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TWaybillService extends IService<TWaybill> {
    /**
     * 新建运单
     *
     * @param qo
     * @return
     */
    String addWaybill(WaybillAddQO qo);

    /**
     * 完成运单
     *
     * @param qo
     */
    void completeWaybill(WaybillCompleteQO qo);

    /**
     * 删除运单
     *
     * @param waybillId
     */
    void deleteWaybill(String waybillId);

    /**
     * 我的运单列表
     *
     * @param current
     * @param size
     * @param startAt
     * @param endAt
     * @return
     */
    Page<MyWaybillListVO> myList(int current, int size, Long startAt, Long endAt);

    /**
     * 运单列表
     *
     * @param userId
     * @param userPhone
     * @param local
     * @param current
     * @param size
     * @param startAt
     * @param endAt
     * @return
     */
    Page<WaybillListVO> list(String userId, String userPhone, String local, int current, int size, Long startAt, Long endAt);

    /**
     * 运单详情
     *
     * @param waybillId
     * @return
     */
    WaybillDetailsVO details(String waybillId);

    /**
     * 导出
     *
     * @param key
     * @param map
     */
    void listExport(String key, Map<String, Object> map);

    /**
     * 检查导出结果
     *
     * @param key
     * @return
     */
    String checkExport(String key);

    /**
     * 增加车辆信息
     *
     * @param qo
     */
    void addInfo(WaybillInfoAddQO qo);
}
