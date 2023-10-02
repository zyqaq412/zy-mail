import request from '@/utils/request'
const api_name = '/sources'
export default {

  getSources(){
    return request({
      url: `${api_name}`,
      method: 'get',
    })
  }
}
