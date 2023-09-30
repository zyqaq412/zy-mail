import request from '@/utils/request'
const api_name = '/mails'
export default {

  /*
  获取权限(菜单/功能)列表
  */
  sendMail(mail) {
    return request({
      url: `${api_name}`,
      method: 'post',
      data: mail
    })
  },
}
