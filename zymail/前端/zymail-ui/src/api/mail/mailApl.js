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
  getMailById(id) {
    return request({
      url: `${api_name}/${id}`,
      method: 'get',
    })
  },
  saveTemplate(mail) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: mail
    })
  },
  getTemplates() {
    return request({
      url: `${api_name}/`,
      method: 'get',
    })
  },
  sendMail(mail) {
    return request({
      url: `${api_name}`,
      method: 'post',
      data: mail
    })
  },
}
