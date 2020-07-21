import Vue from 'vue';
import Router from 'vue-router';
import Home from './views/StaffList.vue';
import Login from './views/Login.vue';
import Register from './views/Register.vue';

Vue.use(Router);

export const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/home',
      component: Home
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/register',
      component: Register
    },
    {
      path: '/calendar',
      name: 'Calendar',
      // lazy-loaded
      component: () => import('./views/Calendar.vue')
    },
    {
      path: '/delete',
      name: 'Delete',
      // lazy-loaded
      component: () => import('./views/DeleteConfirmation.vue')
    },
    {
      path: '/event/:id',
      name: 'EventInfo',
      component: () => import('./views/EventInfo.vue')
    },
    {
      path: '/profile',
      name: 'profile',
      // lazy-loaded
      component: () => import('./views/Profile.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      // lazy-loaded
      component: () => import('./views/BoardAdmin.vue')
    },
    {
      path: '/mod',
      name: 'moderator',
      // lazy-loaded
      component: () => import('./views/BoardModerator.vue')
    },
    {
      path: '/user',
      name: 'user',
      // lazy-loaded
      component: () => import('./views/BoardUser.vue')
    },
    

    {
      path: "/vehicles",
      name: "VehicleList",
      component: () => import("./views/VehicleList")
    },
    {
      path: "/vehicles/:id",
      name: "VehicleForm",
      component: () => import("./views/VehicleForm")
    },
    {
      path: "/users",
      name: "StaffList",
      component: () => import("./views/StaffList")
    },
    {
      path: "/users/:id",
      name: "StaffForm",
      component: () => import("./views/StaffForm")
    },
    {
      path: "/publications",
      name: "PublicationList",
      component: () => import("./views/PublicationList")
    },
    {
      path: "/publications/:id",
      name: "PublicationForm",
      component: () => import("./views/PublicationForm")
    },
    {
      path: "/publications",
      name: "PublicationList",
      component: () => import("./views/PublicationList")
    },
    {
      path: "/publications/:id",
      name: "PublicationForm",
      component: () => import("./views/PublicationForm")
    },
    {
      path: "/emails",
      name: "EmailList",
      component: () => import("./views/EmailList")
    },
    {
      path: "/emails/:id",
      name: "EmailForm",
      component: () => import("./views/EmailForm")
    },
    {
      path: "/publications_visualization/:id",
      name: "PublicationVisualization",
      component: () => import ("./views/PublicationVisualization")
    },
    {
      path: "/spider-calendar",
      name: "SpiderCalendar",
      component: () => import ("./views/SpiderCalendar")
    },
    {
      path: "/plan/:id",
      name: "PlanEdit",
      component: () => import ("./views/PlanEdit")
    },
    {
      path: "/plan-details/:id",
      name: "PlanDetails",
      component: () => import ("./views/PlanDetails")
    },
    {
      path: '/spider-correction',
      name: 'SpiderCorrection',
      component: () => import ('./views/Calendar/SpiderCorrection')
    },
    {
      path: "/correction-details/:id",
      name: "CorrectionDetails",
      component: () => import ("./views/Calendar/CorrectionDetails")
    },
    {
      path: "/correction-edit/:id",
      name: "CorrectionEdit",
      component: () => import ("./views/Calendar/CorrectionEdit")
    },
    {
      path: "/reproved-publications",
      name: "ReprovedPublications",
      component: () => import ("./views/ReprovedPublications")
    },
    {
      path: "/reproved-publication/:id",
      name: "ReprovedPublicationsDetails",
      component: () => import ("./views/ReprovedPublicationsDetails") 
    }
  ]
});


router.beforeEach((to, from, next) => {
   const publicPages = ['/login', '/register', '/home'];
   const authRequired = !publicPages.includes(to.path);
   const loggedIn = localStorage.getItem('user');

   // trying to access a restricted page + not logged in
   // redirect to login page
   
   if (authRequired && !loggedIn) {
     next('/login');
   } else {
     next();
   }
 });
