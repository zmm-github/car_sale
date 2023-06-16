import request from '@/utils/request'

export default {
  // 获取车型列表
  getCarModelList(searchModel) {
    return request({
      url: '/carModel/getCarModelList',
      method: 'get',
      params: {
        carModel: searchModel.carModel,
        createPeople: searchModel.createPeople,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增车型
  addCarModel(data) {
    return request({
      url: '/carModel/addCarModel',
      method: 'post',
      data
    });
  },

  // 删除车型
  deleteCarModelById(id) {
    return request({
      url: '/carModel/deleteCarModelById/' + id,
      method: 'delete'
    });
  },

  // 修改车型
  updateCarModelById(data) {
    return request({
      url: '/carModel/updateCarModelById',
      method: 'put',
      data
    });
  },

  // 新增或修改车型
  saveCarModel(carModel) {
    if(carModel.id == null || carModel.id == undefined) {
      return this.addCarModel(carModel);
    } else {
      return this.updateCarModelById(carModel);
    }
  },

  // 根据多个id删除多个车型
  deleteCarModelByIds(data) {
    return request({
      url: '/carModel/deleteCarModelByIds',
      method: 'delete',
      data
    });
  },

  // 本地图片上传
  uploadLocalPicture(data) {
    return request({
      url: '/file/upload',
      method: 'post',
      data
    });
  },

  // 根据id获取指定车型信息
  getCarModelById(id) {
    return request({
      url: `/carModel/getCarModelById/${id}`,
      method: 'get'
    });
  },

  // 获取所有车型信息
  getAllCarModel() {
    return request({
      url: '/carModel/getAllCarModel',
      method: 'get'
    });
  }
}

