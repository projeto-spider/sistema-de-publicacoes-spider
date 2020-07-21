<template>
  <div>
    <h3>Veículo</h3><br>
    <div class="container">
      <form @submit="validateAndSubmit">
        <div v-show="errors.length">
          <div class="alert alert-warning" v-bind:key="index" v-for="(error, index) in errors">{{error}}</div>
        </div>
        <div v-show="success" class="alert alert-success" >{{success}}</div>
        
        <fieldset class="form-group">
          <label>Tipo</label><br>
          <input type="radio" name="type" @click="type='Conferência'" v-if="type=='Conferência'" checked>
          <input type="radio" name="type" @click="type='Conferência'" v-else> 
          <label>Conferência</label><br>
          <input type="radio" name="type" @click="type='Periódico'" v-if="type=='Periódico'" checked>
          <input type="radio" name="type" @click="type='Periódico'" v-else>  
          <label>Periódico</label>
        </fieldset>
        <fieldset class="form-group" v-if="type=='Periódico'">
          <label>URL</label>
          <input type="url" class="form-control" v-model="url">
        </fieldset>
        <fieldset class="form-group">
          <label>Nome</label>
          <input type="text" class="form-control" v-model="name">
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 9px;">Qualis:</label>
          <select v-model="qualis">
            <option v-for="(item, index) in qualisList" :key='index'>{{item}}</option>
          </select>
        </fieldset>
        <fieldset class="form-group">
          <label style="margin-right: 15px;">Linha:</label>
          <select v-model="line">
            <option v-for="(item, index) in lineList" :key='index'>{{item}}</option>
          </select>
        </fieldset>
        
        <br>
        
        <div class="form-group">
          <button class="btn btn-danger" @click="backToMain">Cancelar</button>
        </div>
        <div class="form-group">
          <button class="btn btn-success" type="submit">Salvar</button>
        </div>
        
      </form>
    </div>
  </div>
</template>
<script>
import ObjectDataService from "../services/ObjectDataService";

export default {
  name: "VehicleForm",
  
  data() {
    return {
      type: "",
      name: "",
      qualis: "",
      qualisList: ["A1", "A2", "B1", "B2", "B3", "B4", "B5"],
      line: "",
      lineList: ["Qualidade de Software", "Processo de Software", "Informática Educativa", "Gamificação", "Jogos"],
      object: "vehicles",
      errors: [],
      success: '',
      url: ''
    };
  },
  computed: {
    id() {
      return this.$route.params.id;
    },
  },
  methods: {
    refreshVehicleDetails() {
        ObjectDataService.get(this.object, this.id).then(res => {
          this.type = res.data.type;
          this.name = res.data.name;
          this.qualis = res.data.qualis;
          this.line = res.data.line;
          this.url = res.data.url;
        });
    },
    backToMain(){
      this.$router.push({ name: "VehicleList" })
    },
    checkValidURL (string) {
            try {
                new URL(string);
            } catch (_) {
                return false;  
            }
            return true;
    },
    validateAndSubmit(e) {
        e.preventDefault();
        this.errors = [];
        if(!this.type || !this.name || !this.qualis || !this.line || (this.type == "Periódico" && !this.url)) {
            this.errors.push("Todos os campos devem ser preenchidos");
            return
        } else if (this.type == "Conferência") {
          this.url = ''
        } else if (!this.checkValidURL(this.url)) {
          this.errors.push("A URL é inválida");
          return
        }
        if(this.errors.length === 0) {
            if (this.id === '-1') {
                ObjectDataService.create(this.object,{
                  type: this.type,
                  name: this.name,
                  qualis: this.qualis,
                  line: this.line,
                  url: this.url
                })
                .then(() => {
                      this.errors = ''
                      this.success = "Veículo Cadastrado com Sucesso!"
                      let vm = this
                      setTimeout(function() {vm.backToMain()}, 2000)
                });
                
            } else {
                ObjectDataService.update(this.object, this.id, {
                    id: this.id,
                    type: this.type,
                    name: this.name,
                    qualis: this.qualis,
                    line: this.line,
                    url: this.url
                })
                .then(() => {
                      this.errors = ''
                      this.success = "Veículo Editado com Sucesso!"
                      let vm = this
                      setTimeout(function() {vm.backToMain()}, 2000)
                });
            }
        }
    }
  },
  created() {
    this.refreshVehicleDetails();
  }
};
</script>