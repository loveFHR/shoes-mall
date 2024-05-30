<template>
	<div class="layout">
		<el-container class="layout-container">
			<AsideMenu />
			<el-container class="layout-content-container">
				<el-header>
					<Header />
				</el-header>
				<Tabs />
				<el-main>
					<router-view v-slot="{ Component }">
						<transition name="slide-right" mode="out-in">
							<component :is="Component" class="layout-main" />
						</transition>
					</router-view>
				</el-main>
			</el-container>
		</el-container>
	</div>
</template>

<script setup>
import AsideMenu from './components/AsideMenu/index.vue'
import Header from './components/Header/index.vue'
import Tabs from './components/Tabs/index.vue'
</script>
