import request from '@/utils/request'

export default {
  // 获取所有配件列表信息
  getAccessoryList(searchModel) {
    return request({
      url: "/accessory/getAccessoryList",
      method: 'get',
      params: {
        accessoryName: searchModel.accessoryName,
        carSeries: searchModel.carSeries,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增配件
  addAccessory(data) {
    return request({
      url: "/accessory/addAccessory",
      method: "post",
      data
    });
  },

  // 修改配件
  updateAccessory(data) {
    return request({
      url: "/accessory/updateAccessory",
      method: "put",
      data
    });
  },

  // 修改或新增配件
  saveAccessory(accessory) {
    if (accessory.id == null || accessory.id == undefined) {
      return this.addAccessory(accessory);
    } else {
      return this.updateAccessory(accessory);
    }
  },

  // 根据指定Id删除指定配件
  deleteAccessoryById(id) {
    return request({
      url: "/accessory/deleteAccessoryById/" + id,
      method: "delete"
    });
  },

  // 删除选中的多个配件信息
  deleteAccessoryByIds(data) {
    return request({
      url: "/accessory/deleteAccessoryByIds",
      method: 'delete',
      data: data
    });
  },

  // 根据id查询指定配件
  getAccessoryById(id) {
    return request({
      url: `/accessory/getAccessoryById/${id}`,
      method: 'get'
    });
  }

}
