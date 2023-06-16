import request from '@/utils/request'

export default {
  // 获取所有订单列表信息
  getOrdersList(searchModel) {
    return request({
      url: "/orders/getOrdersList",
      method: 'get',
      params: {
        name: searchModel.name,
        saleName: searchModel.saleName,
        status: searchModel.status,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增订单
  addOrders(data) {
    return request({
      url: "/orders/addOrders",
      method: "post",
      data
    });
  },

  // 修改订单
  updateOrders(data) {
    return request({
      url: "/orders/updateOrders",
      method: "put",
      data
    });
  },

  // 修改或新增订单
  saveOrders(orders) {
    if (orders.id == null || orders.id == undefined) {
      return this.addOrders(orders);
    } else {
      return this.updateOrders(orders);
    }
  },

  // 根据Id删除指定订单
  deleteOrdersById(id) {
    return request({
      url: "/orders/deleteOrdersById/" + id,
      method: "delete"
    });
  },

  // 删除选中的多个订单信息
  deleteOrdersByIds(data) {
    return request({
      url: "/orders/deleteOrdersByIds",
      method: 'delete',
      data: data
    });
  },

  // 根据id查询指定订单
  getOrdersById(id) {
    return request({
      url: `/orders/getOrdersById/${id}`,
      method: 'get'
    });
  }

}
