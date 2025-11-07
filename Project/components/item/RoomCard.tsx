import { RoomResponse } from '@/interface/room'
import { FontAwesome, Ionicons, MaterialIcons } from '@expo/vector-icons'
import React from 'react'
import { Image, Text, TouchableOpacity, View } from 'react-native'

export default function RoomCard({ room, onPress }: {room: RoomResponse, onPress: () => void}) {
  return (
    <TouchableOpacity className="bg-white rounded-2xl shadow-lg mb-6 overflow-hidden" onPress={onPress}>
    {/* Phần ảnh */}
    <View>
      <Image
        source={{ uri: room.imageUrl }}
        className="w-full h-56"
      />
      {/* Nút yêu thích */}
      <TouchableOpacity className="absolute top-4 right-4 bg-black/30 p-2 rounded-full">
        <Ionicons name="heart-outline" size={20} color="white" />
      </TouchableOpacity>
    </View>

    {/* Phần nội dung */}
    <View className="p-4">
      {/* Rating */}
      <View className="flex-row items-center">
        <FontAwesome name="star" size={16} color="#F59E0B" />
        <FontAwesome name="star" size={16} color="#F59E0B" />
        <FontAwesome name="star" size={16} color="#F59E0B" />
        <FontAwesome name="star" size={16} color="#F59E0B" />
        <FontAwesome name="star" size={16} color="#F59E0B" />
        <Text className="text-sm font-bold text-gray-700 ml-2">0</Text>
        <Text className="text-sm text-gray-500 ml-1.5">(0 Reviews)</Text>
      </View>

      {/* Tên phòng */}
      <Text className="text-lg font-bold text-gray-900 mt-1">{room.title}</Text>

      <View className="flex-row items-center mt-1">
        <MaterialIcons name="location-pin" size={16} color="#888" />
        <Text className="text-sm text-gray-500 ml-1">{room.hotelName}</Text>
      </View>

      {/* Giá tiền */}
      <Text className="text-xl font-bold text-gray-900 mt-2">
        {room.price.toLocaleString('vi-VN', {
          style: 'currency',
          currency: 'VND'
        })}
        <Text className="text-base font-normal text-gray-500">/đêm</Text>
      </Text>
    </View>
  </TouchableOpacity>
  )
}
