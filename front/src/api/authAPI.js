import axios from 'axios'
import Constants from '@/constants'

axios.defaults.withCredentials = true

//로그인
const login = (id, password) => {
    return axios.post(`${Constants.API_SERVER}/api/auth/users/${id}/signin/`, {
            "password": password,
        }, {
            headers: {
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            return response.data
        })
        .catch((error) => {
            if (error.response) {
                return error.response.data;
            } else if (error.request) {
                return error;
            } else {
                return error;
            }
        })
}

//로그아웃
const logout = (id) => {
}

//인증여부 체크
const confirmAccessToken = (id) => {
}


export default {
    login,
    logout,
    confirmAccessToken
}