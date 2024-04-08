import mavonEditor from 'mavon-editor'
import marked  from 'marked'
import 'mavon-editor/dist/css/index.css'

import sourceApi from "@/api/source/sourceApi";
import axios from 'axios';
import mailApi from "@/api/mail/mailApl";
export default {
  components: {
    'mavon-editor': mavonEditor.mavonEditor,


  },
  data() {
    return {
      dialogVisible: false,
      interval: '',
      appIds: [/*'zymail-server','测试系统'*/],
      template:{
        name:'',
        source:'',
        content:'',
        createTime:''
      },
      form: {
        subject: '',
        toUser: '',
        startTime: '',
        endTime:'',
        cron: '',
        timer: false,
        content: '',
        source:'',
        tempContent:''
      },
      rules: {
        subject: [
          { required: true, message: '请输入邮件主题', trigger: 'blur' }
        ],
        toUser: [
          { required: true, message: '请选择接收者邮箱', trigger: 'blur' }
        ],
        source: [
          { required: true, message: '请选择调度源', trigger: 'change' }
        ],
      },
    }
  },
  created() {
    this.getSources();
  },
  methods: {
    saveTemplate(){
      this.dialogVisible = false;
      mailApi.saveTemplate(this.template).then(res=>{
        this.$message({
          message: '模板保存成功',
          type: 'success'
        });
      })
    },
    onChange (index) {
      switch (index) {
        case '1':
          this.form.cron = '0 0/1 * * * ? *'
          break
        case '2':
          this.form.cron = '0 0 0/1 * * ? *'
          break
        case '3':
          this.form.cron = '0 0 0 1/1 * ? *'
          break
      }

    },
    getSources(){
      sourceApi.getSources().then(res =>{
        this.appIds = res.data;
      })
    },
    sendMail() {
      // 在发送邮件的方法中可以调用表单的验证方法
      this.$refs.form.validate(valid => {
        if (valid) {
          // 将markdown格式转为html
          this.form.content = marked(this.form.tempContent);
          this.template.content = this.form.tempContent;
          this.template.source = this.form.source;
          this.dialogVisible = true;
          // 表单验证通过，执行发送邮件的逻辑
          if(this.form.timer){
            let temp = this.$notify({
              title: '提示',
              message: '定时任务创建中，请等待',
              duration: 0
            });
            mailApi.sendMail(this.form).then(()=>{
              temp.close()
              this.form = [];
              this.$message({
                message: '任务创建成功',
                type: 'success'
              });
            })
          }else {
            let temp = this.$notify({
              title: '提示',
              message: '消息发送中，请等待',
              duration: 0
            });
            mailApi.sendMail(this.form).then(()=>{
              temp.close()
              this.form = [];
              this.$message({
                message: '邮件发送成功',
                type: 'success'
              });
            })
          }
        } else {
          // 表单验证失败，不执行发送邮件的逻辑
          return false;
        }
      });

    },
    // 使用picgo 实现图片生成
    addImg(pos, file) {
      return axios.post('http://127.0.0.1:36677/upload').then(response => {
        const imgUrl = response.data.result[0];
        const markdownImage = `![图片描述]( ${imgUrl} )`;
        // 获取编辑器的内容
        let editorContent = this.form.tempContent;
        // let url = '/!\\[image\\.png\\]\\(' + pos + '\\)/'
        let url = '![image.png]('+pos+')'
        // 替换默认生成的文本
        editorContent = editorContent.replace(url, markdownImage);

        // 更新编辑器的内容
        this.form.tempContent = editorContent;
        return imgUrl

      }).catch(error => {
        throw new Error(error.message)
      })
    },
  }
}
