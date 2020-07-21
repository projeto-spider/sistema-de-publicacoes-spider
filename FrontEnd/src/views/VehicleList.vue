<template>
    <div class="container">
         <div v-show="success" class="alert alert-success" >{{success}}</div>
         <br>
                <DeleteConfirmation 
                    :object='component' 
                    v-if="showDeleteConfirmation"
                    @shownDeleteConfirmation = 'deleteVehicle($event, idDeleteObject)'
                >
                </DeleteConfirmation>
                <div>
                    <h3>Todos os veículos</h3>
                    <div v-if="message" class="alert alert-success">{{this.message}}</div>
                    <div class="container">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Tipo do veículo</th>
                                    <th>Nome ou sigla</th>
                                    <th>Qualis</th>
                                    <th>Linha de pesquisa</th>
                                    <th>URL</th>
                                    <th v-if="showModeratorBoard">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="vehicle in vehicles" v-bind:key="vehicle.id">
                                    <td>{{vehicle.type}}</td>
                                    <td>{{vehicle.name}}</td>
                                    <td>{{vehicle.qualis}}</td>
                                    <td>{{vehicle.line}}</td>
                                    <td><a :href="vehicle.url">{{vehicle.url}}</a></td>
                                    <td v-if="showModeratorBoard"><button class="btn btn-success" v-on:click="updateVehicle(vehicle.vehicle_id)">Editar</button></td>
                                    <td v-if="showModeratorBoard"><button class="btn btn-warning" v-on:click="showDeleteConfirmation=true" v-on:click.prevent="idDeleteObject = vehicle.vehicle_id">Apagar</button></td>
                                    
                                </tr>
                            </tbody>
                        </table>
                        
                        <div class="row" v-if="showModeratorBoard">
                            <button class="btn btn-success" v-on:click="addVehicle()">Adicionar</button>
                        </div>
                    </div>
                </div>
            </div>
</template>
<script>
import ObjectDataService from '../services/ObjectDataService';
import DeleteConfirmation from './DeleteConfirmation.vue'

export default {
    name: "VehicleList",
    components: {
        DeleteConfirmation
    },
    
    data() {
        return {
            deleteObject: '',
            vehicles: [],
            message: "",
            object:"vehicles",
            idDeleteObject: '',
            component: 'veículo',
            showDeleteConfirmation: false,
            success: ''
            //currentVehicle: ''
        }
    },
    computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showAdminBoard() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_ADMIN');
      }

      return false;
    },
    showModeratorBoard() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_MODERATOR');
      }

      return false;
    },
  },
    methods: {
        refreshVehicles() {
            ObjectDataService.getAll(this.object)
                .then(response => {
                this.vehicles = response.data;
                console.log(response.data);
                })
                /*.catch(e => {
                console.log(e);
                })*/;
        },
        addVehicle() {
            this.$router.push(`/${this.object}/-1`);
        },
        updateVehicle(id) {
            this.$router.push(`/${this.object}/${id}`);
        },
        deleteVehicle(permission, id) {
                if (permission) {
                    ObjectDataService.delete(this.object, id)
                        .then(() => {
                        this.refreshVehicles();
                        this.showDeleteConfirmation = false
                        this.success = 'Veículo excluído'
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
        this.refreshVehicles();
    }
}
</script>
