<template>
  <div>
      <h3>Plano</h3><br>
      <div> 
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Veículo:</b></label>
          <div class="card">{{vehicle}}</div>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Ano:</b></label>
          <div class="card">{{year}}</div>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Aluno:</b></label>
          <div class="card">{{user}}</div>
        </fieldset>
         <fieldset class="form-group">
          <label><b>Título do Artigo</b></label>
          <div class="card">{{title}}</div>
        </fieldset>
        <fieldset class="form-group">
          <label><b>Estrutura do artigo</b></label>
          <div class="card">{{structure}}</div>
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
        <fieldset v-show="status == 'Reprovado'" class="form-group">
          <label><b>Justificativa da Reprovação:</b></label>
          <div class="card">{{justification}}</div>
        </fieldset>
      </div>
      <br>
      <button class="btn btn-success" style="margin-right: 5px;" @click="backToMain()">Voltar</button>
      <br><br><br>
  </div>
</template>

<script>
import ObjectDataService from '../services/ObjectDataService'
export default {
    data () {
      return {
          title: '',
          structure: '',
          revisionDate: '',
          submissionDate: '',
          vehicle: '',
          user: '',
          status: '',
          justification: ''
      }
    },
    computed: {
      id () {
        return this.$route.params.id;
      }
    },
    methods: {
        backToMain() {
            this.$router.push({name: 'SpiderCalendar'})
        },
        refreshPlanDetails() {
            ObjectDataService.get('planning', this.id)
              .then (response => {
                  this.title = response.data.title
                  this.structure = response.data.structure
                  this.revisionDate = response.data.revisionDate
                  this.submissionDate = response.data.submissionDate
                  this.vehicle = response.data.vehicle.name
                  //this.vehicleId = response.data.vehicle.vehicle_id
                  this.user = response.data.user.name
                  //this.userId = response.data.user.id
                  this.status = response.data.status
                  this.year = response.data.year
                  this.justification = response.data.justification
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