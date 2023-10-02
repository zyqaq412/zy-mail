import request from '@/utils/request'
const api_name = '/mails'
export default {

  mailList(mailPage) {
    return request({
      url: `${api_name}/list`,
      method: 'post',
      data: mailPage
    })
  },
}
