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
    <h3>Correção</h3><br>
      <div> 
        <fieldset class="form-group">
          <label style="margin-right: 10px;">Plano:</label>
          {{plan}}
        </fieldset>
        <fieldset class="form-group">
          <span>
          <label style="margin-right: 10px;">Título: </label>
            {{title}}
          </span>
        </fieldset>
        <fieldset class="form-group">
          <label>Correções: </label><br>
            <textarea v-model="corrections" style='width: 100%;'> 
            </textarea>
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
      </div>
      <br>
      <button v-if="showModeratorContent() && lastStatus!='Submetido' && lastStatus!='Publicado'" class="btn btn-success" style="margin-right: 5px;" @click="validateAndSubmit()">Salvar</button>
      <button class="btn btn-danger" style="margin-right: 5px;" @click="backToMain">Cancelar</button>
      <button v-if="showModeratorContent()" class="btn btn-warning" v-show="visualization == 'Calendário'" @click="showDeleteConfirmation = true">Apagar</button>
      <button v-show="showModeratorContent() && status=='Publicado' && isRegistered != 1 && visualization == 'Calendário'" class="btn btn-info" @click="registerPublication()" style="margin-left: 5px;">Registrar</button>
      <br><br><br>
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
            approvedPlans: '',
            component: 'correção',
            status: '',
            submissionDate: '',
            revisionDate: '',
            plan: '', 
            title: '',
            allStatus: ['Em correção', 'Em revisão', 'Submetido', 'Publicado'],
            showTitle: true,
            error: '',
            success: '',
            lastStatus: '',
            lastSubmissionDate: '',
            lastRevisionDate: '',
            user: '',
            vehicleName: '',
            corrections: '',
            isRegistered: ''
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
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        isNotAdmin (user) {
          return user.username != "admin"
        },
        backToMain() {
            this.$router.push('/spider-correction')
        },
        registerPublication() {
            localStorage.setItem('idApprovedPublication', this.id)
            this.$router.push(`/publications/-1`)
        },
        deletePlan(permision) {
            if (permision) {
                ObjectDataService.delete('approved', this.id)
                  .then(() => {
                    this.showDeleteConfirmation = false
                    this.success = "Correção excluída com sucesso!"
                    setTimeout(() => {
                      this.success = ""
                      this.backToMain()
                    }, 2000)
                  })
            } else {
                this.showDeleteConfirmation = false
            }
        },
        refreshCorrectionDetails() {
            ObjectDataService.get('approved', this.id)
              .then (response => {
                  this.user = response.data.planning.user
                  this.vehicleName = response.data.planning.vehicle.name
                  this.plan = response.data.planning.user.name + ' - ' + response.data.planning.vehicle.name + ' ' + response.data.planning.year
                  this.title = response.data.planning.title
                  this.revisionDate = response.data.newRevisionDate
                  this.submissionDate = response.data.newSubmissionDate
                  this.lastStatus = response.data.newStatus
                  this.status = response.data.newStatus 
                  this.lastSubmissionDate = response.data.newSubmissionDate
                  this.lastRevisionDate = response.data.newRevisionDate
                  this.corrections = response.data.corrections
                  this.isRegistered = response.data.isRegistered
            })
        },
        refreshApprovedPlans() {
            this.approvedPlans = []
            ObjectDataService.getAll('approved')
                .then(response => {
                    this.approvedPlans = response.data
                    //for (let item of response.data) {
                    //    this.approvedPlans.push(item.planning.user.name + ' - ' + item.planning.vehicle.name)
                    //}
                })
        },
        validateAndSubmit() {
          if (!this.revisionDate || !this.submissionDate || !this.status) {
            this.error = "Correção não atualizada. Todos os campos devem ser preenchidos!"
            return
          }

          if (this.submissionDate != this.lastSubmissionDate || this.revisionDate != this.lastRevisionDate) {
            ObjectDataService.sendEmail(this.user, this.vehicleName, this.revisionDate)
                .then(() => {
                })
          }
          
          ObjectDataService.update('approved', this.id, {
            newStatus: this.status,
            newSubmissionDate: this.submissionDate,
            newRevisionDate: this.revisionDate,
            corrections: this.corrections
          }).then (() => {
              this.error = ''
              this.success = "Correção atualizada com sucesso!"
              setTimeout(() => {
                this.success= ''
                this.backToMain()
              }, 2000)
          })
        
        }
    },
    created () {
      this.refreshCorrectionDetails()
      this.refreshApprovedPlans()
    }
}
</script>

<style scoped>

</style>