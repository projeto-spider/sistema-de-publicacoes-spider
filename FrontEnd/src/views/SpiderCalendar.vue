<template>
    <div>
        {{updateVisualization}}
        <div v-if="showModeratorContent()" class="card" style="margin-top: 20px;">
            <h4 class="card-header">Cadastrar Novo Plano:</h4>
            <br>
            <p style="margin-left: 20px; margin-top: 25px;">Selecione um veículo:
                <select v-model="vehicle">
                    <option v-for="(item, index) in vehicles" :key="index" :value="item">
                        {{item.name}}
                    </option>
                </select>
                <br>
            </p>
            <div v-if="vehicle.type=='Conferência'">
                <p style="margin-left: 20px">DeadLine de Submissão:</p>
                <p style="margin-left: 20px; margin-bottom: 0px;"><b>Ano - Data de Submissão</b></p>
                <div style="margin-left: 20px" v-for="(item, index) in vehicle.calendar" :key="index">
                     • {{item.year}} - {{item.submissionDate.slice(8,10) + '/' + item.submissionDate.slice(5,7) + '/' + item.submissionDate.slice(0,4)}}
                </div>
                <div style="margin-left: 20px" v-if="vehicle.calendar.length==0">
                    Não foram encontrados registros para essa conferência
                </div>
                <br>
            </div>
            <p style="margin-left: 20px">Ano:
            <input style="margin-right:20px;" v-model="year">
            </p>
            <br>
            <p style="margin-left: 20px">Selecione um aluno:
                <select v-model="user">
                    <option v-for="(item, index) in users" :key="index">
                        {{item.name}}
                    </option>
                </select>
            </p>
            <br><br>
            <p style="margin-left: 20px">Título do artigo:</p>
            <input style="margin-left: 20px; margin-right:20px; margin-bottom: 20px;" v-model="title">
            <br>
            <p style="margin-left: 20px">Estrutura do artigo: </p>
            <textarea style="margin-left: 20px; margin-right:20px; margin-bottom: 20px;" v-model="structure"></textarea>
            <br>
            <p style="margin-left: 20px">Data de revisão:
                <input type="date" v-model="revisionDate">
            </p>
            <br>
            <p style="margin-left: 20px">Data de submissão:
                <input type="date" v-model="submissionDate">
            </p>
            <p style="margin-left: 20px">Status:

                <select v-model="status">
                    <option v-for="(item, index) in allStatus" :key="index" :value='item'>
                        {{item}}
                    </option>
                </select>
            </p>
            <div v-show="status=='Reprovado'" >
                <p style="margin-left: 20px">Justificativa da Reprovação:</p>
                <textarea v-model="justification" style="margin-left: 20px; margin-bottom: 20px; width: 96.5%;"></textarea>
            </div>
             <p style="text-align: center;">
                <a class="btn btn-success" @click="addPlan()">Adicionar Plano</a><br>
            </p>
        </div>
        <br>
        <div v-show="success" class="alert alert-success" >{{success}}</div>
        <div v-show="error" class="alert alert-warning">{{error}}</div>
        <br>
        <p style="text-align: right;">
            <span style="font-size: 20px;">Visualizar como: </span>
            <select v-model="visualization">
                <option>Calendário</option>
                <option>Lista</option>
            </select>
        </p>
        <br>
        <SpiderCalendarVisualization
            v-show = " visualization == 'Calendário' "
            ref = "SpiderCalendarVisualization"
        >
        </SpiderCalendarVisualization>
        <SpiderListVisualization
            @deletedPlan = '$refs.SpiderCalendarVisualization.refreshPlans()'
            v-show = " visualization == 'Lista' "
            ref = 'SpiderListVisualization'
        >
        </SpiderListVisualization>
    </div>
</template>


<script>
import SpiderCalendarVisualization from './SpiderCalendarVisualization'
import SpiderListVisualization from './SpiderListVisualization'
import ObjectDataService from '../services/ObjectDataService'

export default {
    components: {
        SpiderCalendarVisualization,
        SpiderListVisualization
    }, 
    data () {
        return {
            vehicles: [],
            users: [],
            allStatus: ['Em Confecção', 'Em Revisão', 'Submetido', 'Aprovado', 'Reprovado'],
            visualization: 'Calendário',
            vehicle: '',
            vehicleId: '',
            user: '',
            userId: '',
            title: '',
            structure: '',
            revisionDate: '',
            submissionDate: '',
            status: '',
            success: '',
            error: '',
            resubmissionMode: '',
            idResubmission: '',
            year: '',
            justification: ''
        }
    },
    created() {
        this.checkRessubmtion()
        this.refreshVehiclesDetails()
        this.refreshUsersDetails()
        this.visualization = 'Calendário'
    },
    computed: {
        updateVisualization() {
            return localStorage.setItem('spider-calendar-visualization', this.visualization)
        }
    },
    methods: {
        isNotAdmin(user) {
            return user.username != 'admin'
        },
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        addPlan() {
            if (!this.vehicle || !this.user || !this.title || !this.structure || !this.revisionDate || !this.submissionDate || !this.status || ! this.year || (this.status == 'Reprovado' && !this.justification)) {
                this.error = "Plano não cadastrado. Todos os campos devem ser preenchidos!"
                return
            }

            let userData
            for (let user of this.users) {
              if (user.name == this.user) {
                this.userId = user.id
                userData = user
              }
            }

            if (this.resubmissionMode) {
                ObjectDataService.updatePlanningResubStatus('planning', this.idResubmission, 1, this.vehicle.name + ' ' + this.year)
                    .then(() => {
                        this.resubmissionMode = ''
                        this.idResubmission = ''
                    })
            }

            ObjectDataService.sendPlanningEmail(userData, this.vehicle, this.revisionDate)
                .then(() => {
                })


            ObjectDataService.createPlan('planning', this.userId, this.vehicle.vehicle_id, {
                title: this.title,
                structure: this.structure,
                submissionDate: this.submissionDate,
                revisionDate: this.revisionDate,
                status: this.status,
                year: this.year,
                justification: this.justification
            }).then(() => {
                this.error = ''
                this.$refs.SpiderListVisualization.refreshPlansDetails()
                this.$refs.SpiderCalendarVisualization.refreshPlans()
                this.success = 'Plano Cadastrado com Sucesso!'
                setTimeout(() => {
                    this.user = ''
                    this.vehicle = ''
                    this.title = ''
                    this.structure = ''
                    this.submissionDate = ''
                    this.revisionDate = ''
                    this.status = ''
                    this.success = ''
                    this.year = '',
                    this.justification = ''
                }, 2000)
            })
            
        },
        refreshVehiclesDetails() {
            ObjectDataService.getAll('vehicles')
                .then(response => {
                    this.vehicles = response.data
                })
        },
        refreshUsersDetails() {
            ObjectDataService.getAll('auth/users')
                .then(response => {
                    //this.users = response.data.filter(this.isNotAdmin)
                    this.users = response.data
                })
        },
        checkRessubmtion() {
            if (localStorage.getItem('idResubmission') != '') {
                ObjectDataService.get('planning', parseInt(localStorage.getItem('idResubmission')))
                    .then(response => {
                        this.idResubmission = parseInt(localStorage.getItem('idResubmission'))
                        this.user = response.data.user.name
                        this.title = response.data.title
                        this.structure = response.data.structure
                        this.resubmissionMode = true
                        localStorage.setItem('idResubmission', '')
                    })
            }
        }
    }
}
</script>


<style scoped>

</style>