import request from '@/utils/request'

export default {
  // 获取所有救援记录列表信息
  getRescueRecordList(searchModel) {
    return request({
      url: "/rescueRecord/getRescueRecordList",
      method: 'get',
      params: {
        name: searchModel.name,
        content: searchModel.content,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增救援记录
  addRescueRecord(data) {
    return request({
      url: "/rescueRecord/addRescueRecord",
      method: "post",
      data
    });
  },

  // 修改救援记录
  updateRescueRecord(data) {
    return request({
      url: "/rescueRecord/updateRescueRecord",
      method: "put",
      data
    });
  },

  // 修改或新增救援记录
  saveRescueRecord(rescueRecord) {
    if (rescueRecord.id == null || rescueRecord.id == undefined) {
      return this.addRescueRecord(rescueRecord);
    } else {
      return this.updateRescueRecord(rescueRecord);
    }
  },

  // 根据Id删除指定救援记录
  deleteRescueRecordById(id) {
    return request({
      url: "/rescueRecord/deleteRescueRecordById/" + id,
      method: "delete"
    });
  },

  // 删除选中的多个救援记录信息
  deleteRescueRecordByIds(data) {
    return request({
      url: "/rescueRecord/deleteRescueRecordByIds",
      method: 'delete',
      data: data
    });
  },

  // 根据id查询指定救援记录
  getRescueRecordById(id) {
    return request({
      url: `/rescueRecord/getRescueRecordById/${id}`,
      method: 'get'
    });
  },

  // 获取救援统计折线图
  getRescueLine() {
    return request({
      url: '/rescueRecord/getRescueLine',
      method: 'get'
    });
  }

}
