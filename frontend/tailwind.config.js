/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // 自定义主题色彩，让商城富有高级烘焙面包店的美感（如温暖的焦糖色、麦芽金）
        bakery: {
          primary: '#e07a5f', // 温馨暖橙
          secondary: '#f4f1de', // 面包奶油黄
          dark: '#3d405b', // 炭烤褐
          accent: '#81b29a', // 抹茶绿
          light: '#f2cc8f', // 麦芽金
          cream: '#FAF8F5', // 奶油白底色 (参考设计)
          burgundy: '#4A2E2B', // 深酒红/黑巧克力字色 (参考设计)
          brown: '#725C58' // 浅可可字色 (参考设计)
        }
      }
    },
  },
  plugins: [],
}
