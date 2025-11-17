import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";

export const axiosInstance = axios.create({
  baseURL: "http://192.168.2.199:8080/api/v1",
  headers: { "Content-Type": "application/json" },
  timeout: 10000,
});

// Gắn token cho mọi request
axiosInstance.interceptors.request.use(async (config) => {
  const accessToken = await AsyncStorage.getItem("accessToken");
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
});

// Refresh token nếu 401
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = await AsyncStorage.getItem("refreshToken");
      if (!refreshToken) return Promise.reject(error);

      try {
        // 🟢 Gọi refresh token trực tiếp trong axiosInstance
        const response = await axios.post(
          "http://192.168.2.199:8080/api/v1/auth/refresh-token",
          { refreshToken },
          { headers: { "Content-Type": "application/json" } }
        );

        const newAccessToken = response.data.accessToken;
        await AsyncStorage.setItem("accessToken", newAccessToken);

        // Gắn lại header và retry request cũ
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return axiosInstance(originalRequest);
      } catch (err) {
        // Refresh token hết hạn → xóa token, logout
        await AsyncStorage.multiRemove(["accessToken", "refreshToken"]);
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);
