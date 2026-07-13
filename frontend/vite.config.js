import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Frontend runs on :5173 (matches CorsConfig on the backend, Step 5)
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
  },
});