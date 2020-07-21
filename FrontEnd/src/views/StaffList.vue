<template>
    
    <div class="container">
        <div v-show="success" class="alert alert-success" >{{success}}</div>
        <br>
        <DeleteConfirmation 
            :object='component' 
            v-if="showDeleteConfirmation"
            @shownDeleteConfirmation = 'deleteStaff($event, idDeleteStaff)'
        >
        </DeleteConfirmation>
        <h3>Equipe</h3>
        <div class="container">
            <table class="table">
                <thead>
                    <tr>
                        <th>Usuário</th>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Telefone</th>
                        <th>Condição</th>
                        <th v-if="showModeratorContent">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in users" v-bind:key="user.id">
                        <td>{{user.username}}</td>
                        <td>{{user.name}}</td>
                        <td>{{user.email}}</td>
                        <td>{{user.phone}}</td>
                        <td>{{user.status}}</td>
                        <td v-if="showModeratorContent"><button class="btn btn-success" v-on:click="updateStaff(user.id)">Editar</button></td>
                        <td v-if="showModeratorContent"><button class="btn btn-warning" v-on:click="idDeleteStaff = user.id" v-on:click.prevent="showDeleteConfirmation = true">Apagar</button></td>
                    </tr>
                </tbody>
            </table>
            <div class="row" v-if="showModeratorContent"><button class="btn btn-success" v-on:click="addStaff">Adicionar</button></div>

        </div>
    </div>
</template>

<script>
import ObjectDataService from '../services/ObjectDataService';
import DeleteConfirmation from './DeleteConfirmation.vue'

export default {
    components: {
        DeleteConfirmation
    },
    name: "StaffList",
    data () {
        return {
            users: [],
            messages: ["Usuário Cadastrado com Sucesso", "Usuário Editado com Sucesso"],
            object: 'auth/users',
            component: 'usuário',
            showDeleteConfirmation: false,
            idDeleteStaff: '',
            success: ''
        }
    },
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
        refreshStaff() {
            ObjectDataService.getAll(this.object)
                .then(response => {
                    this.users = response.data;
                    console.log(response.data)
                })
        },
        addStaff () {
            this.$router.push(`/users/-1`);
        },
        updateStaff (id) {
            this.$router.push(`/users/${id}`);
        },
        deleteStaff (permision, id) {
            if (permision) {
                ObjectDataService.delete(this.object, id)
                    .then(() => {
                    this.refreshStaff();
                    this.showDeleteConfirmation = false
                    this.success = 'Usuário excluído'
                    setTimeout(() => {
                        this.success = ''
                    }, 2000)
                }); 
            } else {
                this.showDeleteConfirmation = false
            }
        }
    }, 
    created() {
        this.refreshStaff()
    },
}
</script>

<style scoped>

</style>
