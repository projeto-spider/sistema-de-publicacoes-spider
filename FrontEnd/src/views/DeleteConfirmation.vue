<template>
<div>
    <div class='defocus' @click="handleClick(false)">
    </div>
    <div  class="card" 
        style="width: 30rem; top: 30%; left: 33%; z-index: 9999; position: fixed;">
            <span class="card-header text-light bg-dark">
            <h5 style="text-align: center;">
                <button @click="handleClick(false)" type="button" class="close" data-dismiss="card" aria-label="Close">
                    <span aria-hidden="true" class="text-white" style="font-size: 35px;">&times;</span>
                </button>
                Tem certeza que deseja apagar os dados deste {{object}}?
            </h5>
            </span>

            <span class="bg-light" style = "text-align: center;">
                <br>
                <button 
                    class = "btn btn-warning"
                    @click = "handleClick(true)"
                >
                    Sim
                </button>
                <button 
                    style = "margin-left: 30px;" 
                    class = "btn btn-danger"
                    @click = "handleClick(false)"
                >
                    Cancelar
                </button>
                <br>
                <br>
            </span>
    </div>
</div>
</template>

<script>
export default {
    props: {
        object: {
            type: String,
            required: true
        }
    },
    methods: {
        handleClick(arg) {
            this.$emit('shownDeleteConfirmation', arg)
        }
    }

}
</script>

<style scoped>
.defocus {
  position: fixed;
  z-index: 2;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(162, 162, 162, 1);
  display: table;
  opacity: 0.75;
  transition: opacity .3s ease;
}
</style>>
