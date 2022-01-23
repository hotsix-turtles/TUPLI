<template>
  <div class="playroom">
    <div class="playerWrapper">
      <div class="player">

      </div>
    </div>
    <div class="playerNav">
      <div class="playroomLike"></div>
      <div class="playroomChat"></div>
      <div class="playroomShare"></div>
      <div class="playroomReport"></div>
    </div>
    <div class="playroomInfo">
      <div class="playroomTitleWrapper">
        <div class="playroomPublicBadge">

        </div>
        <div class="playroomTitle">

        </div>
      </div>
      <div class="playroomAuthorWrapper">
        <div class="authorProfilePic">

        </div>
        <div class="authorName">

        </div>
        <div class="authorFollowerWrapper">
          <span class="authorFollowerLabel">팔로워</span>
          <span class="authorFollower"></span>
        </div>
      </div>
      <div class="playroomPlaytimeWrapper">
        <span class="playtimeLabel">PLAY TIME</span>
        <span class="playtime"></span>
      </div>
      <div class="playroomContentWrapper">
        <p class="playroomContent"></p>
        <p class="playroomContentMore">더 보기</p>
      </div>
      <div class="playroomTagItems">
      </div>
    </div>
    <div class="playlistWrapper">
      <div class="playlistThumbnailWrapper">
        <div class="thumbnailItems">

        </div>
      </div>
      <div class="playlistVideoWrapper">
        <div class="playlistVideoNav">

        </div>
        <div class="playlistVideoItems">
          
        </div>
        <div class="playlistNavWrapper">
          <div class="videoCounter">

          </div>
          <div class="navBtnsWrapper">
            <div class="playVideo">

            </div>
            <div class="showMyPlaylist">

            </div>
            <div class="saveVideo">

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PlayroomDetail',
  metaInfo: {
    title: 'RoomName',
    titleTemplate: '%s | Tupli',
    htmlAttrs: {
      lang: 'ko-KR'
    },
    meta: [
      { charset: 'utf-8' },
      //{ name: 'description', content: ''},
      { name: 'viewport', content: 'width=device-width, initial-scale=1' }
    ]
  }
}
</script>

<style lang="scss">
@import '~@/assets/scss/common';
</style>