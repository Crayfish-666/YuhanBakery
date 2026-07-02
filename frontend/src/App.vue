<template>
  <!-- 根视图入口 -->
  <router-view />
  <LoginModal />
</template>

<script setup>
import LoginModal from '@/components/LoginModal.vue'
</script>
