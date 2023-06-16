import request from '@/utils/request'

export default {
  // 获取所有促销列表信息
  getPromotionList(searchModel) {
    return request({
      url: "/promotion/getPromotionList",
      method: 'get',
      params: {
        title: searchModel.title,
        createPeople: searchModel.createPeople,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增促销
  addPromotion(data) {
    return request({
      url: "/promotion/addPromotion",
      method: "post",
      data
    });
  },

  // 修改促销
  updatePromotion(data) {
    return request({
      url: "/promotion/updatePromotion",
      method: "put",
      data
    });
  },

  // 修改或新增促销
  savePromotion(promotion) {
    if (promotion.id == null || promotion.id == undefined) {
      return this.addPromotion(promotion);
    } else {
      return this.updatePromotion(promotion);
    }
  },

  // 根据指定Id删除指定促销
  deletePromotionById(id) {
    return request({
      url: "/promotion/deletePromotionById/" + id,
      method: "delete"
    });
  },

  // 删除选中的多个促销信息
  deletePromotionByIds(data) {
    return request({
      url: "/promotion/deletePromotionByIds",
      method: 'delete',
      data: data
    });
  },

  // 根据id查询指定促销
  getPromotionById(id) {
    return request({
      url: `/promotion/getPromotionById/${id}`,
      method: 'get'
    });
  }

}
