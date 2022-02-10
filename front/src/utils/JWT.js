export const getToken = () => {
  return window.localStorage.getItem('jwt');
};
