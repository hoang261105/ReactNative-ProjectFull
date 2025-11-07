import React from "react";
import {
  ActivityIndicator,
  Image,
  ImageBackground,
  ScrollView,
  StatusBar,
  Text,
  TouchableOpacity,
  View,
} from "react-native";

// Import các bộ icon cần thiết
import { getRoomById } from "@/apis/room.api";
import { RoomImage } from "@/interface/room";
import {
  Feather,
  FontAwesome,
  Ionicons,
  MaterialCommunityIcons,
  MaterialIcons,
} from "@expo/vector-icons";
import { useQuery } from "@tanstack/react-query";
import { useLocalSearchParams, useRouter } from "expo-router";
import { SafeAreaView } from "react-native-safe-area-context";


// --- Kết thúc dữ liệu giả ---

// --- Component con: Feature Highlight (để tái sử dụng) ---
const FeatureHighlight = ({ iconName, library, title, text }: any) => {
  // Hàm trợ giúp để render đúng thư viện icon
  const IconComponent = () => {
    switch (library) {
      case "Ionicons":
        return <Ionicons name={iconName} size={24} color="#333" />;
      case "MaterialCommunity":
        return (
          <MaterialCommunityIcons name={iconName} size={24} color="#333" />
        );
      default:
        return null;
    }
  };

  return (
    <View className="flex-row items-start py-4">
      <IconComponent />
      <View className="ml-4 flex-1">
        <Text className="text-base font-bold text-gray-800">{title}</Text>
        <Text className="text-sm text-gray-600 mt-1">{text}</Text>
      </View>
    </View>
  );
};

