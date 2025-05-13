package org.festimate.team.domain.festival.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.api.admin.dto.FestivalRequest;
import org.festimate.team.domain.festival.entity.Category;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.repository.FestivalRepository;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.festimate.team.domain.festival.validator.DateValidator.isFestivalDateValid;
import static org.festimate.team.domain.festival.validator.DateValidator.isMatchingStartTimeValid;
import static org.festimate.team.domain.festival.validator.FestivalRequestValidator.isFestivalValid;

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
                .title(request.title())
                .category(Category.toCategory(request.category()))
                .startDate(request.startDate())
                .endDate(request.endDate())
                .matchingStartAt(request.matchingStartAt())
                .inviteCode(inviteCode)
                .build();

        festival.addHost(host);

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
        return festivalRepository.findDistinctByFestivalHosts_Host(user);
    }

    @Override
    public Festival getFestivalDetailByIdOrThrow(Long festivalId, User user) {
        Festival festival = getFestivalByIdOrThrow(festivalId);
        if (!isHost(user, festival)) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
        return festival;
    }

    @Override
    public boolean isHost(User user, Festival festival) {
        return festival.getFestivalHosts().stream()
                .anyMatch(fh -> fh.getHost().getUserId().equals(user.getUserId()));
    }

    @Override
    public void validateCreateFestival(FestivalRequest request) {
        isFestivalValid(request.title(), request.category());
        isFestivalDateValid(request.startDate(), request.endDate());
        isMatchingStartTimeValid(request.startDate(), request.matchingStartAt());
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
