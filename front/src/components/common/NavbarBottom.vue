/* eslint-disable vue/require-default-prop */
<template>
  <div>
    <div
      v-if="isClickedMakeBtn"
      class="dark-background"
      @click="toggle"
    />
    <div class="d-flex-column">
      <div
        v-if="isClickedMakeBtn"
        class="d-flex-column text-center"
      >
        <div class="my-5">
          <v-btn
            rounded
            @click="toggle"
          >
            <router-link
              :to="{ name: 'PlaylistForm' }"
              class="text-decoration-none text-primary"
            >
              PlaylistForm
            </router-link>
          </v-btn>
        </div>
        <div class="my-5">
          <v-btn
            rounded
            @click="toggle"
          >
            <router-link
              :to="{ name: 'PlaylistFormVideo' }"
              class="text-decoration-none text-primary"
            >
              PlaylistFormVideo
            </router-link>
          </v-btn>
        </div>
      </div>
      <div class="d-flex justify-space-around fixed-bottom navbar-background">
        <div class="clickable d-flex-column justify-center">
          <v-icon>mdi-home</v-icon>
          <span>홈</span>
        </div>

        <div
          class="clickable d-flex-column justify-center"
          @click="$router.push({ name: 'Search' })"
        >
          <v-icon>mdi-magnify</v-icon>
          <span>검색</span>
        </div>

        <div
          class="clickable d-flex-column justify-center"
          @click="toggle"
        >
          <v-icon>mdi-plus-box</v-icon>
          <span>만들기</span>
        </div>

        <div
          class="clickable d-flex-column justify-center"
          @click="$router.push({ name: 'Category' })"
        >
          <v-icon>mdi-compass</v-icon>
          <span>둘러보기</span>
        </div>

        <div
          class="clickable d-flex-column justify-center"
        >
          <v-icon>mdi-account-circle-outline</v-icon>
          <span>프로필</span>
        </div>
      </div>
      <!-- <v-bottom-navigation
        :value="value"
        color="#5B5C9D"
        style=""
        grow
      >
        <v-btn>
          <span>홈</span>
          <v-icon>mdi-home</v-icon>
        </v-btn>

        <v-btn>
          <span>
            <router-link
              :to="{ name: 'Search' }"
              class="text-decoration-none"
            >
              검색
            </router-link>
          </span>
          <router-link
            :to="{ name: 'Search' }"
            class="text-decoration-none"
          >
            <v-icon>
              mdi-magnify
            </v-icon>
          </router-link>
        </v-btn>

        <v-btn @click="toggle">
          <span>만들기</span>
          <v-icon>mdi-plus-box</v-icon>
        </v-btn>

        <v-btn>
          <span>
            <router-link
              :to="{ name: 'Category' }"
              class="text-decoration-none"
            >
              둘러보기
            </router-link>
          </span>
          <router-link
            :to="{ name: 'Category' }"
            class="text-decoration-none"
          >
            <v-icon>
              mdi-compass
            </v-icon>
          </router-link>
        </v-btn>

        <v-btn>
          <span>프로필</span>
          <v-icon>mdi-account-circle-outline</v-icon>
        </v-btn>
      </v-bottom-navigation> -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'NavbarBottom',
  components: {
  },
  data: function() {
    return {
      value: 1,
      isClickedMakeBtn: false
    }
  },
  methods: {
    toggle: function() {
      this.isClickedMakeBtn = !this.isClickedMakeBtn
    }
  }
}
</script>
