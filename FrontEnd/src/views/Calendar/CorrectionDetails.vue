<template>
  <div>
    <br>
      <h3>Correção</h3><br>
      <div> 
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Plano</b></label>
          <div class="card">{{plan}}</div>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Título</b></label>
          <div class="card">{{title}}</div>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Correções</b></label>
          <div class="card">{{corrections}}</div>
        </fieldset>
        <fieldset class="form-group">
          <span>
            <label style="margin-right: 5px;"><b>Data de revisão:</b></label>
            {{revisionDate.slice(8, 10) + '/' + revisionDate.slice(5, 7) + '/' + revisionDate.slice(0,4)}}
          </span>
        </fieldset>
        <fieldset class="form-group">
          <span>
            <label style="margin-right: 5px;"><b>Data de submissão:</b></label>
            {{submissionDate.slice(8, 10) + '/' + submissionDate.slice(5, 7) + '/' + submissionDate.slice(0,4)}}
          </span>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Status:</b></label>
          <div class="card">{{status}}</div>
        </fieldset>  
      </div>
      <br>
      <button class="btn btn-success" style="margin-right: 5px;" @click="backToMain()">Voltar</button>
      <br><br><br>
  </div>
</template>

<script>
import ObjectDataService from '../../services/ObjectDataService'
export default {
    data () {
      return {
          plan: '',
          title: '',
          revisionDate: '',
          submissionDate: '',
          status: '',
          corrections: ''
      }
    },
    computed: {
      id () {
        return this.$route.params.id;
      }
    },
    methods: {
        backToMain() {
            this.$router.push({name: 'SpiderCorrection'})
        },
        refreshPlanDetails() {
            ObjectDataService.get('approved', this.id)
              .then (response => {
                  this.plan = response.data.planning.user.name + ' - ' + response.data.planning.vehicle.name + ' ' + response.data.planning.year
                  this.title = response.data.planning.title
                  this.status = response.data.newStatus
                  this.revisionDate = response.data.newRevisionDate
                  this.submissionDate = response.data.newSubmissionDate
                  this.corrections = response.data.corrections
              })
        }
    },
    created() {
      this.refreshPlanDetails()
    }
}
</script>

<style scoped>

</style>