export default function HotelDetailScreen() {
  const router = useRouter();
  const { roomId } = useLocalSearchParams();

  const { data: room, isLoading } = useQuery({
    queryFn: async () => {
      const response = await getRoomById(+roomId);
      return response.data;
    },
    queryKey: ["room"],
  });

  if (isLoading) {
    return (
      <SafeAreaView className="flex-1 items-center justify-center bg-white">
        <ActivityIndicator size="large" color="#504DE4" />
        <Text className="mt-2 text-gray-600">Đang tải dữ liệu phòng...</Text>
      </SafeAreaView>
    );
  }

  return (
    // Dùng SafeAreaView cho toàn bộ màn hình
    <SafeAreaView className="flex-1 bg-white">
      <StatusBar barStyle="light-content" />

      {/* --- 1. Nội dung cuộn (Toàn bộ màn hình trừ footer) --- */}
      <ScrollView
        className="flex-1"
        // Thêm padding ở dưới cùng để cuộn không bị che bởi footer
        contentContainerStyle={{ paddingBottom: 120 }}
      >
        {/* --- Ảnh Header --- */}
        <ImageBackground
          source={{ uri: room.images[0]?.imageURL }}
          className="w-full h-80"
        >
          {/* Dùng 1 View trong suốt để đặt icon */}
          <View className="flex-1 flex-row items-start justify-between p-4 bg-black/10">
            {/* Nút Back */}
            <TouchableOpacity
              className="bg-white/70 w-10 h-10 items-center justify-center rounded-full mt-8"
              onPress={() => router.back()}
            >
              <Feather name="chevron-left" size={24} color="#333" />
            </TouchableOpacity>
            {/* Nút Heart */}
            <TouchableOpacity className="bg-white/70 w-10 h-10 items-center justify-center rounded-full mt-8">
              <Ionicons name="heart-outline" size={24} color="#333" />
            </TouchableOpacity>
          </View>
        </ImageBackground>

        {/* --- Thẻ nội dung trắng (kéo đè lên ảnh) --- */}
        <View className="bg-white rounded-t-3xl -mt-10 p-6">
          {/* Rating */}
          <View className="flex-row items-center">
            <FontAwesome name="star" size={16} color="#F59E0B" />
            <FontAwesome name="star" size={16} color="#F59E0B" />
            <FontAwesome name="star" size={16} color="#F59E0B" />
            <FontAwesome name="star" size={16} color="#F59E0B" />
            <FontAwesome name="star" size={16} color="#F59E0B" />
            <Text className="text-sm font-bold text-gray-700 ml-2">5.0</Text>
            <Text className="text-sm text-gray-500 ml-1.5">(120 Reviews)</Text>
          </View>

          {/* Tên khách sạn */}
          <Text className="text-3xl font-bold text-gray-900 mt-2">
            {room.title}
          </Text>
          {/* Vị trí */}
          <View className="flex-row items-center mt-1">
            <MaterialIcons name="location-pin" size={16} color="#888" />
            <Text className="text-base text-gray-500 ml-1">
              {room?.hotelName}
            </Text>
          </View>

          {/* Dấu gạch ngang */}
          <View className="h-px bg-gray-200 my-4" />

          {/* Overview */}
          <Text className="text-xl font-bold text-gray-900">Overview</Text>
          <Text className="text-base text-gray-600 mt-2">
            {room.description}
          </Text>

          {/* Photos */}
          <View className="flex-row justify-between items-center mt-6">
            <Text className="text-xl font-bold text-gray-900">Photos</Text>
            <TouchableOpacity
              onPress={() => router.push({
                pathname: "/home/room/roomDetail/imageDetail",
                params: {
                  roomId: +roomId
                }
              })}
            >
              <Text className="text-sm font-medium text-blue-600">See All</Text>
            </TouchableOpacity>
          </View>
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={false}
            className="mt-3"
          >
            {room.images.map((image: RoomImage) => (
              <Image
                key={image.id}
                source={{ uri: image.imageURL }}
                className="w-28 h-28 rounded-xl mr-4"
              />
            ))}
          </ScrollView>

          {/* Dấu gạch ngang */}
          <View className="h-px bg-gray-200 my-4" />

          {/* Hosted by */}
          <View className="flex-row justify-between items-center">
            <View className="flex-1">
              <Text className="text-xl font-bold text-gray-900">
                Phòng thuộc
              </Text>
              <Text className="text-base text-gray-600 mt-1">
                quyền sở hữu của{" "}
                <Text className="font-bold">{room.hotelName}</Text>
              </Text>
              <Text className="text-base text-gray-600 mt-2">
                {room?.guestCount} guests • {room?.bedroomCount} phòng ngủ •{" "}
                {room?.bedCount} giường • {room?.bathRoomCount} phòng tắm
              </Text>
            </View>
            <Image source={{ uri: room.images[0]?.imageURL }} className="w-14 h-14 rounded-full" />
          </View>

          {/* Dấu gạch ngang */}
          <View className="h-px bg-gray-200 my-4" />

          {/* --- Render các Feature Highlights --- */}
          <FeatureHighlight
            library="Ionicons"
            iconName="sparkles-outline"
            title="Enhanced Clean"
            text="This host committed to Airbnb's clone 5-step enhanced cleaning process."
          />
          <FeatureHighlight
            library="Ionicons"
            iconName="location-outline"
            title="Great Location"
            text="95% of recent guests give the location a 5-star rating."
          />
          <FeatureHighlight
            library="Ionicons"
            iconName="key-outline"
            title="Great check-in-experience"
            text="90% of recent guests gave the check-in process a 5-star rating."
          />
          <FeatureHighlight
            library="Ionicons"
            iconName="calendar-outline"
            title="Free cancellation until 2:00 PM on 8 May"
            text="" // Không có text phụ
          />
        </View>
      </ScrollView>

      {/* --- 2. Footer (Dính ở dưới cùng) --- */}
      <View className="absolute bottom-0 left-0 right-0 bg-white border-t border-gray-200">
        <SafeAreaView edges={["bottom"]}>
          <View className="flex-row justify-between items-center px-6 pt-6">
            {/* Giá tiền */}
            <View>
              <Text className="text-xl font-bold text-gray-900">
                {room.price.toLocaleString("vi-VN", {
                  style: "currency",
                  currency: "VND",
                })}
                <Text className="text-base font-normal text-gray-500">
                  /đêm
                </Text>
              </Text>
            </View>
            {/* Nút bấm */}
            <TouchableOpacity className="bg-blue-600 px-8 py-3 rounded-lg">
              <Text className="text-white text-base font-bold">
                Chọn ngày
              </Text>
            </TouchableOpacity>
          </View>
        </SafeAreaView>
      </View>
    </SafeAreaView>
  );
}
