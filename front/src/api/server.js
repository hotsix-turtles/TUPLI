export default{
  // URL: "http://127.0.0.1:8080", // Local
  URL: "https://tupli.kr/api/v1",  //EC2 SSAFY
  URLFRONT: "https://tupli.kr",  // EC2 SSAFY
  BACKEND_PORT: 8080,
  FRONTEND_PORT: 80,

  ROUTES: {
    accounts: {
      default: '/account',
      getUserInfo: '/account/userinfo',

      kakaoPay: '/kakaoPay/'
    },
    // GCS
    image : 'https://storage.cloud.google.com/tupli_profile',
  }

}

