
const friend = {
  namespaced: true,
  state: {
    addedFriends: [],
    selectedFriends: [],
    followerFriends: [],
    cofollowerFriends: [],
    numberOfFriend: 0
  },
  mutations: {

    // [플레이룸]
    ADD_FRIENDS: function (state) {
      state.addedFriends = []
      state.selectedFriends.map(selectedFriend => state.addedFriends.push(selectedFriend))
      state.selectedFriends = []
      console.log('state.addedFriends', state.addedFriends)
    },
    REVOKE_FRIENDS: function (state) {
      state.selectedFriends = []
      state.addedFriends.map(addedFriend => state.selectedFriends.push(addedFriend))
      state.addedFriends = []

      console.log('state.revokeFriends', state.selectedFriends)
    },
    SELECT_FRIEND: function (state, toAddFriend) {
      state.selectedFriends.push(toAddFriend)
      console.log('select_friend', state.selectedFriends)
    },
    DESELECT_FRIEND: function (state, toRemoveFriend) {
      const idx = state.selectedFriends.findIndex(selectedFriend => selectedFriend.id == toRemoveFriend.id)
      state.selectedFriends.splice(idx, 1)
      console.log('deselect_friend', state.selectedFriends)
    },
    RESET_ADDED_FRIENDS: function (state) {
      state.selectedFriends = []
      state.addedFriends = []
    },
    SET_FOLLOWER_FRIENDS: function (state, value) {
      state.followerFriends = value ? value : state.followerFriends;
    },
    SET_COFOLLOWER_FRIENDS: function (state, value) {
      state.cofollowerFriends = value ? value : state.cofollowerFriends;
    }
  },
  actions: {
    addFriends: function ({ commit }) {
      commit('ADD_FRIENDS')
    },
    revokeFriends: function ({ commit }) {
      commit('REVOKE_FRIENDS')
    },
    selectFriend: function ({ commit }, friend) {
      commit('SELECT_FRIEND', friend)
    },
    deselectFriend: function ({ commit }, friend) {
      commit('DESELECT_FRIEND', friend)
    },
    resetAddedFriends: function ({ commit }) {
      commit('RESET_ADDED_FRIENDS')
    },
    setFollowerFriend: function ({ commit }, { data }) {
      commit('SET_FOLLOWER_FRIENDS', data);
    },
    setCofollowerFriend: function ({ commit }, { data }) {
      commit('SET_COFOLLOWER_FRIENDS', data);
    }

  },
  getters: {
    numberOfAddedFriends: state => state.addedFriends.length,
  }
}

export default friend
