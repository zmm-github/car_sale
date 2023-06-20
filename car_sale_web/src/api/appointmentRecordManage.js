import request from '@/utils/request'

export default {
  // 获取所有预约记录列表信息
  getAppointmentRecordList(searchModel) {
    return request({
      url: "/appointmentRecord/getAppointmentRecordList",
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
  addAppointmentRecord(data) {
    return request({
      url: "/appointmentRecord/addAppointmentRecord",
      method: "post",
      data
    });
  },

  // 修改预约记录
  updateAppointmentRecord(data) {
    return request({
      url: "/appointmentRecord/updateAppointmentRecord",
      method: "put",
      data
    });
  },

  // 修改或新增预约记录
  saveAppointmentRecord(appointmentRecord) {
    if (appointmentRecord.id == null || appointmentRecord.id == undefined) {
      return this.addAppointmentRecord(appointmentRecord);
    } else {
      return this.updateAppointmentRecord(appointmentRecord);
    }
  },

  // 根据Id删除指定预约记录
  deleteAppointmentRecordById(id) {
    return request({
      url: "/appointmentRecord/deleteAppointmentRecordById/" + id,
      method: "delete"
    });
  },

  // 删除选中的多个预约记录信息
  deleteAppointmentRecordByIds(data) {
    return request({
      url: "/appointmentRecord/deleteAppointmentRecordByIds",
      method: 'delete',
      data: data
    });
  },

  // 根据id查询指定预约记录
  getAppointmentRecordById(id) {
    return request({
      url: `/appointmentRecord/getAppointmentRecordById/${id}`,
      method: 'get'
    });
  },

  // 获取预约统计柱状图
  getAppointmentPie() {
    return request({
      url: '/appointmentRecord/getAppointmentPie',
      method: 'get'
    });
  }

}
