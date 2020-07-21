<template>
    <div>
        <br>
         <DeleteConfirmation 
            :object='component' 
            v-if="showDeleteConfirmation"
            @shownDeleteConfirmation = 'deleteEvent($event, idDeleteEvent)'
        >
        </DeleteConfirmation>
        <div v-if="error" class="alert alert-warning">
            {{error}}
        </div>
        <div v-if="invalidURLMessage" class="alert alert-warning">
            {{invalidURLMessage}}
        </div>
        <div v-if="showModeratorContent()" class="card" style="margin-top: 20px;">    
            <h4 class="card-header">Cadastrar Novo Evento</h4><br>
            <p style="margin-top: 25px; margin-left: 20px">Selecione a conferência:   
                <select v-model="conferenceName">
                    <option v-for="(item, index) in conferences" :key="index">
                        {{item.name}}
                    </option>   
                </select><br>
            </p>  
            <p style="margin-left: 20px">Ano: 
                <input type="text" v-model="year"> 
            </p>
            <p style="margin-left: 20px">Data de início: 
                <input type="date" v-model="startDate"> 
            </p>
            <p style="margin-left: 20px">Data de fim: 
                <input type="date" v-model="endDate">
            </p>
            <p style="margin-left: 20px">Data de submissão: 
                <input type="date" v-model="submissionDate">
            </p>
            <p style="margin-left: 20px">Local: 
                <input v-model="place">
            </p>
            <p style="margin-left: 20px">Call for Paper: 
                <input type='url' v-model="callForPaper">
            </p>
            <br><br>
            <p style="text-align: center;">
                <a class="btn btn-success" @click="addEvent">Adicionar Evento</a><br>
            </p>
        </div><br><br>
        <div v-show="success" class="alert alert-success">
            {{success}}
        </div>
        <p style="text-align: right;">
            <span style="font-size: 20px;">Visualizar como: </span>
            <select v-model="visualization">
                <option>Calendário</option>
                <option>Lista</option>
            </select>
        </p>
         <CalendarListVisualization v-if="visualization=='Lista'"/>
         <FullCalendar 
            v-if="visualization=='Calendário'"
            themeSystem = 'standard'
            defaultView = 'dayGridMonth'
            :plugins="calendarPlugins"
            :events="events"
            locale = 'pt-br'
            :buttonText="general"
            :displayEventTime="false"
            @eventClick="viewEventInfo"
            >
        </FullCalendar>
        <div v-if="visualization=='Lista'">
            <table class="table">
                <thead>
                    <tr>
                        <th>Conferência</th>
                        <th>Ano</th>
                        <th>Data de Início</th>
                        <th>Data de Fim</th>
                        <th>Data de Submissão</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item, index) in eventsList" :key="index">
                        <td >{{item.title}}</td>
                        <td>{{item.year}}</td>
                        <td>{{item.start.slice(8, 10) + '/' + item.start.slice(5, 7) + '/' + item.start.slice(0,4)}}</td>
                        <td>{{item.end.slice(8, 10) + '/' + item.end.slice(5, 7) + '/' + item.end.slice(0,4)}}</td>
                        <td>{{item.submission.slice(8, 10) + '/' + item.submission.slice(5, 7) + '/' + item.submission.slice(0,4)}}</td>
                        <td><button v-if="showModeratorContent()" class="btn btn-success" @click="viewEventInfo(item.id)">Editar</button></td>
                        <td><button v-if="showModeratorContent()" class="btn btn-warning" @click='showDeleteConfirmation = true' @click.prevent='idDeleteEvent = index'>Apagar</button></td>
                    </tr>
                </tbody>
            </table>
        </div>

    </div>
</template>

<script>
import FullCalendar from '@fullcalendar/vue';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import ObjectDataService from '../services/ObjectDataService';
import DeleteConfirmation from './DeleteConfirmation'

