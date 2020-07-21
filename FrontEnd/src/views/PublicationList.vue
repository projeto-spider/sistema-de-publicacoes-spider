<template>
<div class="container" >
    <div v-show="success" class="alert alert-success" >{{success}}</div>
    <br>
    <DeleteConfirmation 
            :object='component' 
            v-if="showDeleteConfirmation"
            @shownDeleteConfirmation = 'deletePublication($event, idDeletePublication)'
        >
    </DeleteConfirmation>
    <h3>Todas as Publicações</h3>
    <div class="container">
        <table class="table">
            <thead>
                <tr>
                    <th>Nome da Publicação</th>
                    <th>Nome do Veiculo</th>
                    <th>Tipo de Veiculo</th>
                    <th>Ano</th>
                    <th>Ação</th> 
                </tr>
            </thead>
            <tbody>
                <tr v-for="publication in publications" v-bind:key="publication.id">
                    <td>{{publication.nome}} </td>
                    <td>{{publication.nomeveiculo}} </td>
                    <td>{{publication.type}} </td>
                    <td>{{publication.year}} </td>
                    <td><button class="btn btn-primary" v-on:click="visualization(publication.id)">Detalhes</button></td>
                    <td><button v-if="showModeratorContent()" class="btn btn-success" v-on:click="updatePublication(publication.id)">Editar</button></td>
                    <td><button v-if="showModeratorContent()" class="btn btn-warning" v-on:click="idDeletePublication = publication.id" v-on:click.prevent="showDeleteConfirmation = true">Apagar</button></td>
                
                </tr>

            </tbody>
        </table>
        <div class="row"><button v-if="showModeratorContent()" class="btn btn-success" v-on:click="addPublication">Adicionar</button></div>
    </div>
    
</div>
</template>
<script>
import ObjectDataService from '../services/ObjectDataService';
import DeleteConfirmation from './DeleteConfirmation.vue'
export default {
    name:"PublicationList",
    components: {
        DeleteConfirmation
    },
    data() {
        return {
            publications:[],
            messages:["Usuário Cadastrado com Sucesso!", "Usuário Editado com Sucesso"],
            object:'publications',
            view: 'publications_visualization',
            showDeleteConfirmation: false,
            idDeletePublication: '',
            success: '',
            component: 'artigo'
        }
    },
    methods: {
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        refreshPublications(){
            ObjectDataService.getAll(this.object)
                .then(response => {
                    this.publications = response.data;
                    console.log(response.data)
                })
        },
        addPublication(){
            this.$router.push(`/${this.object}/-1`);
        },
        updatePublication(id){
            this.$router.push(`/${this.object}/${id}`);
        },
        deletePublication(permision, id){
            if (permision) {
                ObjectDataService.delete(this.object, id)
                    .then(() => {
                        this.refreshPublications();
                        this.showDeleteConfirmation = false
                        this.success = 'Publicação excluída'
                        setTimeout(() => {
                            this.success = ''
                        }, 2000)
                    })
            } else {
                this.showDeleteConfirmation = false
            }
        
        },
        visualization(id){
            this.$router.push(`/${this.view}/${id}`);
        }
    },
    created() {
        this.refreshPublications()
    },
}
</script>
<style>
    
</style>