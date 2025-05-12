import axios from 'axios';

const studentApi = axios.create({
    baseURL: 'http://localhost:8083/api/student'
});

// Add a request interceptor to add the token to all requests
studentApi.interceptors.request.use(
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

export default studentApi;
