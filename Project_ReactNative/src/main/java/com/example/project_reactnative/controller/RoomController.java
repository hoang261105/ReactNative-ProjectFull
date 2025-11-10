package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.dto.response.RoomDetail;
import com.example.project_reactnative.model.dto.response.RoomImageResponse;
import com.example.project_reactnative.model.dto.response.RoomResponse;
import com.example.project_reactnative.model.entity.Room;
import com.example.project_reactnative.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<APIResponse<List<RoomResponse>>> getAllRooms(@RequestParam(required = false) String title) {
        return new ResponseEntity<>(new APIResponse<>(true, "Tìm kiếm phòng thành công", roomService.getRoomsByTitle(title)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<RoomDetail>> getRoomById(@PathVariable Long id) {
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy chi tiết phòng thành công!", roomService.getRoomById(id)), HttpStatus.OK);
    }

    @GetMapping("/{roomId}/images/{imageId}")
    public ResponseEntity<APIResponse<RoomImageResponse>> getRoomImageById(@PathVariable Long roomId, @PathVariable Long imageId) {
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy chi tiết hình ảnh thành công!", roomService.getImageById(imageId, roomId)), HttpStatus.OK);
    }
}
