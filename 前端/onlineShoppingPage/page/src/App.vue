<template>
  <div id="app">
    <NavBar />
    <main class="page">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import NavBar from '@/components/NavBar.vue'
</script>

<style scoped>
/* 与 NavBar 高度一致，避免内容被遮住 */
.page {
  padding-top: 76px; /* NavBar 实际高度 72，加一点安全距离 */
  background: #fafafa;
  min-height: 100vh;
}
</style>
