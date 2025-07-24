package com.example.campy.service;

import com.example.campy.dto.material.MaterialListDto;
import com.example.campy.dto.material.request.MaterialRequestDto;
import com.example.campy.dto.material.response.MaterialResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;

public interface MaterialService {

    /**
     * 자료 등록 메서드
     * @param requestDto 등록할 자료의 정보 (제목, 내용, 썸네일, 가격 등)
     * @param userId 등록한 사용자 ID (토큰에서 추출된 인증 사용자)
     * @return 생성된 자료 정보를 담은 DTO
     */
    MaterialResponseDto createMaterial(MaterialRequestDto requestDto, Integer userId);

    /**
     * 전체 자료 목록 조회 (페이징 + 정렬 + 키워드 검색 통합)
     * @param page 페이지 번호 (0부터 시작)
     * @param size 한 페이지당 자료 수
     * @param sort 정렬 기준 (예: createdAt,desc 또는 price,asc)
     * @param keyword 제목 키워드 (검색어, 선택값)
     * @return 페이징 처리된 자료 목록 DTO
     */
    Page<MaterialListDto> getAllMaterials(int page, int size, String sort, String keyword);

    /**
     * 자료 삭제 메서드 (Soft Delete 처리)
     * @param materialId 삭제할 자료 ID
     * @param userId 삭제 요청자 ID (판매자 본인인지 확인)
     */
    void deleteMaterial(Integer materialId, Integer userId);

    /**
     * 자료 수정 메서드
     * @param materialId 수정할 자료 ID
     * @param sellerId 수정 요청자 ID (판매자 본인인지 확인)
     * @param updateDto 수정할 자료의 새 정보
     * @return 수정된 자료 정보를 담은 DTO
     */
    MaterialResponseDto updateMaterial(Integer materialId, Integer sellerId, MaterialRequestDto updateDto);

    /**
     * 자료 상세 조회 메서드
     * @param materialId 조회할 자료 ID
     * @return 자료 상세 정보를 담은 DTO
     */
    MaterialResponseDto getMaterialById(Integer materialId);

    /**
     * 자료 키워드 검색 (별도 API)
     * @param keyword 검색할 제목 키워드
     * @param pageable 페이징 정보
     * @return 조건에 맞는 자료 목록 페이지
     */
    Page<MaterialListDto> searchMaterials(String keyword, Pageable pageable);

    /**
     * 구매한 자료 파일 다운로드
     * @param materialId 자료 ID
     * @param userId 요청 사용자 ID (구매 여부 확인)
     * @return PDF 파일 리소스 (다운로드용 응답)
     */
    ResponseEntity<Resource> downloadMaterialFile(Integer materialId, Integer userId);

    /**
     * 내가 등록한 자료 목록 조회
     * @param userId 사용자 ID
     * @param pageable 페이징 정보 (페이지 번호, 정렬 등)
     * @return 사용자 본인이 등록한 자료 목록 (Soft Delete 제외)
     */
    Page<MaterialListDto> getMyMaterials(Integer userId, Pageable pageable);
}