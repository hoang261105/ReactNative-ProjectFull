import { Stack } from 'expo-router'
import React from 'react'

export default function RoomDetailLayout() {
  return (
    <Stack screenOptions={{headerShown: false}}>
        <Stack.Screen name='imageDetail' options={{title: "Danh sách ảnh"}}/>
        <Stack.Screen name='[roomId]' options={{title: "Chi tiết phòng"}}/>
    </Stack>
  )
}
