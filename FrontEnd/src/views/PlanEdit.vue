<template>
  <div>
    <br>
    <div v-show="success" class="alert alert-success" >{{success}}</div>
    <div v-show="error" class="alert alert-warning">{{error}}</div>
    <DeleteConfirmation
            :object="component" 
            v-if="showDeleteConfirmation && visualization == 'Calendário' "
            @shownDeleteConfirmation = 'deletePlan($event)'
    >
    </DeleteConfirmation>
    <h3>Plano</h3><br>
      <div> 
        <fieldset class="form-group">
          <label style="margin-right: 10px;">Veículo:</label>
          <select v-model="vehicle">
            <option v-for="(item, index) in vehicles" :key="index">
              {{item.name}}
            </option>
          </select>
        </fieldset>
        <fieldset class="form-group">
          <label>Ano</label>
          <input type="text" class="form-control" v-model="year">
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;">Aluno: </label>
          <select v-model="user">
            <option v-for="(item, index) in users" :key="index">
              {{item.name}}
            </option>
          </select>
        </fieldset>
         <fieldset class="form-group">
          <label>Título do Artigo</label>
          <input type="text" class="form-control" v-model="title">
        </fieldset>
        <fieldset class="form-group">
          <label>Estrutura do artigo</label>
          <textarea class="form-control" v-model="structure"></textarea>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;">Data de revisão:</label>
          <input type="date" v-model="revisionDate">
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;">Data de submissão:</label>
          <input type="date" v-model="submissionDate">
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;">Status: </label>
          <select v-model="status">
            <option v-for="(item, index) in allStatus" :key="index">
              {{item}}
            </option>
          </select>
        </fieldset>  
       <fieldset v-if="(status == 'Reprovado' || lastStatus=='Reprovado') && lastStatus != 'Aprovado'" class="form-group">
          <label>Justificativa da Reprovação:</label>
          <textarea class="form-control" v-model="justification"></textarea>
        </fieldset>
      </div>
      <br>
      <button v-if="lastStatus!='Aprovado' && lastStatus!='Reprovado' && showModeratorContent()" class="btn btn-success" style="margin-right: 5px;" @click="validateAndSubmit()">Salvar</button>
      <button class="btn btn-danger" style="margin-right: 5px;" @click="backToMain">Cancelar</button>
      <button class="btn btn-warning" v-show="visualization == 'Calendário' && showModeratorContent()" @click="showDeleteConfirmation = true">Apagar</button>
      <br><br><br>
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
            allStatus: ['Em Confecção', 'Em Revisão', 'Submetido', 'Aprovado', 'Reprovado'],
            showDeleteConfirmation: false,
            component: 'plano',
            vehicles: [],
            users: [],
            vehicle: '',
            vehicleId: '',
            user: '',
            userId: '',
            title: '',
            structure: '',
            submissionDate: '',
            revisionDate: '',
            status: '',
            success: '',
            error: '',
            lastStatus: '',
            lastRevisionDate: '',
            lastSubmissionDate: '',
            userData: '',
            year: '',
            justification: ''
        }
    },
    computed: {
        visualization() {
            return localStorage.getItem("spider-calendar-visualization")
        },
        id () {
          return this.$route.params.id
        }
    },
    methods: {
        isNotAdmin (user) {
          return user.username != "admin"
        },
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        backToMain() {
            this.$router.push('/spider-calendar')
        },
        deletePlan(permision) {
            if (permision) {
                ObjectDataService.delete('planning', this.id)
                  .then(() => {
                    this.showDeleteConfirmation = false
                    this.success = "Plano excluído com sucesso!"
                    setTimeout(() => {
                      this.success = ""
                      this.backToMain()
                    }, 2000)
                  })
            } else {
                this.showDeleteConfirmation = false
            }
        },
        refreshPlanDetails() {
            ObjectDataService.get('planning', this.id)
              .then (response => {
                  this.title = response.data.title
                  this.structure = response.data.structure
                  this.revisionDate = response.data.revisionDate
                  this.submissionDate = response.data.submissionDate
                  this.vehicle = response.data.vehicle.name
                  this.vehicleId = response.data.vehicle.vehicle_id
                  this.userData = response.data.user
                  this.user = response.data.user.name
                  this.userId = response.data.user.id
                  this.status = response.data.status
                  this.lastStatus = response.data.status
                  this.lastRevisionDate = response.data.revisionDate
                  this.lastSubmissionDate = response.data.submissionDate
                  this.year = response.data.year
                  this.justification = response.data.justification
              })
        },
        validateAndSubmit() {
          if (!this.vehicle || !this.user || !this.title || !this.structure || !this.revisionDate || !this.submissionDate || !this.status || !this.year || (this.status == 'Reprovado' && !this.justification)) {
            this.error = "Plano não atualizado. Todos os campos devem ser preenchidos!"
            return
          }

          for (let vehicle of this.vehicles) {
              if (vehicle.name == this.vehicle) {
                this.vehicleId = vehicle.vehicle_id
              }
          }

          for (let user of this.users) {
              if (user.name == this.user) {
                this.userId = user.id
              }
          }

          if (this.lastRevisionDate != this.revisionDate || this.lastSubmissionDate != this.submissionDate) {
            ObjectDataService.sendPlanningEmail(this.userData, this.vehicle, this.revisionDate)
                .then(() => {
                })
          }
          
          ObjectDataService.updatePlan('planning', this.id, this.userId, this.vehicleId, {
            title: this.title,
            structure: this.structure,
            submissionDate: this.submissionDate,
            revisionDate: this.revisionDate,
            status: this.status,
            year: this.year,
            justification: this.justification
          }).then (() => {
              this.error = ''
              this.success = "Plano atualizado com sucesso!"
              setTimeout(() => {
                this.success= ''
                this.backToMain()
              }, 2000)
          })
        
        },
        refreshVehiclesDetails() {
            ObjectDataService.getAll('vehicles')
              .then (response => {
                  this.vehicles = response.data
              })
        },
        refreshUsersDetails() {
            ObjectDataService.getAll('auth/users')
              .then (response => {
                this.users = response.data.filter(this.isNotAdmin)
              })
        }
    },
    created () {
      this.refreshPlanDetails()
      this.refreshVehiclesDetails()
      this.refreshUsersDetails()
    }
}
</script>

<style scoped>

</style>