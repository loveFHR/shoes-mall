import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import { visualizer } from 'rollup-plugin-visualizer'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import eslintPlugin from 'vite-plugin-eslint'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, __dirname)
	return {
		plugins: [
			vue(),
			AutoImport({
				imports: ['vue', 'vue-router', 'pinia'],
				resolvers: [ElementPlusResolver()]
			}),
			Components({
				resolvers: [ElementPlusResolver()]
			}),
			visualizer({
				open: false,
				filename: 'stats.html',
				gzipSize: true,
				brotliSize: true
			}),
			eslintPlugin({
				include: ['src/**/*.js', 'src/**/*.vue', 'src/*.js', 'src/*.vue']
			}),
			createSvgIconsPlugin({
				iconDirs: [path.resolve(process.cwd(), 'src/icons')],
				symbolId: 'icon-[dir]-[name]'
			})
		],
		server: {
			open: true,
			host: '0.0.0.0',
			port: '8080',
			proxy: {
				'/api': {
					target: 'http://139.9.181.192:2913/api',
					// target: 'http://192.168.0.108:2913/api',
					changeOrigin: true,
					rewrite: (path) => {
						const reg = new RegExp('/api', 'g')
						return path.replace(reg, '')
					}
				}
			}
		},
		resolve: {
			alias: [{ find: '@', replacement: path.resolve(__dirname, 'src') }]
		}
	}
})
