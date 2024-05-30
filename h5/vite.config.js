import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import postcsspxtoviewport8plugin from 'postcss-px-to-viewport-8-plugin'
import eslintPlugin from 'vite-plugin-eslint'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { VantResolver } from '@vant/auto-import-resolver'

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, __dirname)
	return {
		plugins: [
			vue(),
			AutoImport({
				imports: ['vue', 'vue-router', 'pinia'],
				dts: 'src/auto-import.d.ts',
				eslintrc: {
					enabled: true
				}
			}),
			Components({
				resolvers: [VantResolver()]
			}),
			eslintPlugin({
				include: ['src/**/*.js', 'src/**/*.vue', 'src/*.js', 'src/*.vue']
			}),
			createSvgIconsPlugin({
				iconDirs: [resolve(process.cwd(), 'src/icons')],
				symbolId: 'icon-[dir]-[name]'
			})
		],
		server: {
			host: '0.0.0.0',
			port: 8080,
			open: true,
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
			alias: [{ find: '@', replacement: resolve(__dirname, 'src') }]
		},
		css: {
			postcss: {
				plugins: [
					postcsspxtoviewport8plugin({
						unitToConvert: 'px',
						viewportWidth: (file) => (file.indexOf('van') > 0 ? 375 : 750),
						unitPrecision: 5, // 单位转换后保留的精度
						propList: ['*'], // 能转化为vw的属性列表
						viewportUnit: 'vw', // 希望使用的视口单位
						fontViewportUnit: 'vw', // 字体使用的视口单位
						selectorBlackList: ['ignore-'], // 需要忽略的CSS选择器，不会转为视口单位，使用原有的px等单位。
						minPixelValue: 1, // 设置最小的转换数值，如果为1的话，只有大于1的值会被转换
						mediaQuery: true, // 媒体查询里的单位是否需要转换单位
						replace: true, //  是否直接更换属性值，而不添加备用属性
						exclude: [], // 忽略某些文件夹下的文件或特定文件，例如 'node_modules' 下的文件
						include: [], // 如果设置了include，那将只有匹配到的文件才会被转换
						landscape: false, // 是否添加根据 landscapeWidth 生成的媒体查询条件 @media (orientation: landscape)
						landscapeUnit: 'vw', // 横屏时使用的单位
						landscapeWidth: 1628 // 横屏时使用的视口宽度
					})
				]
			}
		}
	}
})
