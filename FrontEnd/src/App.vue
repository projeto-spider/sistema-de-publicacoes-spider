<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <a class="navbar-brand" style="color: #F5F5F5;">SPIDER</a>
      <div v-if="currentUser">
      <div class="navbar-nav mr-auto">
<!--
        <li class="nav-item">
          <router-link to="/home" class="nav-link">
            <font-awesome-icon icon="home" />Home
          </router-link>
        </li>

        <li v-if="showAdminBoard" class="nav-item">
          <router-link to="/users" class="nav-link">Usuários</router-link>
        </li>

        <li v-if="showModeratorBoard" class="nav-item">
          <router-link to="/vehicles" class="nav-link">Veículos</router-link>
        </li>
-->
          <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Administrativo
          </a>
          <div class="dropdown-menu bg-light" aria-labelledby="navbarDropdown">
            <a class="dropdown-item" href="#">
              <router-link to="/users" class="nav-link nav-item" style="color: black;">Equipe</router-link>
            </a>
            <a class="dropdown-item" href="#">
              <router-link to="/emails" class="nav-link nav-item" style="color: black;">Email</router-link>
            </a>
            <a class="dropdown-item" href="#">
                <router-link to="/vehicles" class="nav-link nav-item" style="color: black;">Veículos</router-link>
            </a>
          </div>
        </li>
        <li class="nav-item">
          <router-link to="/publications" class="nav-link">Publicações</router-link>
        </li>
        <li class="nav-item">
          <router-link to="/calendar" class="nav-link">Calendário - Eventos</router-link>
        </li>

        <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Calendário - SPIDER
        </a>
        <div class="dropdown-menu bg-light" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="#">
            <router-link to="/spider-calendar" class="nav-link nav-item" style="color: black;">Calendário SPIDER - Submissão</router-link>
          </a>
          <a class="dropdown-item" href="#">
             <router-link to="/spider-correction" class="nav-link" style="color: black;">Calendário SPIDER - Correção</router-link>
          </a>
        </div>
      </li>

      <li class="nav-item">
          <router-link to="/reproved-publications" class="nav-link">Artigos Reprovados</router-link>
      </li>
      </div>
      </div>

      <div v-if="!currentUser" class="navbar-nav ml-auto">
        <!-- <li class="nav-item">
          <router-link to="/register" class="nav-link">
            <font-awesome-icon icon="user-plus" />Sign Up
          </router-link>
        </li> -->
        <li class="nav-item">
          <router-link to="/login" class="nav-link">
            <font-awesome-icon icon="sign-in-alt" />Entrar
          </router-link>
        </li>
      </div>

     
      
      <div v-if="currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <a class="nav-link" href="https://github.com/projeto-spider/sistema-de-publicacoes-spider/raw/master/Manual%20do%20Usuario%20-%20SISTEMA%20DE%20PUBLICACOES%20SPIDER.doc">
            <font-awesome-icon icon="question-circle" size="1g" /> Tutorial da Ferramenta
          </a>
        </li>
        <li class="nav-item">
          <router-link to="/profile" class="nav-link">
            <font-awesome-icon icon="user" />
            {{ currentUser.username }}
          </router-link>
        </li>
        <li class="nav-item">
          <a class="nav-link" href @click.prevent="logOut">
            <font-awesome-icon icon="sign-out-alt" />Sair
          </a>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showAdminContent() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_ADMIN');
      }

      return false;
    },
    showModeratorContent() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_MODERATOR');
      }

      return false;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    }
  }
};
</script>
