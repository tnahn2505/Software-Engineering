import axios from 'axios';

const teacherApi = axios.create({
    baseURL: 'http://localhost:8088/api/teacher',
});

teacherApi.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default teacherApi;
