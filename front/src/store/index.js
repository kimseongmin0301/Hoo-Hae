import { createStore } from 'vuex'
import menuStore from './modules/menuStore'
// Create a new store instance.
const store = createStore({
  // namespaced: true,
  state: { // 컴포넌트 간에 공유할 data 속성
  },
  getters: {},
  mutations: {},
  actions: {},
  modules: {
      menuStore,
  }
})

export default store