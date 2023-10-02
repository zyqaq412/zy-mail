<template>
  <div>
    <div id="a">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item class="input" label="主题">
          <el-input v-model="form.subject"></el-input>
        </el-form-item>
        <el-form-item class="input" label="收件人">
          <el-input v-model="form.toUser"></el-input>
        </el-form-item>
        <el-form-item label="定时">
          <el-switch v-model="form.timer"></el-switch>
        </el-form-item>
        <el-form-item class="input" label="cron表达式" v-if="form.timer">
          <el-input v-model="form.cron"></el-input>
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

        <el-form-item label="调度源" prop="source">
          <el-radio-group v-model="form.source">
            <el-radio v-for="appId in appIds" :label="appId">{{appId}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮件内容">
          <mavon-editor id="edit" ref="myEditor" v-model="form.content"
                        defaultOpen="edit"
                        :toolbars="toolbars"
                        :fullscreen="fullscreen"
                        @imgAdd="addImg" />
        </el-form-item>

        <el-form-item>
          <el-button  type="primary" @click="sendMail">发送邮件</el-button>
        </el-form-item>
      </el-form>
    </div>

  </div>
</template>
<script>
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import api from '@/api/mail/sendMail'
import source from "@/api/source/source";
export default {
  components: {
    'mavon-editor': mavonEditor.mavonEditor,


  },
  data() {
    return {
      appIds: [/*'zymail-server','测试系统'*/],
      form: {
        subject: '',
        toUser: '',
        startTime: '',
        endTime:'',
        cron: '',
        timer: false,
        content: '',
        source:''
      },
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: false, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true, // 预览
      },
      fullscreen:false,
    }
  },
  created() {
    this.getSources();
  },
  methods: {
    getSources(){
      source.getSources().then(res =>{
        this.appIds = res.data;
      })
    },
    sendMail() {
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
    },
    addImg(pos, file) {
      console.log("pos",pos)
/*      // 第一步.将图片上传到服务器.
      this.uploadImg(file).then(response => {
        // TODO 图片能成功上传，但是这里转成url有问题
        this.$refs.myEditor.$img2Url(pos, response)
      }).catch(error => {
        this.$message.error(error.msg)
      })*/
    },
  }
}
</script>
<style>
#edit{
  width: 1200px;
  resize: none; /* 禁止拖动 */
}
.input{
  width: 300px;
}
#a{
  margin-top: 20px;
}
</style>
