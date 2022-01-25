<template>
    <v-avatar size="80" color="grey" class="ml-2 my-2">
        <v-img :src=src contain></v-img>
    </v-avatar>
</template>

<script>
export default {
    name: 'PlaylistThumbnailItem',
    props: {
        src: String
    },
    data() {
        return {

        }
    }
}
</script>

<style>

</style>