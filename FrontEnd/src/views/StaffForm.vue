<template>
    <div>
        <h3>Usuário</h3><br>
        <div class="container">
            <div v-show="error" class="alert alert-warning" >{{error}}</div>
            <div v-show="success" class="alert alert-success" >{{success}}</div>
            <div>
                <fieldset class="form-group">
                    <label>Usuário</label><br>
                    <input class="form-control" v-model='username'>
                </fieldset>
                <fieldset v-if="id==-1" class="form-group">
                    <label>Senha</label><br>
                    <input type="password" class="form-control" v-model='password'>
                </fieldset>
                <fieldset class="form-group">
                    <label>Nome</label><br>
                    <input class="form-control" v-model='name'>
                </fieldset>
                <!-- <fieldset class="form-group">
                    <label>Login</label><br>
                    <input class="form-control" v-model='login'>
                </fieldset>
                <fieldset class="form-group">
                    <label>Senha</label><br>
                    <input class="form-control" type="password" v-model='password'>
                </fieldset> -->
                <fieldset class="form-group">
                    <label>Email</label><br>
                    <input class="form-control" v-model='email'>
                </fieldset>
                <fieldset class="form-group">
                    <label>Telefone</label><br>
                    <input class="form-control" v-model='phone'>
                </fieldset>
                <fieldset class="form-group">
                    <label style="margin-right:15px;">Condição:</label>
                    <select v-model='status'>
                        <option v-for="(item, index) in statusOptions" v-bind:key="index">{{item}}</option>
                    </select>
                </fieldset>
                <div class="form-group">
                    <button class="btn btn-danger" v-on:click="backToMain">Cancelar</button>
                </div>
                <div class="form-group">
                    <button class="btn btn-success" v-on:click="validateAndSubmit">Salvar</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import ObjectDataService from "../services/ObjectDataService";

export default {
    data () {
        return {
            id: this.$route.params.id,
            object: 'auth/users',
            name: '',
            username: '',
            email: '',
            password: '',
            phone: '',
            status: '',
            statusOptions: ['Graduação', 'Iniciação Científica', 'Mestrado', 'Doutorado'],
            error: '',
            success: ''
        }
    },
    methods: {
        refreshStaffDetails() {
            if (this.id != "-1") {
                ObjectDataService.get(this.object, this.id).then(res => {
                    this.name = res.data.name;
                    this.username = res.data.username;
                    //this.password = res.data.password;
                    this.email = res.data.email;
                    this.phone = res.data.phone;
                    this.status = res.data.status;
                });    
            }
        },
        backToMain () {
            this.$router.push({ name: "StaffList" })
        },
        validateAndSubmit () {
                if (!this.name || !this.email || !this.phone || !this.status || !this.username || (!this.password && this.id==-1)){
                    this.error = 'Preencha todos os campos!'
                    return
                }

                if (this.id === '-1') {
                    ObjectDataService.create('auth/signup', {
                        username: this.username,
                        password: this.password,
                        name: this.name,
                        email: this.email,
                        phone: this.phone,
                        status: this.status
                    }).then(() => {
                        this.error = ''
                        this.success = "Cadastrado concluído!"
                        let vm = this
                        setTimeout(function() {vm.backToMain()}, 2000)
                    });
                    
                } else {
                    ObjectDataService.update(this.object, this.id, {
                            username: this.username,
                            name: this.name,
                            email: this.email,
                            phone: this.phone,
                            status: this.status,
                    }).then(() => {
                        this.error = ''
                        this.success = "Dados alterados com Sucesso!"
                        let vm = this
                        setTimeout(function() {vm.backToMain()}, 2000)
                    });
                }
                
               

        },
    },
    created() {
        this.refreshStaffDetails() 
    }    
}
</script>

<style scoped>

</style>
