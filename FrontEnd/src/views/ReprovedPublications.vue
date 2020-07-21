<template>
    <div>
        <ResubmissionConfirmation
                    v-if="showResubmissionConfirmation"
                    @receiveRessubmitionConfirmation = 'resubmitPublication($event)'
                >
        </ResubmissionConfirmation>
        <br>
        <h3> Artigos Reprovados </h3><br>
         <div class="container">
            <table class="table">
                <thead>
                    <tr>
                        <th>Título</th>
                        <th>Aluno</th>
                        <th>Veículo Reprovado</th>
                        <th>Veículo Resubmetido</th>
                        <th v-if="showModeratorContent()">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item, index) in reprovedPublications" :key="index">
                        <td>{{item.title}}</td>
                        <td>{{item.user.name}}</td>
                        <td>{{item.vehicle.name}} {{item.year}}</td>
                        <td>{{item.vehicleResubmitted}}</td>
                        <td><button class="btn btn-primary" @click="seeReprovedPublicationDetails(item.id)">Detalhes</button></td>
                        <td><button v-show='item.resubmitted!=1 && showModeratorContent()' class="btn btn-success" @click="showResubmissionConfirmation=true" @click.prevent='idResubmission = item.id'>Resubmeter</button></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import ObjectDataService from '../services/ObjectDataService'
import ResubmissionConfirmation from './ResubmissionConfirmation'
export default {
    components: {
        ResubmissionConfirmation
    },
    data() {
        return {
            reprovedPublications: '',
            showResubmissionConfirmation: false,
            idResubmission: ''
        }
    },
    methods: {
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        isReproved(planning) {
            return planning.status == 'Reprovado'
        },
        refreshReprovedPublications() {
            ObjectDataService.getAll('planning')
                .then(response => {
                    this.reprovedPublications = response.data.filter(this.isReproved)
                })
        },
        resubmitPublication(arg) {
            if (arg) {
                this.showResubmissionConfirmation = false
                localStorage.setItem('idResubmission', this.idResubmission)
                setTimeout(() => {
                    this.$router.push('spider-calendar')
                }, 500)
            } else {
                this.showResubmissionConfirmation = false
            }
        },
        seeReprovedPublicationDetails(arg) {
            this.$router.push(`/reproved-publication/${arg}`);
        }
    },
    created() {
        this.refreshReprovedPublications()
    }
}
</script>

<style scoped>

</style>