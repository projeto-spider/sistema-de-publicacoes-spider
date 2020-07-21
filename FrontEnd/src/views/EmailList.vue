<template>
    <div>
        <div v-show="success" class="alert alert-success" >{{success}}</div>
        <br>
        <DeleteConfirmation 
            :object='component' 
            v-if="showDeleteConfirmation"
            @shownDeleteConfirmation = 'deleteEmail($event, idDeleteEmail)'
        >
        </DeleteConfirmation>
        <h3>Email</h3>
        <div class="container">
            <table class="table">
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>Host</th>
                        <th>Port</th> 
                    </tr>
                </thead>
                <tbody>
                <tr v-for="email in emails" v-bind:key="email.id">
                    <td>{{email.email}} </td>
                    <td>{{email.host}} </td>
                    <td>{{email.port}} </td>
                    <td><button v-if="showModeratorContent()" class="btn btn-success" v-on:click="updateEmail(email.id)">Editar</button></td>
                    <td><button v-if="showModeratorContent()" class="btn btn-warning" v-on:click="idDeleteEmail = email.id" v-on:click.prevent="showDeleteConfirmation = true">Apagar</button></td>
                
                </tr>
            </tbody>
            </table>
            <button v-if="showModeratorContent()" class="btn btn-success"  @click="addEmail">Adicionar</button>
        </div>
    </div>
</template>
<script>
import ObjectDataService from "../services/ObjectDataService"
import DeleteConfirmation from './DeleteConfirmation.vue'
export default {
    name: "EmailList",
    components: {
        DeleteConfirmation
    },
    data() {
        return {
            emails:[],
            object:'email',
            success:'',
            messages:["Usuário Cadastrado com Sucesso!", "Usuário Editado com Sucesso"],
            showDeleteConfirmation: false,
            idDeleteEmail: '',
        }
    },
    methods:{
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        refreshEmails(){
            ObjectDataService.getAll(this.object)
                .then(response => {
                    this.emails = response.data;
                    console.log(response.data)
                })},
        addEmail(){
            if(this.emails.length >=1)return
            this.$router.push('/emails/-1')
             
        },
        updateEmail(id){
            this.$router.push(`/emails/${id}`);
        },
        deleteEmail(permision, id){
            if (permision) {
                ObjectDataService.delete(this.object, id)
                    .then(() => {
                        this.refreshEmails();
                        this.showDeleteConfirmation = false
                        this.success = 'Email excluído'
                        setTimeout(() => {
                            this.success = ''
                        }, 2000)
                    })
            } else {
                this.showDeleteConfirmation = false
            }
        
        },
    },
    created() {
        this.refreshEmails()
    },
    
}
</script>
<style>
    
</style>