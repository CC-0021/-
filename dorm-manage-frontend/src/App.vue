<template>
  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<script setup>
</script>

<style>
#app {
  min-height: 100vh;
  font-family: 'DM Sans', 'Noto Sans SC', system-ui, -apple-system, sans-serif;
  color: #292524;
  background: #faf8f5;
}

/* ---------- Page transition ---------- */
.fade-enter-active {
  transition: opacity 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.fade-leave-active {
  transition: opacity 0.15s ease;
}
.fade-enter-from {
  opacity: 0;
  transform: translateY(8px);
}
.fade-leave-to {
  opacity: 0;
}

/* ---------- Typography defaults ---------- */
h1, h2, h3, h4, h5, h6 {
  font-family: 'Noto Serif SC', 'DM Sans', 'Noto Sans SC', serif;
}

/* ---------- Button refinements ---------- */
.el-button {
  font-weight: 500;
  transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
}
.el-button:active {
  transform: scale(0.97);
}

/* ---------- Input refinements ---------- */
.el-input__wrapper {
  border-radius: 8px !important;
  transition: all 0.2s ease !important;
}

/* ---------- Tag refinements ---------- */
.el-tag {
  font-weight: 500;
  letter-spacing: 0.01em;
}

/* ---------- Card-like hover utility ---------- */
.hover-lift {
  transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1),
              box-shadow 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.hover-lift:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(28, 25, 23, 0.08);
}
</style>
