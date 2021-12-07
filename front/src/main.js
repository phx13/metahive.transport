import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";
import $ from "jquery";
import "@fortawesome/fontawesome-free/css/fontawesome.min.css";
import "@fortawesome/fontawesome-free/css/brands.min.css";
import "@fortawesome/fontawesome-free/css/solid.min.css";
import "./assets/css/style.css";
import 'xe-utils'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'
import * as echarts from 'echarts';
import dayjs from "dayjs"


const app = createApp(App);
app.use(store);
app.use(router);
app.use(VXETable);
app.use($);
app.config.globalProperties.$XModal = VXETable.modal;
app.config.globalProperties.$dayjs = dayjs;
app.config.globalProperties.echarts = echarts;
app.mount('#app');
