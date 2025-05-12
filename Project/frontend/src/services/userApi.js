import axios from 'axios';

const userApi = axios.create({
    baseURL: 'http://localhost:8089/users',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default userApi;
