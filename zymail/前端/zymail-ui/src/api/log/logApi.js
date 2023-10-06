import request from '@/utils/request'
const api_name = '/logs'
export default {

  // 获取全部日志
/*  getAllLogs() {
    return request({
      url: `${api_name}`,
      method: 'get',
    })
  },
  getLogsByAppId(appId) {
    return request({
      url: `${api_name}/${appId}`,
      method: 'get',
    })
  },*/
  // 获取全部日志
  getPageLogs(mailPage,appId) {
    return request({
      url: `${api_name}/${appId}`,
      method: 'post',
      data:mailPage
    })
  },
}
