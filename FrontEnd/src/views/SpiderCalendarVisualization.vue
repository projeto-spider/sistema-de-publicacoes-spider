<template>
    <div>
        <FullCalendar
                    themeSystem = 'standard'
                    defaultView = 'dayGridMonth'
                    locale = 'pt-br'
                    :plugins="calendarPlugins"
                    :buttonText="text"
                    :events="plans"
                    @eventClick="viewPlanInfo"
                > 
        </FullCalendar>
    </div>
</template>

<script>

import ObjectDataService from '../services/ObjectDataService'
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
            plans: []
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
                this.$router.push(`/plan/${arg.event.id}`);
            } else {
                this.$router.push(`/plan-details/${arg.event.id}`);
            }
            
        },
        refreshPlans() {
            this.plans = []
            ObjectDataService.getAll('planning')
                .then(response => {
                    for (let item of response.data) {
                        this.plans.push({
                            title: item.vehicle.name + ' ' + item.year + ' -\n ' + item.user.name + ' - \n' + 'Revisão',
                            start: item.revisionDate,
                            end: item.revisionDate,
                            id: item.id
                        })
                        this.plans.push({
                            title: item.vehicle.name + ' ' + item.year + ' -\n' + item.user.name + ' - \n' + 'Submissão',
                            start: item.submissionDate,
                            end: item.submissionDate,
                            id: item.id
                        })
                    }
                })
        }
    },
    created () {
        this.refreshPlans()
    }
}
</script>

<style scoped>

@import '~@fullcalendar/core/main.css';
@import '~@fullcalendar/daygrid/main.css';

</style>