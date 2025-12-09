import { defineStore } from "pinia";
import api from "@/api/axios";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    token: localStorage.getItem("token") || null,
    user: null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.token,
  },
  actions: {
    async login(username, password) {
      try {
        const response = await api.post("/auth/login", { username, password });
        this.token = response.data.token;
        localStorage.setItem("token", this.token);
        return true;
      } catch (error) {
        console.error("Login failed", error);
        throw error;
      }
    },
    async register(account) {
      await api.post("/auth/register", account);
    },
    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem("token");
    },
  },
});
