import {createRouter, createWebHistory} from 'vue-router'
import Home from '@/views/Home'
import AccountManagement from "@/views/AccountManagement";
import TerminalManagement from "@/views/TerminalManagement";
import Login from "@/views/Login";
import ForgotPassword from "@/views/ForgotPassword";
import Register from "@/views/Register";
import Map from "@/components/OlMap";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/account_management',
    name: 'AccountManagement',
    component: AccountManagement
  },
  {
    path: '/terminal_management',
    name: 'TerminalManagement',
    component: TerminalManagement
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/forgot_password',
    name: 'ForgotPassword',
    component: ForgotPassword
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/map',
    name: 'Map',
    component: Map
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
