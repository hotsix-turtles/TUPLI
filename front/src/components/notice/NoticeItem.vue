<template>
  <div>
    <div class="d-flex align-center px-4 my-3">
      <!-- 프로필 사진 -->
      <div
        class="d-flex justify-center align-center notice-img"
        style="background-color: yellow; border-radius: 100%;"
        @click="setProfile"
      >
        <img
          src="@/assets/tupli_logo2_dark.png"
          alt="profile img"
          width="30"
        >
      </div>

      <!-- [알림 종류] -->
      <!-- 팔로우 -->
      <div
        v-if="notice.type === 'follow'"
        class="mx-3 d-flex align-center"
      >
        <div
          class="
        d-flex
        flex-column"
        >
          <div class="notice-content">
            <p
              class="mb-0"
              @click="setProfile"
            >
              {{ notice.fromId }}
              님이&nbsp;
              {{ nickname }}님을 팔로우합니다.
            </p>
          </div>
          <p
            class="mb-0"
          >
            15분
          </p>
        </div>
        <v-btn
          class="ml-4"
          @click="clickFollow"
        >
          팔로우
        </v-btn>
      </div>

      <!-- 플레이룸 초대 -->
      <div
        v-else-if="notice.type === 'invite'"
        class="mx-3 align-center"
      >
        <div class="d-flex">
          <div class="d-flex flex-column mb-0">
            <div class="d-flex flex-wrap notice-content">
              <p
                class="mb-0"
                @click="setProfile"
              >
                {{ notice.fromId }}님이&nbsp;{{ nickname }}님을 초대하였습니다.
                플레이룸 제목이 들어갈 공간입니다
              </p>
            </div>
            <p
              class="mb-0"
            >
              15분
            </p>
          </div>
          <div>
            <v-btn class="ml-4 notice-btn">
              수락
            </v-btn>
          </div>
        </div>
      </div>

      <!-- 플레이룸 개설 알림 -->
      <div
        v-else-if="notice.type === 'playroomMake'"
        class="mx-3  align-center"
      >
        <div class="d-flex mb-0">
          <p
            class="mb-0"
            @click="setProfile"
          >
            {{ notice.fromId }}
          </p>
          <p class="mb-0">
            님이&nbsp;
          </p>
          <p class="mb-0">
            {{ nickname }}님의 플레이리스트로 플레이룸을 생성하였습니다.
          </p>
          <br>
          <p>
            플레이룸 제목이 들어갈 공간
          </p>
        </div>
        <div>
          <p
            class="mb-0"
          >
            15분
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'NoticeItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    notice: { type: Object },
  },
  data: function() {
    return {
      noticeType: '',
    }
  },
  computed: {
    ...mapState(['nickname'])
  },
  methods: {
    // 타 유저 프로필로 가기
    setProfile: function() {
      this.$router.push({ name: 'Profile' })
    },
    // 팔로우
    clickFollow: function() {

    }
  },
}
</script>

<style scoped>
p {
  font-size: 10px;
}

/* 알림 이미지 */
.notice-img {
  width: 44px ;
  height: 44px;
}

/* 알림 내용 */
.notice-content {
  margin-right: 10px;
  overflow: hidden;
  line-height: 1.2em;
  max-height: 2.4em;
}

/* 알림 버튼 */
.notice-btn {
  width: 20px;
}
</style>
