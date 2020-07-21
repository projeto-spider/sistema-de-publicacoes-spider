<template>
  <div>
       <div v-show="success" class="alert alert-success" >{{success}}</div>
       <DeleteConfirmation
            :object="component" 
            v-if="showDeleteConfirmation"
            @shownDeleteConfirmation = 'deletePlan($event)'
       >
       </DeleteConfirmation>
       <div class="container">
            <table class="table">
                <thead>
                    <tr>
                        <th>Plano</th>
                        <th>Data de revisão</th>
                        <th>Data de submissão</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                    <tbody>
                        <tr v-for="(item, index) in corrections" :key="index">
                            <td>{{item.planning.user.name}} - {{item.planning.vehicle.name}} {{item.planning.year}}</td>
                            <td>{{item.newRevisionDate.slice(8, 10) + '/' + item.newRevisionDate.slice(5, 7) + '/' + item.newRevisionDate.slice(0,4)}}</td>
                            <td>{{item.newSubmissionDate.slice(8, 10) + '/' + item.newSubmissionDate.slice(5, 7) + '/' + item.newSubmissionDate.slice(0,4)}}</td>
                            <td>{{item.newStatus}}</td>
                            <td><button class="btn btn-primary" @click="viewDetails(item.id)">Detalhes</button></td>
                            <td><button v-show="showModeratorContent() && item.newStatus!='Submetido' && item.newStatus!='Publicado'" class="btn btn-success" @click="editPlan(item.id)">Editar</button></td>
                            <td><button v-show="showModeratorContent()" class="btn btn-warning" @click="showDeleteConfirmation=true" @click.prevent="correctionId = item.id">Apagar</button></td>
                            <td><button v-show="showModeratorContent() && item.newStatus=='Publicado' && item.isRegistered != 1" class="btn btn-info" @click="registerPublication(item.id)">Registrar</button></td>
                        </tr>
                    </tbody>
                </table>
        </div>
        <br><br>
  </div>
</template>

<script>

import DeleteConfirmation from '../DeleteConfirmation'
import ObjectDataService from '../../services/ObjectDataService'

export default {
    components: {
        DeleteConfirmation
    },
    data() {
        return {
            showDeleteConfirmation: false,
            component: 'correção',
            corrections: [],
            correctionId: '',
            success: ''
        }
    },
    methods: {
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        editPlan(id) {
            this.$router.push(`/correction-edit/${id}`)
        },
        viewDetails(id) {
            this.$router.push(`/correction-details/${id}`)
        },
        registerPublication(id) {
            localStorage.setItem('idApprovedPublication', id)
            this.$router.push(`/publications/-1`)
        },
        deletePlan(permision) {
            if (permision) {
                ObjectDataService.delete('approved', this.correctionId)
                    .then (() => {
                        this.showDeleteConfirmation = false
                        this.refreshCorrections()
                        this.success = "Correção Excluída com Sucesso!"
                        this.$emit('deletedCorrection')
                        setTimeout(() => {
                            this.success = ''
                        }, 2000)
                    })
            } else {
                this.showDeleteConfirmation = false
            }
        },
        refreshCorrections() {
            ObjectDataService.getAll('approved')
                .then(response => {
                    this.corrections = response.data
                })
        }
    },
    created() {
        this.refreshCorrections()
    }
}
</script>

<style scoped>

</style>