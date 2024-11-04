package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.common.exception.PortfolioException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequest;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;
import com.sparta.doguin.domain.portfolio.repository.PortfolioRepository;
import com.sparta.doguin.domain.portfolio.validate.PortfolioValidator;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponsePortfolioEnum.PORTFOLIO_NOT_FOUND;
import static com.sparta.doguin.domain.common.response.ApiResponsePortfolioEnum.PORTFOLIO_OK;


@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentGetService attachmentGetService;
    private final AttachmentUpdateService attachmentUpdateService;
    private final AttachmentDeleteService attachmentDeleteService;

    /**
     * ID로 특정 포트폴리오 찾는 메서드
     *
     * @param portfolioId / 조회할 포트폴리오 ID
     * @return ApiResponse<PortfolioDto> / 포트폴리오 데이터 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    @Override
    public PortfolioResponse getPortfolio(Long portfolioId) {
        Portfolio portfolio = findById(portfolioId);
        List<String> filePaths = attachmentGetService.getAllAttachmentPath(portfolio.getUser().getId(), portfolio.getId(), AttachmentTargetType.PORTFOLIO);
        return PortfolioResponse.PortfolioResponseGetFilePaths.of(portfolio,filePaths);
    }

    /**
     * 포트폴리오 생성 메서드
     *
     * @param portfolioRequest / 생성할 포트폴리오 데이터
     * @param authUser / 포트폴리오 생성할 유저
     * @return ApiResponse<Void> / 성공 응답 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<PortfolioResponse> createPortfolio(PortfolioRequest.PortfolioRequestCreate portfolioRequest, AuthUser authUser, List<MultipartFile> files) {
        User user = User.fromAuthUser(authUser);
        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .title(portfolioRequest.title())
                .content(portfolioRequest.content())
                .work_experience(portfolioRequest.work_experience())
                .work_type(portfolioRequest.work_type())
                .project_history(portfolioRequest.proejct_history())
                .area(portfolioRequest.area())
                .build();
        Portfolio savePortfolio = portfolioRepository.save(portfolio);
        if (files == null) {
            PortfolioResponse portfolioResponse = PortfolioResponse.PortfolioResponseGetFilePaths.of(portfolio);
            return ApiResponse.of(PORTFOLIO_OK,portfolioResponse);
        } else {
            attachmentUploadService.upload(files,authUser,savePortfolio.getId(), AttachmentTargetType.PORTFOLIO);
            List<Long> attachmentIds = attachmentGetService.getFileIds(portfolio.getUser().getId(), portfolio.getId(), AttachmentTargetType.PORTFOLIO);
            PortfolioResponse portfolioResponse = PortfolioResponse.PortfolioResponseGetIds.of(portfolio,attachmentIds);
            return ApiResponse.of(PORTFOLIO_OK,portfolioResponse);
        }
    }

    /**
     * 포트폴리오 수정 메서드
     *
     * @param portfolioId / 수정할 포트폴리오 ID
     * @param portfolioRequestUpdate / 수정할 포트폴리오 데이터
     * @return ApiResponse<Void> / 성공 응답 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioRequest.PortfolioRequestUpdate portfolioRequestUpdate, AuthUser authUser, List<MultipartFile> updateFiles) {
        User user = User.fromAuthUser(authUser);
        Portfolio findPortfolio = findById(portfolioId);
        Portfolio portfolio = Portfolio.builder()
                .id(findPortfolio.getId())
                .user(findPortfolio.getUser())
                .title(portfolioRequestUpdate.title() == null ? findPortfolio.getTitle() : portfolioRequestUpdate.title())
                .content(portfolioRequestUpdate.content() == null ? findPortfolio.getContent() : portfolioRequestUpdate.content())
                .work_experience(portfolioRequestUpdate.work_experience() == null ? findPortfolio.getWork_experience() : portfolioRequestUpdate.work_experience())
                .work_type(portfolioRequestUpdate.work_type() == null ? findPortfolio.getWork_type() : portfolioRequestUpdate.work_type())
                .project_history(portfolioRequestUpdate.proejct_history() == null ? findPortfolio.getProject_history() : portfolioRequestUpdate.proejct_history())
                .area(portfolioRequestUpdate.area() == null ? findPortfolio.getArea() : portfolioRequestUpdate.area())
                .build();
        if (updateFiles != null) {
            attachmentUpdateService.update(updateFiles,portfolioRequestUpdate.fileIds(),authUser);
        }
        PortfolioValidator.isMe(user.getId(),findPortfolio.getUser().getId());
        portfolioRepository.save(portfolio);
        return ApiResponse.of(PORTFOLIO_OK);
    }

    /**
     * 포트폴리오 삭제 메서드
     *
     * @param portfolioId / 삭제할 포트폴리오 ID
     * @return ApiResponse<Void> / 성공 응답 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> deletePortfolio(
            Long portfolioId,
            AuthUser authUser
    ) {
        Portfolio portfolio = findById(portfolioId);
        PortfolioValidator.isMe(authUser.getUserId(),portfolio.getUser().getId());
        List<Long> fileIds = attachmentGetService.getFileIds(portfolio.getUser().getId(), portfolio.getId(), AttachmentTargetType.PORTFOLIO);
        attachmentDeleteService.delete(authUser,fileIds);
        portfolioRepository.delete(portfolio);
        return ApiResponse.of(PORTFOLIO_OK);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PortfolioResponse> getAllMyPortfolio(Pageable pageable, AreaType area,AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        Page<Portfolio> pageablePortfolio;
        if (area == null) {
            pageablePortfolio = portfolioRepository.findAllByUser(user,pageable);
        } else {
            pageablePortfolio = portfolioRepository.findAllByUserAndArea(user,pageable,area);
        }

        return pageablePortfolio.map(portfolio -> {
            // 각 포트폴리오에 대해 file_paths를 가져옴
            List<String> filePaths = attachmentGetService.getAllAttachmentPath(authUser.getUserId(), portfolio.getId(), AttachmentTargetType.PORTFOLIO);
            return PortfolioResponse.PortfolioResponseGetFilePaths.of(portfolio, filePaths); // filePaths를 포함하여 변환
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PortfolioResponse> getAllOtherPortfolio(Pageable pageable, AreaType area) {
        Page<Portfolio> pageablePortfolio;
        if (area == null) {
            pageablePortfolio = portfolioRepository.findAllBy(pageable);
        } else {
            pageablePortfolio = portfolioRepository.findAllByArea(pageable,area);
        }

        return pageablePortfolio.map(portfolio -> {
            // 각 포트폴리오에 대해 file_paths를 가져옴
            List<String> filePaths = attachmentGetService.getAllAttachmentPath(portfolio.getId(), AttachmentTargetType.PORTFOLIO);
            return PortfolioResponse.PortfolioResponseGetFilePaths.of(portfolio, filePaths); // filePaths를 포함하여 변환
        });
    }

    /**
     * ID로 포트폴리오 찾는 메서드
     *
     * @param portfolioId / 찾을 포트폴리오 ID
     * @return Portfoli / 찾은 포트폴리오 데이터 반환
     * @throws PortfolioException / 포트폴리오 조회 실패시 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    public Portfolio findById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioException(PORTFOLIO_NOT_FOUND));
    }
}
