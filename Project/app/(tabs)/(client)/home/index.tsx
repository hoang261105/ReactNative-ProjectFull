import { getAllProvinces } from "@/apis/province.api";
import { getAllRooms } from "@/apis/room.api";
import ProvinceItem from "@/components/item/ProvinceItem";
import { ProvinceResponse } from "@/interface/province";
import { RoomResponse } from "@/interface/room";
import {
  Feather,
  FontAwesome,
  Ionicons,
  MaterialIcons,
} from "@expo/vector-icons";
import { useQuery } from "@tanstack/react-query";
import { useRouter } from "expo-router";
import React from "react";
import {
  ActivityIndicator,
  FlatList,
  Image,
  ScrollView,
  StatusBar,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

const bestHotels = [
  {
    id: 1,
    name: "Malon Greens",
    location: "Mumbai, Maharashtra",
    price: 120,
    rating: 5.0,
    reviews: 120,
    image:
      "https://images.unsplash.com/photo-1566073771259-6a8506099945?fit=crop&w=300&q=80",
  },
  {
    id: 2,
    name: "Fortune Land",
    location: "Goa, Maharashtra",
    price: 150,
    rating: 4.5,
    reviews: 90,
    image:
      "https://images.unsplash.com/photo-1582719508461-905c673771fd?fit=crop&w=300&q=80",
  },
];

const nearbyHotels = [
  {
    id: 3,
    name: "Malon Greens",
    location: "Mumbai, Maharashtra",
    price: 110,
    rating: 4.0,
    reviews: 80,
    image:
      "https://images.unsplash.com/photo-1566073771259-6a8506099945?fit=crop&w=200&q=80",
  },
  {
    id: 4,
    name: "Sabro Prime",
    location: "Mumbai, Maharashtra",
    price: 90,
    rating: 5.0,
    reviews: 76,
    image:
      "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?fit=crop&w=200&q=80",
  },
  {
    id: 5,
    name: "Peradise Mint",
    location: "Mumbai, Maharashtra",
    price: 120,
    rating: 4.0,
    reviews: 115,
    image:
      "https://images.unsplash.com/photo-1587061949409-02df43d75b3d?fit=crop&w=200&q=80",
  },
];

// --- Component con: Card Khách Sạn (nhỏ) ---
const HotelCardSmall = ({ room, onPress }: { room: RoomResponse, onPress: () => void }) => (
  <TouchableOpacity className="flex-row items-center bg-white rounded-2xl shadow p-3 mb-4" onPress={onPress}>
    <Image source={{ uri: room.imageUrl }} className="w-20 h-20 rounded-xl" />
    <View className="flex-1 ml-3">
      <Text className="text-base font-bold text-gray-800">{room.title}</Text>
      <View className="flex-row items-center mt-1">
        <MaterialIcons name="location-pin" size={14} color="#888" />
        <Text className="text-xs text-gray-500 ml-1">{room.hotelName}</Text>
      </View>
      <View className="flex-row items-center mt-2">
        <FontAwesome name="star" size={14} color="#FFD700" />
        <Text className="text-sm font-bold text-gray-700 ml-1">5.0</Text>
        <Text className="text-xs text-gray-500 ml-1">(0 Reviews)</Text>
      </View>
    </View>
    <Text className="text-xl font-bold text-gray-900 mt-2">
      {room.price.toLocaleString("vi-VN", {
        style: "currency",
        currency: "VND",
      })}
      <Text className="text-base font-normal text-gray-500">/đêm</Text>
    </Text>
  </TouchableOpacity>
);

// --- Component con: Card Khách Sạn (lớn) ---
const HotelCardLarge = ({ hotel }: { hotel: any }) => (
  <TouchableOpacity className="bg-white rounded-2xl shadow-md w-64 mr-4">
    <Image
      source={{ uri: hotel.image }}
      className="w-full h-48 rounded-t-2xl"
    />
    <TouchableOpacity className="absolute top-3 right-3 bg-white/70 p-1.5 rounded-full">
      <Ionicons name="heart-outline" size={20} color="#333" />
    </TouchableOpacity>
    <View className="p-4">
      <Text className="text-lg font-bold text-gray-800">{hotel.name}</Text>
      <View className="flex-row items-center mt-1">
        <FontAwesome name="star" size={14} color="#FFD700" />
        <Text className="text-sm font-bold text-gray-700 ml-1">
          {hotel.rating.toFixed(1)}
        </Text>
        <Text className="text-xs text-gray-500 ml-1">
          ({hotel.reviews} Reviews)
        </Text>
      </View>
      <View className="flex-row items-center mt-1">
        <MaterialIcons name="location-pin" size={14} color="#888" />
        <Text className="text-xs text-gray-500 ml-1">{hotel.location}</Text>
      </View>
      <View className="mt-2">
        <Text className="text-lg font-bold text-primary">
          ${hotel.price}
          <Text className="text-sm font-normal text-gray-500">/night</Text>
        </Text>
      </View>
    </View>
  </TouchableOpacity>
);

export default function HomeScreen() {
  const router = useRouter();
  const { data: rooms } = useQuery({
    queryFn: async () => {
      const response = await getAllRooms();
      return response.data;
    },
    queryKey: ["rooms"],
  });

  const handlePress = (id: number) => {
    router.push({
      pathname: "/(tabs)/(client)/home/hotel/[provinceId]",
      params: {
        provinceId: id,
      },
    });
  };

  const handleDetail = (id: number) => {
    router.push({
      pathname: "/(tabs)/(client)/home/room/roomDetail/[roomId]",
      params: {
        roomId: id,
      },
    });
  }

  const {
    data: provinces,
    isLoading,
    isError,
  } = useQuery({
    queryFn: async () => {
      const response = await getAllProvinces();
      return response.data;
    },
    queryKey: ["provinces"],
  });

  if (isLoading) {
    return <ActivityIndicator size="large" color="#504DE4" className="mt-10" />;
  }
  if (isError) {
    return (
      <Text className="text-center text-red-500 mt-10">
        Có lỗi xảy ra khi tải dữ liệu.
      </Text>
    );
  }
  return (
    <SafeAreaView className="flex-1 bg-gray-100">
      <StatusBar barStyle="light-content" backgroundColor="#504DE4" />

      {/* --- 1. Header & Search --- */}
      <View className="bg-primary pt-4 pb-6 px-4 rounded-b-3xl bg-[#504DE4]">
        {/* Top bar */}
        <View className="flex-row justify-between items-center mb-4">
          {/* 1. Icon bên trái (Bọc trong View để set kích thước) */}
          <View className="w-8 h-8 justify-center">
            <TouchableOpacity>
              <Feather name="grid" size={24} color="white" />
            </TouchableOpacity>
          </View>

          {/* 2. Logo (Tự động căn giữa) */}
          <Image
            source={require("../../../../assets/images/live-green-blue.jpg")}
            style={{ width: 200, height: 50 }}
            className="bg-[#504DE4]"
          />

          {/* 3. View "ma" bên phải (Phải có cùng chiều rộng w-8) */}
          <View className="w-8 h-8" />
        </View>

        {/* Search Bar & Filter */}
        <View className="flex-row items-center">
          <View className="flex-1 flex-row items-center bg-white/30 rounded-lg px-3 py-2.5">
            <Feather name="search" size={20} color="white" />
            <TextInput
              placeholder="Tìm kiếm phòng tại đây"
              placeholderTextColor="#FFFFFF"
              className="flex-1 ml-2 text-white"
              onPress={() => router.push("/home/search")}
            />
          </View>
          <TouchableOpacity className="ml-3 bg-white/30 p-3 rounded-lg">
            <Ionicons name="options" size={20} color="white" />
          </TouchableOpacity>
        </View>
      </View>

      {/* --- 2. Nội dung có thể cuộn --- */}
      <ScrollView
        className="flex-1"
        contentContainerStyle={{ paddingBottom: 20 }}
        showsVerticalScrollIndicator={false}
      >
        {/* --- 3. Danh mục Địa điểm --- */}
        <View className="mt-6">
          <FlatList
            horizontal={true}
            showsHorizontalScrollIndicator={false}
            contentContainerStyle={{ paddingHorizontal: 16 }}
            data={provinces}
            keyExtractor={(item: ProvinceResponse) => item.id.toString()}
            renderItem={({ item }: { item: ProvinceResponse }) => (
              <ProvinceItem pro={item} onPress={() => handlePress(item.id)} />
            )}
          />
        </View>

        <View className="mt-8 px-4">
          {/* Tiêu đề section */}
          <View className="flex-row justify-between items-center mb-4">
            <Text className="text-xl font-bold text-gray-800">
              Danh sách phòng
            </Text>
          </View>

          <FlatList
            data={rooms}
            keyExtractor={(item: RoomResponse) => item.id.toString()}
            renderItem={({ item }: { item: RoomResponse }) => (
              <HotelCardSmall room={item} onPress={() => handleDetail(item.id)}/>
            )}
          />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}
