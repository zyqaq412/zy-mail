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
          <mavon-editor id="edit" v-model="form.tempContent" @imgAdd="addImg"
                        ref="myEditor"

          />
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

<script src="./sendMail.js" />
<link rel="stylesheet" type="text/css" href="./sendMail.css">
