<template>
  <div>
    <el-row>
      <el-col :span="6" v-for="(mailTemplate, index) in templateList" :key="index" :offset="index > 0 ? 1 : 0">
        <el-card>
          <div slot="header">
            <span>{{mailTemplate.name}}</span>
            <el-button type="text" class="button" style="padding: 3px 0"
                       v-clipboard:copy="mailTemplate.content"
                       v-clipboard:success="copySuccess">复制
            </el-button>
          </div>
          <div v-html="mailTemplate.content"></div>
          <div style="margin-top: 50px; border-top: 1px solid #ddd; font-size: 13px">
            <div style="margin-top: 10px">调度源：
              <router-link :to="`/app-monitor?appId=${mailTemplate.source}`"
                           style="text-decoration: underline">
                {{mailTemplate.source}}
              </router-link>
            </div>
            <div class="bottom time">创建时间：{{mailTemplate.createTime}}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import api from '@/api/mail/mail'
import VueClipboard from 'vue-clipboard2';
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
    getTemplates() {
      api.getTemplates().then(res=>{
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

</script>

<style>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 480px;
}
</style>
