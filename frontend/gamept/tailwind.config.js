/** @type {import('tailwindcss').Config} */
export default {
  content: [
    // 모든 html 파일 경로 등록
    "./**/*.html",
    "./src/**/*.{html,js}",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      width: {
        '300': '300px',
        '400': '400px'
      },
      backgroundColor: {
        '1B1212': '#1B1212',
      },
    },
  },
  plugins: [],
}

