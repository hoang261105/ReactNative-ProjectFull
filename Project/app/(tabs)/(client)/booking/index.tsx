import BookingCard from "@/components/item/BookingCard";
import { useBookings } from "@/hooks/useBookings";
import { useCurrentUser } from "@/hooks/useUser";
import { BookingResponse, Status } from "@/interface/booking";
import { Ionicons } from "@expo/vector-icons";
import { useRouter } from "expo-router";
import React, { useState } from "react";
import {
  ActivityIndicator,
  FlatList,
  StatusBar,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

// --- Component con: Chuyển Tab ---
const TabSwitcher = ({
  activeTab,
  setActiveTab,
}: {
  activeTab: Status;
  setActiveTab: (tab: Status) => void;
}) => {
  return (
    <View className="flex-row bg-gray-100 rounded-full p-1 mx-5 mb-5">
      {/* Tab Upcoming */}
      <TouchableOpacity
        onPress={() => setActiveTab(Status.PENDING)}
        className={`flex-1 py-3 rounded-full ${
          activeTab === Status.PENDING
            ? "bg-blue-600 shadow-md"
            : "bg-transparent"
        }`}
      >
        <Text
          className={`text-base font-bold text-center ${
            activeTab === Status.PENDING ? "text-white" : "text-gray-500"
          }`}
        >
          Đang chờ
        </Text>
      </TouchableOpacity>
      {/* Tab Past */}
      <TouchableOpacity
        onPress={() => setActiveTab(Status.CONFIRMED)}
        className={`flex-1 py-3 rounded-full ${
          activeTab === Status.CONFIRMED
            ? "bg-blue-600 shadow-md"
            : "bg-transparent"
        }`}
      >
        <Text
          className={`text-base font-bold text-center ${
            activeTab === Status.CONFIRMED ? "text-white" : "text-gray-500"
          }`}
        >
          Đã đặt
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() => setActiveTab(Status.CANCELLED)}
        className={`flex-1 py-3 rounded-full ${
          activeTab === Status.CANCELLED
            ? "bg-blue-600 shadow-md"
            : "bg-transparent"
        }`}
      >
        <Text
          className={`text-base font-bold text-center ${
            activeTab === Status.CANCELLED ? "text-white" : "text-gray-500"
          }`}
        >
          Đã hủy
        </Text>
      </TouchableOpacity>
    </View>
  );
};

// --- Màn hình chính ---
const BookingsScreen = () => {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState<Status>(Status.PENDING);
  const { data: user, isLoading: userLoading } = useCurrentUser();

  const { data: bookings = [], isLoading: bookingsLoading } = useBookings(
    user?.id,
    activeTab
  );

  // Gộp 2 trạng thái loading
  const isLoading = userLoading || bookingsLoading;

  return (
    // *** 1. CHỈ DÙNG MỘT SafeAreaView ***
    <SafeAreaView className="flex-1 bg-white">
      <StatusBar barStyle="dark-content" />

      {/* 1. Header Title (Luôn luôn hiển thị) */}
      <View className="flex-row items-center p-5 border-b border-gray-100">
        <TouchableOpacity onPress={() => router.back()}>
          <Ionicons name="arrow-back" size={24} color="black" />
        </TouchableOpacity>
        <Text className="flex-1 text-center text-xl font-bold text-gray-900">
          Phòng của tôi
        </Text>
      </View>

      {/* 2. Dùng toán tử 3 ngôi BÊN TRONG */}
      {isLoading ? (
        // 2.A: Nếu đang loading
        <View className="flex-1 items-center justify-center">
          <ActivityIndicator size="large" color="#504DE4" />
        </View>
      ) : (
        // 2.B: Nếu đã tải xong
        <>
          {/* Tab Switcher (Chỉ hiện khi tải xong) */}
          <TabSwitcher activeTab={activeTab} setActiveTab={setActiveTab} />

          {/* Danh sách Đặt phòng */}
          <FlatList
            data={bookings}
            keyExtractor={(item: BookingResponse) => item.bookingId.toString()}
            renderItem={({ item }) => <BookingCard booking={item} />}
            showsVerticalScrollIndicator={false}
            extraData={activeTab}
            contentContainerStyle={{ paddingHorizontal: 20 }}
            ListEmptyComponent={
              <View className="items-center justify-center mt-20">
                <Text className="text-lg text-gray-500">
                  No {activeTab} bookings.
                </Text>
              </View>
            }
          />
        </>
      )}
    </SafeAreaView>
  );
};

export default BookingsScreen;
