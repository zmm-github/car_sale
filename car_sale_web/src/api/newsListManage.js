import request from '@/utils/request'

export default {
  // 获取所有顾客列表信息
  getNewsList(searchModel) {
    return request({
      url: "/news/getNewsList",
      method: 'get',
      params: {
        title: searchModel.title,
        author: searchModel.author,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增新闻
  addNews(data) {
    return request({
      url: "/news/addNews",
      method: "post",
      data
    });
  },

  // 修改新闻
  updateNewsById(data) {
    return request({
      url: "/news/updateNewsById",
      method: "put",
      data
    });
  },

  // 修改或新增预约记录
  saveNews(news) {
    if (news.id == null || news.id == undefined) {
      return this.addNews(news);
    } else {
      return this.updateNewsById(news);
    }
  },

  // 根据指定Id删除指定新闻
  deleteNewsById(id) {
    return request({
      url: "/news/deleteNewsById/" + id,
      method: "delete"
    });
  },

  // 删除选中的多个新闻信息
  deleteNewsByIds(data) {
    return request({
      url: "/news/deleteNewsByIds",
      method: "delete",
      data: data
    });
  },

  // 根据id查询指定新闻内容
  getNewsById(id){
    return request({
      url: "/news/getNewsById/" + id,
      method: "get"
    });
  },

  // 获取所有新闻
  getAllNews() {
    return request({
      url: "/news/getAllNews",
      method: 'get'
    });
  }

}
