import axios from "axios";



// let refresh = false;

// axios.interceptors.response.use(resp => resp, async error => {
//     if (error.response.status === 403 && !refresh) {
//         refresh = true;
//         const token = localStorage.getItem('refresh_Token');
//         localStorage.removeItem('token')
//         localStorage.removeItem('token')
//         localStorage.removeItem('token')
//         const response = await axios.post(`${UsersService.BASE_URL}/auth/refresh-token`, {token});
//         if (response.status === 200) {
//             const newToken = response.data.token;
//             localStorage.setItem("token", newToken);
//
//             return axios.request(error.config);
//         }
//     }
//     refresh = false;
//     return error;
// });

class UsersService {
    static BASE_URL = "http://localhost:8080"

   static async login(email,password){
        try {
            const response = await axios.post(`${UsersService.BASE_URL}/auth/login`, { email,password});
            console.log(response.data)
            return response.data;

        }catch (e) {
            throw e;
        }
   }
    static async register(userData,token){
        try {
            const response = await axios.post(`${UsersService.BASE_URL}/admin/register`,  userData,{
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        }catch (e) {
                throw e;

        }
    }
    static async getAllUsers(token){
        try {
            const response = await axios.get(`${UsersService.BASE_URL}/admin/get-all-users`,{
                headers: {Authorization: `Bearer ${token}`}
            });
            return response.data;
        }catch (e) {
            throw e;
        }
    }
    static async getYourProfile(token){
        try {
            const response = await axios.get(`${UsersService.BASE_URL}/adminuser/get-profile`,
                {
                headers: {Authorization: `Bearer ${token}`}
            });
            return response.data;
        }catch (e) {
            throw e;
        }
    }

    static async getUserById(userId,token){
        try {
            const response = await axios.get(`${UsersService.BASE_URL}/admin/get-users/${userId}`,
                {
                    headers: {Authorization: `Bearer ${token}`}
                });
            return response.data;
        }catch (e) {
            throw e;
        }
    }
    static async deleteUser(userId,token){
        try {
            const response = await axios.delete(`${UsersService.BASE_URL}/admin/delete/${userId}`,
                {
                    headers: {Authorization: `Bearer ${token}`}
                });
            return response.data;
        }catch (e) {
            throw e;
        }
    }
    static async updateUser(userId,userData,token){
        try {
            const response = await axios.put(`${UsersService.BASE_URL}/admin/update/${userId}`,userData,
                {
                    headers: {Authorization: `Bearer ${token}`}
                });
            return response.data;
        }catch (e) {
            throw e;
        }
    }

//   AUTHENTICATION CHECKER
    static async logout(){
        try {
            const token = localStorage.getItem('refresh_Token');
            await axios.post(`${UsersService.BASE_URL}/auth/logout`,{token})
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            localStorage.removeItem("refresh_Token")
        }catch (e) {
            console.log(e);
        }
    }

    static isAuthenticated(){
        const token = localStorage.getItem('token')
        return !!token
    }

    static isAdmin(){
        const role = localStorage.getItem('role')
        return role === 'ADMIN'
    }

    static isUser(){
        const role = localStorage.getItem('role')
        return role === 'USER'
    }

    static adminOnly(){
        return this.isAuthenticated() && this.isAdmin();
    }

}

export default UsersService;