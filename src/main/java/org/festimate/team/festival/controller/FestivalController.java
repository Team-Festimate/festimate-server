package org.festimate.team.festival.controller;

import lombok.RequiredArgsConstructor;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/festivals")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    // 페스티벌 등록
    @PostMapping
    public ResponseEntity<Festival> createFestival(@RequestBody Festival festival) {
        Festival savedFestival = festivalService.createFestival(festival);
        return ResponseEntity.ok(savedFestival);
    }

    // 초대 코드로 페스티벌 조회
    @GetMapping("/{inviteCode}")
    public ResponseEntity<Festival> getFestivalByInviteCode(@PathVariable String inviteCode) {
        Festival festival = festivalService.getFestivalByInviteCode(inviteCode);
        return ResponseEntity.ok(festival);
    }

    // 전체 페스티벌 목록 조회
    @GetMapping
    public ResponseEntity<List<Festival>> getAllFestivals() {
        List<Festival> festivals = festivalService.getAllFestivals();
        return ResponseEntity.ok(festivals);
    }
}
