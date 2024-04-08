import mailApi from '@/api/mail/mailApl'
import VueClipboard from 'vue-clipboard2';
import  marked  from 'marked';
export default {
  mounted() {
    this.getTemplates();
    VueClipboard.config.autoSetContainer = true;
  },
  data() {
    return {
      templateList:[]
    }
  },

  methods:{
    getHtml(content){
      return marked(content);
    },
    getTemplates() {
      mailApi.getTemplates().then(res=>{
        this.templateList = res.data;
      })
    },
    copySuccess() {
      this.$message({
        message: '复制成功，快去发送邮件吧',
        type: 'success'
      })
    }
  }
}
