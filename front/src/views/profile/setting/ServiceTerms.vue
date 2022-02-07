<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          서비스 이용 약관
        </h3>
      </v-row>
    </v-container>
    <div class="mx-8">
      <p>
        우리(TUPLI 운영자)는 TUPLI라는 소셜 비디오 플랫폼을 운영하고 있으며 여러분이 그것을 사용하기를 바랍니다. 
        <br>
        TUPLI의 기본 서비스는 무료이며 TUPLI + 프리미엄 고급 기능에 대한 유료 업그레이드를 제공합니다. 
        <br>
        우리 서비스는 TUPLI 대화방에서 일어나는 일에 대해 최대한 많은 통제와 소유권을 제공하도록 설계되었습니다. 
        <br>
        그러나 게시한 내용에 대해서는 책임을 져야 합니다. 
        <br>
        특히 스팸, 바이러스 또는 심각한 폭력 위협과 같은 금지 품목이 방에 나타나지 않도록 하십시오.
        <br>
        <br>
        본 이용 약관을 위반한다고 생각되는 TUPLI 방을 발견하면 <a href="mailto:mingu4969@naver.com?subject=튜플리 문의드립니다.">[저희에게 연락해 주십시오.]</a>
      </p>
    </div>
  </v-app>
</template>

<script>
export default {

}
</script>

<style>

</style>
