import request from '@/utils/request'

export default {
  // 获取所有顾客列表信息
  getCustomerList(searchModel) {
    return request({
      url: "/customer/getCustomerList",
      method: 'get',
      params: {
        name: searchModel.name,
        status: searchModel.status,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增顾客
  addCustomer(data) {
    return request({
      url: "/customer/addCustomer",
      method: "post",
      data
    });
  },

  // 修改顾客
  updateCustomer(data) {
    return request({
      url: "/customer/updateCustomer",
      method: "put",
      data
    });
  },

  // 修改或新增顾客
  saveCustomer(customer) {
    if (customer.id == null || customer.id == undefined) {
      return this.addCustomer(customer);
    } else {
      return this.updateCustomer(customer);
    }
  },

  // 根据指定Id删除指定顾客
  deleteCustomerById(id) {
    return request({
      url: "/customer/deleteCustomerById/" + id,
      method: "delete"
    });
  },

  // 删除选中的多个顾客信息
  deleteCustomerByIds(data) {
    return request({
      url: "/customer/deleteCustomerByIds",
      method: 'delete',
      data: data
    });
  },

  // 根据id查询指定顾客
  getCustomerById(id) {
    return request({
      url: `/customer/getCustomerById/${id}`,
      method: 'get'
    });
  }

}
