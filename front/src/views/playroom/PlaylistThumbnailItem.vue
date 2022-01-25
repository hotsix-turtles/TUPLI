<template>
    <div>
        <v-list-item-avatar size="80" color="grey" src="">
            <v-badge color="#EAEAEA" inline class="mx-auto"></v-badge>
        </v-list-item-avatar>
    </div>
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