<template>
    <div class="container">
        <div v-show="error" class="alert alert-warning">{{error}}</div>
        <div v-show="success" class="alert alert-success" >{{success}}</div>
        <h3>Email</h3>
        <fieldset class="form-group">
            <label>Email</label>
            <input class="form-control" type="text" v-model="login"><br>
        </fieldset>
        <fieldset v-if="id === '-1'" class="form-group">
            <label>Senha</label>
            <input class="form-control" type="password" v-model="password"><br>
        </fieldset>
        <fieldset class="form-group">
            <label>Host</label>
            <input class="form-control" type="text" v-model="host"><br>
        </fieldset>
        <fieldset class="form-group">
            <label>Port</label>
            <input class="form-control" type="text" v-model="port"><br>
        </fieldset>
        <button class="btn btn-success" @click="backToMain">Cancelar</button>
        <button class="btn btn-danger" @click="validateAndSubmit">Salvar</button>
    </div>
</template>
<script>

import ObjectDataService from "../services/ObjectDataService"

export default {
    data() {
        return {
            id: this.$route.params.id,
            error:'',
            success:'',
            host:'',
            port:'',
            login:'',
            password:'',
            object:'email',
        }
    },
    methods:{
        refreshEmailDetails(){
            ObjectDataService.get(this.object, this.id).then(res =>{
                this.login = res.data.email;
                this.password = res.data.password;
                this.host = res.data.host;
                this.port = res.data.port; 
            })
        },
        backToMain(){
            this.$router.push({name:"EmailList"})
        },
        validateAndSubmit(){
            if(this.id === '-1'){
                ObjectDataService.create(this.object,{
                    email: this.login,
                    password: this.password,
                    host: this.host,
                    port: this.port,
                    
                }).then(()=>{
                this.error = ''
                this.success = "Email Cadastrado com Sucesso!"
                let vm = this
                setTimeout(function(){vm.backToMain()},2000)})
            } else{
            ObjectDataService.update(this.object, this.id,{
                    email: this.login,
                    password: this.password,
                    host: this.host,
                    port: this.port,
            }).then(() =>{
                this.error = ''
                this.success = "Email Editado com Sucesso!"
                let vm = this
                setTimeout(function() {vm.backToMain()}, 2000)
                    })
            }
        }
    },
    created(){
        this.refreshEmailDetails()
    }
}
</script>
<style>
</style>