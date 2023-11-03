import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 3000,
    hmr: {
        host: "localhost",
        port: 8080,
        // protocol: "wss",
    },
  },
  plugins: [react()],
  resolve: {
    alias: [{ find: '@', replacement: '/src' }],
  },
  define: {
    global: {},
  },
});
