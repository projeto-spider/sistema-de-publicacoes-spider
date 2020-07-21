<template>
    <div>
        <br>
        <h3>Publicações</h3>
        <br>
        <div class="container">
                
                <fieldset class="form-group">
                    <label>Nome da Publicação</label>
                    <input type="text" class="form-control" v-model='nome'>
                </fieldset>
                <fieldset class="form-group">
                    <label style="margin-right: 1%">Veículo:</label>
                    <select v-model='nomeveiculo'>
                        <option v-for="vehicle in vehicles" v-bind:key="vehicle.id">{{vehicle.name}}</option>
                    </select><br>
                    <p v-on="vehicleFunction()" style="margin-right: 10%">Tipo: {{type}}</p>
                    <p v-on="vehicleFunction()">Qualis: {{qualis}} </p>
                    <p v-on="vehicleFunction()">Linha de pesquisa:  {{line}} </p>
                </fieldset>
                <fieldset class="form-group">
                    <label>Autor</label>
                    <input type="text" class="form-control" v-model='autor'>
                </fieldset>
                <fieldset class="form-group">
                    <label>Ano</label>
                    <input class="form-control" v-model='year'>
                </fieldset>    
                <fieldset class="form-group">    
                    <label>Resumo</label>
                    <textarea class="form-control" rows="0" v-model="summary"></textarea>
                </fieldset>
                <fieldset class="form-group">
                    <label>Palavras-Chave</label>
                    <input type="text" class="form-control" v-model='keyword'>
                </fieldset>
                <fieldset>
                    <label>Link do evento</label>
                    <input type="text" class="form-control" v-model='eventlink'>    
                </fieldset>
                <br>
                <div v-show="error" class="alert alert-warning">{{error}}</div>
                <div v-show="success" class="alert alert-success" >{{success}}</div>
                <br>
                <button class="btn btn-success" @click="validateAndSubmit" style="margin-right: 2%;">Salvar</button>
                <button class="btn btn-danger" @click="backToMain">Cancelar</button>
                <br>
                
                
                <br>
        </div>
    </div>
</template>
<script>
import ObjectDataService from '../services/ObjectDataService'
export default {
    data() {
        return {
            id: this.$route.params.id,
            nomeveiculo:'',
            vehicles:[],
            type: '',
            line: '',
            object: 'publications',
            objects: 'vehicles',
            nome: '',
            autor: '',
            year: '',
            summary: '',
            keyword:'',
            researchline:'',
            eventlink:'',
            error:'',
            success: '',
            registerApprovedMode: '',
            idApprovedPublication: ''
        }
    },
    methods:{
        refreshPublicationDetails() {
            ObjectDataService.getAll(this.objects)
                .then(response => {
                    this.vehicles = response.data;
                    console.log(response.data)
                })
            if (this.id != "-1") {
                ObjectDataService.get(this.object, this.id).then(res => {
                    this.nomeveiculo = res.data.nomeveiculo;
                    this.type = res.data.type;
                    this.qualis = res.data.qualis;
                    this.line = res.data.researchline;
                    this.nome = res.data.nome;
                    this.autor = res.data.autor;
                    this.year = res.data.year;
                    this.summary = res.data.summary;
                    this.keyword = res.data.keyword;
                    this.researchline = res.data.researchline;
                    this.eventlink = res.data.eventlink;
                });    
            }
        },
        backToMain () {
            this.$router.push({ name: "PublicationList" })
        },
        vehicleFunction(){
            for (let i = 0; i < this.vehicles.length; i ++) {
                if (this.vehicles[i].name == this.nomeveiculo) {
                    this.qualis = this.vehicles[i].qualis
                    this.type = this.vehicles[i].type
                    this.line = this.vehicles[i].line}}},
        validateAndSubmit(){
            if (!this.nome || !this.nomeveiculo || !this.autor || !this.year || !this.summary || !this.keyword || !this.eventlink ){
                this.error = 'Preencha todos os campos!'
                return
        }
            if(this.id === '-1'){
                if (this.registerApprovedMode) {
                    ObjectDataService.updateApprovedPlanRegistered('approved', this.idApprovedPublication, 1)
                        .then(() => {})
                }
            ObjectDataService.create(this.object,{
                nomeveiculo: this.nomeveiculo,
                type: this.type,
                qualis: this.qualis,
                researchline: this.line,
                nome: this.nome,
                autor: this.autor,
                year: this.year,
                summary: this.summary,
                keyword: this.keyword,
                eventlink: this.eventlink,
            }).then(() =>{
                this.error = ''
                this.success = "Publicação Cadastrada com Sucesso!"
                let vm = this
                setTimeout(function() {vm.backToMain()}, 2000)
                    })
            }
            else{
            ObjectDataService.update(this.object, this.id,{
                nomeveiculo: this.nomeveiculo,
                type: this.type,
                qualis: this.qualis,
                researchline: this.line,
                nome: this.nome,
                autor: this.autor,
                year: this.year,
                summary: this.summary,
                keyword: this.keyword,
                eventlink: this.eventlink,
            }).then(() =>{
                this.error = ''
                this.success = "Publicação Editada com Sucesso!"
                let vm = this
                setTimeout(function() {vm.backToMain()}, 2000)
                    })
            }
        },
        registerApprovedPublication() {
            if(localStorage.getItem('idApprovedPublication') != '') {
                ObjectDataService.get('approved', parseInt(localStorage.getItem('idApprovedPublication')))
                    .then(response => {
                        this.idApprovedPublication = parseInt(localStorage.getItem('idApprovedPublication'))
                        this.autor = response.data.planning.user.name
                        this.nome = response.data.planning.title
                        this.nomeveiculo = response.data.planning.vehicle.name
                        this.registerApprovedMode = true
                        localStorage.setItem('idApprovedPublication', '')
                    })
                
            }
        }
        
    },
    created(){
        this.registerApprovedPublication()
        this.refreshPublicationDetails()
    }
}
</script>

<style>
</style>