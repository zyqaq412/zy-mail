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
    </div>
    <div id="body">
      <el-table
        id="table"
        ref="filterTable"
        :data="logs"
        style="width: 100%">
        <el-table-column
          prop="createTime"
          label="时间"
          sortable
          width="180">
        </el-table-column>
        <el-table-column
          prop="level"
          label="级别"
          width="100"
          :filters="[{ text: '通知', value: 0 }, { text: '警告', value: 1 }, { text: '危险', value: 2 }]"
          :filter-method="filterState"
          filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusTagType(scope.row.level)"
              disable-transitions>{{ getStatusDescription(scope.row.level) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="source"
          label="调度源"
          width="180"
        >
        </el-table-column>
        <el-table-column
          label="内容"
        >
          <template slot-scope="scope">
            <div v-html="scope.row.content"></div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="mailPage.currentPage"
        :page-sizes="[3, 5, 10]"
        :page-size="100"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import source from "@/api/source/source";
import log from "@/api/log/log";

export default {
  data() {
    return {
      total: 0,
      mailPage: {
        currentPage: 1,
        size: 3,
      },
      appId:'',
      logs:[],
      status:undefined,
      appIds: [/*'zymail-server','测试系统'*/],
      // 状态数字到状态描述的映射
      statusMap: {
        0: '通知',
        1: '警告',
        2: '危险',
      },
    }
  },
  mounted() {
    this.getSources();
    this.getPageLogs();
  },
  watch:{
    appId: function(newValue, oldValue) {
      // 在这里执行您的逻辑，比如调用 this.getJobsByAppId(this.appId);
      this.getPageLogs();
    }

  },
  methods: {
    // region 分页查询
    handleSizeChange(val) {
      this.mailPage.size = val;
      this.getPageLogs();
    },
    handleCurrentChange(val) {
      this.mailPage.currentPage = val;
      this.getPageLogs();
    },
    getPageLogs() {
      log.getPageLogs(this.mailPage,this.appId).then(res=>{
        this.logs = res.data.rows;
        this.total = res.data.total
      })
    },
      // endregion

    getSources() {
      source.getSources().then(res => {
        this.appIds = res.data;
      })
    },
    // 根据状态数字获取状态标签的类型（颜色）
    getStatusTagType(state) {
      switch (state) {
        case 0:
          return 'info';
        case 1:
          return 'warning';
        case 2:
          return 'danger';
        default:
          return 'info'; // 未知状态
      }
    },
    // 根据状态数字获取状态描述
    getStatusDescription(state) {
      return this.statusMap[state] || '未知状态';
    },
    filterState(value, row) {
      return row.level === value;
    },
  }
}
</script>
<style>
#table {
  height: 75vh; /* 设置表格的高度为屏幕高度的100% */
  /* 可以添加其他样式属性，例如滚动条、背景色等 */
  overflow-y: auto; /* 如果表格内容过多，添加垂直滚动条 */
}
#head {
  background-color: #f0f0f0; /* 设置表头背景颜色 */
  border-bottom: 1px solid #ddd; /* 添加底部边框 */
  padding: 10px; /* 添加内边距 */
}

/* 表体样式 */
#body {
  /* 如果需要添加背景色或其他样式，可以在这里添加 */
  padding: 10px; /* 添加内边距 */
}
</style>
