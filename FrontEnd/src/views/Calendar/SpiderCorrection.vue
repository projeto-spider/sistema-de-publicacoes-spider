<template>
    <div>
        {{updateVisualization}}
        <div v-if="showModeratorContent()" class="card" style="margin-top: 20px;">
            <h4 class="card-header">Cadastrar Nova Correção:</h4>
            <br>
            <label style="margin-left: 20px; margin-top: 25px;">Selecione um plano:
                <select v-model="selectedPlan">
                    <option v-for="(item, index) in approvedPlans" :key="index" :value="item">
                        {{item.user.name}} - {{item.vehicle.name}} {{item.year}}
                    </option>
                </select>
                <br>
            </label>
            <br>
            <p style="margin-left: 20px">Título do artigo:
                {{selectedPlan.title}}
            </p>
            <br>
            <p style="margin-left: 20px">Correções:
            </p>
            <textarea v-model="corrections" style="margin-left: 20px; margin-right:20px; margin-bottom: 20px;"> 
            </textarea>
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
                    <option v-for="(item, index) in allStatus" :key="index">
                        {{item}}
                    </option>
                </select>
            </p>
             <p style="text-align: center;">
                <a class="btn btn-success" @click="addCorrection()">Adicionar Correção</a><br>
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
        <CorrectionCalendarVisualization
            v-show = " visualization == 'Calendário' "
            ref = "CorrectionCalendarVisualization"
        >
        </CorrectionCalendarVisualization>
        <CorrectionListVisualization
            v-show = " visualization == 'Lista' "
            ref = "CorrectionListVisualization"
            @deletedCorrection = '$refs.CorrectionCalendarVisualization.refreshCorrections()'
        >
        </CorrectionListVisualization>
        <br>
    </div>
</template>


<script>
import ObjectDataService from '../../services/ObjectDataService'
import CorrectionCalendarVisualization from './CorrectionCalendarVisualization'
import CorrectionListVisualization from './CorrectionListVisualization'

export default {
    components: {
        CorrectionCalendarVisualization,
        CorrectionListVisualization
    },
    data () {
        return {
            allStatus: ['Em correção', 'Em revisão', 'Submetido', 'Publicado'],
            visualization: 'Calendário',
            selectedPlan: '',
            idSelectedPlan: '',
            revisionDate: '',
            submissionDate: '',
            status: '',
            success: '',
            error: '',
            approvedPlans: '',
            object: 'approved',
            corrections: ''
        }
    },
    created() {
        this.refreshApprovedPlans()
        this.visualization = 'Calendário'
    },
    computed: {
        updateVisualization() {
            return localStorage.setItem('spider-calendar-visualization', this.visualization)
        }
    },
    methods: {
        isApprovedAndNotRegistered(plan) {
            return plan.status == 'Aprovado' && plan.isRegistered != 1
        },
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        addCorrection() {
            if (!this.selectedPlan || !this.revisionDate || !this.submissionDate || !this.status) {
                this.error = "Correção não cadastrada. Todos os campos devem ser preenchidos!"
                return
            }

            ObjectDataService.sendEmail(this.selectedPlan.user, this.selectedPlan.vehicle.name, this.revisionDate)
            .then(() => {
            })
              
            ObjectDataService.updateApprovedPlanRegistered('planning', this.selectedPlan.id, 1)
            .then(() => {
                
            })
            

            ObjectDataService.createCalendar(this.object, this.selectedPlan.id, {
                newRevisionDate: this.revisionDate,
                newSubmissionDate: this.submissionDate,
                newStatus: this.status,
                corrections: this.corrections
            })
            .then(() => {
                this.error = ''
                //this.$refs.SpiderListVisualization.refreshPlansDetails()
                //this.$refs.SpiderCalendarVisualization.refreshPlans()
                this.$refs.CorrectionListVisualization.refreshCorrections()
                this.$refs.CorrectionCalendarVisualization.refreshCorrections()
                this.success = 'Correção Cadastrada com Sucesso!'
                setTimeout(() => {
                    this.selectedPlan = ''
                    this.revisionDate = ''
                    this.submissionDate = ''
                    this.status = ''
                    this.success = ''
                    this.corrections = ''
                    this.refreshApprovedPlans()
                }, 2000)
            })

            
            
        },
        refreshApprovedPlans() {
            ObjectDataService.getAll('planning')
                .then(response => {
                    this.approvedPlans = response.data.filter(this.isApprovedAndNotRegistered)
                })
        }
    }
}
</script>


<style scoped>

</style>