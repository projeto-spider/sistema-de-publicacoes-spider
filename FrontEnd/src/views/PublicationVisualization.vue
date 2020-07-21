<template>
    <div class="container">
        <label>Nome da publicação</label>
        <div class="card">
        <p> {{nome}} </p>
        </div>
        <br>
        <label>Nome do Veiculo</label>
        <div class='card'>
        <p> {{nomeveiculo}} </p>
        </div>
        <br>
        <label>Tipo do Veiculo</label>
        <div class='card'>
        <p>{{type}}</p>
        </div>
        <br>
        <label>Qualis do Veiculo</label>
        <div class='card'>
        <p>{{qualis}} </p>
        </div>
        <br>
        <label>Linha de pesquisa</label>
        <div class='card'>
        <p>{{line}} </p>
        </div>
        <br>
        <label>Autor</label>
        <div class='card'>
        <p>{{autor}} </p>
        </div>
        <br>
        <label>Ano</label>
        <div class='card'>
        <p> {{year}} </p>
        </div>
        <br>
        <label>Resumo</label>
        <div class='card'>
        <p> {{summary}} </p>
        </div>
        <br>
        <label>Palavra chave</label>
        <div class='card'>
        <p> {{keyword}} </p>
        </div>
        <br>
        <label>Link do Evento</label>
        <div class='card'>
        <p> {{eventlink}} </p>
        </div>
        <br>
        <button class="btn btn-danger" @click="backToMain">Voltar</button>
    </div>
</template>
<script>
import ObjectDataService from "../services/ObjectDataService";
export default {
    data() {
        return {
            id: this.$route.params.id,
            publications:[],
            object: 'publications',
            nomeveiculo:'',
            vehicles:[],
            type: '',
            line: '',
            nome: '',
            autor: '',
            year: '',
            summary: '',
            keyword:'',
            researchline:'',
            eventlink:'',
            error:'',
            success: '',
        }
    },
    methods: {
        refreshPublicationsDetails(){
            ObjectDataService.getAll(this.object)
                .then(response => {
                    this.publications = response.data;
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
        
    },
    created(){
        this.refreshPublicationsDetails()
    }
}
</script>
<style>
</style>