import request from '@/utils/request'

export default {
  // 获取所有预约记录列表信息
  getOrderRecordList(searchModel) {
    return request({
      url: "/orderRecord/getOrderRecordList",
      method: 'get',
      params: {
        name: searchModel.name,
        type: searchModel.type,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增预约记录
  addOrderRecord(data) {
    return request({
      url: "/orderRecord/addOrderRecord",
      method: "post",
      data
    });
  },

  // 修改预约记录
  updateOrderRecord(data) {
    return request({
      url: "/orderRecord/updateOrderRecord",
      method: "put",
      data
    });
  },

  // 修改或新增预约记录
  saveOrderRecord(orderRecord) {
    if (orderRecord.id == null || orderRecord.id == undefined) {
      return this.addOrderRecord(orderRecord);
    } else {
      return this.updateOrderRecord(orderRecord);
    }
  },

  // 根据Id删除指定预约记录
  deleteOrderRecordById(id) {
    return request({
      url: "/orderRecord/deleteOrderRecordById/" + id,
      method: "delete"
    });
  },

  // 删除选中的多个预约记录信息
  deleteOrderRecordByIds(data) {
    return request({
      url: "/orderRecord/deleteOrderRecordByIds",
      method: 'delete',
      data: data
    });
  },

  // 根据id查询指定预约记录
  getOrderRecordById(id) {
    return request({
      url: `/orderRecord/getOrderRecordById/${id}`,
      method: 'get'
    });
  }

}
