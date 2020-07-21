<template>
    <div>
        <br>
        <h3>Artigo Reprovado</h3>
        <br>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Título da Publicação:</b></label>
          <div class="card">{{reprovedPublication.title}}</div>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Aluno:</b></label>
          <div class="card">{{reprovedPublication.user.name}}</div>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Veículo Reprovado:</b></label>
          <div class="card">{{reprovedPublication.vehicle.name}} {{reprovedPublication.year}}</div>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Justificativa:</b></label>
          <div class="card">{{reprovedPublication.justification}}</div>
        </fieldset>
         <fieldset class="form-group">
          <label style="margin-right: 10px;"><b>Veículo Resubmetido:</b></label>
          <div class="card" v-if = "reprovedPublication.vehicleResubmitted">{{reprovedPublication.vehicleResubmitted}}</div>
          <div class="card" v-else style="color: darkRed;">Este Artigo ainda não foi Resubmetido</div>
        </fieldset><br>
        <button @click="backToReprovedVehicles()" class="btn btn-danger">Voltar</button>
    </div>
</template>

<script>
import ObjectDataService from '../services/ObjectDataService'
export default {
    data() {
        return {
            id: this.$route.params.id,
            reprovedPublication: []
        }
    },
    methods: {
        refreshReprovedPublicationsDetails() {
            ObjectDataService.get('planning', this.id)
                .then(response =>
                    this.reprovedPublication = response.data
                )
        },
        backToReprovedVehicles() {
            this.$router.push(`/reproved-publications`);
        }
    },
    created () {
        this.refreshReprovedPublicationsDetails()
    }

}
</script>

<style scoped>

</style>>
