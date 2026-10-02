import axios, { AxiosInstance } from 'axios';

const token = 'oat_MTEy.X0duUGVPRThhNkFRMHlGSzBOU1M4dTE2NW5hZU1CZm9vdWwzbVZKNDg0ODAxOTQ1NQ'; 

const instance: AxiosInstance = axios.create({
  baseURL: 'https://api.example.com',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});

export default instance;