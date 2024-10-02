import { createWebHistory, createRouter } from 'vue-router'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Feed from '@/views/Feed.vue'
import Board from '@/views/Board.vue'
import FarmingNote from '@/views/FarmingNote.vue'
import MyInfo from '@/views/MyInfo.vue'
import store from '@/store'

const routes = [
    { path: '/', component: Home, meta: { name: "home" ,isShowHeader: true,isShowFooter:true}, },
    { path: '/login', component: Login ,meta: { name: "login",isShowHeader: false,isShowFooter:false }, },
    { path: '/feed', component: Feed ,meta: { name: "feed",isShowHeader: true ,isShowFooter:true}, },
    { path: '/board', component: Board ,meta: { name: "board" ,isShowHeader: true,isShowFooter:true}, },
    { path: '/farming-note', component: FarmingNote ,meta: { name: "farming-note" ,isShowHeader: true,isShowFooter:true},},
    { path: '/my-info', component: MyInfo ,meta: { name: "my-info" ,isShowHeader: true,isShowFooter:true}, },
  ]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.afterEach((to,from) => {
  let menuInfo = to.meta;
  store.commit('setMenuInfo', menuInfo); //path 값으로 해도 되지만 일단 router에서 store 접근하는 부분 테스트를 위해 적용해봄
});
export default router