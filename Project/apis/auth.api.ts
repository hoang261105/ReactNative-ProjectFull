import { UserLogin, UserRequest } from "@/interface/interface"
import { axiosInstance } from "@/utils/axiosInstance"

// Hàm đăng ký tài khoản
export const registerUser = async (data: UserRequest) => {
    const response = await axiosInstance.post("/auth/register", data);
    return response.data;
}

// Hàm lấy danh sách user
export const getAllUsers = async () => {
    const response = await axiosInstance.get("/users");
    return response.data;
}

// Hàm đăng nhập tài khoản
export const loginUser = async (data: UserLogin) => {
    const response = await axiosInstance.post("/auth/login", data);
    return response.data;
}