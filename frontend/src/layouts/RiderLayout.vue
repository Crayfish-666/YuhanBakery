<template>
  <div class="rider-layout flex flex-col min-h-screen bg-rider-dark text-white">
    <!-- 中间主内容路由渲染区 -->
    <main class="flex-grow pb-[80px] relative">
      <router-view />
    </main>

    <!-- 底部导航栏 -->
    <van-tabbar v-model="active" active-color="#00FF66" inactive-color="#8E8E93" route class="bg-rider-surface border-t border-[#333333]">
      <van-tabbar-item replace to="/rider/lobby" icon="wap-home-o">抢单</van-tabbar-item>
      <van-tabbar-item replace to="/rider/active" icon="logistics">配送中</van-tabbar-item>
      <van-tabbar-item replace to="/rider/earnings" icon="gold-coin-o">收益</van-tabbar-item>
      <van-tabbar-item replace to="/rider/profile" icon="contact">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Tabbar as VanTabbar, TabbarItem as VanTabbarItem } from 'vant'

const active = ref(0)
</script>

<style scoped>
.rider-layout {
  overscroll-behavior-y: none;
  user-select: none;
  background-color: #0A0A0A;
}
:deep(.van-tabbar) {
  background-color: #1C1C1E !important;
}
:deep(.van-tabbar-item--active) {
  background-color: transparent !important;
}
:deep(.van-dialog.dark-dialog) {
  background-color: #1C1C1E;
  color: #FFFFFF;
}
:deep(.van-dialog__header) {
  color: #FFFFFF;
}
:deep(.van-dialog__message) {
  color: #8E8E93;
}
</style>
