import axios, { type AxiosInstance, AxiosError, type InternalAxiosRequestConfig } from "axios";
import type { ErrorResponseDTO } from "@/types/transaction";

/**
 * Cliente axios configurado para la API de transacciones.
 * En Docker/build: usar VITE_API_URL (ej. http://localhost:8080).
 */
const apiClient: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error: AxiosError<ErrorResponseDTO>) => {
    if (error.response) {
      const errorData = error.response.data;
      const status = error.response.status;

      console.error("Error de API:", {
        status,
        message: errorData?.message || error.message,
        path: errorData?.path || error.config?.url,
        validationErrors: errorData?.validationErrors,
      });
    }
    // No logueamos errores de red aquí, los componentes los manejan de manera más específica
    // else if (error.request) {
    //   console.error('Error de red:', error.message)
    // } else {
    //   console.error('Error:', error.message)
    // }

    return Promise.reject(error);
  },
);

export default apiClient;
