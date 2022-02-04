/* eslint-disable vue/require-default-prop */
<template>
  <div>
    <!-- 만들기 버튼 나오면서 나오는 검정 배경 -->
    <div
      v-if="isClickedMakeBtn"
      class="dark-background"
      @click="toggle"
    />
    <!-- 만들기 버튼 (플레이리스트/플레이룸/게시글) -->
    <div class="d-flex-column pb-5">
      <div
        v-if="isClickedMakeBtn"
        class="d-flex-column text-center"
      >
        <div class="my-5">
          <v-btn
            rounded
            @click="goPlaylistForm"
          >
            플레이리스트
          </v-btn>
        </div>
        <div class="my-5">
          <v-btn
            rounded
            @click="goPlayroomForm"
          >
            플레이룸
          </v-btn>
        </div>
        <div class="my-5">
          <v-btn
            rounded
          >
            게시글
          </v-btn>
        </div><br><br>
      </div>
      <!-- 네브바 -->
      <div
        class="d-flex justify-space-around fixed-bottom navbar-background py-1"
      >
        <div class="clickable d-flex-column text-center">
          <v-icon>mdi-home</v-icon>
          <div>홈</div>
        </div>

        <div
          class="clickable d-flex-column text-center"
          @click="$router.push({ name: 'Search' })"
        >
          <v-icon>mdi-magnify</v-icon>
          <div>검색</div>
        </div>

        <div
          class="clickable d-flex-column text-center"
          @click="toggle"
        >
          <v-icon>mdi-plus-box</v-icon>
          <div>만들기</div>
        </div>

        <div
          class="clickable d-flex-column text-center"
          @click="$router.push({ name: 'Category' })"
        >
          <v-icon>mdi-compass</v-icon>
          <div>둘러보기</div>
        </div>

        <div
          class="clickable d-flex-column text-center"
        >
          <v-icon>mdi-account-circle-outline</v-icon>
          <div>프로필</div>
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
    },
    goPlaylistForm: function() {
      this.$router.push({ name: 'PlaylistForm' })
      this.toggle()
    },
    goPlayroomForm: function() {
      this.$router.push({ name: 'PlayroomForm' })
      this.toggle()
    },
    // goBoardForm: function() {
    //   this.$router.push({ name: 'BoardForm' })
    //   this.toggle()
    // },
  }
}
</script>
