<template>
    <div>
         <DeleteConfirmation 
            :object='component' 
            v-if="showDeleteConfirmation"
            @shownDeleteConfirmation = 'deleteEvent($event)'
        >
         </DeleteConfirmation>
        <div style="margin-top: 20px;">
            <div v-if="success" class="alert alert-success">
                {{success}}
            </div>
            <div v-if="error" class="alert alert-warning">
                {{error}}
            </div>
            <div v-if="invalidURLMessage" class="alert alert-warning">
                {{invalidURLMessage}}
            </div>
            <h4><b>Informações do Evento</b></h4><br>
            <label><b>Conferência:</b> </label> {{conference.name}}<br>
            <label><b>Ano:</b></label>
            <input v-if="showModeratorContent()" v-model="year" style="margin-left: 10px;" type="text">
            <p style="display: inline;" v-if="!showModeratorContent()">{{year}}</p>
            <br>
            <label><b>Data de início:</b></label>
            <input v-if="showModeratorContent()" v-model="startDate" style="margin-left: 10px;" type="date">
            <p style="display: inline;" v-if="!showModeratorContent()">{{startDate.slice(8,10) + '/' + startDate.slice(5,7) + '/' + startDate.slice(0,4)}}</p>
            <br>
            <label><b>Data de fim:</b></label>
            <input v-if="showModeratorContent()" v-model="endDate" style="margin-left: 10px;" type="date">
            <p style="display: inline;" v-if="!showModeratorContent()">{{endDate.slice(8,10) + '/' + endDate.slice(5,7) + '/' + endDate.slice(0,4)}}</p>
            <br>
            <label><b>Data de submissão:</b></label>
            <input v-if="showModeratorContent()" v-model="submissionDate" style="margin-left: 10px;" type="date">
            <p style="display: inline;" v-if="!showModeratorContent()">{{submissionDate.slice(8,10) + '/' + submissionDate.slice(5,7) + '/' + submissionDate.slice(0,4)}}</p>
            <br>
            <label><b>Local:</b></label> 
            <input v-if="showModeratorContent()" v-model="place"> 
            <div style="display: inline;" v-if="!showModeratorContent()">{{place}}</div>
            <br>
            <label><b>Call for Paper:</b></label> 
            <input type="url" v-if="showModeratorContent()" v-model="callForPaper"> 
            <div style="display: inline;" v-if="!showModeratorContent()">
                <a :href='callForPaper'>
                    {{callForPaper}}
                </a>
            </div>
            <br><br>
            <button v-if="showModeratorContent()" @click="updateEvent" class="btn btn-success">Salvar Alterações</button>
            <button v-if="showModeratorContent()" @click="showDeleteConfirmation = true" class="btn btn-danger">Apagar Evento</button>
        </div>
    </div>
</template>

<script>
import ObjectDataService from '../services/ObjectDataService'
import DeleteConfirmation from './DeleteConfirmation.vue'

export default {
    components: {
        DeleteConfirmation
    },
    data () {
        return {
            object: 'calendar',
            startDate: '',
            endDate: '',
            submissionDate: '',
            place: '',
            callForPaper: '',
            conferences: [],
            conference: '',
            error: '',
            success: '',
            component: 'evento',
            showDeleteConfirmation: false,
            invalidURLMessage: '',
            year: ''
        }
    },
    computed: {
        id() {
            return this.$route.params.id;
        }
    },
    methods: {
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        checkValidURL (string) {
            try {
                new URL(string);
            } catch (_) {
                return false;  
            }
            return true;
        },
        deleteEvent(permision) {
            if (permision) {
                ObjectDataService.deleteCalendar(this.object, this.conference.vehicle_id, this.id)
                    .then (() => {
                        this.success = "Evento excluído"
                        this.showDeleteConfirmation = false
                        setTimeout(() => {
                            this.$router.push({ name: "Calendar" })
                        }, 2000)
                    })
            } else {
                this.showDeleteConfirmation = false
            }
            
        },
        updateEvent() {
            this.error = ''
            this.invalidURLMessage = ''
            if (!this.startDate || !this.endDate || !this.place || !this.callForPaper || !this.year) {
                this.error = 'Todos os campos precisam ser preenchidos!'
                return
            }

            if (!this.checkValidURL(this.callForPaper)) {
                this.invalidURLMessage = 'A URL digitada é inválida!'
                return 
            }

            ObjectDataService.update(this.object, this.id, {
                startDate: this.startDate,
                endDate: this.endDate + 'T12:00:00',
                submissionDate: this.submissionDate,
                place: this.place,
                callForPaper: this.callForPaper,
                year: this.year
            }).then (() => {
                this.error = ''
                this.invalidURLMessage = ''
                this.success = "Suas alterações foram salvas"
                setTimeout(() => {
                    this.$router.push({ name: "Calendar" })
                }, 2000)
            })
        },
        findConference() {
            ObjectDataService.getAll("vehicles")
                .then(response => {
                    for (let i = 0; i < response.data.length; i++) {
                        if (response.data[i].type == "Conferência") {
                            this.conferences.push(response.data[i])
                        }
                    }
                    for (let i = 0; i < this.conferences.length; i++) {
                        for (let j = 0; j < this.conferences[i].calendar.length; j++) {
                            if (this.id == this.conferences[i].calendar[j].id) {
                                this.conference = this.conferences[i]
                            }
                        }
                    }
                })
        },
        getEventInfo() {
            ObjectDataService.get(this.object, this.id)
            .then(response => {
                this.startDate = response.data.startDate
                this.endDate = response.data.endDate.slice(0, 10)
                this.submissionDate = response.data.submissionDate.slice(0, 10)
                this.place = response.data.place
                this.callForPaper = response.data.callForPaper
                this.year = response.data.year
            })
        }
    },
    created() {
        this.findConference()
        this.getEventInfo()
    }
}
</script>

<style scoped>
label {
    margin-right: 10px;
}
</style>