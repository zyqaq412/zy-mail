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
          <div v-html="getHtml(mailTemplate.content)"></div>
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

<script src="./template.js" />
<link rel="stylesheet" type="text/css" href="./template.css">
