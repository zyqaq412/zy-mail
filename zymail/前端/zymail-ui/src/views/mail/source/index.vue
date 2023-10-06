<template>
  <div>
    <div id="head">
      <el-select v-model="appId" placeholder="请选择调度源">
        <el-option
          v-for="id in appIds"
          :key="id"
          :label="id"
          :value="id">
        </el-option>
      </el-select>
      <span style="margin-left: 25px">调度器状态：
                <el-tag :type="getStatusType_source(status)"
                        disable-transitions>{{ getStatusDescription_source(status) }}
                    </el-tag>
            </span>
      <span style="margin-left: 50px">操作：</span>
      <el-button type="success" plain @click="StartScheduler">开启调度</el-button>
      <el-button type="warning" plain @click="PauseScheduler">暂停调度</el-button>
    </div>
    <div id="body">
      <el-table
        ref="filterTable"
        :data="jobs"
        id="table"
        style="width: 100%">
        <el-table-column
          label="工作名"
          width="180"
        >
          <template slot-scope="scope">
            <div @click="showModifyView(scope.row)" v-html="scope.row.jobName"></div>
          </template>
        </el-table-column>
        <el-table-column
          prop="state"
          label="状态"
          width="100"
          :filters="[{ text: '不存在', value: 0 }, { text: '运行', value: 1 }, { text: '暂停', value: 2 }
                    , { text: '完成', value: 3 }, { text: '错误', value: 4 }, { text: '阻塞', value: 5 } ]"
          :filter-method="filterState"
          filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusTagType(scope.row.state)"
              disable-transitions>{{ getStatusDescription(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="startTime"
          label="开始时间"
          sortable
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="endTime"
          label="结束时间"
          sortable
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="previousFireTime"
          label="上次调度时间"
          sortable
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="nextFireTime"
          label="下次调度时间"
          sortable
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="mail.subject"
          label="主题"
          width="180"
        >
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100">
          <template slot-scope="scope">
            <el-button type="warning" @click="PauseJob(scope.row.jobName,scope.row.jobGroupName,scope.row.state)"
                       size="small" plain>暂停
            </el-button>
            <el-button type="success" @click="ResumeJob(scope.row.jobName,scope.row.jobGroupName,scope.row.state)"
                       size="small" plain>恢复
            </el-button>
            <el-button type="danger" @click="DelJob(scope.row.jobName,scope.row.jobGroupName,scope.row.state)"
                       size="small" plain>移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="修改调度参数" :visible.sync="dialogFormVisible">
      <el-form :model="modifyDto">
        <el-form-item label="开始时间" >
          <el-input v-model="modifyDto.startTime" autocomplete="off" :disabled="this.type.s"></el-input>
        </el-form-item>
        <el-form-item label="结束时间" >
          <el-input v-model="modifyDto.endTime" autocomplete="off" :disabled="this.type.e"></el-input>
        </el-form-item>
        <el-form-item label="cron表达式" >
          <el-input v-model="modifyDto.cron" autocomplete="off" :disabled="this.type.c"></el-input>
        </el-form-item>
        <el-form-item label="请选择修改项" >
          <el-radio v-model="radio" label="1">开始时间</el-radio>
          <el-radio v-model="radio" label="2">结束时间</el-radio>
          <el-radio v-model="radio" label="3">cron表达式</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeModifyView">取 消</el-button>
        <el-button type="primary" @click="modify">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script src="./index.js"/>
<link rel="stylesheet" type="text/css" href="./index.css">
