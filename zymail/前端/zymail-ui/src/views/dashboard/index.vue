<template>
  <div class="dashboard-container">
    <div class="dashboard-text">青春是一个短暂的美梦, 当你醒来时, 它早已消失无踪<br>
      少量的邪恶足以抵消全部高贵的品质, 害得人声名狼藉



    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
