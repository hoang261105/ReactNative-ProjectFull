import React from 'react'
import { Stack } from 'expo-router'

export default function AuthLayout() {
  return (
    <Stack screenOptions={{ headerShown: false }}>
      <Stack.Screen name="register" options={{title: "Đăng ký"}}/>
      <Stack.Screen name="login" options={{title: "Đăng nhập"}}/>
    </Stack>
  )
}
