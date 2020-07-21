<template>
    <div>
        <FullCalendar
                    themeSystem = 'standard'
                    defaultView = 'dayGridMonth'
                    locale = 'pt-br'
                    :plugins="calendarPlugins"
                    :buttonText="text"
                    :events="corrections"
                    @eventClick="viewPlanInfo"
                > 
        </FullCalendar>
    </div>
</template>

<script>

import ObjectDataService from '../../services/ObjectDataService'
import FullCalendar from '@fullcalendar/vue';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

export default {
    components: {
        FullCalendar
    },
    data() {
        return {
            calendarPlugins: [dayGridPlugin, interactionPlugin],
            text: {today: 'Retonar à data atual'},
            corrections: []
        }
    },
    methods: {
        showModeratorContent() {
            if (localStorage.getItem("userRole") == "ROLE_MODERATOR") {
                return true
            }
            return false;
        },
        viewPlanInfo(arg) {
            if (this.showModeratorContent()) {
                this.$router.push(`/correction-edit/${arg.event.id}`);
            } else {
                this.$router.push(`/correction-details/${arg.event.id}`);
            }
            
        },
        refreshCorrections() {
            this.corrections = []
            ObjectDataService.getAll('approved')
                .then(response => {
                    for (let item of response.data) {
                        this.corrections.push({
                            title: item.planning.user.name + ' -\n ' + item.planning.vehicle.name + ' ' + item.planning.year + ' - \n' + 'Revisão',
                            start: item.newRevisionDate,
                            end: item.newRevisionDate,
                            id: item.id
                        })
                        this.corrections.push({
                            title: item.planning.user.name + ' -\n' + item.planning.vehicle.name + ' ' + item.planning.year + ' - \n' + 'Submissão',
                            start: item.newSubmissionDate,
                            end: item.newSubmissionDate,
                            id: item.id
                        })
                    }
                })
        }
    },
    created () {
        this.refreshCorrections()
    }
}
</script>

<style scoped>

@import '~@fullcalendar/core/main.css';
@import '~@fullcalendar/daygrid/main.css';

</style>