export default {
    components: {
        FullCalendar,
        DeleteConfirmation
    },
    data () {
        return {
            calendarPlugins: [dayGridPlugin, interactionPlugin],
            events: [],
            general: {today: 'Retonar à data atual'},
            callForPaper: '',
            startDate: '',
            endDate: '',
            submissionDate: '',
            conferenceName: '',
            conferences: [],
            success: '',
            error: '',
            invalidURLMessage: '',
            place: '',
            eventinhos: [],
            object: 'event',
            subject: 'Teste de email',
            body: 'Email enviado automaticamente. Insira o corpo aqui.',
            users: [],
            year: '',
            visualization: 'Calendário',
            eventsList: [],
            component: 'evento',
            showDeleteConfirmation: false,
            idDeleteEvent: ''
        }
    },
    methods: {
        refreshUsers(){
            ObjectDataService.getAll('users')
                .then(response => {
                    this.users = response.data;
                    console.log(response.data)
                })
        },
        checkValidURL (string) {
            try {
                new URL(string);
            } catch (_) {
                return false;  
            }
            return true;
        },
        deleteEvent(permision, id) {
            if (permision) {
                console.log(id)
                ObjectDataService.deleteCalendar('calendar', this.eventsList[id].conferenceId, this.eventsList[id].id)
                  .then(() => {
                      this.showDeleteConfirmation = false
                      this.refreshConferences()
                      this.success = 'Evento Excluído'
                      setTimeout(() => {
                          this.success = ''
                      }, 2000)
                  })
            
                
            } else {
                this.showDeleteConfirmation = false
            }
        },
        addEvent () {

            this.invalidURLMessage = ''
            this.error = ''
            if (!this.conferenceName || !this.startDate || !this.endDate || !this.place || !this.callForPaper || !this.year) {
                this.error = "Veículo não cadastrado, preencha todos os campos!"
                return
            }

            if (!this.checkValidURL(this.callForPaper)){
                this.invalidURLMessage = 'A URL digitada é inválida!'
                return
            }

            let conferenceId
            for (let i = 0; i < this.conferences.length; i++) {
                if (this.conferences[i].name == this.conferenceName) {
                    conferenceId = this.conferences[i].vehicle_id
                }
            }

            ObjectDataService.createCalendar("calendar", conferenceId, { 
                    startDate: this.startDate,
                    endDate: this.endDate + 'T12:00:00',
                    submissionDate: this.submissionDate + 'T08:00:00',
                    place: this.place,
                    callForPaper: this.callForPaper,
                    year: this.year
                }).then(() => {
                    this.success = "Evento cadastrado com sucesso!"
                    this.error = ""
                    this.invalidURLMessage = ''
                    this.refreshConferences()
                    setTimeout(() => {
                        this.conferenceName = ''
                        this.startDate = ''
                        this.endDate = ''
                        this.submissionDate = ''
                        this.place = ''
                        this.success = ''
                        this.callForPaper = ''
                        this.year = ''
                    }, 2000)
                })
        },
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        viewEventInfo(arg){
            if (this.visualization == 'Calendário') {
                this.$router.push(`/event/${arg.event.id}`);
            } else if (this.visualization == 'Lista') {
                this.$router.push(`/event/${arg}`);
            }
            
        },
        refreshConferences() {
            this.conferences = []
            this.events = []
            this.eventsList = []
            ObjectDataService.getAll("vehicles")
                .then(response => {
                    for (let i = 0; i < response.data.length; i++) {
                        if (response.data[i].type == "Conferência") {
                            this.conferences.push(response.data[i])
                        }
                    }
                    for (let i = 0; i < this.conferences.length; i++) {
                        for (let j = 0; j < this.conferences[i].calendar.length; j++) {
                            this.events.push({
                                title: this.conferences[i].name + ' ' + this.conferences[i].calendar[j].year + '\nEvento',
                                start: this.conferences[i].calendar[j].startDate,
                                end: this.conferences[i].calendar[j].endDate,
                                id: this.conferences[i].calendar[j].id
                            })
                            this.events.push({
                                title: this.conferences[i].name + ' ' + this.conferences[i].calendar[j].year + '\nSubmissão',
                                start: this.conferences[i].calendar[j].submissionDate,
                                id: this.conferences[i].calendar[j].id
                            })
                            this.eventsList.push({
                                title: this.conferences[i].name,
                                year: this.conferences[i].calendar[j].year,
                                start: this.conferences[i].calendar[j].startDate,
                                end: this.conferences[i].calendar[j].endDate,
                                submission: this.conferences[i].calendar[j].submissionDate,
                                id: this.conferences[i].calendar[j].id,
                                conferenceId: this.conferences[i].vehicle_id
                            })
                        }
                    }
                })
        },
    },
    created () {
        this.refreshConferences();
        //this.refreshUsers();
    }
}

</script>

<style scoped>

@import '~@fullcalendar/core/main.css';
@import '~@fullcalendar/daygrid/main.css';

</style>