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
                        <th>Veículo</th>
                        <th>Ano</th>
                        <th>Aluno</th>
                        <th>Data de revisão</th>
                        <th>Data de submissão</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                    <tbody>
                        <tr v-for="(item, index) in plans" :key="index">
                            <td>{{item.vehicle.name}}</td>
                            <td>{{item.year}}</td>
                            <td>{{item.user.name}}</td>
                            <td>{{item.revisionDate.slice(8, 10) + '/' + item.revisionDate.slice(5, 7) + '/' + item.revisionDate.slice(0,4)}}</td>
                            <td>{{item.submissionDate.slice(8, 10) + '/' + item.submissionDate.slice(5, 7) + '/' + item.submissionDate.slice(0,4)}}</td>
                            <td>{{item.status}}</td>
                            <td><button class="btn btn-primary" @click="viewDetails(item.id)">Detalhes</button></td>
                            <td><button v-if="showModeratorContent() && item.status!='Aprovado' && item.status!='Reprovado'" class="btn btn-success" @click="editPlan(item.id)">Editar</button></td>
                            <td><button v-show="showModeratorContent()" class="btn btn-warning" @click="showDeleteConfirmation=true" @click.prevent="planId = item.id">Apagar</button></td>
                        </tr>
                    </tbody>
                </table>
        </div>
        <br><br>
  </div>
</template>

<script>

import DeleteConfirmation from './DeleteConfirmation'
import ObjectDataService from '../services/ObjectDataService'

export default {
    components: {
        DeleteConfirmation
    },
    data() {
        return {
            showDeleteConfirmation: false,
            component: 'plano',
            plans: [],
            planId: '',
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
            this.$router.push(`/plan/${id}`)
        },
        viewDetails(id) {
            this.$router.push(`/plan-details/${id}`)
        },
        deletePlan(permision) {
            if (permision) {
                ObjectDataService.delete('planning', this.planId)
                    .then (() => {
                        this.refreshPlansDetails()
                        this.showDeleteConfirmation = false
                        this.success = "Plano Excluído com Sucesso!"
                        this.$emit('deletedPlan')
                        setTimeout(() => {
                            this.success = ''
                        }, 2000)
                    })
            } else {
                this.showDeleteConfirmation = false
            }
        },
        refreshPlansDetails() {
            ObjectDataService.getAll('planning')
                .then(response => {
                    this.plans = response.data
                })
        }
    },
    created() {
        this.refreshPlansDetails()
    }
}
</script>

<style scoped>

</style>