package org.festimate.team.festival.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.entity.Category;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.repository.FestivalRepository;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class FestivalServiceImpl implements FestivalService {

    private final FestivalRepository festivalRepository;

    @Override
    @Transactional
    public Festival createFestival(User host, FestivalRequest request) {
        String inviteCode = generateUniqueInviteCode().trim();

        Festival festival = Festival.builder()
                .host(host)
                .title(request.title())
                .category(Category.toCategory(request.category()))
                .startDate(request.startDate())
                .endDate(request.endDate())
                .inviteCode(inviteCode)
                .build();

        return festivalRepository.save(festival);
    }

    @Override
    @Transactional
    public Festival getFestivalByInviteCode(String inviteCode) {
        log.info("inviteCode: {}", inviteCode);
        Festival festival = festivalRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new FestimateException(ResponseError.FESTIVAL_NOT_FOUND));
        return validateNotPastDate(festival);
    }

    @Override
    public Festival getFestivalByIdOrThrow(Long festivalId) {
        log.info("festivalId: {}", festivalId);
        return festivalRepository.findByFestivalId(festivalId)
                .orElseThrow(() -> new FestimateException(ResponseError.FESTIVAL_NOT_FOUND));
    }

    @Override
    public List<Festival> getAllFestivals(User user) {
        log.info("user: {}", user);
        return festivalRepository.findFestivalByHost(user);
    }

    @Override
    public Festival getFestivalDetailByIdOrThrow(Long festivalId, Long userId) {
        Festival festival = getFestivalByIdOrThrow(festivalId);
        if (!Objects.equals(festival.getHost().getUserId(), userId)) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
        return festival;
    }

    @Override
    public boolean isFestivalExpired(Festival festival) {
        log.info("localdate now : {}", LocalDate.now());
        return festival.getEndDate().isAfter(LocalDate.now());
    }

    private String generateUniqueInviteCode() {
        Random random = new Random();
        String inviteCode;
        do {
            inviteCode = String.valueOf(100000 + random.nextInt(900000));
        } while (festivalRepository.existsByInviteCode(inviteCode));
        return inviteCode;
    }

    private Festival validateNotPastDate(Festival festival) {
        if (festival.getEndDate().isBefore(LocalDate.now())) {
            throw new FestimateException(ResponseError.EXPIRED_INVITE_CODE);
        }
        return festival;
    }
}
