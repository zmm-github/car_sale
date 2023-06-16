import request from '@/utils/request'

export default {
  // 获取车系列表信息
  getCarSeriesList(searchModel) {
    return request({
      url: '/carSeries/getCarSeriesList',
      method: 'get',
      params: {
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize,
        carSeries: searchModel.carSeries,
        createPeople: searchModel.createPeople
      }
    });
  },
  // 新增车系
  addCarSeries(data) {
    return request({
      url: '/carSeries/addCarSeries',
      method: 'post',
      data
    });
  },
  // 删除车系
  deleteCarSeriesById(id) {
    return request({
      url: '/carSeries/deleteCarSeriesById/' + id,
      method: 'delete'
    });
  },
  // 修改车系
  updateCarSeriesById(data) {
    return request({
      url: '/carSeries/updateCarSeriesById',
      method: 'put',
      data
    });
  },

  // 新增或修改车系
  saveCarSeries(carSeries) {
    if (carSeries.id == null || carSeries.id == undefined) {
      return this.addCarSeries(carSeries);
    } else {
      return this.updateCarSeriesById(carSeries);
    }
  },

  // 根据多个id删除多个车系
  deleteCarSeriesByIds(data) {
    return request({
      url: '/carSeries/deleteCarSeriesByIds',
      method: 'delete',
      data
    });
  },

  // 根据id获取指定车系信息
  getCarSeriesById(id) {
    return request({
      url: `/carSeries/getCarSeriesById/${id}`,
      method: 'get'
    });
  },

  // 获取所有车系信息
  getAllCarSeries() {
    return request({
      url: '/carSeries/getAllCarSeries',
      method: 'get'
    });
  }
}