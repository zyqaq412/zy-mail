<template>
  <div>
    <div id="a">
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item class="input" label="主题" prop="subject">
          <el-input v-model="form.subject"></el-input>
        </el-form-item>
        <el-form-item  class="input" label="收件人" prop="toUser">
          <el-input v-model="form.toUser"></el-input>
        </el-form-item>
        <el-form-item label="定时">
          <el-switch v-model="form.timer"></el-switch>
        </el-form-item>
        <el-form-item   label="cron表达式" v-if="form.timer">
          <el-col :span="50">
            <el-tooltip placement="top">
              <div slot="content"><a href="http://cron.qqe2.com/" target="_blank"
                                     style="color: white;font-size: 14px;">在线Cron表达式生成器</a></div>
              <el-input v-model="form.cron" placeholder="请填写cron表达式（如 * * * * * ? * ）"/>
            </el-tooltip>
          </el-col>
          <el-col :span="50" :offset="2">
            <el-radio-group v-model="interval" @change="onChange">
              <el-radio label="1">每分</el-radio>
              <el-radio label="2">每时</el-radio>
              <el-radio label="3">每天</el-radio>
            </el-radio-group>
          </el-col>
<!--          <el-input v-model="form.cron"></el-input>-->
        </el-form-item>
        <el-form-item label="时间范围" v-if="form.timer">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="开始时间"
          ></el-date-picker>
          至
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="结束时间"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="调度源"  prop="source">
          <el-radio-group v-model="form.source">
            <el-radio v-for="appId in appIds" :label="appId">{{appId}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮件内容">
<!--          <mavon-editor id="edit" ref="myEditor" v-model="form.content"
                        defaultOpen="edit"
                        :toolbars="toolbars"
                        :fullscreen="fullscreen"
                        @imgAdd="addImg" />-->
          <mavon-editor id="edit" v-model="form.content" />
        </el-form-item>

        <el-form-item>
          <el-button  type="primary" @click="sendMail">发送邮件</el-button>
        </el-form-item>
      </el-form>
      <el-dialog title="模板保存" :visible.sync="dialogVisible">
        <el-form :model="template">
          <el-form-item label="模板名称" >
            <el-input v-model="template.name" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveTemplate">确 定</el-button>
        </div>
      </el-dialog>
    </div>

  </div>
</template>
<script>
import mavonEditor from 'mavon-editor'
import marked from 'marked';
import 'mavon-editor/dist/css/index.css'
import api from '@/api/mail/sendMail'
import source from "@/api/source/source";
import axios from 'axios';
import mail from "@/api/mail/mail";
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
      mail.saveTemplate(this.template).then(res=>{
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
      source.getSources().then(res =>{
        this.appIds = res.data;
      })
    },
    sendMail() {
      // 在发送邮件的方法中可以调用表单的验证方法
      this.$refs.form.validate(valid => {
        if (valid) {
          this.template.content = this.form.content;
          this.template.source = this.form.source;
          this.dialogVisible = true;
          // 将markdown格式转为html
          this.form.content = marked(this.form.content);

          // 表单验证通过，执行发送邮件的逻辑
          if(this.form.timer){
            let temp = this.$notify({
              title: '提示',
              message: '定时任务创建中，请等待',
              duration: 0
            });
            api.sendMail(this.form).then(()=>{
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
            api.sendMail(this.form).then(()=>{
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
    uploadImg(img) {
      const formData = new FormData()
      formData.append('img', img)
      return axios.post('http://127.0.0.1:36677/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        console.log('上传图片', response.data);
        return response.data.data
      }).catch(error => {
        throw new Error(error.message)
      })
    },
    // 绑定@imgAdd event
    addImg(pos, file) {
      console.log("pos",pos)
      // 第一步.将图片上传到服务器.
      this.uploadImg(file).then(response => {
        // TODO 图片能成功上传，但是这里转成url有问题
        this.$refs.myEditor.$img2Url(pos, response)
      }).catch(error => {
        this.$message.error(error.msg)
      })
    },
  }
}
</script>
<style>
#edit{
  width: 1100px;
  resize: none; /* 禁止拖动 */
}
.input{
  width: 300px;
}
#a{
  margin-top: 20px;
}
</style>
