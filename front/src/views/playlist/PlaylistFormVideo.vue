<template>
  <div>
    <cancel :page-name="pageName" />
    <search-bar
      :label="'새로운 영상을 검색해주세요'"
      :router-page="'VideoSearch'"
    />
    <!-- @click="$router.push({ name: 'VideoSearch' })" -->

    <!-- 탭 -->
    <v-tabs
      v-model="tab"
      background-color="transparent"
      grow
    >
      <v-tab
        v-for="item in items"
        :key="item"
      >
        {{ item }}
      </v-tab>
    </v-tabs>
  </div>
</template>

<script>
import Cancel from '../../components/common/Cancel.vue'
import SearchBar from '../../components/common/SearchBar.vue'
export default {
  name: 'PlaylistFormVideo',
  components: { Cancel, SearchBar },
  data: function() {
    return {
      pageName: "영상 추가하기",
      tab: null,
      items: [
        '좋아한 영상', '저장한 영상',
      ],
    }
  }
}
</script>

<style>

</style>
